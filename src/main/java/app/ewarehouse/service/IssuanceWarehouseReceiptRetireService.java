package app.ewarehouse.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import app.ewarehouse.entity.IssuanceWarehouseReceiptRetire;
public interface IssuanceWarehouseReceiptRetireService {
	
    Page<IssuanceWarehouseReceiptRetire> getAllRetiredReceipts(Pageable pageable);

	IssuanceWarehouseReceiptRetire saveRetirementData(String data);

	Page<IssuanceWarehouseReceiptRetire> getAllRetiredReceiptsForDepositor(Pageable pageable, String depositorId);

}
