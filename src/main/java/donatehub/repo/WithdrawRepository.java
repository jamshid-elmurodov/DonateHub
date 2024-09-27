package donatehub.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import donatehub.domain.entity.WithdrawEntity;
import donatehub.domain.enums.WithdrawStatus;
import donatehub.domain.projection.WithdrawInfo;

public interface WithdrawRepository extends JpaRepository<WithdrawEntity, Long> {
    Page<WithdrawInfo> getAllByStatusOrderByCreatedAt(WithdrawStatus status, Pageable pageable);

    Page<WithdrawInfo> getAllByStreamerIdAndStatusOrderByCreatedAt(Long streamer_id, WithdrawStatus status, Pageable pageable);
}