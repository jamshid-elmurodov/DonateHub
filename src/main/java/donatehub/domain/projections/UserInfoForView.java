package donatehub.domain.projections;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "dd/mm/yyyy HH:MM")
    LocalDateTime getLastOnlineAt();

    Float getBalance();
}