package uz.mydonation.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mydonation.domain.entity.DonationEntity;
import uz.mydonation.domain.model.FullStatisticRes;
import uz.mydonation.domain.model.StatisticRes;
import uz.mydonation.domain.projection.DonationInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DonationRepository extends JpaRepository<DonationEntity, Long> {
    Optional<DonationEntity> findByPaymentInfoPaymentId(String paymentInfo_id);

    Page<DonationInfo> getAllByStreamerIdAndCreatedAtAfterAndCompletedIsTrue(Long streamer_id, LocalDateTime createdAt, Pageable pageable);

    Page<DonationInfo> getAllByCreatedAtAfterAndCompletedIsTrue(LocalDateTime createdAt, Pageable pageable);

    @Query(
            value = "SELECT date_trunc('month', dt.created_at) AS day, SUM(dt.amount) AS amount " +
                    "FROM donations_table dt " +
                    "WHERE dt.created_at >= :date AND dt.completed = TRUE " +
                    "GROUP BY date_trunc('month', dt.created_at) " +
                    "ORDER BY day",
            nativeQuery = true
    )
    List<StatisticRes> getAllMonthlyStatistics(@Param("date") LocalDate date);

    @Query(
            value = "select dt.created_at::date, sum(dt.amount) " +
                    "from donations_table dt " +
                    "where dt.created_at::date >= :date and dt.completed = true " +
                    "group by dt.created_at::date " +
                    "order by dt.created_at::date " +
                    "limit :limit",
            nativeQuery = true
    )
    List<StatisticRes> getAllStatistics(@Param("date") LocalDate date, @Param("limit") int limit);

    @Query(
            value = "select dt.created_at::date, sum(dt.amount) " +
                    "from donations_table dt " +
                    "where dt.streamer_id = :id and dt.created_at::date > :date and dt.completed = true " +
                    "group by dt.created_at::date " +
                    "order by dt.created_at::date " +
                    "limit :limit",
            nativeQuery = true
    )
    List<StatisticRes> getStatisticsOfStreamer(@Param("id") Long streamerId, @Param("date") LocalDate date, @Param("limit") int limit);

    @Query(
            value = "select count(dt), sum(dt.amount), sum(wt.amount), (select at.amount from accounts_table at where at.id = :stid) " +
                    "from donations_table dt " +
                    "join withdraws_table wt " +
                    "on wt.streamer_id = :stid " +
                    "where dt.created_at::date >= :date",
            nativeQuery = true
    )
    FullStatisticRes getFullStatistic(@Param("stid") Long streamerId, @Param("date") LocalDate date);
}