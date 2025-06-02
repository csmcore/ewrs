package app.ewarehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.dto.SalesApplicationListResponse;
import app.ewarehouse.entity.ApplicationStatus;
import app.ewarehouse.entity.BuyerDepositor;
import app.ewarehouse.entity.RegistrationType;
import app.ewarehouse.entity.SalesApplicationMain;


@Repository
public interface SalesApplicationMainRepository extends JpaRepository<SalesApplicationMain, String> {

    Optional<SalesApplicationMain> findBySalesApplicationMainIdAndBitDeleteFlag(String salesApplicationMainId, boolean bitDeleteFlag);

    Optional<SalesApplicationMain> findByApplicationStatusAndBitDeleteFlag(ApplicationStatus applicationStatus, boolean bitDeleteFlag);

   // Optional<SalesApplicationMain> findByApplicationStatusAndBitDeleteFlag(List<String> list, boolean bitDeleteFlag);


   @Query(value="SELECT sam.sales_application_id,sato.new_owner_id,sato.depositor_id FROM t_sales_application_main sam inner join t_sales_application_tab_one sato\r\n"
   		+ "on sam.id_tab_one=sato.id_tab_one inner join\r\n"
   		+ "t_warehouse_receipt twr on twr.vchWarehouseReceiptId=sato.receipt_no\r\n"
   		+ "where sato.receipt_no=:receiptNO \r\n"
   		+ "and twr.bitSaleStatus=0 and twr.bitPaymentStatus=0 and sam.applicationStatus='DealFinal'",nativeQuery = true)
    List<Object[]> getDealDetails(@Param("receiptNO") String receiptNO);




    @Query(value="SELECT sam.sales_application_id,sato.new_owner_id,sato.depositor_id FROM t_sales_application_main sam inner join t_sales_application_tab_one sato\r\n"
       		+ "on sam.id_tab_one=sato.id_tab_one inner join\r\n"
       		+ "t_warehouse_receipt twr on twr.vchWarehouseReceiptId=sato.receipt_no\r\n"
       		+ "where sato.receipt_no=:receiptNO \r\n"
       		+ "and twr.bitSaleStatus=1 and twr.bitPaymentStatus=1 and sam.applicationStatus='SOLD'",nativeQuery = true)
        List<Object[]> getReceiptPaymentDetails(@Param("receiptNO") String receiptNO);

        
        String  queryForSalesApplicationList = """
        		SELECT  new app.ewarehouse.dto.SalesApplicationListResponse(sam.salesApplicationMainId,sato.depositorId,sato.buyerId,sato.receiptNo,satt.priceOfReceipt, 
        		sam.dtmCreatedOn,sam.stmUpdatedAt,sam.applicationStatus)
        		FROM SalesApplicationMain sam 
                JOIN SalesApplicationTabOne sato ON sato.applicationId = sam.salesApplicationMainId 
                JOIN SalesApplicationTabTwo satt ON satt.applicationId = sam.salesApplicationMainId 
                WHERE sam.applicationStatus IN :regTypes AND (:search IS NULL 
	            OR LOWER(sam.salesApplicationMainId) LIKE CONCAT('%', :search, '%')  
	            OR LOWER(sato.depositorId) LIKE LOWER(CONCAT('%', :search, '%')) 
	            OR LOWER(sato.buyerId) LIKE LOWER(CONCAT('%', :search, '%'))
	            OR LOWER(sato.receiptNo) LIKE LOWER(CONCAT('%', :search, '%'))  
	            OR LOWER(CAST(satt.priceOfReceipt AS string)) LIKE LOWER(CONCAT('%', :search, '%'))  
	            OR LOWER(CAST(sam.applicationStatus AS string)) LIKE LOWER(CONCAT('%', :search, '%'))) 
                """;
        @Query(value=queryForSalesApplicationList)
        Page<SalesApplicationListResponse> findAllSalesApplicationList(@Param("regTypes") List<String> regTypes,Pageable pageable, @Param("search") String search);
        
      
}
