package donatehub.domain.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawPayment {
    @Column(name = "card_number")
    private String cardNumber;

    private Float amount;

    private Float commission;
}
