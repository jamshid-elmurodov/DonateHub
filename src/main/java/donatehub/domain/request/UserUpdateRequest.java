package donatehub.domain.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank
    private String description;

    @NotBlank
    private String channelName;

    @NotBlank
    private String channelUrl;

    @Min(value = 1000, message = "Minimal donat summasi 1000 so'm")
    private Float minDonationAmount;
}