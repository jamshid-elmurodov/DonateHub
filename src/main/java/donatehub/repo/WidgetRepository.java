package donatehub.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import donatehub.domain.entities.WidgetEntity;

public interface WidgetRepository extends JpaRepository<WidgetEntity, Long> {
    WidgetEntity findByStreamerId(Long streamer_id);
}