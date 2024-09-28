package donatehub.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import donatehub.domain.entities.WithdrawEntity;
import donatehub.domain.constants.WithdrawStatus;
import donatehub.domain.projections.WithdrawInfo;

public interface WithdrawRepository extends JpaRepository<WithdrawEntity, Long> {
    Page<WithdrawInfo> getAllByStatusOrderByCreatedAt(WithdrawStatus status, Pageable pageable);

    Page<WithdrawInfo> getAllByStreamerIdAndStatusOrderByCreatedAt(Long streamer_id, WithdrawStatus status, Pageable pageable);
}