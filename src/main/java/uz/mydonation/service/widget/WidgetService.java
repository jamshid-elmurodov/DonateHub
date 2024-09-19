package uz.mydonation.service.widget;

import org.springframework.web.multipart.MultipartFile;
import uz.mydonation.domain.entity.UserEntity;
import uz.mydonation.domain.entity.WidgetEntity;

public interface WidgetService {
    WidgetEntity create(UserEntity streamer);

    void update(Long streamerId, MultipartFile videoFile, MultipartFile audioFile);

    WidgetEntity getDonationWidgetOfStreamer(Long streamerId);
}
