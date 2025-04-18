package app.ewarehouse.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RetireReceiptService {

	String retireWarehouseReceipt(String retireReceipt) throws JsonProcessingException;

	String replaceLostReceipt(String lostReceipt) throws JsonProcessingException;
	
	String validateReceipt(String receipt) throws JsonProcessingException;

}
