package app.ewarehouse.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Strings;

import app.ewarehouse.dto.AocCompProfDetailsMainDTO;
import app.ewarehouse.dto.CollateralTakeActionCCDetailsDto;
import app.ewarehouse.dto.FormOneCResponseDto;
import app.ewarehouse.entity.ApplicationOfConformityCommodityDetails;
import app.ewarehouse.entity.ApplicationOfConformityFormOneA;
import app.ewarehouse.entity.ApplicationOfConformityFormOneB;
import app.ewarehouse.entity.ApplicationOfConformityLocationDetails;
import app.ewarehouse.entity.ComplaintManagementNewEntity;
import app.ewarehouse.entity.ComplaintReinstatement;
import app.ewarehouse.entity.ComplaintReinstatementTakeActionCeoSecond;
import app.ewarehouse.entity.ComplaintReinstatementTakeActionCm;
import app.ewarehouse.entity.ComplaintReinstatementTakeActionCmDetails;
import app.ewarehouse.entity.OnlineServiceApprovalNotings;
import app.ewarehouse.entity.ReinstatementActionHistory;
import app.ewarehouse.entity.TOnlineServiceApproval;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.master.repository.ReinstatementActionHistoryRepository;
import app.ewarehouse.repository.ApplicationConformityLocDetRepository;
import app.ewarehouse.repository.ApplicationOfConformityCommodityDetailsRepository;
import app.ewarehouse.repository.ApplicationOfConformityFormOneARepository;
import app.ewarehouse.repository.ApplicationOfConformityFormOneBRepository;
import app.ewarehouse.repository.ComplaintManagementNewRepository;
import app.ewarehouse.repository.ComplaintReinstatementTakeActionCeoSecondRe;
import app.ewarehouse.repository.ComplaintReinstatementTakeActionCmDetailsRepo;
import app.ewarehouse.repository.ComplaintReinstatementTakeActionCmRepository;
import app.ewarehouse.repository.ConfirmityFormOneCRepo;
import app.ewarehouse.repository.ConformityMainRepository;
import app.ewarehouse.repository.OnlineServiceApprovalNotingsRepository;
import app.ewarehouse.repository.ReinstatementRepository;
import app.ewarehouse.repository.TOnlineServiceApprovalRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.AocCompProfDetailsMainService;
import app.ewarehouse.service.ReinstatementService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.DocumentUploadutil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.FolderAndDirectoryConstant;

@Service
public class ReinstatementServiceImpl implements ReinstatementService {
	
	@Autowired
	private ComplaintManagementNewRepository complaintManagementNewRepository;

	@Autowired
	private TuserRepository tuserRepository;

	@Autowired
	private ApplicationConformityLocDetRepository applicationConformityLocDetRepository;

	@Autowired
	private OnlineServiceApprovalNotingsRepository onlineServiceApprovalNotingsRepository;

	@Autowired
	private ReinstatementRepository reinstatementRepository;

	@Autowired
	private ComplaintReinstatementTakeActionCmRepository complaintReinstatementTakeActionCmRepository;

	@Autowired
	private ComplaintReinstatementTakeActionCmDetailsRepo complaintReinstatementTakeActionCmDetailsRepo;

	@Autowired
	private ComplaintReinstatementTakeActionCeoSecondRe complaintReinstatementTakeActionCeoSecondRe;

	@Autowired
	private ObjectMapper om;

	@Autowired
	private ApplicationOfConformityCommodityDetailsRepository applicationOfConformityCommodityDetailsRepository;

	@Autowired
	private ApplicationOfConformityFormOneARepository applicationOfConformityFormOneARepository;

	@Autowired
	private ApplicationOfConformityFormOneBRepository applicationOfConformityFormOneBRepository;

	@Autowired
	ConfirmityFormOneCRepo documentRepository;

	@Autowired
	private ConformityMainRepository conformityMainRepository;
	
	@Autowired
	private AocCompProfDetailsMainService aocService;
	
	@Autowired
	private ReinstatementActionHistoryRepository reinstatementActionHistoryRepository;
	
	@Autowired
	private TOnlineServiceApprovalRepository tOnlineServiceApprovalRepository;

	@Autowired
	private ErrorMessages errorMessages;

	@Value("${tempUpload.path}")
	private String tempUploadPath;

	@Value("${finalUpload.path}")
	private String finalUploadPath;
	  
	  private static final Logger logger = LoggerFactory.getLogger(ReinstatementServiceImpl.class);

	@Override
	public Map<String, Object> getReinstatementData(JSONObject jsonObject) {
		Integer complaintType = jsonObject.getInt("complaintType");
		Integer complainAgainst = jsonObject.getInt("complainAgainst");
		Integer userId = jsonObject.getInt("userId");
		ComplaintManagementNewEntity complaintList=complaintManagementNewRepository.getSuspendCheck(complaintType,complainAgainst,userId);
		ComplaintManagementNewEntity revocationList=complaintManagementNewRepository.getRevocationCheck(complaintType,complainAgainst,userId);
		Map<String, Object> response = new HashMap<>();
		if(complaintList != null) {
			String vchWarehouse=tuserRepository.findWarehouseIdByIntId(userId);
			ApplicationOfConformityLocationDetails locationData = applicationConformityLocDetRepository.findByWarehouseId(vchWarehouse);
			String licenceNo=complaintManagementNewRepository.getLicenceNo(vchWarehouse);
			OnlineServiceApprovalNotings notingData=onlineServiceApprovalNotingsRepository.getNotingData(complaintList.getId(),8);
//			ComplaintReinstatement reinstatementCheckData = reinstatementRepository.findTopByComplaintIdOrderByReinstatementDateDesc(complaintList.getId());
			ComplaintReinstatement reinstatementCheckData = reinstatementRepository.findTopByComplaintIdOrderByCreatedOnDesc(complaintList.getId());
	        response.put("warehouseLocations", locationData);
	        response.put("licenceNo", licenceNo);
	        response.put("complaintList", complaintList);
	        response.put("notingsData", notingData);
	        response.put("reinstatementCheckData", reinstatementCheckData);
		}else {
			response.put("status", "error");
	        response.put("message", "No suspension found for the given complaint type and complainant.");
		}
		response.put("revocationList", revocationList);
		return response;
	}

	@Override
	public String saveReinstaement(String reinStaementData) {
		  String decodedData = CommonUtil.inputStreamDecoder(reinStaementData);
          System.err.println(decodedData);
	        try {
	        	ComplaintReinstatement reinStatement = om.readValue(decodedData, ComplaintReinstatement.class);
	        	ComplaintManagementNewEntity complaintList=complaintManagementNewRepository.findByIdAndDeletedFlag(reinStatement.getComplaintId(),false);
	        	complaintList.setApplicationStatus("Under reinstatement");
	        	complaintManagementNewRepository.save(complaintList);
	        	TOnlineServiceApproval onlineApprovalSuspendData=tOnlineServiceApprovalRepository.findByIntOnlineServiceIdAndBitDeletedFlag(reinStatement.getComplaintId(),false);
	        	onlineApprovalSuspendData.setVchForwardAllAction("Under reinstatement");
	        	tOnlineServiceApprovalRepository.save(onlineApprovalSuspendData);
	        	if(reinStatement.getReinstatementId()==null) {
	        		reinStatement.setReinstatementId(getId("RID"));
	        		reinStatement.setStatus(100);
	        		 if(reinStatement.isFetchFileDb()) {
	        			 reinStatement.setSupportingDoc(reinStatement.getSupportingDoc()); 
	        		 }else {
	 	            byte[] decodedBytes = Base64.getDecoder().decode(reinStatement.getSupportingDoc().getBytes());
	 	            String uploadedPath = DocumentUploadutil.uploadFileByte("ReinStatement_Pro" + System.currentTimeMillis(), decodedBytes, FolderAndDirectoryConstant.REINSTATEMENT);
	 	           reinStatement.setSupportingDoc(uploadedPath);
	        		 }
	        		 ComplaintReinstatement savedReinstatement = reinstatementRepository.save(reinStatement);
	        		
	        	}else {
//	        		ComplaintReinstatement editData=reinstatementRepository.findByReinstatementId(reinStatement.getReinstatementId());
//	        		reinStatement.setCreatedOn(editData.getCreatedOn());
	        		reinStatement.setStatus(100);
	        		reinStatement.setReinstatementId(reinStatement.getReinstatementId());
	        		 if(reinStatement.isFetchFileDb()) {
	        			 reinStatement.setSupportingDoc(reinStatement.getSupportingDoc()); 
	        		 }else {
	 	            byte[] decodedBytes = Base64.getDecoder().decode(reinStatement.getSupportingDoc().getBytes());
	 	            String uploadedPath = DocumentUploadutil.uploadFileByte("ReinStatement_Pro" + System.currentTimeMillis(), decodedBytes, FolderAndDirectoryConstant.REINSTATEMENT);
	 	           reinStatement.setSupportingDoc(uploadedPath);
	        		 }
	        		 reinstatementRepository.save(reinStatement);
	        	}
	           
	            return "success";
	        }
	        catch (CustomGeneralException exception) {
	        	exception.printStackTrace();
	            logger.error("Inside Reinstatement method of ReinstatementServiceIMpl some error occur:" + exception.getMessage());
	            throw exception;
	        }
	        catch (Exception e) {
	        	
	        	e.printStackTrace();
	            logger.error("Inside Reinstatement method of ReinstatementServiceIMpl some error occur:" + e.getMessage());
	            throw new CustomGeneralException(errorMessages.getUnknownError());
	        }	}

		private String getId(String idName) {
	    String dbCurrentId =reinstatementRepository.getId();

	    if (dbCurrentId == null) {
	        
	        return idName + (System.currentTimeMillis() % 1000000); 
	    } else {
	        
	        long idNum = Long.parseLong(dbCurrentId.substring(3));
	        long nextVal = idNum + 1;  
	        return idName + nextVal;
	    }
	}

		
		@Override
		public Page<Map<String, Object>> getReinstatementViewData(int page, int size, int userId) {
			Pageable pageable = PageRequest.of(page, size);
//		    Page<ComplaintReinstatement> reinstatementPage = reinstatementRepository.findByUserId(userId, pageable);
		    Page<ComplaintReinstatement> reinstatementPage = reinstatementRepository.findByUserIdOrderByCreatedOnDesc(userId, pageable);

		    // Convert ComplaintReinstatement to Map<String, Object>
		    Page<Map<String, Object>> dtoPage = reinstatementPage.map(reinstatement -> {
		        Map<String, Object> data = new HashMap<>();
		        String warehouseId = tuserRepository.findWarehouseIdByIntId(reinstatement.getUserId());
		        data.put("complaintData",complaintManagementNewRepository.findByIdAndDeletedFlag(reinstatement.getComplaintId(),false));
		        data.put("warehouseLocations", applicationConformityLocDetRepository.findByWarehouseId(warehouseId));
		        data.put("licenceNo", complaintManagementNewRepository.getLicenceNo(warehouseId));
		        data.put("notingsData", onlineServiceApprovalNotingsRepository.getNotingData(reinstatement.getComplaintId(), 8));
		        data.put("reinstatement", reinstatement);
		        data.put("allocatedCommitte", complaintReinstatementTakeActionCmRepository.findByReinstatement(reinstatement));
		        return data;
		    });

		    return dtoPage;
		}

		@Override
		public Page<Map<String, Object>> getReinstatementListData(Integer page, Integer size, Integer userId,
				Integer roleId) {
			Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "createdOn"));
			Page<ComplaintReinstatement> reinstatementPage=null;
//			if(roleId==5) {
//			 reinstatementPage=reinstatementRepository.findByIsCerLicCheck("1",pageable);
//			}else if(roleId==13) {
//			  reinstatementPage=reinstatementRepository.findByIsCerLicCheck("2",pageable);
//			}else {
//			  reinstatementPage=reinstatementRepository.findAll(pageable);
//			}
			reinstatementPage=reinstatementRepository.findAll(pageable);
//		    Page<ComplaintReinstatement> reinstatementPage = reinstatementRepository.findByUserId(userId, pageable);

		    // Convert ComplaintReinstatement to Map<String, Object>
		    Page<Map<String, Object>> dtoPage = reinstatementPage.map(reinstatement -> {
		        Map<String, Object> data = new HashMap<>();
		        String warehouseId = tuserRepository.findWarehouseIdByIntId(reinstatement.getUserId());
		        data.put("complaintData",complaintManagementNewRepository.findByIdAndDeletedFlag(reinstatement.getComplaintId(),false));
		        data.put("warehouseLocations", applicationConformityLocDetRepository.findByWarehouseId(warehouseId));
		        data.put("licenceNo", complaintManagementNewRepository.getLicenceNo(warehouseId));
		        data.put("notingsData", onlineServiceApprovalNotingsRepository.getNotingData(reinstatement.getComplaintId(), 8));
		        data.put("reinstatement", reinstatement);
		        data.put("allocatedCommitte", complaintReinstatementTakeActionCmRepository.findByReinstatement(reinstatement));
		        
		        return data;
		    });

		    return dtoPage;
		}

		@Override
		public JSONObject forwardToCommitteeMember(String data) {
			String decodedData = CommonUtil.inputStreamDecoder(data);
		    JSONObject response = new JSONObject();

		    try {
		        JsonNode jsonNode = om.readTree(decodedData);
		        Integer userId = jsonNode.get("userId").asInt();
		        Integer roleId = jsonNode.get("roleId").asInt();
		        String userName = jsonNode.get("userName").asText();
		        String roleName = jsonNode.get("roleName").asText();
		        String reinsatementId = jsonNode.get("reinsatementId").asText();
		        ArrayNode selectedMembers = (ArrayNode) jsonNode.get("selectedMembers");
		        
		        ComplaintReinstatement reinStatementData = reinstatementRepository.findByReinstatementId(reinsatementId);
		        if (reinStatementData == null) {
		            response.put("status", "error");
		            response.put("message", "Reinstatement data not found.");
		            return response;
		        }
		        
		        for (JsonNode member : selectedMembers) {
		            int memberId = member.get("userId").asInt();
		            ComplaintReinstatementTakeActionCm entity = new ComplaintReinstatementTakeActionCm();
		            entity.setReinstatement(reinStatementData);
		            entity.setCommitteeMemberId(memberId);
		            entity.setCreatedBy(userId);
		            complaintReinstatementTakeActionCmRepository.save(entity);
		        }
		        reinStatementData.setStatus(101);
		        reinStatementData.setIsCommitteeMemeber(true);
		        reinstatementRepository.save(reinStatementData);
		        
		        saveReinstatementActionHistory(reinsatementId, userId, roleId, userName, roleName, "NA",0);
		        response.put("status", "success");
		        response.put("message", "Data saved successfully.");
		    } catch (Exception e) {
		    	e.printStackTrace();
		        response.put("status", "error");
		        response.put("message", "An error occurred : " + e.getMessage());
		    }

		    return response;
		}

		@Override
		public JSONObject submitCMData(String data) {
			String decodedData = CommonUtil.inputStreamDecoder(data);
		    JSONObject response = new JSONObject();
		    List<String> fileUploadList = new ArrayList<>();
		    try {
		    	JsonNode jsonNode = om.readTree(decodedData);
		    	ArrayNode documents = (ArrayNode) jsonNode.get("documents");
		    	String remarks = jsonNode.get("remarks").asText();
		    	int userId = jsonNode.get("userId").asInt();
		    	Integer roleId = jsonNode.get("roleId").asInt();
		        String userName = jsonNode.get("userName").asText();
		        String roleName = jsonNode.get("roleName").asText();
		    	String reinsatementId = jsonNode.get("reinsatementId").asText();
		    	 ComplaintReinstatement reinStatementData = reinstatementRepository.findByReinstatementId(reinsatementId);
		    	 ComplaintReinstatementTakeActionCm takeActionData = complaintReinstatementTakeActionCmRepository.findByCommitteeMemberIdAndReinstatement(userId, reinStatementData);
		    	 takeActionData.setRemarkByCM(remarks);
		    	 takeActionData.setForwardStatus(true);
		    	 complaintReinstatementTakeActionCmRepository.save(takeActionData);
		    	for (JsonNode doc : documents) {
		    		String documentName = doc.get("documentName").asText();
		    		String file = doc.get("file").asText();
		    		ComplaintReinstatementTakeActionCmDetails ccDetails = new ComplaintReinstatementTakeActionCmDetails();
		    		ccDetails.setAllocateCCId(takeActionData);
		    		ccDetails.setRemarkByCM(remarks);
		    		ccDetails.setReinstatement(reinStatementData);
		    		ccDetails.setSupportingDocumentName(documentName);
		    		ccDetails.setSupportingDocument(file);
		    		ccDetails.setCreatedBy(userId);
		    		ComplaintReinstatementTakeActionCmDetails savedCCDetails = complaintReinstatementTakeActionCmDetailsRepo.save(ccDetails);
		    		fileUploadList.add(savedCCDetails.getSupportingDocument());
		    		}
		    	fileUploadList.stream().filter(file -> !Strings.isNullOrEmpty(file))
				.forEach(file -> copyFileToDestination(file, "creinstatement-cm-take-action"));
		    	Integer result = complaintReinstatementTakeActionCmRepository.areAllRecordsTrue(reinsatementId);
		    	Boolean areAllSubmittedData = (result != null && result == 1);
		    	System.err.println("Status Check:----"+areAllSubmittedData);
		    	if(Boolean.TRUE.equals(areAllSubmittedData)) {
		    		reinStatementData.setIsCommitteMemberRemark(true);
		    		reinStatementData.setStatus(102); // Only set if isInspectRemark is true
		    		reinstatementRepository.save(reinStatementData);
		    	}
		    	
		    	saveReinstatementActionHistory(reinsatementId, userId, roleId, userName, roleName, remarks,0);
			} catch (Exception e) {
				e.printStackTrace();
				response.put("status", "error");
		        response.put("message", "An error occurred: " + e.getMessage());
			}
			return response;
		}
		
		private void copyFileToDestination(String fileUpload, String folderName) {
			String srcPath = tempUploadPath + fileUpload;
			// String destDir = finalUploadPath + "certificate-conformity-form-one-a/";
			String destDir = finalUploadPath + folderName + "/";
			String destPath = destDir + fileUpload;

			File srcFile = new File(srcPath);
			if (!srcFile.exists()) {
				logger.error("File not found: {}", srcPath);
				return;
			}

			try {
				Files.createDirectories(Paths.get(destDir));
				Files.copy(srcFile.toPath(), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
				Files.delete(srcFile.toPath());
				logger.info("File moved successfully: {}", fileUpload);
			} catch (IOException e) {
				logger.error("Error moving file: {}", fileUpload, e);
				throw new RuntimeException("Error moving file");
			}
		}

		@Override
		public Map<String, Object> getCMData(String reinsatementId) {
			ComplaintReinstatement data = reinstatementRepository.findByReinstatementId(reinsatementId);
			List<ComplaintReinstatementTakeActionCm> takeActionData = complaintReinstatementTakeActionCmRepository.findByReinstatement(data);
	    	 
	    	 List<CollateralTakeActionCCDetailsDto> takeActionDtoList = new ArrayList<>();
	    	 if (takeActionData != null) {  
	    		    for (ComplaintReinstatementTakeActionCm cmTakeAction : takeActionData) {
	    		    	List<ComplaintReinstatementTakeActionCmDetails> detailsList=complaintReinstatementTakeActionCmDetailsRepo.findByAllocateCCId(cmTakeAction);
	    		    	 for (ComplaintReinstatementTakeActionCmDetails detail : detailsList) {
	    		             CollateralTakeActionCCDetailsDto dto = new CollateralTakeActionCCDetailsDto();

	    		             dto.setAllocateCCId(detail.getAllocateCCId().getAllocateCCId());
	    		             String ccName = tuserRepository.getUserName(detail.getCreatedBy());
	    		             dto.setCcName(ccName);
	    		             dto.setRemarkByCC(detail.getRemarkByCM());
	    		             dto.setApplicationId(detail.getReinstatement().getReinstatementId());
	    		             dto.setCcDetailsId(detail.getCmDetailsId());
	    		             dto.setSupportingDocument(detail.getSupportingDocument());
	    		             dto.setSupportingDocumentName(detail.getSupportingDocumentName());

	    		             takeActionDtoList.add(dto); 
	    		         }
	    		   }
	    		}
	    	 
	    	 
			Map<String, Object> response = new HashMap<>();
	        response.put("ccTakeActionData", takeActionDtoList);
	        
	        return response;
		}

		@Override
		public JSONObject submitCeosecondData(String data) {
			String decodedData = CommonUtil.inputStreamDecoder(data);
			JSONObject response = new JSONObject();
			try{
				JsonNode jsonNode = om.readTree(decodedData);
				String remarks = jsonNode.get("remarks").asText();
		    	int userId = jsonNode.get("userId").asInt();
		    	Integer roleId = jsonNode.get("roleId").asInt();
		        String userName = jsonNode.get("userName").asText();
		        String roleName = jsonNode.get("roleName").asText();
		    	int radioCheck = jsonNode.get("radioCheck").asInt();
		    	String reinsatementId = jsonNode.get("reinsatementId").asText();
		    	ComplaintReinstatement reinstatementData = reinstatementRepository.findByReinstatementId(reinsatementId);
		    	ComplaintManagementNewEntity compaintdata=complaintManagementNewRepository.findByIdAndDeletedFlag(reinstatementData.getComplaintId(),false);
		    	ComplaintReinstatementTakeActionCeoSecond ceoTakeActionData=new ComplaintReinstatementTakeActionCeoSecond();
		    	ceoTakeActionData.setCeoRemark(remarks);
		    	ceoTakeActionData.setReinstatement(reinstatementData);
		    	ceoTakeActionData.setCreatedBy(userId);
		    	ceoTakeActionData.setDeletedFlag(false);
		    	TOnlineServiceApproval onlineApprovalSuspendData=tOnlineServiceApprovalRepository.findByIntOnlineServiceIdAndBitDeletedFlag(reinstatementData.getComplaintId(),false);
		    	if(radioCheck==1) {
		    		Tuser userData=tuserRepository.findByIntId(reinstatementData.getUserId());
		    		if(reinstatementData.getIsCerLicCheck().equals("1")) {
		    			userData.setOpcsuspStatus(false);
		    			userData.setVchUserStatus(null);
		    		}else if(reinstatementData.getIsCerLicCheck().equals("2")) {
		    			userData.setVchUserStatus(null);
		    			userData.setOplsuspStatus(false);
		    		}
		    		onlineApprovalSuspendData.setVchForwardAllAction("Suspend removed");
		    		compaintdata.setApplicationStatus("Suspend removed");
		    		reinstatementData.setStatus(103);
		    		 tuserRepository.save(userData);
		    	}else if(radioCheck==2) {
		    		compaintdata.setApplicationStatus("Suspended");
		    		onlineApprovalSuspendData.setVchForwardAllAction("Suspended");
		    		reinstatementData.setStatus(104);
		    	}
		    	tOnlineServiceApprovalRepository.save(onlineApprovalSuspendData);
		    	complaintManagementNewRepository.save(compaintdata);
		    	complaintReinstatementTakeActionCeoSecondRe.save(ceoTakeActionData);
		    	 reinstatementRepository.save(reinstatementData);
		    	 saveReinstatementActionHistory(reinsatementId, userId, roleId, userName, roleName, remarks,radioCheck);
		    	response.put("status", "Success");
			}catch (Exception e) {
				e.printStackTrace();
				response.put("status", "error");
		        response.put("message", "An error occurred: " + e.getMessage());
			}
			return response;
		}

		@Override
		public Map<String, Object> getReinstatementViewDataById(String reinsatementId) {
			ComplaintReinstatement reinstatement = reinstatementRepository.findByReinstatementId(reinsatementId);
			Map<String, Object> data = new HashMap<>();
			if (reinstatement != null) {
			    String warehouseId = tuserRepository.findWarehouseIdByIntId(reinstatement.getUserId());
			    ApplicationOfConformityLocationDetails locationData = applicationConformityLocDetRepository.findByWarehouseId(warehouseId);
			    String companyId=reinstatementRepository.findCompanyIdByWarehouseId(warehouseId);
//			    List<AocCompProfDetailsMainDTO> companyDetailsDTO = aocService.getCompanyProfileDataByUserId(reinstatement.getUserId());
			    AocCompProfDetailsMainDTO companyDetailsDTO=aocService.getCompanyDetails(companyId);
			    List<ApplicationOfConformityCommodityDetails> commodityData=applicationOfConformityCommodityDetailsRepository.findByWarehouseLocation(locationData);
				ApplicationOfConformityFormOneA formOneA=applicationOfConformityFormOneARepository.findByWarehouseLocationAndDeletedFlagFalse(locationData);
				ApplicationOfConformityFormOneB formOneB=applicationOfConformityFormOneBRepository.findByWarehouseLocationAndDeletedFlagFalse(locationData);
				List<FormOneCResponseDto> formOneCData=documentRepository.findByWareHouseIdAndDeletedFlagFalse(warehouseId).stream()
						.map(doc -> new FormOneCResponseDto(doc.getDocName(), doc.getFileName(), doc.getFormOneCId(),doc.getId()))
						.collect(Collectors.toList());
				Integer confirmityIdCert = conformityMainRepository.findIdByWarehouseId(warehouseId);
				
				data.put("commodityData", commodityData);
				data.put("formOneA", formOneA);
				data.put("formOneB", formOneB);
				data.put("formOneCData", formOneCData);
				data.put("companyDetailsDTO", companyDetailsDTO);
				data.put("confirmityIdCert", confirmityIdCert);
				
			    data.put("complaintData", complaintManagementNewRepository.findByIdAndDeletedFlag(reinstatement.getComplaintId(), false));
			    data.put("warehouseLocations", locationData);
			    data.put("licenceNo", complaintManagementNewRepository.getLicenceNo(warehouseId));
			    data.put("notingsData", onlineServiceApprovalNotingsRepository.getNotingData(reinstatement.getComplaintId(), 8));
			    data.put("reinstatement", reinstatement);
			}
 
			return data;
		}
		
		private void saveReinstatementActionHistory(String reinstatementId, Integer userId, Integer roleId, String userName, String roleName, String remark, Integer ceoSuspendStatus) {
		    ReinstatementActionHistory history = new ReinstatementActionHistory();
		    history.setReinstatementId(reinstatementId);
		    history.setUserId(userId);
		    history.setRoleId(roleId);
		    history.setUserName(userName);
		    history.setRollName(roleName);
		    history.setRemark(remark);
		    history.setDtmActionTaken(LocalDateTime.now());
		    history.setCeoSuspendStatus(ceoSuspendStatus);
		    
		    reinstatementActionHistoryRepository.save(history);
		}

		@Override
		public List<ReinstatementActionHistory> getHistoryData(String reinsatementId) {
			return reinstatementActionHistoryRepository.findByReinstatementId(reinsatementId);
		}

		


}
