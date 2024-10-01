package donatehub.service.donation;

import donatehub.domain.projections.DonationFullStatistic;
import org.springframework.data.domain.Page;
import donatehub.domain.entities.UserEntity;
import donatehub.domain.constants.PaymentMethod;
import donatehub.domain.response.CreateDonateResponse;
import donatehub.domain.projections.DonationStatistic;
import donatehub.domain.projections.DonationInfo;
import donatehub.domain.request.DonationCreateRequest;

import java.util.List;

public interface DonationService {
    CreateDonateResponse donate(DonationCreateRequest donateReq, Long streamerId);

    void complete(String body, PaymentMethod method);

    Page<DonationInfo> getDonationsOfStreamer(Long streamerId, int page, int size);

    List<DonationStatistic> getDonationStatistics(int days);

    List<DonationStatistic> getDonationStatisticsOfStreamer(Long streamerId, int days);

    void testDonate(DonationCreateRequest donationCreateRequest, UserEntity streamer);

    DonationFullStatistic getFullStatistic();
}
