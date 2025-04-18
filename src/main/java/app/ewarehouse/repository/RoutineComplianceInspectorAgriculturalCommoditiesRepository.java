package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceInspectorAgriculturalCommodity;
import jakarta.transaction.Transactional;

@Repository
public interface RoutineComplianceInspectorAgriculturalCommoditiesRepository extends JpaRepository<RoutineComplianceInspectorAgriculturalCommodity, Long> {
	
	
	@Modifying
    @Transactional
    @Query(value = "UPDATE t_routine_compliance_inspector_agricultural_commodities SET bitDeleteFlag = 1 WHERE agricultural_commodities_id = :id", nativeQuery = true)
    int markAsDeleted(@Param("id") Long id);

}
