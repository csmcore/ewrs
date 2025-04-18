package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceTrail;

@Repository
public interface RoutineComplianceTrailRepository extends JpaRepository<RoutineComplianceTrail, Integer> {

}
