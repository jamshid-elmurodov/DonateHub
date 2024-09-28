package donatehub.service.donation;

import donatehub.domain.entities.PaymentInfo;
import donatehub.domain.response.*;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import donatehub.domain.entities.DonationEntity;
import donatehub.domain.entities.UserEntity;
import donatehub.domain.entities.WidgetEntity;
import donatehub.domain.constants.PaymentMethod;
import donatehub.domain.exceptions.BaseException;
import donatehub.domain.projections.DonationInfo;
import donatehub.domain.request.ClickRequest;
import donatehub.domain.request.DonationCreateRequest;
import donatehub.domain.request.MirPayRequest;
import donatehub.repo.DonationRepository;
import donatehub.service.payment.click.ClickService;
import donatehub.service.payment.mirpay.MirPayService;
import donatehub.utils.BotExecutor;
import donatehub.service.user.UserService;
import donatehub.service.widget.WidgetService;

import java.time.LocalDate;
import java.util.List;

@Service
public class DonationServiceImpl implements DonationService {
    private final Logger log;
    private final DonationRepository repo;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private final WidgetService widgetService;
    private final MirPayService mirPayService;
    private final ClickService clickService;
    private final BotExecutor botExecutor;

    public DonationServiceImpl(
            Logger log, DonationRepository repo,
            UserService userService,
            SimpMessagingTemplate messagingTemplate,
            WidgetService widgetService,
            MirPayService mirPayService,
            ClickService clickService,
            BotExecutor botExecutor
    ) {
        this.log = log;
        this.repo = repo;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
        this.widgetService = widgetService;
        this.mirPayService = mirPayService;
        this.clickService = clickService;
        this.botExecutor = botExecutor;
    }

    @Transactional
    @Override
    public CreateDonateResponse donate(DonationCreateRequest donateReq, Long streamerId) {
        log.info("Donat qabul qilinmoqda. Streamer ID: {}, Donat miqdori: {}", streamerId, donateReq.getAmount());

        if (!userService.findById(streamerId).getEnable()) {
            throw new BaseException(
                    "Streamer tasdiqlanmagan",
                    HttpStatus.BAD_REQUEST
            );
        }

        var paymentInfo = PaymentInfo.builder()
                .amount(donateReq.getAmount())
                .commission(getCommission(donateReq))
                .build();

        var paymentResponse = switch (donateReq.getMethod()) {
            case CLICK -> clickService.create(donateReq.getAmount() + paymentInfo.getCommission());
            case MIRPAY -> mirPayService.create(donateReq.getAmount() + paymentInfo.getCommission());
        };

        paymentInfo.setPaymentId(paymentResponse.getId());

        repo.save(DonationEntity.builder()
                .donaterName(donateReq.getDonaterName())
                .completed(false)
                .message(donateReq.getMessage())
                .streamer(userService.findById(streamerId))
                .paymentInfo(paymentInfo)
                .build());

        return new CreateDonateResponse(paymentResponse.getRedirectUrl());
    }

    @Override
    public void complete(String body, PaymentMethod method) {
        log.info("To'lov tugallanishi kutilmoqda, usul: {}", method);

        DonationEntity donation = switch (method){
            case CLICK -> clickComplete(body);
            case MIRPAY -> mirPayComplete(body);
        };

        if (donation.getCompleted()){
            throw new BaseException(
                    "Xayriya avval amalga oshirilgan",
                    HttpStatus.BAD_REQUEST
            );
        }

        completeDonation(donation);
    }

    private DonationEntity clickComplete(String body) {
        DonationEntity donation;
        ClickRequest clickRequest = clickService.readBody(body);

        donation = getDonationByPaymentId(String.valueOf(clickRequest.getClickTransId()));

        clickService.complete(clickRequest, donation.getPaymentInfo().getAmount());
        return donation;
    }

    private DonationEntity mirPayComplete(String body) {
        DonationEntity donation;
        MirPayRequest mirPayRequest = mirPayService.readBody(body);

        mirPayService.complete(mirPayRequest);

        donation = getDonationByPaymentId(mirPayRequest.getId());
        return donation;
    }

    private void completeDonation(DonationEntity donation) {
        log.info("Donat to'liq amalga oshirildi: {}", donation.getId());

        donation.setCompleted(true);
        repo.save(donation);

        log.info("Streamer balansini qayta hisoblash: {}", donation.getStreamer().getId());

        userService.recalculateStreamerBalance(donation.getStreamer().getId(), donation.getPaymentInfo().getAmount());

        if (userService.findById(donation.getStreamer().getId()).getOnline()) {
            log.info("Streamer onlayn, donatni jonli efirga uzatish");

            executeToStream(donation);
        }

        log.info("Telegram botga xabar yuborilyapti: {}", donation.getStreamer().getId());
        botExecutor.sendDonationInfo(donation.getStreamer().getId(), donation);
    }

    private void executeToStream(DonationEntity donation) {
        log.info("Donatni jonli efirga uzatish: {}", donation);

        WidgetEntity widget = widgetService.getDonationWidgetOfStreamer(donation.getStreamer().getId());

        messagingTemplate.convertAndSend("/topic/donation/" + donation.getStreamer().getApi(),
                new DonationResponse(donation.getDonaterName(), donation.getMessage(),
                        donation.getPaymentInfo().getAmount(), widget.getVideoUrl(), widget.getAudioUrl()));
    }

    private DonationEntity getDonationByPaymentId(String paymentId) {
        log.info("To'lov ID orqali donatni topish: {}", paymentId);

        return repo.findByPaymentInfoPaymentId(paymentId).orElseThrow(
                () -> new BaseException(
                        "Donation not found with this payment id",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    private Float getCommission(DonationCreateRequest donateReq) {
        log.info("Komissiya hisoblanmoqda: miqdor {}", donateReq.getAmount());

        return donateReq.getAmount() / 100 * 5;
    }

    @Override
    public Page<DonationInfo> getDonationsOfStreamer(Long streamerId, int page, int size) {
        log.info("Streamer ID: {} uchun donatlar so'ralmoqda", streamerId);

        UserEntity user = userService.findById(streamerId);

        return repo.getAllByStreamerIdAndCompletedIsTrue(user.getId(), PageRequest.of(page, size));
    }

    @Override
    public Page<DonationInfo> getAllDonations(int page, int size) {
        log.info("barcha donatlar so'ralmoqda");

        return repo.getAllByCompletedIsTrue(PageRequest.of(page, size));
    }

    @Override
    public List<DonationStatisticResponse> getStatisticsForAdmin(int days) {
        log.info("Administrator uchun {} kunlik statistika so'ralmoqda", days);

        if (days > 30){
            return repo.getAllMonthlyStatistics(days);
        }

        return repo.getAllStatistics(days);
    }

    @Override
    public List<DonationStatisticResponse> getStatisticsForStreamer(Long streamerId, int days) {
        log.info("Streamer ID: {} uchun {} kunlik statistika so'ralmoqda", streamerId, days);

        UserEntity user = userService.findById(streamerId);

        return repo.getStatisticsOfStreamer(user.getId(), LocalDate.now().minusDays(days), days);
    }

    @Override
    public void testDonate(DonationCreateRequest donationCreateRequest, Long streamerId, UserEntity user) {
        if (!user.getId().equals(streamerId)) {
            throw new BaseException(
                    "Faqat streamerning o'zi test donate amalga oshirishi mumkin",
                    HttpStatus.FORBIDDEN
            );
        }

        executeToStream(new DonationEntity(
                userService.findById(streamerId),
                donationCreateRequest.getDonaterName(),
                donationCreateRequest.getMessage(),
                true,
                new PaymentInfo("test", donationCreateRequest.getAmount(), 0f)
        ));
    }
}