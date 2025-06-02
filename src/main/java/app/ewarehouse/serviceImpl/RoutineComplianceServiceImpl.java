package app.ewarehouse.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import app.ewarehouse.dto.FormOneCResponseDto;
import app.ewarehouse.dto.RoutineComplianceInspDTO;
import app.ewarehouse.entity.ApplicationOfConformityCommodityDetails;
import app.ewarehouse.entity.ApplicationOfConformityFormOneA;
import app.ewarehouse.entity.ApplicationOfConformityFormOneB;
import app.ewarehouse.entity.ApplicationOfConformityLocationDetails;
import app.ewarehouse.entity.OperatorLicenseFormOneC;
import app.ewarehouse.entity.Recommendation;
import app.ewarehouse.entity.RoutineComplianceCeoApproval;
import app.ewarehouse.entity.RoutineComplianceCeoSecond;
import app.ewarehouse.entity.RoutineComplianceComplianceTwo;
import app.ewarehouse.entity.RoutineComplianceConclusion;
import app.ewarehouse.entity.RoutineComplianceInspection;
import app.ewarehouse.entity.RoutineComplianceInspectorAgriculturalCommodity;
import app.ewarehouse.entity.RoutineComplianceInspectorConditionOfGoodsStorage;
import app.ewarehouse.entity.RoutineComplianceInspectorEnvironmentIssues;
import app.ewarehouse.entity.RoutineComplianceInspectorITSystem;
import app.ewarehouse.entity.RoutineComplianceInspectorInsurance;
import app.ewarehouse.entity.RoutineComplianceInspectorLocation;
import app.ewarehouse.entity.RoutineComplianceInspectorPhysicalFireProtection;
import app.ewarehouse.entity.RoutineComplianceInspectorShrink;
import app.ewarehouse.entity.RoutineComplianceInspectorTwo;
import app.ewarehouse.entity.WarehousePersonalEquipment;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.ApplicationConformityLocDetRepository;
import app.ewarehouse.repository.ApplicationOfConformityCommodityDetailsRepository;
import app.ewarehouse.repository.ApplicationOfConformityFormOneARepository;
import app.ewarehouse.repository.ApplicationOfConformityFormOneBRepository;
import app.ewarehouse.repository.ConfirmityFormOneCRepo;
import app.ewarehouse.repository.ConformityMainRepository;
import app.ewarehouse.repository.OperatorLicenseFormOneCRepository;
import app.ewarehouse.repository.RecomendationRepository;
import app.ewarehouse.repository.RoutineComInspecEnvIssuesRepo;
import app.ewarehouse.repository.RoutineComInspecGoodsStorageRepo;
import app.ewarehouse.repository.RoutineComInspecInsuranceRepo;
import app.ewarehouse.repository.RoutineComInspecShrinkRepo;
import app.ewarehouse.repository.RoutineComplianceCeoApprovalRepository;
import app.ewarehouse.repository.RoutineComplianceCeoSecondRepository;
import app.ewarehouse.repository.RoutineComplianceComplianceTwoRepository;
import app.ewarehouse.repository.RoutineComplianceConclusionRepository;
import app.ewarehouse.repository.RoutineComplianceInspectionRepository;
import app.ewarehouse.repository.RoutineComplianceInspectorAgriculturalCommoditiesRepository;
import app.ewarehouse.repository.RoutineComplianceInspectorITSystemRepository;
import app.ewarehouse.repository.RoutineComplianceInspectorLocationRepository;
import app.ewarehouse.repository.RoutineComplianceInspectorPhysicalFireProtectionRepo;
import app.ewarehouse.repository.RoutineComplianceInspectorTwoRepo;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.repository.WarehousePersonalEquipmentRepository;
import app.ewarehouse.service.RoutineComplianceService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.DocumentUploadutil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.FolderAndDirectoryConstant;

@Service
public class RoutineComplianceServiceImpl implements RoutineComplianceService {
	
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	
	@Value("${finalUpload.path}")
	private String finalUploadPath;

    
    @Autowired
    private RoutineComplianceInspectionRepository routineComplianceInspectionRepository;
    
    @Autowired
    private ApplicationConformityLocDetRepository applicationConformityLocDetRepository;
    
    
    @Autowired
    private ErrorMessages errorMessages;
    
    
    @Autowired
    private ObjectMapper om;
    
    @Autowired
    private RoutineComplianceCeoApprovalRepository routineComplianceCeoApprovalRepository;
    
    @Autowired
    private RoutineComplianceComplianceTwoRepository routineComplianceComplianceTwoRepository;
    
    @Autowired
    private ApplicationOfConformityCommodityDetailsRepository applicationOfConformityCommodityDetailsRepository;
    
    @Autowired
    private ApplicationOfConformityFormOneARepository applicationOfConformityFormOneARepository;
    
    @Autowired
    private ApplicationOfConformityFormOneBRepository applicationOfConformityFormOneBRepository;
    
    @Autowired
    private OperatorLicenseFormOneCRepository OperatorLicenseFormOneCRepository;
    
    @Autowired
    private RoutineComplianceCeoSecondRepository routineComplianceCeoSecondRepository;
    
    @Autowired
    private RoutineComplianceInspectorLocationRepository routineComplianceInspectorLocationRepository;
    
    @Autowired
    private RoutineComplianceInspectorAgriculturalCommoditiesRepository routineComplianceInspectorAgriculturalCommoditiesRepository;
    
    @Autowired
    private WarehousePersonalEquipmentRepository warehousePersonalEquipmentRepository;
    
    @Autowired
    private RoutineComplianceInspectorPhysicalFireProtectionRepo routineComInspecPhysicalFireProtectionRepo;
    
    @Autowired
    private RoutineComInspecGoodsStorageRepo routineComInspecGoodsStorageRepo;
    
    @Autowired
    private RoutineComInspecEnvIssuesRepo routineComInspecEnvIssuesRepo;
    
    @Autowired
    private RoutineComInspecInsuranceRepo routineComInspecInsuranceRepo;
    
    @Autowired
    private RoutineComInspecShrinkRepo routineComInspecShrinkRepo;
    
    @Autowired
    private RoutineComplianceInspectorITSystemRepository routineComInspecITSystemRepo;
    
    @Autowired
    private RecomendationRepository recomendationRepository;
    @Autowired
    private RoutineComplianceConclusionRepository routineComplianceConclusionRepository;
    
    @Autowired
    private RoutineComplianceInspectorTwoRepo routineComplianceInspectorTwoRepo;
    
    @Autowired
    private ConformityMainRepository conformityMainRepository;
    
    
    @Autowired
    private TuserRepository tuserRepository;
    
    @Autowired
	ConfirmityFormOneCRepo documentRepository;
    private static final Logger logger = LoggerFactory.getLogger(RoutineComplianceServiceImpl.class);

    

	@Override
	public String saveCompliance(String routineComplianceInspection) {
	      String decodedData = CommonUtil.inputStreamDecoder(routineComplianceInspection);
           System.err.println(decodedData);
	        try {
	        	RoutineComplianceInspection routineCompliance = om.readValue(decodedData, RoutineComplianceInspection.class);
	        	if(routineCompliance.getRoutineComId()==null) {
	        		 routineCompliance.setStatus(100);
	        		 if(routineCompliance.isFetchFileDb()) {
	        			 routineCompliance.setInspectionPlan(routineCompliance.getInspectionPlan()); 
	        		 }else {
	 	            byte[] decodedBytes = Base64.getDecoder().decode(routineCompliance.getInspectionPlan().getBytes());
	 	            String uploadedPath = DocumentUploadutil.uploadFileByte("Rotine_Complinace" + System.currentTimeMillis(), decodedBytes, FolderAndDirectoryConstant.ROUTINE_COMPLIANCE);
	 	            routineCompliance.setInspectionPlan(uploadedPath);
	        		 }
	 	            RoutineComplianceInspection savedRoutineCompliance = routineComplianceInspectionRepository.save(routineCompliance);
	        		
	        	}else {
	        		routineCompliance.setStatus(100);
	        		routineCompliance.setRoutineComId(routineCompliance.getRoutineComId());
	        		 if(routineCompliance.isFetchFileDb()) {
	        			 routineCompliance.setInspectionPlan(routineCompliance.getInspectionPlan()); 
	        		 }else {
	 	            byte[] decodedBytes = Base64.getDecoder().decode(routineCompliance.getInspectionPlan().getBytes());
	 	            String uploadedPath = DocumentUploadutil.uploadFileByte("Rotine_Complinace" + System.currentTimeMillis(), decodedBytes, FolderAndDirectoryConstant.ROUTINE_COMPLIANCE);
	 	            routineCompliance.setInspectionPlan(uploadedPath);
	        		 }
	        		routineComplianceInspectionRepository.save(routineCompliance);
	        	}
	           
	            return "success";
	        }
	        catch (CustomGeneralException exception) {
	        	exception.printStackTrace();
	            logger.error("Inside saveCompliance method of RoutineComplianceServiceImpl some error occur:" + exception.getMessage());
	            throw exception;
	        }
	        catch (Exception e) {
	        	
	        	e.printStackTrace();
	            logger.error("Inside saveCompliance method of RoutineComplianceServiceImpl some error occur:" + e.getMessage());
	            throw new CustomGeneralException(errorMessages.getUnknownError());
	        }
	}

	@Override
	public Page<RoutineComplianceInspDTO> getCompanyRoutineCompData(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> data=routineComplianceInspectionRepository.getRoutineCompData(pageable);
		Page<RoutineComplianceInspDTO> dtoPage = data.map(row -> 
	    new RoutineComplianceInspDTO(
	        (Long) row[0],  // routineComId
	        (String) row[1],  // companyId
	        (String) row[2],  // warehouseId
	        (String) row[3],  // facilityType
	        (String) row[4],  // inspectionType
	        (String) row[5],  // inspectionTime
	        (String) row[6],  // startDate
	        (String) row[7],  // endDate
	        (String) row[8],  // requestedBy
	        (String) row[9],  // remarks
	        (String) row[10], // inspectionPlan
	        (Integer) row[11], // status  
	        (String) row[12], // companyName  
	        (String) row[13],  // warehouseName  
	        (String) row[14],  // statusdescription  
	        (Integer) row[15],  // Inspectorcomplianceofficer  
	        (String) row[16],  // officeroffice  
	        (String) row[17],  // other facility  
	        (Integer) row[18]  // editIdcompliance
	    )
	);

		return dtoPage;
	}

	@Override
	public Page<RoutineComplianceInspDTO> findByFiltersData(Integer roleId, String search, String sortColumn, String sortDirection,
			Integer userId, Pageable pageable) {
		logger.info("Inside findByFiltersData method of RoutineComplianceServiceImpl");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
                sortColumn != null ? sortColumn : "routineComId");
        
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Object[]> routineCompliancePage = routineComplianceInspectionRepository.findByFiltersData(search, userId,sortedPageable);
        
		Page<RoutineComplianceInspDTO> dtoPage = routineCompliancePage.map(row -> 
	    new RoutineComplianceInspDTO(
	        (Long) row[0],  // routineComId
	        (String) row[1],  // companyId
	        (String) row[2],  // warehouseId
	        (String) row[3],  // facilityType
	        (String) row[4],  // inspectionType
	        (String) row[5],  // inspectionTime
	        (String) row[6],  // startDate
	        (String) row[7],  // endDate
	        (String) row[8],  // requestedBy
	        (String) row[9],  // remarks
	        (String) row[10], // inspectionPlan
	        (Integer) row[11], // status  
	        (String) row[12], // companyName  
	        (String) row[13],  // warehouseName  
	        (String) row[14],  // statusdescription  
	        (Integer) row[15],  // Inspectorcomplianceofficer  
	        (String) row[16],  // officeroffice  
	        (String) row[17],  // other facility  
	        (Integer) row[18]  // editIdcompliance
	    )
	);
        return dtoPage;
	}

	@Override
	public Map<String, Object> getWareHosedata(String warehouseId,String status) {
		ApplicationOfConformityLocationDetails locationData = applicationConformityLocDetRepository.findByWarehouseId(warehouseId);
		
		
		List<ApplicationOfConformityCommodityDetails> commodityData=applicationOfConformityCommodityDetailsRepository.findByWarehouseLocation(locationData);
		ApplicationOfConformityFormOneA formOneA=applicationOfConformityFormOneARepository.findByWarehouseLocationAndDeletedFlagFalse(locationData);
		ApplicationOfConformityFormOneB formOneB=applicationOfConformityFormOneBRepository.findByWarehouseLocationAndDeletedFlagFalse(locationData);
		OperatorLicenseFormOneC formOneC=OperatorLicenseFormOneCRepository.findByWarehouseLocationAndDeletedFlagFalse(locationData);
//		List<FormOneCResponseDto> formOneCData = documentRepository.findByWareHouseIdAndDeletedFlagFalse(warehouseId)
//				.stream().map(doc -> new FormOneCResponseDto(doc.getDocName(), doc.getFileName(), doc.getFormOneCId(),doc.getId())
//				.collect(Collectors.toList());
				
				List<FormOneCResponseDto> formOneCData=documentRepository.findByWareHouseIdAndDeletedFlagFalse(warehouseId).stream()
				.map(doc -> new FormOneCResponseDto(doc.getDocName(), doc.getFileName(), doc.getFormOneCId(),doc.getId()))
				.collect(Collectors.toList());
				
				Integer confirmityIdCert = conformityMainRepository.findIdByWarehouseId(warehouseId);

		Map<String, Object> response = new HashMap<>();
        response.put("warehouseLocations", locationData);
        response.put("commodityData", commodityData);
        response.put("formOneA", formOneA);
        response.put("formOneB", formOneB);
        response.put("formOneC", formOneC);
        response.put("formOneCData", formOneCData);
        response.put("confirmityIdCert", confirmityIdCert);
        //response.put("documents", documents);

        return response;
	}
	
	
	

	@Transactional
	@Override
    public String takeAction(String data) throws JsonMappingException, JsonProcessingException {
        logger.info("Inside takeAction method of RoutineComplianceServiceImpl");
        
        String decodedData = CommonUtil.inputStreamDecoder(data);
        JsonNode rootNode = om.readTree(decodedData); 
//        Integer routineComplianceIdone = rootNode.path("data").path("routineComplianceId").asInt();
        Long routineComplianceId = rootNode.path("data").path("routineComplianceId").asLong();
        String stage = rootNode.path("stage").asText();
        String action = rootNode.path("action").asText();
        String message=null;
        if(stage.equals("ceoOne")) {
            RoutineComplianceCeoApproval ceoApproval = om.treeToValue(rootNode.path("data").path("ceoApproval"), RoutineComplianceCeoApproval.class);
            ceoApproval.setRoutineComplianceId(routineComplianceId);
            if(ceoApproval.getIntId()!=null) {
            	ceoApproval.setIntId(ceoApproval.getIntId());
            }
            routineComplianceCeoApprovalRepository.save(ceoApproval);
            RoutineComplianceInspection routineComplianceInspection=routineComplianceInspectionRepository.findByRoutineComId(routineComplianceId);
            routineComplianceInspection.setStatus("approved".equalsIgnoreCase(action) ? 101 : "deferred".equalsIgnoreCase(action) ? 105 : 0);
            routineComplianceInspection.setIntComplianceEdit(1);
            routineComplianceInspectionRepository.save(routineComplianceInspection);
            message=routineComplianceInspection.getWarehouseId();
        }else if(stage.equals("complianceTwo")) {
        	RoutineComplianceComplianceTwo complianceTwoApproval = om.treeToValue(rootNode.path("data").path("complianceTwo"), RoutineComplianceComplianceTwo.class);
        	complianceTwoApproval.setRoutineComplianceId(routineComplianceId);
            if(complianceTwoApproval.getIntId()!=null) {
            	complianceTwoApproval.setIntId(complianceTwoApproval.getIntId());
            }
            routineComplianceComplianceTwoRepository.save(complianceTwoApproval);
            RoutineComplianceInspection routineComplianceInspection=routineComplianceInspectionRepository.findByRoutineComId(routineComplianceId);
            routineComplianceInspection.setStatus("approved".equalsIgnoreCase(action) ? 103 : "deferred".equalsIgnoreCase(action) ? 106 : 0);
            routineComplianceInspectionRepository.save(routineComplianceInspection);
            message=routineComplianceInspection.getWarehouseId();
        }else if(stage.equals("ceoSecond")) {
        	RoutineComplianceCeoSecond ceoTwoApproval = om.treeToValue(rootNode.path("data").path("ceoSecond"), RoutineComplianceCeoSecond.class);
        	ceoTwoApproval.setRoutineComplianceId(routineComplianceId);
            if(ceoTwoApproval.getIntId()!=null) {
            	ceoTwoApproval.setIntId(ceoTwoApproval.getIntId());
            }
            routineComplianceCeoSecondRepository.save(ceoTwoApproval);
            RoutineComplianceInspection routineComplianceInspection=routineComplianceInspectionRepository.findByRoutineComId(routineComplianceId);
            routineComplianceInspection.setStatus("approved".equalsIgnoreCase(action) ? 104 : "deferred".equalsIgnoreCase(action) ? 105 : 0);
            routineComplianceInspectionRepository.save(routineComplianceInspection);
            message=routineComplianceInspection.getWarehouseId();
        	
        }else if(stage.equals("isInspector")) {
            
        	// Save or update location
        	RoutineComplianceInspectorLocation location=om.treeToValue(rootNode.path("data").path("inspectorTwo").path("location"), RoutineComplianceInspectorLocation.class);
            if (location.getIntLocationId() != null) {
            	//location = routineComplianceInspectorLocationRepository.findById(location.getIntLocationId()).orElse(location);
            	location.setIntLocationId(location.getIntLocationId());
            }
            location = routineComplianceInspectorLocationRepository.save(location);
            JsonNode commoditiesNode = rootNode.path("data").path("inspectorTwo").path("location").path("agriculturalCommodities");
//            if (commoditiesNode.isArray()) {
//                for (JsonNode commodityNode : commoditiesNode) {
//                    RoutineComplianceInspectorAgriculturalCommodity commodity = om.treeToValue(commodityNode, RoutineComplianceInspectorAgriculturalCommodity.class);
//                    // Check if commodity exists
//                    if (commodity.getAgriculturalCommoditiesId() != null) {
////                        commodity = routineComplianceInspectorAgriculturalCommoditiesRepository.findById(commodity.getAgriculturalCommoditiesId()).orElse(commodity);
//                    	commodity.setAgriculturalCommoditiesId(commodity.getAgriculturalCommoditiesId());
//                    }
//                    commodity.setRoutineComplianceInspectorLocation(location);
//                    routineComplianceInspectorAgriculturalCommoditiesRepository.save(commodity);
//                }
//            }
            
            //WarehousePersonalEquipment
			JsonNode warehouseEquipmentNode = rootNode.path("data").path("inspectorTwo")
					.path("warehousePersonalEquipment");
				WarehousePersonalEquipment warehouseEquipment = om.treeToValue(warehouseEquipmentNode,
						WarehousePersonalEquipment.class);
				if (warehouseEquipment.getIntWareHousePersonalEquipmentId() != null
						&& warehousePersonalEquipmentRepository
								.existsById(warehouseEquipment.getIntWareHousePersonalEquipmentId())) {
					warehouseEquipment = warehousePersonalEquipmentRepository.save(warehouseEquipment);
				} else {
					warehouseEquipment = warehousePersonalEquipmentRepository.save(warehouseEquipment);
				}
			
			  //Physical Security And FireProtection 
			JsonNode physicalSecurityAndFireProtectionNode = rootNode.path("data").path("inspectorTwo")
					.path("physicalSecurityAndFireProtection");
				RoutineComplianceInspectorPhysicalFireProtection physicalSecurityAndFireProtection = om.treeToValue(physicalSecurityAndFireProtectionNode,
						RoutineComplianceInspectorPhysicalFireProtection.class);
				if (physicalSecurityAndFireProtection.getIntPhysicalFireProtectionId() != null
						&& routineComInspecPhysicalFireProtectionRepo
								.existsById(physicalSecurityAndFireProtection.getIntPhysicalFireProtectionId())) {
					physicalSecurityAndFireProtection = routineComInspecPhysicalFireProtectionRepo.save(physicalSecurityAndFireProtection);
				} else {
					physicalSecurityAndFireProtection = routineComInspecPhysicalFireProtectionRepo.save(physicalSecurityAndFireProtection);
				}
			
			  //Condition Of Goods Storage
			JsonNode conditionGoodNode = rootNode.path("data").path("inspectorTwo")
					.path("conditionOfGoodsStorage");
				RoutineComplianceInspectorConditionOfGoodsStorage conditionGoodStorage = om.treeToValue(conditionGoodNode,
						RoutineComplianceInspectorConditionOfGoodsStorage.class);
				if (conditionGoodStorage.getIntConditionOfGoodsStorageId() != null
						&& routineComInspecGoodsStorageRepo
								.existsById(conditionGoodStorage.getIntConditionOfGoodsStorageId())) {
					conditionGoodStorage = routineComInspecGoodsStorageRepo.save(conditionGoodStorage);
				} else {
					conditionGoodStorage = routineComInspecGoodsStorageRepo.save(conditionGoodStorage);
				}
			
			 //EnvironmentIssues
			JsonNode environmentIssuesNode = rootNode.path("data").path("inspectorTwo")
					.path("environmentIssues");
				RoutineComplianceInspectorEnvironmentIssues environmentIssues = om.treeToValue(environmentIssuesNode,
						RoutineComplianceInspectorEnvironmentIssues.class);
				if (environmentIssues.getIntEnvironmentIssuesId() != null
						&& routineComInspecEnvIssuesRepo
								.existsById(environmentIssues.getIntEnvironmentIssuesId())) {
					environmentIssues = routineComInspecEnvIssuesRepo.save(environmentIssues);
				} else {
					environmentIssues = routineComInspecEnvIssuesRepo.save(environmentIssues);
				}
			
			 //Insurance
			JsonNode insuranceNode = rootNode.path("data").path("inspectorTwo")
					.path("insurance");
				RoutineComplianceInspectorInsurance insurance = om.treeToValue(insuranceNode,
						RoutineComplianceInspectorInsurance.class);
				if (insurance.getIntInsuranceId()!= null
						&& routineComInspecInsuranceRepo
								.existsById(insurance.getIntInsuranceId())) {
					insurance = routineComInspecInsuranceRepo.save(insurance);
				} else {
					insurance = routineComInspecInsuranceRepo.save(insurance);
				}
			// Shrink
			JsonNode shrinkNode = rootNode.path("data").path("inspectorTwo")
			        .path("shrink");
			    RoutineComplianceInspectorShrink shrink = om.treeToValue(shrinkNode,
			            RoutineComplianceInspectorShrink.class);
			    if (shrink.getIntShrinkId() != null
			            && routineComInspecShrinkRepo.existsById(shrink.getIntShrinkId())) {
			        shrink = routineComInspecShrinkRepo.save(shrink);
			    } else {
			        shrink = routineComInspecShrinkRepo.save(shrink);
			    }
			
			//IT System
			JsonNode itSystemNode = rootNode.path("data").path("inspectorTwo").path("itSystem");
			    RoutineComplianceInspectorITSystem itSystem = om.treeToValue(itSystemNode, RoutineComplianceInspectorITSystem.class);

			    if (itSystem.getIntItSystemId() != null && routineComInspecITSystemRepo.existsById(itSystem.getIntItSystemId())) {
			        itSystem = routineComInspecITSystemRepo.save(itSystem);
			    } else {
			        itSystem = routineComInspecITSystemRepo.save(itSystem);
			    }
			
			//Recommendation
		    JsonNode recomandationsNodes = rootNode.path("data").path("inspectorTwo").path("recommendationSection").path("recommendation");
            if (recomandationsNodes.isArray()) {
                for (JsonNode recomandationNode : recomandationsNodes) {
                	Recommendation recomendation = om.treeToValue(recomandationNode, Recommendation.class);
                    // Check if commodity exists
                    if (recomendation.getRecommendationId() != null) {
                    	recomendation = recomendationRepository.findById(recomendation.getRecommendationId()).orElse(recomendation);
                    }
                    recomendation.setIntLocationId(location.getIntLocationId());
                    recomendationRepository.save(recomendation);
                }
            }
            
            // Extract Conclusion Data
            JsonNode conclusionsNodes = rootNode.path("data").path("inspectorTwo").path("conclusionSection").path("conclusion");
            if (conclusionsNodes.isArray()) {
                for (JsonNode conclusionNode : conclusionsNodes) {
                	RoutineComplianceConclusion conclusion = om.treeToValue(conclusionNode, RoutineComplianceConclusion.class);
                    // Check if conclusion already exists
                    if (conclusion.getConclusionId() != null) {
                        conclusion = routineComplianceConclusionRepository.findById(conclusion.getConclusionId()).orElse(conclusion);
                    }
                    conclusion.setIntLocationId(location.getIntLocationId());
                    routineComplianceConclusionRepository.save(conclusion);
                }
            }
            
            // Extract Conclusion Data
        	JsonNode mainNode = rootNode.path("data").path("inspectorTwo").path("inspection");
			if (mainNode != null && !mainNode.isNull()) {
				RoutineComplianceInspectorTwo mainInspectionData = om.treeToValue(mainNode, RoutineComplianceInspectorTwo.class);
	
			    if (mainInspectionData.getIntId() != null && routineComplianceInspectorTwoRepo.existsById(mainInspectionData.getIntId())) {
					mainInspectionData.setRoutineComplianceId(routineComplianceId);
					mainInspectionData = routineComplianceInspectorTwoRepo.save(mainInspectionData);
			    } else { 
			    	
			    	mainInspectionData.setIntLocationId(location);
					mainInspectionData.setIntWareHousePersonalEquipmentId(warehouseEquipment);
					mainInspectionData.setIntphysicalFireProtectionId(physicalSecurityAndFireProtection);
					mainInspectionData.setIntConditionOfGoodsStorageId(conditionGoodStorage);
					mainInspectionData.setIntEnviromentId(environmentIssues);
					mainInspectionData.setIntInsuranceId(insurance);
					mainInspectionData.setIntShrinkId(shrink);
					mainInspectionData.setIntItSystemId(itSystem);
					mainInspectionData.setRoutineComplianceId(routineComplianceId);
					mainInspectionData.setRecommendationIdLocation(location.getIntLocationId());
					mainInspectionData.setConclusionLocationId(location.getIntLocationId());
			    	mainInspectionData = routineComplianceInspectorTwoRepo.save(mainInspectionData);
			    }
			    
			    List<String> fileUploadList = Arrays.asList(mainInspectionData.getVchReportFilePath(),mainInspectionData.getVchPhotographicEvidenceFilePath());
			    fileUploadList.stream().filter(file -> !Strings.isNullOrEmpty(file))
				.forEach(file -> copyFileToDestination(file, "Routine Compliance"));
			}
			
			  RoutineComplianceInspection routineComplianceInspection=routineComplianceInspectionRepository.findByRoutineComId(routineComplianceId);
	            routineComplianceInspection.setStatus(102);
	            routineComplianceInspectionRepository.save(routineComplianceInspection);
	            message=routineComplianceInspection.getWarehouseId();

            

        }
       
   
        return message;
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
	public Object getCeoOneData(Long routineComplianceId) {
		Object ceoApprovalData=routineComplianceCeoApprovalRepository.findByRoutineComplianceIdAndBitDeleteFlagFalse(routineComplianceId);
		return ceoApprovalData;
	}

	@Override
	public Object getComplianceTwoData(Long editId) {
		Object complianceTwo=routineComplianceComplianceTwoRepository.findByRoutineComplianceIdAndBitDeleteFlagFalse(editId);
		return complianceTwo;
	}

	@Override
	public Object getCeoSecondData(Long editId) {
		Object ceoSecond=routineComplianceCeoSecondRepository.findByRoutineComplianceIdAndBitDeleteFlagFalse(editId);
		System.err.println(ceoSecond);
		return ceoSecond;
	}
	
	@Override
	public Object getInspectorData(Long editId) {
		 Map<String, Object> inspectorAllData = new HashMap<>();
           RoutineComplianceInspectorTwo inspectorData=routineComplianceInspectorTwoRepo.findByRoutineComplianceIdAndBitDeleteFlagFalse(editId);
//           if (inspectorData != null) {
//        	    inspectorData.getIntLocationId().setAgriculturalCommodities(
//        	        inspectorData.getIntLocationId().getAgriculturalCommodities()
//        	            .stream()
//        	            .filter(commodity -> !commodity.getBitDeleteFlag())  // Ensure only non-deleted commodities
//        	            .collect(Collectors.toList())
//        	    );
//        	}
//           RoutineComplianceInspectorLocation locationData = inspectorData.getIntLocationId();
//           List<Recommendation> recomendationData=recomendationRepository.findByIntLocationIdAndBitDeletedFlagFalse(locationData.getIntLocationId());
//           List<RoutineComplianceConclusion> conclusionData=routineComplianceConclusionRepository.findByIntLocationIdAndBitDeletedFlagFalse(locationData.getIntLocationId());
//            inspectorAllData.put("routineInspection", inspectorData);
//            inspectorAllData.put("recomendationData", recomendationData);
//            inspectorAllData.put("conclusionData", conclusionData);
           RoutineComplianceInspectorLocation locationData = null;
           List<Recommendation> recomendationData = new ArrayList<>();
           List<RoutineComplianceConclusion> conclusionData = new ArrayList<>();
           if(inspectorData != null) {
        	    locationData = inspectorData.getIntLocationId(); // Get Location Data Safely

        	    if (locationData != null) {
        	        // Filter agricultural commodities only if location data exists
        	        locationData.setAgriculturalCommodities(
        	            locationData.getAgriculturalCommodities()
        	                .stream()
        	                .filter(commodity -> !commodity.getBitDeleteFlag())  // Ensure only non-deleted commodities
        	                .collect(Collectors.toList())
        	        );

        	        // Fetch Recommendations & Conclusions only if locationData is not null
        	        recomendationData = recomendationRepository.findByIntLocationIdAndBitDeletedFlagFalse(locationData.getIntLocationId());
        	        conclusionData = routineComplianceConclusionRepository.findByIntLocationIdAndBitDeletedFlagFalse(locationData.getIntLocationId());
        	    }
        	}

        	// Add Data to Response
        	inspectorAllData.put("routineInspection", inspectorData);   // Can be null
        	inspectorAllData.put("recomendationData", recomendationData);
        	inspectorAllData.put("conclusionData", conclusionData);
		return inspectorAllData;
	}
	@Override
	public Map<String, Object> getWareHouseIdandRcId(Long routineComId) {
		RoutineComplianceInspection result = routineComplianceInspectionRepository.findWarehouseAndCompanyByRoutineComId(routineComId);
		if (result != null) {
		    Map<String, Object> response = new HashMap<>();
		    response.put("warehouseId", result.getWarehouseId());
		    response.put("companyId", result.getCompanyId());
		    return response;
		}
        return Collections.emptyMap();
	}

	@Override
	public Map<String, Object> getEditRoutineStageWiseData(Long routinComplianceId) {
		RoutineComplianceInspection routincompliancStageOneData=routineComplianceInspectionRepository.findByRoutineComId(routinComplianceId);
		String inspectorName=tuserRepository.findFullNameById(routincompliancStageOneData.getComplianceOfficer());
		Object ceoApprovalData=routineComplianceCeoApprovalRepository.findByRoutineComplianceIdAndBitDeleteFlagFalse(routinComplianceId);
		Object complianceTwo=routineComplianceComplianceTwoRepository.findByRoutineComplianceIdAndBitDeleteFlagFalse(routinComplianceId);
		
//		RoutineComplianceInspectorTwo inspectorData=routineComplianceInspectorTwoRepo.findByRoutineComplianceIdAndBitDeleteFlagFalse(routinComplianceId);
//        RoutineComplianceInspectorLocation locationData = inspectorData.getIntLocationId();
//        List<Recommendation> recomendationData=recomendationRepository.findByIntLocationIdAndBitDeletedFlagFalse(locationData.getIntLocationId());
//        List<RoutineComplianceConclusion> conclusionData=routineComplianceConclusionRepository.findByIntLocationIdAndBitDeletedFlagFalse(locationData.getIntLocationId());
        
        RoutineComplianceInspectorTwo inspectorData = routineComplianceInspectorTwoRepo.findByRoutineComplianceIdAndBitDeleteFlagFalse(routinComplianceId);
        RoutineComplianceInspectorLocation locationData = null;
        List<Recommendation> recomendationData = new ArrayList<>();
        List<RoutineComplianceConclusion> conclusionData = new ArrayList<>();

        if (inspectorData != null) {
            locationData = inspectorData.getIntLocationId();
            if (locationData != null) {
                recomendationData = recomendationRepository.findByIntLocationIdAndBitDeletedFlagFalse(locationData.getIntLocationId());
                conclusionData = routineComplianceConclusionRepository.findByIntLocationIdAndBitDeletedFlagFalse(locationData.getIntLocationId());
            }
        }
        
		Map<String, Object> response = new HashMap<>();
        response.put("routincompliancStageOneData", routincompliancStageOneData);
        response.put("ceoApprovalData", ceoApprovalData);
        response.put("complianceTwo", complianceTwo);
        response.put("inspectorName", inspectorName);
        response.put("inspectorApprovalaData", inspectorData);
        response.put("recomendationData", recomendationData);
        response.put("conclusionData", conclusionData);

        return response;
	}

	@Override
	public boolean checkDuplicateWarehouse(String warehouseId) {
		return routineComplianceInspectionRepository.existsByWarehouseId(warehouseId);
	}

	@Override
	public Object removeAddmore(Long id, String type) {
		JSONObject response = new JSONObject();
		if(type.equals("commodity")) {
			routineComplianceInspectorAgriculturalCommoditiesRepository.markAsDeleted(id);
		}else if(type.equals("recommendation")) {
			recomendationRepository.markAsDeleted(id);
		}else if(type.equals("conclusion")) {
			routineComplianceConclusionRepository.markAsDeleted(id);
		}
		
		response.put("status", "Experience Removed");
		return response;
	}

	

	
}
