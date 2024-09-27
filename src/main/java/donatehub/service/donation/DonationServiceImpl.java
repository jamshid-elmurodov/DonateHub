package donatehub.service.donation;

import donatehub.domain.model.PaymentInfo;
import donatehub.domain.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import donatehub.domain.entity.DonationEntity;
import donatehub.domain.entity.UserEntity;
import donatehub.domain.entity.WidgetEntity;
import donatehub.domain.enums.PaymentMethod;
import donatehub.domain.exception.BaseException;
import donatehub.domain.projection.DonationInfo;
import donatehub.domain.request.ClickReq;
import donatehub.domain.request.DonationCreateReq;
import donatehub.domain.request.MirPayReq;
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
    private final Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");;

    private final DonationRepository repo;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private final WidgetService widgetService;
    private final MirPayService mirPayService;
    private final ClickService clickService;
    private final BotExecutor botExecutor;

    public DonationServiceImpl(
            DonationRepository repo,
            UserService userService,
            SimpMessagingTemplate messagingTemplate,
            WidgetService widgetService,
            MirPayService mirPayService,
            ClickService clickService,
            BotExecutor botExecutor
    ) {
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
    public CreateDonateRes donate(DonationCreateReq donateReq, Long streamerId) {
        log.info("Donat qabul qilinmoqda. Streamer ID: {}, Donat miqdori: {}", streamerId, donateReq.getAmount());

        UserEntity streamer = userService.findById(streamerId);

        if (!streamer.getEnable()) {
            throw new BaseException(
                    "Streamer tasdiqlanmagan",
                    HttpStatus.BAD_REQUEST
            );
        }

        DonationEntity donation = DonationEntity.builder()
                .donaterName(donateReq.getDonaterName())
                .completed(false)
                .message(donateReq.getMessage())
                .streamer(streamer)
                .build();

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .amount(donateReq.getAmount())
                .commission(getCommission(donateReq))
                .build();

        PaymentRes paymentRes = switch (donateReq.getMethod()) {
            case CLICK -> clickService.create(donateReq.getAmount() + paymentInfo.getCommission());
            case MIRPAY -> mirPayService.create(donateReq.getAmount() + paymentInfo.getCommission());
        };

        paymentInfo.setPaymentId(paymentRes.getId());
        donation.setPaymentInfo(paymentInfo);

        repo.save(donation);

        return new CreateDonateRes(paymentRes.getRedirectUrl());
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
        ClickReq clickReq = clickService.readBody(body);

        donation = getDonationByPaymentId(String.valueOf(clickReq.getClickTransId()));

        clickService.complete(clickReq, donation.getPaymentInfo().getAmount());
        return donation;
    }

    private DonationEntity mirPayComplete(String body) {
        DonationEntity donation;
        MirPayReq mirPayReq = mirPayService.readBody(body);

        mirPayService.complete(mirPayReq);

        donation = getDonationByPaymentId(mirPayReq.getId());
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
                new DonationRes(donation.getDonaterName(), donation.getMessage(),
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

    private Float getCommission(DonationCreateReq donateReq) {
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
    public List<DonationStatisticRes> getStatisticsForAdmin(int days) {
        log.info("Administrator uchun {} kunlik statistika so'ralmoqda", days);

        if (days > 30){
            return repo.getAllMonthlyStatistics(days);
        }

        return repo.getAllStatistics(days);
    }

    @Override
    public List<DonationStatisticRes> getStatisticsForStreamer(Long streamerId, int days) {
        log.info("Streamer ID: {} uchun {} kunlik statistika so'ralmoqda", streamerId, days);

        UserEntity user = userService.findById(streamerId);

        return repo.getStatisticsOfStreamer(user.getId(), LocalDate.now().minusDays(days), days);
    }

    @Override
    public void testDonate(DonationCreateReq donationCreateReq, Long streamerId, UserEntity user) {
        log.info("Test donat amalga oshirilmoqda. Streamer ID: {}, Foydalanuvchi ID: {}", streamerId, user.getId());

        UserEntity streamer = userService.findById(streamerId);

        if (!user.getId().equals(streamerId)) {
            log.error("Test donat qilish uchun ruxsat yo'q. Foydalanuvchi: {}, Streamer: {}", user.getId(), streamerId);

            throw new BaseException(
                    "Faqat streamerning o'zi test donate amalga oshirishi mumkin",
                    HttpStatus.FORBIDDEN
            );
        }

        log.info("Test donat streamga uzatilmoqda: {}", donationCreateReq);

        executeToStream(new DonationEntity(
                streamer,
                donationCreateReq.getDonaterName(),
                donationCreateReq.getMessage(),
                true,
                new PaymentInfo("test", donationCreateReq.getAmount(), 0f)
        ));
    }
}