package uz.mydonation.service.withdraw;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger log = LoggerFactory.getLogger("CUSTOM_LOGGER");;

    private final WithdrawRepository repo;
    private final UserService userService;

    @Override
    public void create(Long streamerId, Integer amount, String cardNumber) {
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

    private Integer getCommission(Integer amount) {
        return amount / 100 * 2;
    }

    @Override
    public Page<WithdrawInfo> getWithdrawsByStatus(int page, int size, int days, WithdrawStatus status) {
        log.info("Holat bo'yicha chiqarish so'rovlarini olish: status - {}, page - {}, size - {}, days - {}", status, page, size, days);
        return repo.getAllByCreatedAtAfterAndStatus(LocalDateTime.now().minusDays(days), status, PageRequest.of(page, size));
    }

    @Override
    public Page<WithdrawInfo> getWithdrawsOfStreamerByStatus(Long streamerId, int page, int size, int days, WithdrawStatus status) {
        log.info("Streamer bo'yicha holat bo'yicha chiqarish so'rovlarini olish: streamerId - {}, status - {}, page - {}, size - {}, days - {}", streamerId, status, page, size, days);
        return repo.getAllByStreamerIdAndCreatedAtAfterAndStatus(streamerId, LocalDateTime.now().minusDays(days), status, PageRequest.of(page, size));
    }
}
