package app.ewarehouse.repository;

import app.ewarehouse.entity.TsplitReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TsplitReceiptRepository extends JpaRepository<TsplitReceipt , String> {

   Optional<TsplitReceipt> getBySplitReceiptNoAndBitDeleteFlag(String splitReceiptNo,boolean bitDeleteFlag);


@Query("SELECT t FROM TsplitReceipt t " +
        "JOIN t.splitReceipt m " +
        "JOIN m.warehouseReceipt w " +
        "JOIN w.receiveCommodity c " +
        "JOIN c.depositor d " +
        "WHERE t.bitDeleteFlag = false " +
        "AND (:search IS NULL OR " +
        "CAST(t.qtyInEachLot AS string) LIKE (CONCAT('%', :search, '%')) " +
        "OR CAST(t.lotNo AS string) LIKE (CONCAT('%', :search, '%')) " +
        "OR CAST(t.splitReceiptNo AS string) LIKE CONCAT('%', :search, '%') " +
        "OR CAST(w.txtWarehouseReceiptId AS string) LIKE CONCAT('%', :search, '%') " +
        "OR CAST(m.totalLotNo AS string) LIKE CONCAT('%', :search, '%') " +
        "OR CAST(d.intId AS string) LIKE CONCAT('%', :search, '%'))")
Page<TsplitReceipt> findByFilters(String search, Pageable pageable);

}
