package donatehub.domain.entities;

import donatehub.domain.embeddables.DonationPayment;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity(name = "donations_table")

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonationEntity extends BaseEntity {
    @ManyToOne
    private UserEntity streamer;

    @Column(name = "donater_name")
    private String donaterName;

    private String message;

    private Boolean completed;

    @Embedded
    private DonationPayment payment;
}
