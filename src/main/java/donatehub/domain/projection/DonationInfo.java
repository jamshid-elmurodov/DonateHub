package donatehub.domain.projection;

import donatehub.domain.entity.DonationEntity;

import java.time.LocalDateTime;

/**
 * Projection for {@link DonationEntity}
 */
public interface DonationInfo {
    Long getId();

    LocalDateTime getCreatedAt();

    String getDonaterName();

    String getMessage();

    Integer getAmount();
}