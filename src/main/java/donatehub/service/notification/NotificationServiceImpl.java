package donatehub.service.notification;

import donatehub.domain.entities.NotificationEntity;
import donatehub.domain.projections.NotificationInfo;
import donatehub.domain.request.NotificationRequest;
import donatehub.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repo;

    @Override
    public void create(NotificationRequest notificationRequest) {
        NotificationEntity saved = repo.save(new NotificationEntity(
                notificationRequest.getTitle(),
                notificationRequest.getMessage()
        ));


    }

    @Override
    public void update(Long id, NotificationRequest notificationRequest) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<NotificationInfo> getAll() {
        return List.of();
    }
}
