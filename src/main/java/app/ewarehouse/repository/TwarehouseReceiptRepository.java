package app.ewarehouse.repository;
import app.ewarehouse.entity.Status;

import app.ewarehouse.entity.TwarehouseReceipt;
import app.ewarehouse.entity.enmReceiptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface TwarehouseReceiptRepository extends JpaRepository<TwarehouseReceipt,String> {

    TwarehouseReceipt findByTxtWarehouseReceiptIdAndStatusAndBitDeleteFlag(String txtWarehouseReceiptId,Status status, boolean bitDeleteFlag);

    TwarehouseReceipt findByReceiveCommodity_TxtReceiveCIdAndBitDeleteFlag(String receiveCId, boolean bitDeleteFlag);

    List<TwarehouseReceipt> findAllByReceiveCommodity_Depositor_IntIdAndBitDeleteFlagAndStatusAndReceiptStatus(String intId, boolean bitDeleteFlag, Status status, enmReceiptStatus receiptStatus);

    List<TwarehouseReceipt> findAllByReceiveCommodity_Depositor_IntIdAndBitDeleteFlagAndStatusAndReceiptStatusIn(String intId, boolean bitDeleteFlag, Status status,   List<enmReceiptStatus> receiptStatus);

@Query("SELECT t FROM TwarehouseReceipt t " +
        "JOIN t.receiveCommodity r " +
        "JOIN r.depositor d " +
        "JOIN r.commodity c " +
        "WHERE t.bitDeleteFlag = false " +
        "AND (:status IS NULL OR t.status = :status) " +
        "AND (:search IS NULL OR " +
        "LOWER(c.seasonality.seasonname) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(r.txtGrade) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR CAST(r.txtReceiveCId AS string) LIKE CONCAT('%', :search, '%') " +
        "OR CAST(t.txtWarehouseReceiptId AS string) LIKE CONCAT('%', :search, '%') " +
        "OR CAST(d.intId AS string) LIKE CONCAT('%', :search, '%'))")
    Page<TwarehouseReceipt> findByFilters(Status status, String search, Pageable pageable);

	@Query("FROM TwarehouseReceipt r WHERE r.receiveCommodity.depositor.intId=:id AND r.bitDeleteFlag=:flag AND r.status=:status AND r.receiptStatus NOT IN :receiptStatus")
	List<TwarehouseReceipt> getEligibleLossReceiptListByDepositor(String id, boolean flag, Status status,
			List<enmReceiptStatus> receiptStatus);

    @Query("SELECT t.txtWarehouseReceiptId FROM TwarehouseReceipt t JOIN t.receiveCommodity r JOIN r.depositor d where t.bitDeleteFlag=:bitDeleteFlag and d.intId = :intId and t.status=:status and t.receiptStatus=:receiptStatus ")
    List<String> findReceiptNoUsingDepositor(@Param("intId") String intId,
                                             @Param("bitDeleteFlag") boolean bitDeleteFlag,
                                             @Param("status") Status status,
                                             @Param("receiptStatus") enmReceiptStatus receiptStatus);
}
