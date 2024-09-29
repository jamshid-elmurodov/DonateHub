package donatehub.domain.response;

import donatehub.domain.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    public UserProfileResponse(UserEntity user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.username = user.getUsername();
        this.description = user.getDescription();
        this.channelUrl = user.getChannelUrl();
        this.channelName = user.getChannelName();
        this.profileImgUrl = user.getProfileImgUrl();
        this.bannerImgUrl = user.getBannerImgUrl();
        this.token = user.getToken();
        this.online = user.getOnline();
        this.enable = user.getEnable();
        this.balance = user.getBalance();
    }

    private Long id;

    private String firstName;

    private String username;

    private String description;

    private String channelUrl;

    private String channelName;

    private String profileImgUrl;

    private String bannerImgUrl;

    private String token;

    private Boolean online;

    private Boolean enable;

    private Float balance;
}
