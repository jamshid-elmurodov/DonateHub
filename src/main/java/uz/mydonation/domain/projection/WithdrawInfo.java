package uz.mydonation.domain.projection;

import uz.mydonation.domain.entity.WithdrawEntity;
import uz.mydonation.domain.enums.WithdrawStatus;

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