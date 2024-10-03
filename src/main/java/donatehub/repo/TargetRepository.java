package donatehub.repo;

import donatehub.domain.entities.TargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TargetRepository extends JpaRepository<TargetEntity, UUID> {
}