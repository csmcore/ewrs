package app.ewarehouse.service;

import app.ewarehouse.dto.WarehouseReceiptResponse;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TwarehouseReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TwarehouseReceiptService {

    List<TwarehouseReceipt> findAll();

    WarehouseReceiptResponse getDetailsById(String id);

    TwarehouseReceipt save(String data);

    List<WarehouseReceiptResponse> getReceiptForDepositor(String id);

    Page<WarehouseReceiptResponse> getFilteredReceipts(Status status, String search, String sortColumn, String sortDirection, Pageable sortedPageable);

	List<WarehouseReceiptResponse> getEligibleLossReceiptListByDepositor(String id);

    List<String> getReceiptNoForDepositor(String id);

    List<WarehouseReceiptResponse> getReceiptForBuyerPaymentInvoice(String id);
    
    List<WarehouseReceiptResponse> getReceiptForBuyerPayment(String id);
}
