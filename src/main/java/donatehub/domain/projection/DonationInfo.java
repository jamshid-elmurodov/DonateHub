package donatehub.domain.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import donatehub.domain.entity.DonationEntity;
import donatehub.domain.model.PaymentInfo;
import donatehub.domain.model.PaymentInfoDto;

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

    PaymentInfoDto getPaymentInfo();
}
