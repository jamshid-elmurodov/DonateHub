package donatehub.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "widgets_table")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WidgetEntity extends BaseEntity {
    @Column(name = "stremaer_id")
    private Long streamerId;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "audio_url")
    private String audioUrl;

    private Integer time;
}
