package donatehub.repo;

import donatehub.domain.projections.WithdrawFullStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import donatehub.domain.entities.WithdrawEntity;
import donatehub.domain.constants.WithdrawStatus;
import donatehub.domain.projections.WithdrawInfo;
import org.springframework.data.jpa.repository.Query;

public interface WithdrawRepository extends JpaRepository<WithdrawEntity, Long> {
    Page<WithdrawInfo> getAllByStatusOrderByUpdateAt(WithdrawStatus status, Pageable pageable);

    Page<WithdrawInfo> getAllByStreamerIdAndStatusOrderByCreatedAt(Long streamer_id, WithdrawStatus status, Pageable pageable);

    @Query(
            value = """
            select
                count(*) as totalCount,
                count(case when status = 'PENDING' then 1 end) as pendingCount,
                count(case when status = 'COMPLETED' then 1 end) as completedCount,
                count(case when status = 'CANCELED' then 1 end) as canceledCount,
                coalesce(sum(case when status = 'COMPLETED' then amount end), 0) as completedAmount
            from withdraws_table
            """,
            nativeQuery = true
    )
    WithdrawFullStatistic getFullStatistic();
}