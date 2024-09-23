package donatehub.domain.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import donatehub.domain.model.PaymentInfo;

@Entity(name = "donations_table")

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonationEntity extends BaseEntity {
    @ManyToOne
    private UserEntity streamer;

    private String donaterName;

    private String message;

    private Boolean completed;

    @Embedded
    private PaymentInfo paymentInfo;
}
