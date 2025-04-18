package app.ewarehouse.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import app.ewarehouse.entity.BuyerDepositorAndWareHouseOperator;
import app.ewarehouse.entity.IssuanceWareHouseRecipt;
import app.ewarehouse.entity.IssuanceWarehouseReceiptActionHistory;

public interface IssuanceWareHouseReciptService {

	//List<Map<String, Object>> getAllDepositorId(String vchWarehouseId);

	Map<String, Object> getDepositorIdByWareHouse(String id);


	List<Map<String, Object>> getAllCommodityName();

	List<Map<String, Object>> getCommodityNameByOriginAndCode(Integer id);

	List<Map<String, Object>> getAllSeason();

	List<Map<String, Object>> getAllChargesHeader();

	List<Map<String, String>> getChargesHeaderByUnitType(Integer chargesId);

	IssuanceWareHouseRecipt saveIssuanceWareHouseRecipt(String dto);

	Page<IssuanceWareHouseRecipt> viewIssuance(String vchWarehouseId, Pageable pageable);

	Map<String, Object> updateConfirmationByDepositor(String id,Integer id1,String data);

	Map<String, Object> generateWarehouseReceiptNo(String id,Integer id1,String data);

	Map<String, Object> takeActionForCentralRegistration(String id,Integer id1, String data);

	Map<String, Object> takeActionForIssueRegistration(String id,Integer id1, String data);

	Map<String, Object> takeActionForBlockRegistration(String id,Integer id1, String data);

	Page<IssuanceWareHouseRecipt> viewIssuanceOic(Pageable pageable);

	Page<IssuanceWareHouseRecipt> viewIssuanceCEO(Pageable pageable);

	String getOperatorLicenceNo(String warehouseId);

	List<Map<String, Object>> getAllDepositorId();


	BuyerDepositorAndWareHouseOperator save(String data);


	Page<IssuanceWareHouseRecipt> viewTransferIssuance(Pageable pageable);

	List<IssuanceWarehouseReceiptActionHistory> getIssuanceWhReceiptActionHistory(Integer issuanceWhId, String depositorId);


	Page<IssuanceWareHouseRecipt> viewTransferIssuanceOIC(Pageable pageable);


	Page<IssuanceWareHouseRecipt> viewTransferIssuanceCEO(Pageable pageable);


	

}
