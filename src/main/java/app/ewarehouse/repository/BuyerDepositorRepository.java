package app.ewarehouse.repository;


import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import app.ewarehouse.entity.BuyerDepositor;
import app.ewarehouse.entity.RegistrationType;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.enmReceiptStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;



@Repository
public interface BuyerDepositorRepository extends JpaRepository<BuyerDepositor, String> {

	   
    BuyerDepositor findByIntIdAndBitDeletedFlag(String intId, boolean bitDeletedFlag);

    Optional<BuyerDepositor> findByIntIdAndEnmRegistrationTypeInAndEnmStatusAndBitDeletedFlag(String intId, List<RegistrationType> regTypes, Status status, boolean bitDeletedFlag);

    List<BuyerDepositor> findAllByBitDeletedFlagOrderByDtmCreatedOnDesc(Boolean bitDeletedFlag);

    Page<BuyerDepositor> findAllByBitDeletedFlagOrderByDtmCreatedOnDesc(Boolean bitDeletedFlag, Pageable pageable);

    @Query("SELECT b FROM BuyerDepositor b WHERE b.bitDeletedFlag = false  AND (:fromDate IS NULL OR b.dtmCreatedOn >= :fromDate) AND (:toDate IS NULL OR b.dtmCreatedOn <= :toDate) AND (:status IS NULL OR b.enmStatus = :status) order by b.dtmCreatedOn DESC")
    Page<BuyerDepositor> findByFilters(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("status") Status status, Pageable pageable);

    @Query("SELECT b FROM BuyerDepositor b WHERE b.bitDeletedFlag = false " +
            "AND (:fromDate IS NULL OR b.dtmCreatedOn >= :fromDate) " +
            "AND (:toDate IS NULL OR b.dtmCreatedOn <= :toDate) " +
            "AND (:status IS NULL OR b.enmStatus = :status) " +
            "AND (:search IS NULL OR " +
            "LOWER(b.txtName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(b.txtEmailAddress) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(b.txtTelephoneNumber) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR CAST(b.intId AS string) LIKE CONCAT('%', :search, '%') " +
            "OR LOWER(CAST(b.enmStatus AS string)) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(CAST(b.enmRegistrationType AS string)) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<BuyerDepositor> findByFilters(@Param("fromDate") Date fromDate,
                                       @Param("toDate") Date toDate,
                                       @Param("status") Status status,
                                       @Param("search") String search,
                                       Pageable pageable);


    @Modifying
    @Transactional
    @Query("UPDATE BuyerDepositor b SET b.enmStatus = :status WHERE b.intId = :id")
    int updateBuyerStatus(@Param("id") String id, @Param("status") Status status);

    @Query("Select b.enmStatus FROM BuyerDepositor b WHERE b.intId = :id")
    Status getBuyerStatus(@Param("id") String id);

    @Query("SELECT DISTINCT b FROM BuyerDepositor b " +
            "JOIN TreceiveCommodity rc ON b = rc.depositor " +
            "JOIN TwarehouseReceipt wr ON rc = wr.receiveCommodity " +
            "WHERE b.enmRegistrationType IN :regTypes " +
            "AND b.enmStatus = :enmStatus " +
            "AND b.bitDeletedFlag = :bitDeletedFlag " +
            "AND wr.status = :status " +
            "AND wr.receiptStatus= :enmReceiptStatus")
    List<BuyerDepositor> findAllDepositorsWithAcceptedWarehouseReceipt(
            @Param("regTypes") List<RegistrationType> regTypes,
            @Param("enmStatus") Status enmStatus,
            @Param("bitDeletedFlag") boolean bitDeletedFlag,
            @Param("status") Status status,
            @Param("enmReceiptStatus") enmReceiptStatus enmReceiptStatus);

    @Query("SELECT DISTINCT b.intId FROM BuyerDepositor b " +
            "JOIN TreceiveCommodity rc ON b = rc.depositor " +
            "JOIN TwarehouseReceipt wr ON rc = wr.receiveCommodity " +
            "WHERE b.enmRegistrationType IN :regTypes " +
            "AND b.enmStatus = :enmStatus " +
            "AND b.bitDeletedFlag = :bitDeletedFlag " +
            "AND wr.status = :status " +
            "AND wr.receiptStatus= :enmReceiptStatus")
    List<String> findAllDepositorsIntIdsWithAcceptedWarehouseReceipt(
            @Param("regTypes") List<RegistrationType> regTypes,
            @Param("enmStatus") Status enmStatus,
            @Param("bitDeletedFlag") boolean bitDeletedFlag,
            @Param("status") Status status,
            @Param("enmReceiptStatus") enmReceiptStatus enmReceiptStatus);




    @Query("SELECT b FROM BuyerDepositor b " +
            "WHERE (:regTypes IS NULL OR b.enmRegistrationType IN :regTypes) " +
            "AND (:enmStatus IS NULL OR b.enmStatus = :enmStatus) " +
            "AND b.bitDeletedFlag = :bitDeletedFlag")
    List<BuyerDepositor> findAllBuyers(@Param("regTypes") List<RegistrationType> regTypes, @Param("enmStatus") Status enmStatus, @Param("bitDeletedFlag") boolean bitDeletedFlag
    );


    @Query("SELECT b.intId FROM BuyerDepositor b " +
            "WHERE (:regTypes IS NULL OR b.enmRegistrationType IN :regTypes) " +
            "AND (:enmStatus IS NULL OR b.enmStatus = :enmStatus) " +
            "AND b.bitDeletedFlag = :bitDeletedFlag")
    List<String> findAllBuyersIds(List<RegistrationType> regTypes, Status enmStatus, boolean bitDeletedFlag);
    
    @Query(value="SELECT count(*)  as countvalue FROM m_buyer_depositor_master  where  vchApplicationId=:depositorId AND enmStatus='Created'",nativeQuery = true)
    String getCehckDepositor(String depositorId);

    
    @Query("SELECT DISTINCT b FROM BuyerDepositor b " +
            "JOIN TreceiveCommodity rc ON b = rc.depositor " +
            "JOIN TwarehouseReceipt wr ON rc = wr.receiveCommodity " +
            "WHERE b.enmRegistrationType IN :regTypes " +
            "AND b.enmStatus = :enmStatus " +
            "AND b.bitDeletedFlag = :bitDeletedFlag " +
            "AND wr.status = :status ")
    List<BuyerDepositor> findAllDepositorsWithAcceptedWarehouseReceiptPd(
            @Param("regTypes") List<RegistrationType> regTypes,
            @Param("enmStatus") Status enmStatus,
            @Param("bitDeletedFlag") boolean bitDeletedFlag,
            @Param("status") Status status);
    
    
    
    
    
    
    
      
}
