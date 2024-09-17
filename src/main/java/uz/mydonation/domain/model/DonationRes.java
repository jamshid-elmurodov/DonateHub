package uz.mydonation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonationRes {
    private String donaterName;
    private String message;
    private Integer amount;
    private String videoUrl;
    private String audioUrl;
}