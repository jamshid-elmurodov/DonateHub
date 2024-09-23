package donatehub.service.withdraw;

import org.springframework.data.domain.Page;
import donatehub.domain.entity.WithdrawEntity;
import donatehub.domain.enums.WithdrawStatus;
import donatehub.domain.projection.WithdrawInfo;

public interface WithdrawService {
    void create(Long streamerId, Float amount, String cardNumber);

    void setStatus(Long withdrawId, WithdrawStatus status);

    WithdrawEntity findById(Long withdrawId);

    Page<WithdrawInfo> getWithdrawsByStatus(int page, int size, int days, WithdrawStatus status);

    Page<WithdrawInfo> getWithdrawsOfStreamerByStatus(Long streamerId, int page, int size, int days, WithdrawStatus status);
}
