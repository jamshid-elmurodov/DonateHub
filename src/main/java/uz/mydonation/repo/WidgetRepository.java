package uz.mydonation.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mydonation.domain.entity.DonationWidgetEntity;

import java.util.UUID;

public interface WidgetRepository extends JpaRepository<DonationWidgetEntity, Long> {
    DonationWidgetEntity findByStreamerApi(UUID streamer_api);

    DonationWidgetEntity findByStreamerId(Long streamer_id);
}