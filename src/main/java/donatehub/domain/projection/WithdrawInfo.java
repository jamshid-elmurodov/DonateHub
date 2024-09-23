package donatehub.domain.projection;

import donatehub.domain.entity.WithdrawEntity;
import donatehub.domain.enums.WithdrawStatus;

import java.time.LocalDateTime;

/**
 * Projection for {@link WithdrawEntity}
 */
public interface WithdrawInfo {
    Long getId();

    LocalDateTime getCreatedAt();

    String getCardNumber();

    Integer getAmount();

    WithdrawStatus getStatus();

    UserInfo getStreamer();
}