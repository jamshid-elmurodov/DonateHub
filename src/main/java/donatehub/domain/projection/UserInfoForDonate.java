package donatehub.domain.projection;

import donatehub.domain.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Projection for {@link UserEntity}
 */
@Schema
public interface UserInfoForDonate {
    Long getId();

    String getDescription();

    String getChannelUrl();

    String getChannelName();

    String getProfileImgUrl();

    String getBannerImgUrl();

    Boolean getOnline();

    Float getMinDonationAmount();
}