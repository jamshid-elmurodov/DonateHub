package uz.mydonation.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mydonation.domain.entity.WidgetEntity;

public interface WidgetRepository extends JpaRepository<WidgetEntity, Long> {
    WidgetEntity findByStreamerId(Long streamer_id);
}