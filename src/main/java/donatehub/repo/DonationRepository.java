package donatehub.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import donatehub.domain.entities.DonationEntity;
import donatehub.domain.response.DonationStatisticResponse;
import donatehub.domain.projections.DonationInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DonationRepository extends JpaRepository<DonationEntity, Long> {
    Optional<DonationEntity> findByPaymentInfoPaymentId(String paymentInfo_id);

    Page<DonationInfo> getAllByStreamerIdAndCompletedIsTrue(Long streamer_id, Pageable pageable);

    Page<DonationInfo> getAllByCompletedIsTrue(Pageable pageable);

    @Query(
            value = "SELECT date_trunc('month', days) AS day, SUM(dt.amount) AS amount " +
                    "FROM donations_table dt " +
                    "LEFT JOIN generate_series(current_date - INTERVAL '1 day' * :days, current_date, '1 day'::interval) AS days ON dt.completed = true " +
                    "GROUP BY date_trunc('month', days)" +
                    "ORDER BY day",
            nativeQuery = true
    )
    List<DonationStatisticResponse> getAllMonthlyStatistics(@Param("days") int days);

    @Query(
            value = "select days, coalesce(count(*), 0) from donations_table " +
                    "left join generate_series(current_date - '1 day' * :days, current_date, '1 day'::interval) as days " +
                    "group by days " +
                    "order by days",
            nativeQuery = true
    )
    List<DonationStatisticResponse> getAllStatistics(@Param("days") int days);

    @Query(
            value = "select dt.created_at::date, sum(dt.amount) " +
                    "from donations_table dt " +
                    "where dt.streamer_id = :id and dt.created_at::date > :date and dt.completed = true " +
                    "group by dt.created_at::date " +
                    "order by dt.created_at::date " +
                    "limit :limit",
            nativeQuery = true
    )
    List<DonationStatisticResponse> getStatisticsOfStreamer(@Param("id") Long streamerId, @Param("date") LocalDate date, @Param("limit") int limit);
}