package uz.mydonation.domain.projection;

import java.time.LocalDateTime;

/**
 * Projection for {@link uz.mydonation.domain.entity.DonationEntity}
 */
public interface DonationInfo {
    Long getId();

    LocalDateTime getCreatedAt();

    String getDonaterName();

    String getMessage();

    Integer getAmount();
}