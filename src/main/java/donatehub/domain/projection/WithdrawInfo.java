package donatehub.domain.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import donatehub.domain.entity.WithdrawEntity;
import donatehub.domain.enums.WithdrawStatus;

import java.time.LocalDateTime;

/**
 * Projection for {@link WithdrawEntity}
 */
public interface WithdrawInfo {
    Long getId();

    @JsonFormat(pattern = "dd/mm/yyyy HH:MM")
    LocalDateTime getCreatedAt();

    String getCardNumber();

    Integer getAmount();

    WithdrawStatus getStatus();

    UserInfoForWithdraw getStreamer();
}