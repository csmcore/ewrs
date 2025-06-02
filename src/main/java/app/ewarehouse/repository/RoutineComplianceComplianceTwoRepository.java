package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceComplianceTwo;

@Repository
public interface RoutineComplianceComplianceTwoRepository extends JpaRepository<RoutineComplianceComplianceTwo, Integer> {


	Object findByRoutineComplianceIdAndBitDeleteFlagFalse(Long editId);

}
