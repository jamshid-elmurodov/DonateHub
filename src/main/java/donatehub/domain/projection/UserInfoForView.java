package donatehub.domain.projection;

import donatehub.domain.entity.UserEntity;

import java.time.LocalDateTime;

/**
 * Projection for {@link UserEntity}
 */
public interface UserInfoForView {
    Long getId();

    String getFirstName();

    String getUsername();

    String getProfileImgUrl();

    Boolean isOnline();

    Boolean isEnable();

    LocalDateTime getLastOnlineAt();

    Float getBalance();
}