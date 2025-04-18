package app.ewarehouse.repository;

import app.ewarehouse.entity.RoutineComplianceInspectorShrink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineComInspecShrinkRepo extends JpaRepository<RoutineComplianceInspectorShrink, Long> {
}
