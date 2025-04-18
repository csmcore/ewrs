package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RoutineComplianceInspectorConditionOfGoodsStorage;

@Repository
public interface RoutineComInspecGoodsStorageRepo extends JpaRepository<RoutineComplianceInspectorConditionOfGoodsStorage, Long> {

}
