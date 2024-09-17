package uz.mydonation.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.mydonation.domain.enums.PaymentMethod;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonationReq {
    private String donaterName;
    private String message;
    private Integer amount;
    private PaymentMethod method;
}
