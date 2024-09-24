package donatehub.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import donatehub.domain.enums.WithdrawStatus;

@Entity(name = "withdraws_table")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawEntity extends BaseEntity {
    @ManyToOne
    private UserEntity streamer;

    @Column(name = "card_number")
    private String cardNumber;

    private Float amount;

    @Enumerated(EnumType.STRING)
    private WithdrawStatus status;
}
