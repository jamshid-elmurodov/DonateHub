package uz.mydonation.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.mydonation.domain.enums.WithdrawStatus;

@Entity(name = "withdraws_table")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawEntity extends BaseEntity {
    @ManyToOne
    private UserEntity streamer;

    private String cardNumber;

    private Integer amount;

    private WithdrawStatus status;
}
