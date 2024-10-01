package donatehub.domain.projections;

import com.fasterxml.jackson.annotation.JsonFormat;
import donatehub.domain.entities.DonationEntity;

import java.time.LocalDateTime;

/**
 * Projection for {@link DonationEntity}
 */
public interface DonationInfo {
    Long getId();

    @JsonFormat(pattern = "dd/mm/yyyy HH:MM")
    LocalDateTime getCreatedAt();

    String getDonaterName();

    String getMessage();

    DonationPaymentInfo getPayment();
}
