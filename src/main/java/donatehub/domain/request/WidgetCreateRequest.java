package donatehub.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WidgetCreateRequest {
    @NotNull
    private Float minAmount;

    @NotNull
    private Integer time;
}
