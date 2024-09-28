package donatehub.domain.projections;

import donatehub.domain.entities.UserEntity;

import java.time.LocalDateTime;

/**
 * Projection for {@link UserEntity}
 */
public interface UserInfoForView {
    Long getId();

    String getFirstName();

    String getUsername();

    String getProfileImgUrl();

    Boolean getOnline();

    Boolean getEnable();

    LocalDateTime getLastOnlineAt();

    Float getBalance();
}