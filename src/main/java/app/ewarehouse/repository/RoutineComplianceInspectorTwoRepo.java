package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceInspectorTwo;

@Repository
public interface RoutineComplianceInspectorTwoRepo extends JpaRepository<RoutineComplianceInspectorTwo, Integer> {

	RoutineComplianceInspectorTwo findByRoutineComplianceIdAndBitDeleteFlagFalse(Long routinComplianceId);

}
