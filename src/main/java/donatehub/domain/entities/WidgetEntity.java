package donatehub.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "widgets_table")

@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"streamer_id", "minDonateAmount"})
})

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WidgetEntity extends BaseEntity {
    @ManyToOne
    private UserEntity streamer;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "audio_url")
    private String audioUrl;

    private Integer time;

    @Column(name = "min_donate_amount")
    private Float minDonateAmount;
}
