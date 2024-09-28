package donatehub.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PaymentInfo {
    @Column(name = "payment_id")
    private String paymentId;

    private Float amount;

    private Float commission;
}
