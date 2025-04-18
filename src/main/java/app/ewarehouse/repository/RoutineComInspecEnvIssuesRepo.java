package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceInspectorEnvironmentIssues;

@Repository
public interface RoutineComInspecEnvIssuesRepo extends JpaRepository<RoutineComplianceInspectorEnvironmentIssues, Long> {

}
