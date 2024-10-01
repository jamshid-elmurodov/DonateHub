package donatehub.domain.projections;

import com.fasterxml.jackson.annotation.JsonFormat;
import donatehub.domain.entities.WithdrawEntity;
import donatehub.domain.constants.WithdrawStatus;

import java.time.LocalDateTime;

/**
 * Projection for {@link WithdrawEntity}
 */
public interface WithdrawInfo {
    Long getId();

    @JsonFormat(pattern = "dd/mm/yyyy HH:MM")
    LocalDateTime getCreatedAt();

    WithdrawPaymentInfo getPayment();

    WithdrawStatus getStatus();

    UserInfoForWithdraw getStreamer();
}