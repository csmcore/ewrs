package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ConformityTakeActionInspector;



@Repository
public interface ConformityTakeActionInspectorRepository extends JpaRepository<ConformityTakeActionInspector, Integer> {
	
	ConformityTakeActionInspector findByConformityIdAndInspectorTeamLeadId(Integer conformityId, Integer inspectorTeamLeadId);
	
	ConformityTakeActionInspector findByConformityId(Integer conformityId);

	ConformityTakeActionInspector findByConformity_Id(Integer confId);
}
