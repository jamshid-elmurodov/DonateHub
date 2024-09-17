package uz.mydonation.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "settings_table")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonationWidgetEntity extends BaseEntity {
    @OneToOne
    private UserEntity streamer;

    private String videoUrl;

    private String audioUrl;

    private Integer time;
}
