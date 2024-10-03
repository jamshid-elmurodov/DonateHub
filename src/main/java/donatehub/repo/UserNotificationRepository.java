package donatehub.repo;

import donatehub.domain.entities.UserNotificationEntity;
import donatehub.domain.entities.UserNotificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotificationEntity, Long> {
    @Modifying
    @Query(
            value = "update user_notifications_table set viewed = true where streamer.id = :id"
    )
    void markAllAsRead(@Param("id") Long userId);

    List<UserNotificationInfo> getAllByStreamerId(Long streamer_id);
}