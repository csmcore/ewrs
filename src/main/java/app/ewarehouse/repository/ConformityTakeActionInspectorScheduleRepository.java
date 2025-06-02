package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ConformityTakeActionInspectorSchedule;

@Repository
public interface ConformityTakeActionInspectorScheduleRepository extends JpaRepository<ConformityTakeActionInspectorSchedule, Integer>{

	ConformityTakeActionInspectorSchedule findByConformityId(Integer conformityId);
}
