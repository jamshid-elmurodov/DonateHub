package donatehub.domain.projections;

import donatehub.domain.entities.NotificationEntity;

import java.time.LocalDateTime;

/**
 * Projection for {@link NotificationEntity}
 */
public interface NotificationInfo {
    Long getId();

    LocalDateTime getCreatedAt();

    String getTitle();

    String getMessage();
}