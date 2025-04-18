package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceInspectorITSystem;

@Repository
public interface RoutineComplianceInspectorITSystemRepository extends JpaRepository<RoutineComplianceInspectorITSystem, Long> {
}
