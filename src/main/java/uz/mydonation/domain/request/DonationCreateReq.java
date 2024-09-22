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
public class DonationCreateReq {
    private String donaterName;
    private String message;
    private Float amount;
    private PaymentMethod method;
}
