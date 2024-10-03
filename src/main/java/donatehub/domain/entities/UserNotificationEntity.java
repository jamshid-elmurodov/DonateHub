package donatehub.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_notifications_table")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationEntity extends BaseEntity {
    @OneToOne
    private UserEntity streamer;

    @OneToOne
    private NotificationEntity notification;

    private Boolean viewed;
}
