package app.ewarehouse.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Strings;

import app.ewarehouse.dto.AocComplianceCollarterDirectorDTO;
import app.ewarehouse.dto.AocComplianceCollarterSignSealDTO;
import app.ewarehouse.dto.ApplicationCertificateOfCollateralDto;
import app.ewarehouse.dto.CollateralCertificateDTO;
import app.ewarehouse.dto.CollateralCvUploadDTO;
import app.ewarehouse.dto.CollateralManagerMainFormDTO;
import app.ewarehouse.dto.CollateralTakeActionCCDetailsDto;
import app.ewarehouse.dto.PaymentDTO;
import app.ewarehouse.dto.TempUserDTO;
import app.ewarehouse.entity.AocComplianceCollarterDirector;
import app.ewarehouse.entity.AocComplianceCollarterSignSeal;
import app.ewarehouse.entity.ApplicationCertificateOfCollateral;
import app.ewarehouse.entity.CeoCollarterTakeAction;
import app.ewarehouse.entity.CollateralCVUploadedDetails;
import app.ewarehouse.entity.CollateralCertificate;
import app.ewarehouse.entity.CollateralTakeActionCC;
import app.ewarehouse.entity.CollateralTakeActionCCDetails;
import app.ewarehouse.entity.OicCollateralTakeAction;
import app.ewarehouse.entity.OicCollateralTakeActionInspector;
import app.ewarehouse.entity.PaymentStatus;
import app.ewarehouse.entity.TempUser;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.repository.AOCCollateralDetailsRepository;
import app.ewarehouse.repository.AOCCollateralDirectorRepository;
import app.ewarehouse.repository.AOCCollateralSignSealRepository;
import app.ewarehouse.repository.CEOTakeActionRepository;
import app.ewarehouse.repository.CollateralCertificateRepository;
import app.ewarehouse.repository.CollateralCvUploadRepository;
import app.ewarehouse.repository.CollateralTakeActionCCDetailsRepository;
import app.ewarehouse.repository.CollateralTakeActionCCRepository;
import app.ewarehouse.repository.CollateralTakeActionInspectorRepository;
import app.ewarehouse.repository.OicCollateralTakeActionRepository;
import app.ewarehouse.repository.PaymentDataRepository;
import app.ewarehouse.repository.TempUserRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.AOCCollateralDetailsService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.Mapper;
import jakarta.transaction.Transactional;
@Service
public class AOCCollateralDetailsServiceImpl implements AOCCollateralDetailsService {
	
	private static final String STATUS = "status";
	private static final String AN_UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred: ";
	private static final String ERROR = "error";
	
	private static final Logger logger = LoggerFactory.getLogger(AOCCollateralDetailsServiceImpl.class);
	
	@Autowired
	private TempUserRepository tempUserRepository;
	
	@Autowired
	private AOCCollateralDetailsRepository collateralDetailsRepository;
	
	@Autowired
	private AOCCollateralDirectorRepository directorRepository;
	
	@Autowired
	private AOCCollateralSignSealRepository signSealRepository;
	
	@Autowired
	private AOCCollateralDetailsRepository aOCCollateralDetailsRepository;
	
	@Autowired
	private CollateralTakeActionCCRepository collateralTakeActionCCRepository;
	
	@Autowired
	private CollateralTakeActionInspectorRepository collateralTakeActionInspectorRepository;
	
	@Autowired
	private CollateralTakeActionCCDetailsRepository collateralTakeActionCCDetailsRepository;
	
	@Autowired
	private OicCollateralTakeActionRepository oicCollateralTakeActionRepository;
	
	@Autowired
	private TuserRepository tuserRepository;
	
	@Autowired
	private CEOTakeActionRepository ceoTakeActionRepository;
	
	@Autowired
	private PaymentDataRepository paymentRepository;
	
	@Autowired
	private CollateralCvUploadRepository cvUploadRepository;
	
	@Autowired
	private CollateralCertificateRepository collateralCertificateRepository;
	
	 @Autowired
	 private ObjectMapper om;
	 
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	
	@Value("${finalUpload.path}")
	private String finalUploadPath;
	

	@Override
	public TempUserDTO getCollateralInfoByUserId(String emailId) {
		
		TempUser tempUserInfo = tempUserRepository.findByEmail(emailId);
		
		TempUserDTO tempUserDto= new TempUserDTO();
		
		BeanUtils.copyProperties(tempUserInfo, tempUserDto);
		
		return tempUserDto;
	}

	
	@Override
	@Transactional
	public JSONObject saveAsDraftCollateralDetails(String data) {
		
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			CollateralManagerMainFormDTO collateralDetailsDTO = om.readValue(decodedData, CollateralManagerMainFormDTO.class);
			
			 String applicationId = collateralDetailsDTO.getCollateralProfile().getApplicationId();
			 System.err.println(applicationId);
			
			 ApplicationCertificateOfCollateral collateralInfo = Mapper.toEntity(collateralDetailsDTO);
			 
			if(collateralDetailsDTO.getCollateralProfile().getApplicationId() != null) {
			Optional<ApplicationCertificateOfCollateral> collateralContent = collateralDetailsRepository.findById(collateralDetailsDTO.getCollateralProfile().getApplicationId());
			collateralInfo.setPaymentMode(collateralContent.isPresent() ? collateralContent.get().getPaymentMode() : "");
			collateralInfo.setPaymentSuccess(collateralContent.isPresent() ? collateralContent.get().getPaymentSuccess() : false);
			if(collateralContent.isPresent() && !collateralDetailsDTO.getCollateralProfile().getEntityType().equals(collateralContent.get().getEntityType())) {
					directorRepository.deleteAllByApplicationId(collateralContent.get().getApplicationId());
				}
			}
			
			// Save collateral profile
			
			collateralInfo.setCreatedBy(collateralDetailsDTO.getUserId());
			collateralInfo.setIsDraft(collateralDetailsDTO.getIsDraft());
			collateralInfo.setActionStatus(100);
			
			if (collateralDetailsDTO.getCollateralProfile().getApplicationId() != null) {
				collateralInfo.setApplicationId(collateralDetailsDTO.getCollateralProfile().getApplicationId());
							
			} else {
				collateralInfo.setApplicationId(getId("WRS"));
			}
			collateralDetailsRepository.save(collateralInfo);
			
			
			// Save directors
			if(!collateralDetailsDTO.getDirectors().isEmpty()) {
			for (AocComplianceCollarterDirectorDTO directorDTO : collateralDetailsDTO.getDirectors()) {
				AocComplianceCollarterDirector director = Mapper.toEntity(directorDTO);
				if (directorDTO.getDirectorId() != null) {
					director.setDirectorId(directorDTO.getDirectorId());
				} else {
					director.setDirectorId(getId("DIR"));
				}
				director.setCreatedBy(collateralDetailsDTO.getUserId());
				director.setApplicationId(collateralInfo);
				directorRepository.save(director);
			}
			}
			
			// save upload cv files
			if(!collateralDetailsDTO.getDirectors().isEmpty()) {
				for (CollateralCvUploadDTO uploadCvDTO : collateralDetailsDTO.getCvUploadFiles()) {
					CollateralCVUploadedDetails uplaodCv = Mapper.toEntity(uploadCvDTO);
					if (uploadCvDTO.getCvId() != null) {
						uplaodCv.setCvId(uploadCvDTO.getCvId());
					} 
					
					uplaodCv.setCreatedBy(collateralDetailsDTO.getUserId());
					uplaodCv.setApplicationId(collateralInfo);
					cvUploadRepository.save(uplaodCv);
				}
			}
			
			// Save sign/seal
			AocComplianceCollarterSignSeal signSeal = Mapper.toEntity(collateralDetailsDTO.getSignSeal());
			if (collateralDetailsDTO.getSignSeal().getSignSealId() != null) {
				signSeal.setSignSealId(collateralDetailsDTO.getSignSeal().getSignSealId());
			} else {
				signSeal.setSignSealId(getId("SIN"));
			}
			signSeal.setCreatedBy(collateralDetailsDTO.getUserId());
			signSeal.setApplicationId(collateralInfo);
			
			signSealRepository.save(signSeal);
			
			
			
			json.put(STATUS, 200);
			json.put("collartorApplicationId", collateralInfo.getApplicationId());
			
		
		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
			e.printStackTrace();
		}
		return json;
	}
	
	@Override
	public CollateralManagerMainFormDTO getCollateralDetails(String collarteralId) {
		CollateralManagerMainFormDTO collateralDetailsDTO = new CollateralManagerMainFormDTO();

		// Get Collateral Manager Information
		Optional<ApplicationCertificateOfCollateral> collateralOptional = collateralDetailsRepository.findById(collarteralId);
		if (collateralOptional.isPresent()) {
			ApplicationCertificateOfCollateral collateralInfo = collateralOptional.get();
			ApplicationCertificateOfCollateralDto collateralDTO = new ApplicationCertificateOfCollateralDto();
			BeanUtils.copyProperties(collateralInfo, collateralDTO);
			collateralDetailsDTO.setCollateralProfile(collateralDTO);

			// Get directors
			List<AocComplianceCollarterDirector> directors = directorRepository.findByApplicationIdAndDeletedFlagFalse(collateralInfo);
			if(!directors.isEmpty()) {
			List<AocComplianceCollarterDirectorDTO> directorDTOs = directors.stream().map(director -> {
				AocComplianceCollarterDirectorDTO directorDTO = new AocComplianceCollarterDirectorDTO();
				BeanUtils.copyProperties(director, directorDTO);
				directorDTO.setApplicationId(collateralInfo.getApplicationId());
				return directorDTO;
			}).collect(Collectors.toList());
			 collateralDetailsDTO.setDirectors(directorDTOs);
			}
			
			// Get Cv Upload
			List<CollateralCVUploadedDetails> staffCvs = cvUploadRepository.findByApplicationIdAndDeletedFlagFalse(collateralInfo);
			if(!staffCvs.isEmpty()) {
			List<CollateralCvUploadDTO> staffCvDTOs = staffCvs.stream().map(cv -> {
				CollateralCvUploadDTO staffCvDTO = new CollateralCvUploadDTO();
				BeanUtils.copyProperties(cv, staffCvDTO);
				staffCvDTO.setApplicationId(collateralInfo.getApplicationId());
				return staffCvDTO;
			}).collect(Collectors.toList());
			 collateralDetailsDTO.setCvUploadFiles(staffCvDTOs);
			}
			

			// Get sign/seal
			AocComplianceCollarterSignSeal signSeal = signSealRepository.findByApplicationId(collateralInfo);
			if (signSeal != null) {
				AocComplianceCollarterSignSealDTO signSealDTO = new AocComplianceCollarterSignSealDTO();
				BeanUtils.copyProperties(signSeal, signSealDTO);
				signSealDTO.setApplicationId(collateralInfo.getApplicationId());
				collateralDetailsDTO.setSignSeal(signSealDTO);
			}
		}
		return collateralDetailsDTO;
	}
	

	@Override
	public List<CollateralManagerMainFormDTO> getCollateralDataByUserId(Integer userId) {
		List<ApplicationCertificateOfCollateral> collateralList = collateralDetailsRepository.findByCreatedByAndPaymentSuccessFalse(userId);

		// Map each profile to AocCompProfDetailsMainDTO
		return collateralList.stream().map(collateral -> {
			CollateralManagerMainFormDTO collateralDetailsDTO = new CollateralManagerMainFormDTO();

			// Copy collateral details
			ApplicationCertificateOfCollateralDto collateralDTO = new ApplicationCertificateOfCollateralDto();
			BeanUtils.copyProperties(collateral, collateralDTO);
			collateralDetailsDTO.setCollateralProfile(collateralDTO);
			collateralDetailsDTO.setUserId(collateral.getCreatedBy());
			collateralDetailsDTO.setIsDraft(collateral.getIsDraft());
			
			// Get directors
			List<AocComplianceCollarterDirector> directors = directorRepository.findByApplicationIdAndDeletedFlagFalse(collateral);
			List<AocComplianceCollarterDirectorDTO> directorDTOs = directors.stream().map(director -> {
				AocComplianceCollarterDirectorDTO directorDTO = new AocComplianceCollarterDirectorDTO();
				BeanUtils.copyProperties(director, directorDTO);
				directorDTO.setApplicationId(collateral.getApplicationId());
				return directorDTO;
			}).collect(Collectors.toList());
			collateralDetailsDTO.setDirectors(directorDTOs);
			
			// Get upload cvs
			List<CollateralCVUploadedDetails> staffCvs = cvUploadRepository.findByApplicationIdAndDeletedFlagFalse(collateral);
			List<CollateralCvUploadDTO> staffCvDTOs = staffCvs.stream().map(cv -> {
				CollateralCvUploadDTO staffCvDTO = new CollateralCvUploadDTO();
				BeanUtils.copyProperties(cv, staffCvDTO);
				staffCvDTO.setApplicationId(collateral.getApplicationId());
				return staffCvDTO;
			}).collect(Collectors.toList());
			 collateralDetailsDTO.setCvUploadFiles(staffCvDTOs);

			 
			// Get sign/seal
			AocComplianceCollarterSignSeal signSeal = signSealRepository.findByApplicationId(collateral);
			if (signSeal != null) {
				AocComplianceCollarterSignSealDTO signSealDTO = new AocComplianceCollarterSignSealDTO();
				BeanUtils.copyProperties(signSeal, signSealDTO);
				signSealDTO.setApplicationId(collateral.getApplicationId());
				collateralDetailsDTO.setSignSeal(signSealDTO);
			}

			return collateralDetailsDTO;
		}).collect(Collectors.toList());
		

	}
	
	@Override
	public Page<CollateralManagerMainFormDTO> getCollateralDataByUserId(Integer userId, int page, int size) {
		
		Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "applicationId"));
		Page<ApplicationCertificateOfCollateral> collateralList = collateralDetailsRepository.findByCreatedByAndDeletedFlagFalseAndIsDraftFalseAndPaymentSuccessTrue(userId, pageable);
		
		return collateralList.map(collateral -> {
			CollateralManagerMainFormDTO collateralDetailsDTO = new CollateralManagerMainFormDTO();

			// Copy collateral details
			ApplicationCertificateOfCollateralDto collateralDTO = new ApplicationCertificateOfCollateralDto();
			BeanUtils.copyProperties(collateral, collateralDTO);
			collateralDetailsDTO.setCollateralProfile(collateralDTO);
			collateralDetailsDTO.setUserId(collateral.getCreatedBy());
			collateralDetailsDTO.setIsDraft(collateral.getIsDraft());
			
			// Get directors
			List<AocComplianceCollarterDirector> directors = directorRepository.findByApplicationIdAndDeletedFlagFalse(collateral);
			List<AocComplianceCollarterDirectorDTO> directorDTOs = directors.stream().map(director -> {
				AocComplianceCollarterDirectorDTO directorDTO = new AocComplianceCollarterDirectorDTO();
				BeanUtils.copyProperties(director, directorDTO);
				directorDTO.setApplicationId(collateral.getApplicationId());
				return directorDTO;
			}).collect(Collectors.toList());
			collateralDetailsDTO.setDirectors(directorDTOs);
			
			
			// Get CVs
			List<CollateralCVUploadedDetails> staffCvs = cvUploadRepository.findByApplicationIdAndDeletedFlagFalse(collateral);
			List<CollateralCvUploadDTO> staffCvDTOs = staffCvs.stream().map(cv -> {
				CollateralCvUploadDTO staffCvDTO = new CollateralCvUploadDTO();
				BeanUtils.copyProperties(cv, staffCvDTO);
				staffCvDTO.setApplicationId(collateral.getApplicationId());
				return staffCvDTO;
			}).collect(Collectors.toList());
			 collateralDetailsDTO.setCvUploadFiles(staffCvDTOs);

			// Get sign/seal
			AocComplianceCollarterSignSeal signSeal = signSealRepository.findByApplicationId(collateral);
			if (signSeal != null) {
				AocComplianceCollarterSignSealDTO signSealDTO = new AocComplianceCollarterSignSealDTO();
				BeanUtils.copyProperties(signSeal, signSealDTO);
				signSealDTO.setApplicationId(collateral.getApplicationId());
				collateralDetailsDTO.setSignSeal(signSealDTO);
			}

			return collateralDetailsDTO;
		});
		
	}
	
	@Override
	public JSONObject updateMainTable(String code) {
		 JSONObject response = new JSONObject();
		 try {
		        String decodedData = CommonUtil.inputStreamDecoder(code);
		        JSONObject obj = new JSONObject(decodedData);

		        String applicationId = obj.optString("applicationId", "");
		        Integer userId = obj.optInt("userId", 0);
		        Integer roleId = obj.optInt("roleId", 0);

		        if (applicationId.isEmpty() || userId == 0 || roleId == 0) {
		            response.put("status", "error");
		            response.put("message", "Invalid input parameters");
		            return response;
		        }
	        
	           Optional<ApplicationCertificateOfCollateral> optionalEntity = collateralDetailsRepository.findById(applicationId);
	        
	           if (optionalEntity.isPresent()) {
	        	   ApplicationCertificateOfCollateral entity = optionalEntity.get();

	        	   entity.setCreatedBy(userId);
	        	   entity.setPaymentMode(obj.optString("paymentType"));
	               entity.setPaymentSuccess(true); 
	               entity.setIsDraft(false); 
	            
	            	collateralDetailsRepository.save(entity);

	            	response.put("status", "success");
	            	response.put("applicationId", entity.getApplicationId());
	            	response.put("draftStatus", entity.getIsDraft());
	            	response.put("paymentStatus", entity.getPaymentSuccess());
	          
	           } else {
	        	   response.put("status", "error");
	        	   response.put("message", "Application not found for ID: " + applicationId);
	        	   
	           }
		 
         } catch (Exception e) {
        	 	logger.error("internal server error", e);
        	 	response.put("status", "error");
        	 	response.put("message", "An error occurred while processing the request");
         }
	        
	        return response;
    }
	
	private String getId(String idName) {
	    String dbCurrentId = switch (idName) {
	        case "WRS" -> collateralDetailsRepository.getId();
	        case "DIR" -> directorRepository.getId();
	        case "SIN" -> signSealRepository.getId();
	        default -> null;
	    };

	    if (dbCurrentId == null) {
	        
	        return idName + (System.currentTimeMillis() % 1000000); 
	    } else {
	        
	        long idNum = Long.parseLong(dbCurrentId.substring(3));
	        long nextVal = idNum + 1;  
	        return idName + nextVal;
	    }
	}
	
	 
	@Override
	public Page<ApplicationCertificateOfCollateralDto> getAllApplicationsData(Integer page, Integer size,
			String status,Integer roleId,Integer userId) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> data = null;
		if(roleId==59) {
			if (status.equals("all")) {
				 data=aOCCollateralDetailsRepository.getAllDataPaginationCC(pageable,userId);
			} else if (status.equals("Approved")) {
				 data=aOCCollateralDetailsRepository.getAllApproveDataPaginationCC(pageable,userId);
			} else if (status.equals("Rejected")) {
				 data=aOCCollateralDetailsRepository.getAllRejectDataPaginationCC(pageable,userId);
			}
		}else if(roleId==3) {
			if (status.equals("all")) {
			        data = aOCCollateralDetailsRepository.getAllInspectorDataPagination(pageable, userId);
			  } else if (status.equals("Approved")) {
			        data = aOCCollateralDetailsRepository.getAllInspectorApproveDataPagination(pageable, userId);
			  } else if (status.equals("Rejected")) {
			        data = aOCCollateralDetailsRepository.getAllInspectorRejectDataPagination(pageable, userId);
			  }
		}else {
			if (status.equals("all")) {
				 data=aOCCollateralDetailsRepository.getAllDataPagination(pageable);
			} else if (status.equals("Approved")) {
				 data=aOCCollateralDetailsRepository.getAllApproveDataPagination(pageable);
			} else if (status.equals("Rejected")) {
				 data=aOCCollateralDetailsRepository.getAllRejectDataPagination(pageable);
			}
		}
		
		Page<ApplicationCertificateOfCollateralDto> dtoPage = data.map(row -> 
		new ApplicationCertificateOfCollateralDto(
	    		(String) row[0],  // applicationId
	            (String) row[1],  // fullName
	            (String) row[2],  // mobileNo
	            (String) row[3],  // email
	            (String) row[4],  // postalCode
	            (String) row[5],  // postalAddress
	            (String) row[6],  // town
	            (String) row[7],  // telephoneNo
	            (String) row[8],  // website
	            (String) row[9],  // entityType
	            (String) row[10], // companyName
	            (String) row[11], // companyRegNo
	            (String) row[12], // kraPin
	            (String) row[13], // paymentMode
	            (Boolean) row[14], // paymentSuccess
	            (Integer) row[15], // actionStatus
	            (Boolean) row[16], // deletedFlag
	            (Boolean) row[17], // inspector
	            (Boolean) row[18], // certificateCommittee
	            (Boolean) row[19], // defer
	            (String) row[20], // vchStatusDescription
	            (Boolean) row[21], // vchStatusDescription
	            (Boolean) row[22], // vchStatusDescription
	            (Boolean) row[23] // vchStatusDescription
	            		 
	          
	    )
	);
		return dtoPage;
	}

	@Override
	public JSONObject forwardToCC(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();

	    try {
	        JsonNode jsonNode = om.readTree(decodedData);
	        Integer userId = jsonNode.get("userId").asInt();
	        String vchApplicationId = jsonNode.get("vchApplicationId").asText();
	        ArrayNode selectedMembers = (ArrayNode) jsonNode.get("selectedMembers");
	        
	        ApplicationCertificateOfCollateral applicantData = aOCCollateralDetailsRepository.findByApplicationId(vchApplicationId);
	        if (applicantData == null) {
	            response.put("status", "error");
	            response.put("message", "Applicant data not found.");
	            return response;
	        }
	        
	        for (JsonNode member : selectedMembers) {
	            int memberId = member.get("id").asInt();
	            CollateralTakeActionCC entity = new CollateralTakeActionCC();
	            entity.setColletral(applicantData);
	            entity.setCertificateCommitteeId(memberId);
	            entity.setCreatedBy(userId);
	            collateralTakeActionCCRepository.save(entity);
	        }
	        applicantData.setActionStatus(101);
	        applicantData.setIsCertificatecommittee(true);
	        aOCCollateralDetailsRepository.save(applicantData);
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
	public JSONObject forwardToInsp(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
	    try {
	        JsonNode jsonNode = om.readTree(decodedData);
	        Integer userId = jsonNode.get("userId").asInt();
	        String vchApplicationId = jsonNode.get("vchApplicationId").asText();
	        int inspectorTeamLeadId = jsonNode.get("inspectorTeamLeadId").asInt();
	        ArrayNode inspectorList = (ArrayNode) jsonNode.get("inspectorList");
	        ApplicationCertificateOfCollateral applicantData = aOCCollateralDetailsRepository.findByApplicationId(vchApplicationId);
	        if (applicantData == null) {
	            response.put("status", "error");
	            response.put("message", "Applicant data not found.");
	            return response;
	        }
	        
	        OicCollateralTakeActionInspector entity = new OicCollateralTakeActionInspector();
			entity.setColletral(applicantData);
			entity.setInspectorTeamLeadId(inspectorTeamLeadId);
			entity.setInspectorId(inspectorList.toString());
			entity.setIsEdit(true);
			entity.setCreatedBy(userId);
			collateralTakeActionInspectorRepository.save(entity);
	        
	        applicantData.setActionStatus(101);
	        applicantData.setIsInspector(true);
	        aOCCollateralDetailsRepository.save(applicantData);
	        response.put("status", "success");
	        response.put("message", "Data saved successfully.");
	    } catch (Exception e) {
	        response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
	    }

	    return response;
	}

	@Override
	public JSONObject submitCCData(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
	    List<String> fileUploadList = new ArrayList<>();
	    try {
	    	JsonNode jsonNode = om.readTree(decodedData);
	    	ArrayNode documents = (ArrayNode) jsonNode.get("documents");
	    	String remarks = jsonNode.get("remarks").asText();
	    	int userId = jsonNode.get("userId").asInt();
	    	String vchApplicationId = jsonNode.get("vchApplicationId").asText();
	    	 ApplicationCertificateOfCollateral applicantData = aOCCollateralDetailsRepository.findByApplicationId(vchApplicationId);
	    	 CollateralTakeActionCC takeActionData = collateralTakeActionCCRepository.findByCertificateCommitteeIdAndColletral(userId, applicantData);
	    	 takeActionData.setRemarkByCC(remarks);
	    	 takeActionData.setForwardToOicStatus(true);
	    	 collateralTakeActionCCRepository.save(takeActionData);
	    	for (JsonNode doc : documents) {
	    		String documentName = doc.get("documentName").asText();
	    		String file = doc.get("file").asText();
	    		CollateralTakeActionCCDetails ccDetails = new CollateralTakeActionCCDetails();
	    		ccDetails.setAllocateCCId(takeActionData);
	    		ccDetails.setRemarkByCC(remarks);
	    		ccDetails.setColletral(applicantData);
	    		ccDetails.setSupportingDocumentName(documentName);
	    		ccDetails.setSupportingDocument(file);
	    		ccDetails.setCreatedBy(userId);
	    		CollateralTakeActionCCDetails savedCCDetails = collateralTakeActionCCDetailsRepository.save(ccDetails);
	    		fileUploadList.add(savedCCDetails.getSupportingDocument());
	    		}
	    	fileUploadList.stream().filter(file -> !Strings.isNullOrEmpty(file))
			.forEach(file -> copyFileToDestination(file, "colletral-cc-take-action"));
	    	Integer result = collateralTakeActionCCRepository.areAllRecordsTrue(vchApplicationId);
	    	Boolean areAllSubmittedData = (result != null && result == 1);
	    	if(Boolean.TRUE.equals(areAllSubmittedData)) {
	    		applicantData.setIsCcRemark(true);
	    		//if (Boolean.TRUE.equals(applicantData.getIsInspectRemark())) {
	    	        applicantData.setActionStatus(102); // Only set if isInspectRemark is true
	    	    //}
	    		aOCCollateralDetailsRepository.save(applicantData);
	    	}
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
	public JSONObject submitInspectorData(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
//	    List<String> fileUploadList = new ArrayList<>();
	    try {
	    	JsonNode jsonNode = om.readTree(decodedData);
	    	String remarks = jsonNode.get("remarks").asText();
	    	String fileName = jsonNode.get("inspectionFilePath").asText();
	    	int userId = jsonNode.get("userId").asInt();
	    	String vchApplicationId = jsonNode.get("vchApplicationId").asText();
	    	 ApplicationCertificateOfCollateral applicantData = aOCCollateralDetailsRepository.findByApplicationId(vchApplicationId);
	    	 OicCollateralTakeActionInspector takeActionData = collateralTakeActionInspectorRepository.findByInspectorTeamLeadIdAndColletral(userId, applicantData);
	    	 takeActionData.setRemarkByInspector(remarks);
	    	 takeActionData.setInspectionFilePath(fileName);
	    	 OicCollateralTakeActionInspector takeActionDataSave=collateralTakeActionInspectorRepository.save(takeActionData);
	    	
	    	 List<String> fileUploadList = Arrays.asList(takeActionDataSave.getInspectionFilePath());
			    fileUploadList.stream().filter(file -> !Strings.isNullOrEmpty(file))
				.forEach(file -> copyFileToDestination(file, "colletral-manager"));
	    		applicantData.setIsInspectRemark(true);
	    		if (Boolean.TRUE.equals(applicantData.getIsCcRemark())) {
	    	        applicantData.setActionStatus(102); // Only set if iscc is true
	    	    }
	    		aOCCollateralDetailsRepository.save(applicantData);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}

	@Override
	public JSONObject submitOicSecondData(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject response = new JSONObject();
		try{
			JsonNode jsonNode = om.readTree(decodedData);
			String remarks = jsonNode.get("remarks").asText();
	    	int userId = jsonNode.get("userId").asInt();
	    	String vchApplicationId = jsonNode.get("vchApplicationId").asText();
	    	ApplicationCertificateOfCollateral applicantData = aOCCollateralDetailsRepository.findByApplicationId(vchApplicationId);
	    	OicCollateralTakeAction oicTakeActionData=new OicCollateralTakeAction();
	    	oicTakeActionData.setRemarkByOic(remarks);
	    	oicTakeActionData.setColletral(applicantData);
	    	oicTakeActionData.setCreatedBy(userId);
	    	oicTakeActionData.setDeletedFlag(false);
    	    applicantData.setActionStatus(103); 
    	   
	    	oicCollateralTakeActionRepository.save(oicTakeActionData);
	    	 aOCCollateralDetailsRepository.save(applicantData);
	    	response.put("status", "Success");
		}catch (Exception e) {
			e.printStackTrace();
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}


	@Override
	public Object getInspectorDataById(String collateralId,Integer userId) {
		// TODO Auto-generated method stub
		ApplicationCertificateOfCollateral applicantData = aOCCollateralDetailsRepository.findByApplicationId(collateralId);
		return collateralTakeActionInspectorRepository.findByInspectorTeamLeadIdAndColletral(userId, applicantData);
	}


	@Override
	public Map<String, Object> getOicTwoData(String vchApplicationId) {
		ApplicationCertificateOfCollateral applicantData = aOCCollateralDetailsRepository.findByApplicationId(vchApplicationId);
//		OicCollateralTakeActionInspector inspectorData = collateralTakeActionInspectorRepository.findByColletral(applicantData);
//		CollateralInspectorDetailsDTO inspectorDto= new CollateralInspectorDetailsDTO();
//		inspectorDto.setAllocateInspectorId(inspectorData.getAllocateInspectorId());
//		inspectorDto.setRemarkByInspector(inspectorData.getRemarkByInspector());;
//		inspectorDto.setInspectionFilePath(inspectorData.getInspectionFilePath());
    	 
		List<CollateralTakeActionCC> takeActionData = collateralTakeActionCCRepository.findByColletral(applicantData);
    	 
    	 List<CollateralTakeActionCCDetailsDto> takeActionDtoList = new ArrayList<>();
    	 if (takeActionData != null) {  // Ensure takeActionData is not null
    		    for (CollateralTakeActionCC collateralTakeActionCC : takeActionData) {
    		    	List<CollateralTakeActionCCDetails> ccDetailsList=collateralTakeActionCCDetailsRepository.findByAllocateCCId(collateralTakeActionCC);
    		    	 for (CollateralTakeActionCCDetails ccDetails : ccDetailsList) {
    		             CollateralTakeActionCCDetailsDto dto = new CollateralTakeActionCCDetailsDto();

    		             dto.setAllocateCCId(ccDetails.getAllocateCCId().getAllocateCCId());
    		             String ccName = tuserRepository.getUserName(ccDetails.getCreatedBy());
    		             dto.setCcName(ccName);
    		             dto.setRemarkByCC(ccDetails.getRemarkByCC());
    		             dto.setApplicationId(ccDetails.getColletral().getApplicationId());
    		             dto.setCcDetailsId(ccDetails.getCcDetailsId());
    		             dto.setSupportingDocument(ccDetails.getSupportingDocument());
    		             dto.setSupportingDocumentName(ccDetails.getSupportingDocumentName());

    		             takeActionDtoList.add(dto); 
    		         }
    		   }
    		}
    	 
    	 String vchRemarkByOIC=oicCollateralTakeActionRepository.findRemarkByOicByApplicationId(vchApplicationId);
    	 
		Map<String, Object> response = new HashMap<>();
        response.put("ccTakeActionData", takeActionDtoList);
       // response.put("inspectorData", inspectorDto);
        response.put("vchRemarkByOIC", vchRemarkByOIC);
        
        return response;

	}
	
	
	@Override
	public PaymentDTO getPaymentDetailsByUserId(String applicationId,Integer userId) {
        return paymentRepository.getPaymentDetailsByApplicationIdandUserId(applicationId,userId);
    }


	@Override
	public JSONObject saveCeoTakeAction(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		try {
			JsonNode jsonNode = om.readTree(decodedData);
	        Integer userId = jsonNode.get("userId").asInt();
	        String applicationId = jsonNode.get("applicationId").asText();
	        String rejectedRemark= jsonNode.get("rejectRemark").asText();
	        ApplicationCertificateOfCollateral applicantData = aOCCollateralDetailsRepository.findByApplicationId(applicationId);
			 CeoCollarterTakeAction action = new CeoCollarterTakeAction();
		        action.setApplicationId(applicationId);
		        action.setRejectRemark(rejectedRemark);
		        action.setCreatedBy(userId);
		        action.setIsEditCase(true);
		      
		        // update status
		        applicantData.setActionStatus(107); 
		        aOCCollateralDetailsRepository.save(applicantData);
		       
		        ceoTakeActionRepository.save(action);
		        json.put(STATUS, 200); 
			
		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
			e.printStackTrace();
		}
		return json;
	}
	
	@Override
	public JSONObject removeCollateralDirector(String directorId) {
		JSONObject response = new JSONObject();
		directorRepository.markAsDeleted(directorId);
		response.put("status", "Director Removed");
		return response;
	}


	@Override
	public JSONObject getCollateralCountByUserId(Integer userId) {
		JSONObject response = new JSONObject();
		List<Map<String, Object>> result=aOCCollateralDetailsRepository.getCollateralCountByUserId(userId);
		for(Map<String,Object> data : result) {
			Integer actionStatus = (Integer) data.get("actionStatus");
		    Long idCount = ((Number) data.get("idCount")).longValue();
		    response.put("actionStatus", actionStatus);
		    response.put("idCount", idCount);
		    
		}
		
		return response;
	}
		
	
	@Override
	public CollateralTakeActionCC getCCdDetailsDataById(Integer roleId, Integer userid, String applicationId) {
	ApplicationCertificateOfCollateral applicantData = aOCCollateralDetailsRepository.findByApplicationId(applicationId);
	    	CollateralTakeActionCC takeActionData = collateralTakeActionCCRepository.findByCertificateCommitteeIdAndColletral(userid, applicantData);
			return takeActionData;
    }


	@Transactional
	@Override
	public JSONObject generateCollateralCertificate(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
		try {
			JsonNode jsonNode = om.readTree(decodedData);
			int userId = jsonNode.get("userId").asInt();
			int roleId = jsonNode.get("roleId").asInt();
			String userName = jsonNode.get("userName").asText();
	        String applicantId = jsonNode.get("intApplicantId").asText();
	        ApplicationCertificateOfCollateral applicantData = aOCCollateralDetailsRepository.findByApplicationId(applicantId);
	        // update status
	        applicantData.setActionStatus(104);
	        aOCCollateralDetailsRepository.save(applicantData);
	        CollateralCertificate certificate = new CollateralCertificate();
	        
	        Tuser tuserData=tuserRepository.findByIntId(applicantData.getCreatedBy());
	        tuserData.setSelRole(46);
	        tuserRepository.save(tuserData);
	        
	        certificate.setApproverRoleId(roleId);
	        certificate.setApplicationId(applicantId);
	        certificate.setApproverUserId(userId);
	        certificate.setApproverUserName(userName);
	        certificate.setCertificateNo(getCertificateNo("WRSC/LC/WHOCC/"));
	        certificate.setValidFrom(LocalDate.now());
			certificate.setValidTo(LocalDate.now().plusYears(1));
			certificate.setPaymentStatus(PaymentStatus.SUCCESS);
			collateralCertificateRepository.save(certificate);
	        response.put("status", "success");
	        response.put("message", "Data saved successfully.");
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}
	
	@Override
	public CollateralCertificateDTO getCollateralcertificate(String applicantId) {
		CollateralCertificate certificate = collateralCertificateRepository.findByApplicationId(applicantId);
		CollateralCertificateDTO dto = new CollateralCertificateDTO();
		ApplicationCertificateOfCollateral collateralData=collateralDetailsRepository.findByApplicationId(applicantId);
		dto.setApplicantName(collateralData.getFullName());
		dto.setCompanyName(collateralData.getCompanyName());
		dto.setPostalCode(collateralData.getPostalCode());
		dto.setApplicationId(collateralData.getApplicationId());
		dto.setCertificateNo(certificate.getCertificateNo());
		dto.setApproverUserName(certificate.getApproverUserName());
		
		AocComplianceCollarterSignSeal seal=signSealRepository.findByApplicationId(collateralData);
		dto.setSealPath(seal.getSealPath());
		dto.setDateOfIssue(certificate.getValidFrom());
		dto.setValidFrom(certificate.getValidFrom());
		dto.setValidTo(certificate.getValidTo());
		return dto;
	}
	
	
	
	private String getCertificateNo(String id) {
		String dbCurrentId = collateralCertificateRepository.getId();
		if (dbCurrentId == null) {
			return id + Year.now().getValue() + "/" + "1";
		} else {
			String[] parts = dbCurrentId.split("/");
			String result = parts[parts.length - 1];
			Integer idNum = Integer.parseInt(result);
			AtomicInteger seq = new AtomicInteger(idNum);
			int nextVal = seq.incrementAndGet();
			return id + Year.now().getValue() + "/" + nextVal;
		}
	}


	@Override
	public PaymentDTO getPaymentDetails(String applicationId) {
		 return paymentRepository.getPaymentDetailsByApplicationId(applicationId);
	}


	@Override
	public JSONObject removeCvDetails(String cvId) {
		JSONObject response = new JSONObject();
		cvUploadRepository.markAsDeleted(cvId);
		response.put("status", "CV Details Removed");
		return response;
	}

	@Override
	public JSONObject getCollateralCount(Integer userId) {
		JSONObject response = new JSONObject();
		Integer result=aOCCollateralDetailsRepository.getCountByUserId(userId);
		
		response.put("result",result);
		return response;
	}
	

	


}
