package donatehub.domain.projections;

import donatehub.domain.entities.UserEntity;

/**
 * Projection for {@link UserEntity}
 */
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