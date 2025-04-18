package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Warehouse_reg;
@Repository
public interface Warehouse_regRepository extends JpaRepository<Warehouse_reg, Integer> {
Warehouse_reg findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

@Query("From Warehouse_reg where bitDeletedFlag=:bitDeletedFlag ")
List<Warehouse_reg> findAllByBitDeletedFlagAndIntInsertStatus(Boolean bitDeletedFlag,Pageable pageRequest);
Integer countByBitDeletedFlag(Boolean bitDeletedFlag);}