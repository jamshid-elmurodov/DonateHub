package donatehub.domain.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonationPayment {
    @Column(name = "payment_id")
    private String paymentId;

    private Float amount;

    private Float commission;
}
