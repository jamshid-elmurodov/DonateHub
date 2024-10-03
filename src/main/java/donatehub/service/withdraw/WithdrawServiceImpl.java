package donatehub.service.withdraw;

import donatehub.domain.embeddables.WithdrawPayment;
import donatehub.domain.projections.WithdrawFullStatistic;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import donatehub.domain.entities.UserEntity;
import donatehub.domain.entities.WithdrawEntity;
import donatehub.domain.constants.WithdrawStatus;
import donatehub.domain.exceptions.BaseException;
import donatehub.domain.projections.WithdrawInfo;
import donatehub.repo.WithdrawRepository;
import donatehub.service.user.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {
    private final Logger log;
    private final WithdrawRepository repo;
    private final UserService userService;

    @Override
    public void create(Long streamerId, Float amount, String cardNumber) {
        log.info("Chiqarish so'rovi yaratilmoqda: streamerId - {}, amount - {}, cardNumber - {}", streamerId, amount, cardNumber);

        UserEntity user = userService.findById(streamerId);

        Float commission = getCommission(amount);

        if (user.getBalance() < amount + commission) {
            log.error("Balansda mablag' yetarli emas: userId - {}, neededAmount - {}", streamerId, amount + commission);

            throw new BaseException(
                    "Balansingizda mablag' yetarli emas: miqdor - " + user.getBalance() + " so'm, kommissiya - " + commission + " so'm",
                    HttpStatus.BAD_REQUEST
            );
        }

        WithdrawEntity withdraw = new WithdrawEntity(
                user,
                new WithdrawPayment(cardNumber, amount, commission),
                WithdrawStatus.PENDING
        );

        withdraw.setUpdateAt(LocalDateTime.now());

        WithdrawEntity saved = repo.save(withdraw);

        log.info("Chiqarish so'rovi yaratildi: {}", saved.getId());
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
                () -> new BaseException(
                        "Chiqarish bo'yicha so'rov topilmadi id:" + withdrawId,
                        HttpStatus.NOT_FOUND
                )
        );
    }

    @Override
    public Page<WithdrawInfo> getWithdrawsByStatus(int page, int size, WithdrawStatus status) {
        log.info("Holat bo'yicha chiqarish so'rovlarini olish: status - {}, page - {}, size - {}", status, page, size);
        return repo.getAllByStatusOrderByUpdateAt(status, PageRequest.of(page, size));
    }

    @Override
    public Page<WithdrawInfo> getWithdrawsOfStreamerByStatus(Long streamerId, int page, int size, WithdrawStatus status) {
        log.info("Streamer bo'yicha holat bo'yicha chiqarish so'rovlarini olish: streamerId - {}, status - {}, page - {}, size - {}", streamerId, status, page, size);
        return repo.getAllByStreamerIdAndStatusOrderByCreatedAt(streamerId, status, PageRequest.of(page, size));
    }

    @Override
    public WithdrawFullStatistic getFullStatistic() {
        return repo.getFullStatistic();
    }

    private Float getCommission(Float amount) {
        log.info("Komissiyani hisoblanmoqda: amount - {}", amount);
        return amount / 100 * 3f;
    }
}
