package donatehub.service.withdraw;

import donatehub.domain.projections.WithdrawFullStatistic;
import org.springframework.data.domain.Page;
import donatehub.domain.entities.WithdrawEntity;
import donatehub.domain.constants.WithdrawStatus;
import donatehub.domain.projections.WithdrawInfo;

public interface WithdrawService {
    void create(Long streamerId, Float amount, String cardNumber);

    void setStatus(Long withdrawId, WithdrawStatus status);

    WithdrawEntity findById(Long withdrawId);

    Page<WithdrawInfo> getWithdrawsByStatus(int page, int size, WithdrawStatus status);

    Page<WithdrawInfo> getWithdrawsOfStreamerByStatus(Long streamerId, int page, int size, WithdrawStatus status);

    WithdrawFullStatistic getFullStatistic();
}
