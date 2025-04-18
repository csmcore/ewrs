package app.ewarehouse.repository;

import app.ewarehouse.entity.BuyerPaymentInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BuyerPaymentInvoiceRepository extends JpaRepository<BuyerPaymentInvoice, Integer> {

    Optional<BuyerPaymentInvoice> findByInvoiceNumber(String invoiceNumber);

    @Query("SELECT b FROM BuyerPaymentInvoice b JOIN b.warehouseReceipt wr JOIN b.buyer buyer WHERE wr.txtWarehouseReceiptId = :receiptNo AND buyer.intId = :buyerId")
    BuyerPaymentInvoice getBuyerPaymentInvoice(@Param("buyerId") String buyerId,@Param("receiptNo") String receiptNo);
    
    
    @Query(value="SELECT sam.sales_application_id FROM t_sales_application_main sam inner join t_sales_application_tab_one sato\r\n"
    		+ "on sam.id_tab_one=sato.id_tab_one where sato.receipt_no=:recepitNO and sam.applicationStatus='DealFinal'",nativeQuery = true)
    String getSalesApplicationNo(@Param("recepitNO") String recepitNO);

    Optional<BuyerPaymentInvoice> getByInvoiceNumber(String invoiceNumber);
}
