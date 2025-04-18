package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceCeoApproval;

@Repository
public interface RoutineComplianceCeoApprovalRepository extends JpaRepository<RoutineComplianceCeoApproval, Integer> {


	Object findByRoutineComplianceIdAndBitDeleteFlagFalse(Long routineComplianceId);

}
