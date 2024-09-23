package donatehub.domain.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import donatehub.domain.entity.UserEntity;

/**
 * Projection for {@link UserEntity}
 */
public interface UserInfo {
    @JsonProperty("id")
    Long getChatId();

    String getFirstName();

    String getUsername();

    String getDescription();

    String getChannelUrl();

    String getChannelName();

    String getProfileImgUrl();

    String getBannerImgUrl();

    Boolean getOnline();

    Boolean getEnable();

    Integer getBalance();
}