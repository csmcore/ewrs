package app.ewarehouse.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;

import app.ewarehouse.dto.FormOneCRequestDto;
import app.ewarehouse.dto.FormOneCResponseDto;
import app.ewarehouse.dto.OPLApplicationStatusDTO;
import app.ewarehouse.dto.OPLFormDetailsDTO;
import app.ewarehouse.dto.OperatorLicenceApprovalResponseDto;
import app.ewarehouse.dto.OperatorLicenceDTO;
import app.ewarehouse.entity.Action;
import app.ewarehouse.entity.OperatorLicence;
import app.ewarehouse.entity.PaymentData;
import app.ewarehouse.entity.Stakeholder;
import app.ewarehouse.entity.Status;

public interface OperatorLicenceService {
	Page<OperatorLicence> getOperatorLicences(Status status, Integer userId, PageRequest of);
	String saveOperatorLicence(String operatorLicence) throws JsonProcessingException;
	Page<OperatorLicenceDTO> getApplications(Pageable pageable, Status status, Stakeholder stakeholder, Action action, String search);
	String handleAction(String technicalActionRequestData, Stakeholder forwardedTo, Stakeholder actionTakenBy, Action action,
			Status status) throws JsonProcessingException;
	Page<OperatorLicenceDTO> getAllApplications(Pageable pageable, Stakeholder forwardedTo, String search);
	OperatorLicence getOperatorLicence(Integer id);
	List<OperatorLicence> getOperatorLicences(Status status, Integer userId);
	String getOperatorWarehouseName(String compId);
	JSONObject saveDocuments(List<FormOneCRequestDto> documentRequestDtos); 
	List<FormOneCResponseDto> getFilesByWarehouseId(String warehouseId);
	JSONObject saveLicenceApplication(JSONObject data); 
	JSONObject checkPaymentStatus(String whid);
	Page<OperatorLicenceApprovalResponseDto> getAllApprovalData(String status, String search, Integer roleId,Integer CLCRoleId,Integer userid,Pageable pageable);
	PaymentData getPaymentDetailsById(Long id);
	JSONObject AllTakeAction(String takeactionData);
	JSONObject getAllActionRemarks(String takeactionData);
	JSONObject getAllActionRemarksList(String takeactionData);
	Page<OPLApplicationStatusDTO> getAllApprovalStatusData(Integer roleId,Integer userid,String status, String search,Pageable pageable);
	JSONObject generateCertificate(String certData);
	JSONObject getCertificateDate(String certId);
	Page<OPLFormDetailsDTO> getOPLFormDetailsData(String userInfo);
	Page<OPLApplicationStatusDTO> getActionTakenOnApplication(String formdata);
	//String getOperatorWarehouseNameByWareHouse(String compId);
	
	List<Map<String, Object>> getOperatorLicenceActionHistory(Integer intLicenceSno);
	
}
