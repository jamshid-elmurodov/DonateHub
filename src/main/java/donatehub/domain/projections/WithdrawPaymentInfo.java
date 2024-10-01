package donatehub.domain.projections;

import donatehub.domain.embeddables.WithdrawPayment;

/**
 * Projection for {@link WithdrawPayment}
 */
public interface WithdrawPaymentInfo {
    String getCardNumber();

    Float getAmount();

    Float getCommission();
}