package uz.mydonation.domain.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PaymentInfo {
    private String paymentId;

    private Integer amount;

    private Integer commission;
}
