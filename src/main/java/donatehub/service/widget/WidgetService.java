package donatehub.service.widget;

import donatehub.domain.projections.WidgetInfo;
import donatehub.domain.request.WidgetCreateRequest;
import org.springframework.web.multipart.MultipartFile;
import donatehub.domain.entities.WidgetEntity;

import java.util.List;

public interface WidgetService {
    WidgetEntity create(Long streamerId, WidgetCreateRequest widgetCreateRequest, MultipartFile videoFile, MultipartFile audioFile);

    void update(Long streamerId, MultipartFile videoFile, MultipartFile audioFile, Long userId);

    WidgetEntity getWidgetOfStreamer(Long streamerId, Float amount);

    List<WidgetInfo> getAllByStreamerId(Long streamerId);
}
