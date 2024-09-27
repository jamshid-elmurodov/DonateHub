package donatehub.service.donation;

import org.springframework.data.domain.Page;
import donatehub.domain.entity.UserEntity;
import donatehub.domain.enums.PaymentMethod;
import donatehub.domain.response.CreateDonateRes;
import donatehub.domain.response.FullStatisticRes;
import donatehub.domain.response.DonationStatisticRes;
import donatehub.domain.projection.DonationInfo;
import donatehub.domain.request.DonationCreateReq;

import java.util.List;

public interface DonationService {
    CreateDonateRes donate(DonationCreateReq donateReq, Long streamerId);

    void complete(String body, PaymentMethod method);

    Page<DonationInfo> getDonationsOfStreamer(Long streamerId, int page, int size);

    Page<DonationInfo> getAllDonations(int page, int size);

    List<DonationStatisticRes> getStatisticsForAdmin(int days);

    List<DonationStatisticRes> getStatisticsForStreamer(Long streamerId, int days);

    void testDonate(DonationCreateReq donationCreateReq, Long streamerId, UserEntity user);
}
