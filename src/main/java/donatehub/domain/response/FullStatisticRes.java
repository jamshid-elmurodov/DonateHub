package donatehub.domain.response;

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
    private Float donatesAmount;
    private Float withdrawsAmount;
    private Float existAmount;
}
