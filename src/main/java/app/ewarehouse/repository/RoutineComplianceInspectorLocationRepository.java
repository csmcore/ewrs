package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceInspectorLocation;

@Repository
public interface RoutineComplianceInspectorLocationRepository extends JpaRepository<RoutineComplianceInspectorLocation, Long> {

}
