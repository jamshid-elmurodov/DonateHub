package uz.mydonation.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "targets_table")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TargetEntity extends BaseEntity {
    @ManyToOne
    private UserEntity streamer;

    private String description;

    private Integer targetAmount;

    private Integer currentAmount;
}
