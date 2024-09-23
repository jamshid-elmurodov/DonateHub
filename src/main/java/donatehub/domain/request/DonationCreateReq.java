package donatehub.domain.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import donatehub.domain.enums.PaymentMethod;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonationCreateReq {
    @NotBlank
    private String donaterName;
    @NotBlank
    private String message;
    @Min(value = 1000, message = "Minimal donat summasi 1000 so'm")
    private Float amount;
    @NotNull
    private PaymentMethod method;
}
