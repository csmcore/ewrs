package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.WarehouseParticulars;

@Repository
public interface WarehouseParticularsRepository extends JpaRepository<WarehouseParticulars, String> {

    WarehouseParticulars findByintWareHouseParticularsIdAndBitDeletedFlag(String intWareHouseParticularsId, boolean bitDeletedFlag);

    List<WarehouseParticulars> findAllByBitDeletedFlag(Boolean bitDeletedFlag);

    Page<WarehouseParticulars> findAllByBitDeletedFlag(Boolean bitDeletedFlag, Pageable pageable);

}
