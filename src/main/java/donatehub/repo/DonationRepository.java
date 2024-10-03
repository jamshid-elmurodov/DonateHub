package donatehub.repo;

import donatehub.domain.projections.DonationFullStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import donatehub.domain.entities.DonationEntity;
import donatehub.domain.projections.DonationStatistic;
import donatehub.domain.projections.DonationInfo;
import org.springframework.scheduling.annotation.Schedules;

import java.util.List;
import java.util.Optional;

public interface DonationRepository extends JpaRepository<DonationEntity, Long> {
    Optional<DonationEntity> findByPaymentPaymentId(String paymentInfo_id);

    Page<DonationInfo> getAllByStreamerIdAndCompletedIsTrueOrderByUpdateAt(Long streamer_id, Pageable pageable);

    @Query(
            value = "SELECT date_trunc('month', days) AS day, COALESCE(SUM(dt.amount), 0) AS amount " +
                    "FROM generate_series(current_date - INTERVAL '1 day' * :days, current_date, '1 day'::interval) AS days " +
                    "LEFT JOIN donations_table dt ON dt.completed = true AND dt.created_at::date = days::date " +
                    "GROUP BY date_trunc('month', days)" +
                    "ORDER BY day",
            nativeQuery = true
    )
    List<DonationStatistic> getAllMonthlyStatistics(@Param("days") int days);

    @Query(
            value = "SELECT days, COALESCE(COUNT(dt.id), 0) AS donation_count " +
                    "FROM generate_series(current_date - INTERVAL '1 day' * :days, current_date, '1 day'::interval) AS days " +
                    "LEFT JOIN donations_table dt ON dt.completed = true AND dt.created_at::date = days " +
                    "GROUP BY days " +
                    "ORDER BY days",
            nativeQuery = true
    )
    List<DonationStatistic> getAllStatistics(@Param("days") int days);

    @Query(
            value = "SELECT days AS day, COALESCE(SUM(dt.amount), 0) AS amount " +
                    "FROM generate_series(current_date - INTERVAL '1 day' * :days, current_date, '1 day'::interval) AS days " +
                    "LEFT JOIN donations_table dt ON dt.completed = true AND dt.streamer_id = :streamerId AND dt.created_at::date = days " +
                    "GROUP BY days " +
                    "ORDER BY days",
            nativeQuery = true
    )
    List<DonationStatistic> getStatisticsOfStreamer(@Param("streamerId") Long streamerId, @Param("days") int days);

    @Query(
            value = """
            select
                count(case when completed = true then 1 end) as totalCount,
                sum(case when completed = true then amount end) as totalAmount,
                count(case when completed = true and created_at >= CURRENT_DATE then 1 end) as dailyCount,
                coalesce(sum(case when completed = true and created_at >= CURRENT_DATE then amount end), 0) as dailyAmount
            from donations_table
            """,
            nativeQuery = true
    )
    DonationFullStatistic getFullStatistic();
}