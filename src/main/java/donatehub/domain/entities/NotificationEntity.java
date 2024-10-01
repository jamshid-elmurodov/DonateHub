package donatehub.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private Boolean viewed;

    @Column(name = "seen_at")
    private LocalDateTime seenAt;
}
