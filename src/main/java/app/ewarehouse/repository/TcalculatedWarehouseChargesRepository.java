package app.ewarehouse.repository;

import app.ewarehouse.entity.TcalculatedWarehouseCharges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcalculatedWarehouseChargesRepository extends JpaRepository<TcalculatedWarehouseCharges,Integer> {
    TcalculatedWarehouseCharges findByWarehouseReceiptNoAndBitDeleteFlag(String warehouseReceipt, boolean bitDeleteFlag);
}
