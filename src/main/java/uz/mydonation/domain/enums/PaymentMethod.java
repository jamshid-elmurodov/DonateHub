package uz.mydonation.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CLICK(3, 2),
    MIRPAY(3, 2);

    private final Integer donationCommissionPercentage;
    private final Integer withdrawCommissionPercentage;

    PaymentMethod(Integer donationCommissionPercentage, Integer withdrawCommissionPercentage) {
        this.donationCommissionPercentage = donationCommissionPercentage;
        this.withdrawCommissionPercentage = withdrawCommissionPercentage;
    }
}
