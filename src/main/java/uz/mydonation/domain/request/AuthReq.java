package uz.mydonation.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthReq {
    private Long id;
    @JsonProperty("first_name")
    private String firstName;
    private String username;
    @JsonProperty("auth_date")
    private Long authDate;
    private String hash;
}
