package donatehub.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import donatehub.domain.entities.DonationEntity;
import donatehub.domain.projections.DonationStatisticResponse;
import donatehub.domain.projections.DonationInfo;

import java.util.List;
import java.util.Optional;

public interface DonationRepository extends JpaRepository<DonationEntity, Long> {
    Optional<DonationEntity> findByPaymentInfoPaymentId(String paymentInfo_id);

    Page<DonationInfo> getAllByStreamerIdAndCompletedIsTrueOrderByUpdateAt(Long streamer_id, Pageable pageable);

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
            value = "SELECT days, COALESCE(COUNT(dt.id), 0) AS donation_count " +
                    "FROM generate_series(current_date - INTERVAL '1 day' * :days, current_date, '1 day'::interval) AS days " +
                    "LEFT JOIN donations_table dt ON dt.completed = true AND dt.created_at::date = days " +
                    "GROUP BY days " +
                    "ORDER BY days",
            nativeQuery = true
    )
    List<DonationStatisticResponse> getAllStatistics(@Param("days") int days);

    @Query(
            value = "SELECT days AS day, COALESCE(SUM(dt.amount), 0) AS amount " +
                    "FROM generate_series(current_date - INTERVAL '1 day' * :days, current_date, '1 day'::interval) AS days " +
                    "LEFT JOIN donations_table dt ON dt.completed = true AND dt.streamer_id = :streamerId AND dt.created_at::date = days " +
                    "GROUP BY days " +
                    "ORDER BY days",
            nativeQuery = true
    )
    List<DonationStatisticResponse> getStatisticsOfStreamer(@Param("streamerId") Long streamerId, @Param("days") int days);
}