package uz.mydonation.service.withdraw;

import org.springframework.data.domain.Page;
import uz.mydonation.domain.entity.WithdrawEntity;
import uz.mydonation.domain.enums.WithdrawStatus;
import uz.mydonation.domain.projection.WithdrawInfo;

public interface WithdrawService {
    void create(Long streamerId, Integer amount, String cardNumber);

    void setStatus(Long withdrawId, WithdrawStatus status);

    Page<WithdrawInfo> getWithdrawsByStatus(int page, int size, int days, WithdrawStatus status);

    Page<WithdrawInfo> getWithdrawsOfStreamerByStatus(Long streamerId, int page, int size, int days, WithdrawStatus status);

    WithdrawEntity findById(Long withdrawId);
}
