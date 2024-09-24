package donatehub.domain.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import donatehub.domain.entity.UserEntity;

/**
 * Projection for {@link UserEntity}
 */
public interface UserInfoForDonate {
    @JsonProperty("id")
    Long getChatId();

    String getDescription();

    String getChannelUrl();

    String getChannelName();

    String getProfileImgUrl();

    String getBannerImgUrl();

    Boolean getOnline();

    Float getMinDonationAmount();
}