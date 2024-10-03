package donatehub.service.user_notification;

import donatehub.domain.entities.NotificationEntity;
import donatehub.domain.entities.UserEntity;
import donatehub.domain.entities.UserNotificationEntity;
import donatehub.domain.entities.UserNotificationInfo;
import donatehub.repo.UserNotificationRepository;
import donatehub.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNotificationServiceImpl implements UserNotificationService {
    private final UserNotificationRepository repo;
    private final UserService userService;

    @Override
    public void create(NotificationEntity notification) {
        List<UserEntity> users = userService.getAllEnabledUsers();

        for (UserEntity user : users) {
            repo.save(new UserNotificationEntity(user, notification, false));
        }
    }

    @Override
    public void markAllAsRead(Long userId) {
        repo.markAllAsRead(userId);
    }

    @Override
    public List<UserNotificationInfo> getAllNotificationsOfUser(Long userId) {
        return repo.getAllByStreamerId(userId);
    }
}
