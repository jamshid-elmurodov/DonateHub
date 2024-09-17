package uz.mydonation.service.withdraw;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.mydonation.domain.entity.UserEntity;
import uz.mydonation.domain.entity.WithdrawEntity;
import uz.mydonation.domain.enums.WithdrawStatus;
import uz.mydonation.domain.exception.BaseException;
import uz.mydonation.domain.projection.WithdrawInfo;
import uz.mydonation.repo.WithdrawRepository;
import uz.mydonation.service.user.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {
    private final WithdrawRepository repo;
    private final UserService userService;

    @Override
    public void create(Long streamerId, Integer amount, String cardNumber) {
        UserEntity user = userService.findById(streamerId);

        if (user.getBalance() < amount + getCommission(amount)) {
            throw new BaseException(
                    "Balansingizda mablag' yetarli emas",
                    HttpStatus.BAD_REQUEST
            );
        }

        repo.save(new WithdrawEntity(
                user,
                cardNumber,
                amount,
                WithdrawStatus.PENDING
        ));
    }

    @Override
    public void setStatus(Long withdrawId, WithdrawStatus status) {
        WithdrawEntity withdraw = findById(withdrawId);

        withdraw.setStatus(status);

        repo.save(withdraw);
    }

    @Override
    public WithdrawEntity findById(Long withdrawId) {
        return repo.findById(withdrawId).orElseThrow(
                () -> new BaseException(
                        "Chiqarish bo'yicha so'rov topilmadi",
                        HttpStatus.NOT_FOUND
                )
        );
    }

    private Integer getCommission(Integer amount) {
        return amount / 100 * 2;
    }

    @Override
    public Page<WithdrawInfo> getWithdrawsByStatus(int page, int size, int days, WithdrawStatus status) {
        return repo.getAllByCreatedAtAfterAndStatus(LocalDateTime.now().minusDays(days), status, PageRequest.of(page, size));
    }

    @Override
    public Page<WithdrawInfo> getWithdrawsOfStreamerByStatus(Long streamerId, int page, int size, int days, WithdrawStatus status) {
        return repo.getAllByStreamerIdAndCreatedAtAfterAndStatus(streamerId, LocalDateTime.now().minusDays(days), status, PageRequest.of(page, size));
    }
}
