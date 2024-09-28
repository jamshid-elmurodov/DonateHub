package donatehub.service.widget;

import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.entities.UserEntity;
import donatehub.domain.entities.WidgetEntity;

public interface WidgetService {
    WidgetEntity create(UserEntity streamer);

    void update(Long streamerId, MultipartFile videoFile, MultipartFile audioFile);

    WidgetEntity getDonationWidgetOfStreamer(Long streamerId);
}
