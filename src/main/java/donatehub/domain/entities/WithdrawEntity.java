package donatehub.domain.entities;

import donatehub.domain.embeddables.WithdrawPayment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import donatehub.domain.constants.WithdrawStatus;

@Entity(name = "withdraws_table")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawEntity extends BaseEntity {
    @ManyToOne
    private UserEntity streamer;

    @Embedded
    private WithdrawPayment payment;

    @Enumerated(EnumType.STRING)
    private WithdrawStatus status;
}
