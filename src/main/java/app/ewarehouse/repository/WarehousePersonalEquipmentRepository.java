package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.WarehousePersonalEquipment;

@Repository
public interface WarehousePersonalEquipmentRepository extends JpaRepository<WarehousePersonalEquipment, Long> {

}
