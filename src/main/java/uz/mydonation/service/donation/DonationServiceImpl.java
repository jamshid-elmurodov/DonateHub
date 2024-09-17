package uz.mydonation.service.donation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mydonation.domain.entity.DonationEntity;
import uz.mydonation.domain.entity.UserEntity;
import uz.mydonation.domain.entity.DonationWidgetEntity;
import uz.mydonation.domain.enums.PaymentMethod;
import uz.mydonation.domain.exception.BaseException;
import uz.mydonation.domain.model.*;
import uz.mydonation.domain.projection.DonationInfo;
import uz.mydonation.domain.request.DonationReq;
import uz.mydonation.repo.DonationRepository;
import uz.mydonation.service.payment.PaymentService;
import uz.mydonation.utils.BotExecutor;
import uz.mydonation.service.user.UserService;
import uz.mydonation.service.widget.WidgetService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonationServiceImpl implements DonationService {
    private final DonationRepository repo;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final WidgetService widgetService;
    private final PaymentService mirPayPaymentService;
    private final PaymentService clickPaymentService;
    private final BotExecutor botExecutor;

    public DonationServiceImpl(
            DonationRepository repo,
            UserService userService,
            ObjectMapper objectMapper,
            SimpMessagingTemplate messagingTemplate,
            WidgetService widgetService,
            @Qualifier("mirpay") PaymentService mirPayPaymentService,
            @Qualifier("click") PaymentService clickPaymentService,
            BotExecutor botExecutor
    ) {
        this.repo = repo;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
        this.widgetService = widgetService;
        this.mirPayPaymentService = mirPayPaymentService;
        this.clickPaymentService = clickPaymentService;
        this.botExecutor = botExecutor;
    }

    @Transactional
    @Override
    public String donate(DonationReq donateReq, Long streamerId) {
        UserEntity streamer = userService.findById(streamerId);
        String channelName = streamer.getChannelName().replaceAll(" ", "-");

        DonationEntity donation = DonationEntity.builder()
                .donaterName(donateReq.getDonaterName())
                .completed(false)
                .message(donateReq.getMessage())
                .build();

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .amount(donateReq.getAmount())
                .commission(getCommission(donateReq))
                .build();

        PaymentRes paymentRes = null;

        switch (donateReq.getMethod()) {
            case CLICK -> paymentRes = clickPaymentService.create(donateReq.getAmount() + paymentInfo.getCommission(), channelName);
            case MIRPAY -> paymentRes = mirPayPaymentService.create(donateReq.getAmount() + paymentInfo.getCommission(), channelName);
        }

        paymentInfo.setPaymentId(paymentRes.getId());
        donation.setPaymentInfo(paymentInfo);

        repo.save(donation);

        return paymentRes.getPayinfo().getRedicet_url();
    }

    @Override
    public void complete(String body, PaymentMethod method) {
        switch (method){
            case CLICK -> {
                completeClick(body);
            }
            case MIRPAY -> {
                completeMirPay(body);
            }
        }
    }

    private void completeClick(String body) {
    }

    private void completeMirPay(String body) {
        try {
            MirPayCompilationRes compilationRes = objectMapper.readValue(body, MirPayCompilationRes.class);
            DonationEntity donation = getDonationByPaymentId(compilationRes);

            if ("Muvaffaqiyatli".equals(compilationRes.getStatus())) {
                completeDonation(donation);
                return;
            }

            throw new BaseException(
                    String.format("Donat summasi to'liq amalga oshirilmagan %s", compilationRes.getId()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void completeDonation(DonationEntity donation) {
        donation.setCompleted(true);
        repo.save(donation);

        userService.addAmountToBalance(donation.getStreamer().getChatId(), donation.getPaymentInfo().getAmount());

        executeToStream(donation);
        botExecutor.sendDonationInfo(donation.getStreamer().getChatId(), donation);
    }

    private void executeToStream(DonationEntity donation) {
        DonationWidgetEntity widget = widgetService.getDonationWidgetOfStreamer(donation.getStreamer().getId());

        messagingTemplate.convertAndSend("/donation/" + donation.getStreamer().getApi(),
                new DonationRes(donation.getDonaterName(), donation.getMessage(),
                        donation.getPaymentInfo().getAmount(), widget.getVideoUrl(), widget.getAudioUrl()));
    }

    private DonationEntity getDonationByPaymentId(MirPayCompilationRes compilationRes) {
        return repo.findByPaymentInfoPaymentId(compilationRes.getId()).orElseThrow(
                () -> new BaseException(
                        "Donation not found with this payment id",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    private Integer getCommission(DonationReq donateReq) {
        return donateReq.getAmount() / 100 * donateReq.getMethod().getDonationCommissionPercentage();
    }

    @Override
    public Page<DonationInfo> getDonationsOfStreamer(Long streamerId, int page, int size, int days) {
        UserEntity user = userService.findById(streamerId);

        return repo.getAllByStreamerIdAndCreatedAtAfterAndCompletedIsTrue(user.getChatId(), LocalDateTime.now().minusDays(days + 1), PageRequest.of(page, size));
    }

    @Override
    public Page<DonationInfo> getDonations(int page, int size, int days) {
        return repo.getAllByCreatedAtAfterAndCompletedIsTrue(LocalDateTime.now().minusDays(days + 1), PageRequest.of(page, size));
    }

    @Override
    public List<StatisticRes> getStatisticsForAdmin(int days) {
        if (days > 30){
            return repo.getAllMonthlyStatistics(LocalDate.now().minusDays(days));
        }

        return repo.getAllStatistics(LocalDate.now().minusDays(days), days);
    }

    @Override
    public List<StatisticRes> getStatisticsForStreamer(Long streamerId, int days) {
        UserEntity user = userService.findById(streamerId);

        return repo.getStatisticsOfStreamer(user.getChatId(), LocalDate.now().minusDays(days), days);
    }

    @Override
    public FullStatisticRes getFullStatistic(Long streamerId, int days) {
        return repo.getFullStatistic(streamerId, LocalDate.now().minusDays(days));
    }
}
