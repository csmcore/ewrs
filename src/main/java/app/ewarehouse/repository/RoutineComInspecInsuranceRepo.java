package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceInspectorInsurance;

@Repository
public interface RoutineComInspecInsuranceRepo extends JpaRepository<RoutineComplianceInspectorInsurance, Long> {

}
