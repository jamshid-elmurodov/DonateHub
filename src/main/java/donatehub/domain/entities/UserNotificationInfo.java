package donatehub.domain.entities;

import donatehub.domain.projections.NotificationInfo;

/**
 * Projection for {@link UserNotificationEntity}
 */
public interface UserNotificationInfo {
    Long getId();

    Boolean isViewed();

    NotificationInfo getNotification();
}