package donatehub.domain.projections;

import donatehub.domain.entities.PaymentInfo;

/**
 * DTO for {@link PaymentInfo}
 */
public interface PaymentInfoDto {
    Float getAmount();
    Float getCommission();
}