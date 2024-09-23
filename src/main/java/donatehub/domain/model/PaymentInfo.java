package donatehub.domain.model;

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

    private Float amount;

    private Float commission;
}
