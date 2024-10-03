package donatehub.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "notifications_table")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEntity extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;
}
