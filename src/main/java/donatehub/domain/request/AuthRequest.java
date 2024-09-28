package donatehub.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotNull
    private Long id;

    @JsonProperty("first_name")
    @NotNull
    private String firstName;

    private String username;

    @NotNull
    @JsonProperty("auth_date")
    private Long authDate;

    @NotBlank
    private String hash;
}
