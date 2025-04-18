package app.ewarehouse.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import app.ewarehouse.dto.ApplicationConformityFormOneADto;
import app.ewarehouse.dto.ApplicationConformityFormOneBDto;
import app.ewarehouse.dto.ApplicationConformityMainRemarksDto;
import app.ewarehouse.dto.ApplicationConformityWhLocationDTO;
import app.ewarehouse.dto.ApplicationOfConformityDTO;
import app.ewarehouse.dto.ApprovedAocIdAndShopDto;
import app.ewarehouse.dto.ConformityCertificateDto;
import app.ewarehouse.dto.DraftedWarehouseForPaymentDto;
import app.ewarehouse.dto.PendingConformityDTO;
import app.ewarehouse.entity.ApplicationOfConformity;
import app.ewarehouse.entity.ApplicationOfConformityActionHistory;
import app.ewarehouse.entity.ApplicationOfConformityCommodityStorage;
import app.ewarehouse.entity.ApplicationOfConformityParticularOfApplicants;
import app.ewarehouse.entity.ApplicationOfConformitySupportingDocuments;
import app.ewarehouse.entity.ApplicationOfConformityWarehouseOperatorViability;
import app.ewarehouse.entity.ConformityMain;
import app.ewarehouse.entity.ConformityTakeActionInspector;
import app.ewarehouse.entity.ConformityTakeActionInspectorSchedule;
import app.ewarehouse.entity.Status;

public interface ConformityParticularService {

	JSONObject saveApplicantData(String data) throws JsonProcessingException;

	JSONObject saveSupportingDocsData(String data) throws JsonProcessingException;

	JSONObject saveOperatorViabilityData(String data) throws JsonProcessingException;

	JSONObject savePaymentData(String data) throws JsonProcessingException;

	List<ApplicationOfConformityDTO> findAll();

	void updateApplicationStatus(String data);

	Page<ApplicationOfConformity> getApplicationByStatusAndRole(Date fromDate, Date toDate, Status status, Integer pendingAt, Pageable pageable);

	ApplicationOfConformity findByApplicationIdWithDirectors(String applicationId);

	void updateApplicationStatus(Status status, Integer role, String appId);

	ApplicationOfConformity findById(String applicationId);

	void giveRemarks(String data) throws JsonProcessingException;

	Long getCountByCreatedByAndDraftStatus(Integer intId);

	ApplicationOfConformityParticularOfApplicants getAocParticularDataById(Integer intId);

	void deleteDirectorById(Integer intId);

	Long getDraftStatusOfSupportingDocs(Integer intId);

	ApplicationOfConformitySupportingDocuments getAocSupportindDocDataById(Integer intId);

	Long getDraftStatusOfViability(Integer intId);

	ApplicationOfConformityWarehouseOperatorViability getViabilityDataById(Integer intId);

	List<ApplicationOfConformityDTO> findByUserIdAndStatus(Integer userId);

	Page<ApplicationOfConformity> getApplicationByUserId(Date fromDate, Date toDate, Integer userId, Pageable pageable);

    String getCommodityTypes(String id);
    
    List<ApprovedAocIdAndShopDto> getApprovedApplicationIdAndShop(Integer countyId, Integer subCountyId,Integer roleId);

	String getOperatorFullName(String applicantId);

	ConformityCertificateDto getCertificate(String applicantId);

	List<ApplicationConformityMainRemarksDto> getRemarks(String applicantId);

	JSONObject generateCertificate(String data);

	List<ApprovedAocIdAndShopDto> getAllApprovedApplicationIdAndShop(String warehouseId);

	JSONObject verifyCertificate(String data);

	ApplicationOfConformityDTO findByUserIdAndApplicationId(Integer userId, String applicationId);
	 List<ApprovedAocIdAndShopDto> getWareHouseIdAndName(Integer countyId, Integer subCountyId);

	JSONObject saveLocationDetails(String code);
	
	JSONArray getDraftedLocationDetails(String companyId);

	JSONObject saveCommodityDetails(String code);

	JSONObject saveFormOneADetails(String code);
	
	JSONObject saveFormOneBDetails(String code);

	List<ApplicationConformityWhLocationDTO> getDraftedWarehouseLocationList(String companyId);

	//JSONObject getDraftedCommodityList(String companyId);
	
	List<ApplicationOfConformityCommodityStorage> getDraftedCommodityList(String companyId);

	List<ApplicationConformityFormOneADto> getDraftedFormOneADetails(String companyId);

	List<ApplicationConformityFormOneBDto> getDraftedFormOneBDetails(String companyId);

	JSONObject deleteLocationDetailsByLocationId(Long locationId);

	List<DraftedWarehouseForPaymentDto> getDraftedWarehousesForPayment(String companyId);

	JSONObject saveInMainTable(String code);
	
	List<ConformityMain> getApplicationsByStatus(String decodedData) throws JsonMappingException, JsonProcessingException;

	Page<PendingConformityDTO> getPendingApplications(String data) throws JsonMappingException, JsonProcessingException;

	ConformityMain getConfirmityDataById(Integer id);

	JSONObject forwardToCC(String data) throws JsonMappingException, JsonProcessingException;

	JSONObject submitCCData(String data) throws JsonMappingException, JsonProcessingException;

	JSONObject forwardToInsp(String data) throws JsonMappingException, JsonProcessingException;

	JSONObject checkInspection(String data);

	JSONObject submitInspectionSchedule(String data);

	JSONObject submitCompleteInspection(String data);

	JSONObject submitInspectorData(String data);

	JSONObject getInspectorCCData(String data);
	
	JSONObject updateLocationDetails(String updateData);
	 
	JSONObject updateFormOneADetails(String code);
 
	JSONObject updateFormOneBDetails(String code);

	JSONObject submitOicData(String data);

	JSONObject getInspectorCCOicData(String data);

	JSONObject generateConformityCertificate(String data);

	ConformityCertificateDto getConformityCertificate(Integer applicantId);

	JSONObject rejectApplication(String data);

	JSONObject deferCeoApplication(String data);

	JSONObject deferOicApplication(String data);
	
	JSONObject updateCommodityDetails(String code);
	
	Map<String, Object> getInspectorPdf(Integer confId);

	List<ConformityMain> getAllApprovedData();

	List<ConformityMain> getAllRejectedData();

	JSONObject getInspRequestedBy(String data);

	ConformityTakeActionInspectorSchedule getInspectionSchedule(Integer applicantId);

	ConformityTakeActionInspector getAllocatedInspectorData(Integer applicantId);

	List<Map<String , Object>> getAllApprovedDataForTakeAction();

	List<Map<String , Object>> getAllDeferedDataForTakeAction();

	Page<Map<String , Object>> getAllApprovedDataForTakeAction(int page, int size);

	Page<Map<String , Object>> getAllDeferedDataForTakeAction(int page, int size);

	Page<Map<String , Object>> getAllRejectedDataForTakeAction(int page, int size);

	List<ApplicationOfConformityActionHistory> getConformityActionHistoryByWareHouseId(String wareHouseId);


//	  JSONObject findByApplicantId(String ApplicantId)throws JsonMappingException, JsonProcessingException;

}
