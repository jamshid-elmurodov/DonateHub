package donatehub.service.notification;

import donatehub.domain.projections.NotificationInfo;
import donatehub.domain.request.NotificationRequest;

import java.util.List;

public interface NotificationService {
    void create(NotificationRequest notificationRequest);

    void update(Long id, NotificationRequest notificationRequest);

    void delete(Long id);

    List<NotificationInfo> getAll();
}
