package uz.mydonation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullStatisticRes {
    private Integer donatesCount;
    private Integer donatesAmount;
    private Integer withdrawsAmount;
    private Integer existAmount;
}
