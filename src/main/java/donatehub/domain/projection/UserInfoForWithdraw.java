package donatehub.domain.projection;

import donatehub.domain.entity.UserEntity;

/**
 * Projection for {@link UserEntity}
 */
public interface UserInfoForWithdraw {
    Long getId();

    String getUsername();
}