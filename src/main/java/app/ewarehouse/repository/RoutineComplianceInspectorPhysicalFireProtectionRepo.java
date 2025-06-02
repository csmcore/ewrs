package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceInspectorPhysicalFireProtection;

@Repository
public interface RoutineComplianceInspectorPhysicalFireProtectionRepo extends JpaRepository<RoutineComplianceInspectorPhysicalFireProtection, Long> {

}
