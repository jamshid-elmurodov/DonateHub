package donatehub.domain.entity;

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
    private Long streamerId;

    private String videoUrl;

    private String audioUrl;

    private Integer time;
}
