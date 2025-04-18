package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.WarehouseUserEmpType;

@Repository
public interface WarehouseUserEmpTypeRepository extends JpaRepository<WarehouseUserEmpType, Integer> {
	List<WarehouseUserEmpType> findByRoleIdAndDeletedFlagFalse(Integer roleId);
}
