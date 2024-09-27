package donatehub.service.withdraw;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import donatehub.domain.entity.UserEntity;
import donatehub.domain.entity.WithdrawEntity;
import donatehub.domain.enums.WithdrawStatus;
import donatehub.domain.exception.BaseException;
import donatehub.domain.projection.WithdrawInfo;
import donatehub.repo.WithdrawRepository;
import donatehub.service.user.UserService;

@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {
    private Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");;

    private final WithdrawRepository repo;
    private final UserService userService;

    @Override
    public void create(Long streamerId, Float amount, String cardNumber) {
        log.info("Chiqarish so'rovi yaratilmoqda: streamerId - {}, amount - {}, cardNumber - {}", streamerId, amount, cardNumber);

        UserEntity user = userService.findById(streamerId);

        if (user.getBalance() < amount + getCommission(amount)) {
            log.error("Balansingizda mablag' yetarli emas: userId - {}, neededAmount - {}", streamerId, amount + getCommission(amount));
            throw new BaseException(
                    "Balansingizda mablag' yetarli emas",
                    HttpStatus.BAD_REQUEST
            );
        }

        WithdrawEntity withdrawEntity = new WithdrawEntity(
                user,
                cardNumber,
                amount,
                WithdrawStatus.PENDING
        );
        repo.save(withdrawEntity);

        log.info("Chiqarish so'rovi yaratildi: {}", withdrawEntity);
    }

    @Override
    public void setStatus(Long withdrawId, WithdrawStatus status) {
        log.info("Chiqarish so'rovining holati yangilanmoqda: withdrawId - {}, newStatus - {}", withdrawId, status);

        WithdrawEntity withdraw = findById(withdrawId);
        withdraw.setStatus(status);

        repo.save(withdraw);

        log.info("Chiqarish so'rovining holati yangilandi: {}", withdraw);
    }

    @Override
    public WithdrawEntity findById(Long withdrawId) {
        log.info("Chiqarish so'rovi olinmoqda: withdrawId - {}", withdrawId);
        return repo.findById(withdrawId).orElseThrow(
                () -> {
                    log.error("Chiqarish so'rovi topilmadi: withdrawId - {}", withdrawId);
                    return new BaseException(
                            "Chiqarish bo'yicha so'rov topilmadi",
                            HttpStatus.NOT_FOUND
                    );
                }
        );
    }

    private Float getCommission(Float amount) {
        return amount / 100 * 3f;
    }

    @Override
    public Page<WithdrawInfo> getWithdrawsByStatus(int page, int size, WithdrawStatus status) {
        log.info("Holat bo'yicha chiqarish so'rovlarini olish: status - {}, page - {}, size - {}", status, page, size);
        return repo.getAllByStatusOrderByCreatedAt(status, PageRequest.of(page, size));
    }

    @Override
    public Page<WithdrawInfo> getWithdrawsOfStreamerByStatus(Long streamerId, int page, int size, WithdrawStatus status) {
        log.info("Streamer bo'yicha holat bo'yicha chiqarish so'rovlarini olish: streamerId - {}, status - {}, page - {}, size - {}", streamerId, status, page, size);
        return repo.getAllByStreamerIdAndStatusOrderByCreatedAt(streamerId, status, PageRequest.of(page, size));
    }
}
