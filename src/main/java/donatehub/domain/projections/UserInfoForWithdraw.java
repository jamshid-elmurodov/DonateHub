package donatehub.domain.projections;

import donatehub.domain.entities.UserEntity;

/**
 * Projection for {@link UserEntity}
 */
public interface UserInfoForWithdraw {
    Long getId();

    String getUsername();
}