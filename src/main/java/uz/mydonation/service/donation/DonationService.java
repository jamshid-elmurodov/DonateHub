package uz.mydonation.service.donation;

import org.springframework.data.domain.Page;
import uz.mydonation.domain.enums.PaymentMethod;
import uz.mydonation.domain.model.FullStatisticRes;
import uz.mydonation.domain.model.StatisticRes;
import uz.mydonation.domain.projection.DonationInfo;
import uz.mydonation.domain.request.DonationReq;

import java.util.List;

public interface DonationService {
    String donate(DonationReq donateReq, Long streamerId);

    void complete(String body, PaymentMethod method);

    Page<DonationInfo> getDonationsOfStreamer(Long streamerId, int page, int size, int days);

    Page<DonationInfo> getDonations(int page, int size, int days);

    List<StatisticRes> getStatisticsForAdmin(int days);

    List<StatisticRes> getStatisticsForStreamer(Long streamerId, int days);

    FullStatisticRes getFullStatistic(Long streamerId, int days);
}
