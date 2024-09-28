package donatehub.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonationResponse {
    private String donaterName;
    private String message;
    private Float amount;
    private String videoUrl;
    private String audioUrl;
}