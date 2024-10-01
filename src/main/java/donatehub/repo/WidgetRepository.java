package donatehub.repo;

import donatehub.domain.projections.WidgetInfo;
import donatehub.domain.projections.WithdrawFullStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import donatehub.domain.entities.WidgetEntity;

import java.util.List;

public interface WidgetRepository extends JpaRepository<WidgetEntity, Long> {
    List<WidgetEntity> getAllByStreamerIdOrderByMinDonateAmountDesc(Long streamer_id);

    List<WidgetInfo> getAllByStreamerId(Long streamer_id);
}