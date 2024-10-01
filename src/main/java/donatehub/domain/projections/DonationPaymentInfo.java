package donatehub.domain.projections;

import donatehub.domain.embeddables.DonationPayment;

/**
 * DTO for {@link DonationPayment}
 */
public interface DonationPaymentInfo {
    Float getAmount();

    Float getCommission();
}