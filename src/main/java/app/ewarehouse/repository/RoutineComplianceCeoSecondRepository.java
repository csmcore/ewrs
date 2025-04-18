package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceCeoSecond;

@Repository
public interface RoutineComplianceCeoSecondRepository extends JpaRepository<RoutineComplianceCeoSecond, Integer> {


	Object findByRoutineComplianceIdAndBitDeleteFlagFalse(Long editId);

}
