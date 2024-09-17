package uz.mydonation.service.widget;

import org.springframework.web.multipart.MultipartFile;
import uz.mydonation.domain.entity.DonationWidgetEntity;

public interface WidgetService {
    void create(Long streamerId);

    void update(Long streamerId, MultipartFile videoFile, MultipartFile audioFile);

    DonationWidgetEntity getDonationWidgetOfStreamer(Long streamerId);
}
