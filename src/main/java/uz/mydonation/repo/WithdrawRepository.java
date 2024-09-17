package uz.mydonation.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.mydonation.domain.entity.WithdrawEntity;
import uz.mydonation.domain.enums.WithdrawStatus;
import uz.mydonation.domain.projection.WithdrawInfo;

import java.time.LocalDateTime;

public interface WithdrawRepository extends JpaRepository<WithdrawEntity, Long> {
    Page<WithdrawInfo> getAllByCreatedAtAfterAndStatus(LocalDateTime createdAt, WithdrawStatus status, Pageable pageable);

    Page<WithdrawInfo> getAllByStreamerIdAndCreatedAtAfterAndStatus(Long streamer_id, LocalDateTime createdAt, WithdrawStatus status, Pageable pageable);
}