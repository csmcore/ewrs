package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ApplicationCollateralDTO;
import app.ewarehouse.dto.SubCountyResponse;
import app.ewarehouse.dto.TakeActionRequest;
import app.ewarehouse.entity.ComplaintObservationResponse;
import app.ewarehouse.entity.Complaint_managment;
import app.ewarehouse.entity.OnlineServiceApprovalNotings;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.SubCounty;
import app.ewarehouse.entity.TApplicationComplianceInspection;
import app.ewarehouse.entity.TapplicationOfCertificateOfCompliance;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.entity.draftStatus;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.mapper.ApplicationCollateralMapper;
import app.ewarehouse.repository.ComplaintObservationResponseRepository;
import app.ewarehouse.repository.Complaint_managmentRepository;
import app.ewarehouse.repository.OnlineServiceApprovalNotingsRepository;
import app.ewarehouse.repository.TapplicationOfCertificateOfComplianceRepository;
import app.ewarehouse.service.TapplicationOfCertificateOfComplianceService;
import app.ewarehouse.service.TuserService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.CompositeKey;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.JsonFileExtractorUtil;
import app.ewarehouse.util.Mapper;
import app.ewarehouse.util.TokenCreaterAndMatcher;
import app.ewarehouse.util.UserIdConstants;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TapplicationOfCertificateOfComplianceServiceImpl implements TapplicationOfCertificateOfComplianceService {

    @Autowired
    TapplicationOfCertificateOfComplianceRepository repo;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private UserIdConstants userIdConstants;
    @Autowired
    private ErrorMessages errorMessages;
    @Value("${salt}")
    private String salt;
    @Autowired
    TuserService userService;
    @Autowired
    Complaint_managmentRepository complaint_managmentRepository;
    @Autowired
    ComplaintObservationResponseRepository complaintObservationResponseRepository;
  @Autowired
  OnlineServiceApprovalNotingsRepository onlineServiceApprovalNotingsRepository;

    private static final Logger logger = LoggerFactory.getLogger(TapplicationOfCertificateOfComplianceServiceImpl.class);

    @Override
    public ApplicationCollateralDTO save(String certApplication) throws JsonProcessingException {
        String decodedData = CommonUtil.inputStreamDecoder(certApplication);

        try {
            TapplicationOfCertificateOfCompliance application=  objectMapper.readValue(decodedData,TapplicationOfCertificateOfCompliance.class);
            application.setTxtDirectorIDs(JsonFileExtractorUtil.uploadFile(application.getTxtDirectorIDs(), FolderAndDirectoryConstant.APPLICATION_CERTIFICATE_COLLATERAL_FOLDER));
            application.setTxtDirectorIDsName("ApplicationCompliance_DirectorId."+Arrays.stream(application.getTxtDirectorIDs().split("\\."))
                    .reduce((first, second) -> second)
                    .orElse(null));
            application.setTxtDirectorsPassports(JsonFileExtractorUtil.uploadFile(application.getTxtDirectorsPassports(), FolderAndDirectoryConstant.APPLICATION_CERTIFICATE_COLLATERAL_FOLDER));
            application.setTxtDirectorsPassportsName("ApplicationCompliance_DirectorPassport."+Arrays.stream(application.getTxtDirectorsPassports().split("\\."))
                    .reduce((first, second) -> second)
                    .orElse(null));
            application.setIntCurrentRole(userIdConstants.getOicLegal());
            application.setIntApprovalStage(1);
            application.setEnmApprovalStatus(Status.Pending);
            application = repo.save(application);
            return ApplicationCollateralMapper.mapToDTO(application);
        }
        catch (DataIntegrityViolationException exception) {
            log.error("Inside save method of TapplicationOfCertificateOfComplianceServiceImpl some error occur:" + exception.getMessage());
            throw new CustomGeneralException(errorMessages.getConstraintError());
        }
        catch (CustomGeneralException exception) {
            log.error("Inside save method of TapplicationOfCertificateOfComplianceServiceImpl some error occur:" + exception.getMessage());
            throw exception;
        }
        catch (Exception e) {
            log.error("Inside save method of TapplicationOfCertificateOfComplianceServiceImpl some error occur:" + e.getMessage());
            throw new CustomGeneralException(errorMessages.getInternalServerError());
        }
    }

    @Override
    public List<TapplicationOfCertificateOfCompliance> findAll() {
          return repo.findAllByBitDeletedFlag(false);
    }

    @Override
    public ApplicationCollateralDTO getApplicationById(String userId) {
        TapplicationOfCertificateOfCompliance tapplication =repo.findByTxtApplicationIdAndBitDeletedFlag(userId,false).orElseThrow(() -> new EntityNotFoundException(errorMessages.getEntityNotFound()));
        ApplicationCollateralDTO dto = ApplicationCollateralMapper.mapToDTO(tapplication);

        Complaint_managment complaint;
        try{
            complaint =complaint_managmentRepository.findBySelNameofCollateralManagerAndBitDeletedFlag(String.valueOf(tapplication.getIntAdminUserId()),false).orElseThrow(() -> new EntityNotFoundException(errorMessages.getEntityNotFound()));
                logger.info("complaint is :"+complaint);
                if (complaint.getVchActionOnApplication().equals("Revocation")) {
                    List<ComplaintObservationResponse> observationResponses = complaintObservationResponseRepository.findByObResAPIdAndObResStageAndDeleteFlag(complaint.getIntId(), 7, false);
                    logger.info("observationResponses is revocation:"+observationResponses);
                   if (observationResponses.isEmpty()){
                       return dto;
                   }
                    ComplaintObservationResponse observation = observationResponses.get(0);
                    OnlineServiceApprovalNotings approvalNotings = onlineServiceApprovalNotingsRepository.findByIntNotingsIdAndBitDeletedFlag(observation.getNotingId(), false);
                    if (approvalNotings != null) {
                        dto.setRemark(approvalNotings.getTxtNoting());
                    }
                } else if (complaint.getVchActionOnApplication().equals("Suspension")) {
                    List<ComplaintObservationResponse> observationResponses = complaintObservationResponseRepository.findByObResAPIdAndObResStageAndDeleteFlag(complaint.getIntId(), 6, false);
                    logger.info("observationResponses is suspension :"+observationResponses);
                    if (observationResponses.isEmpty()){
                        return dto;
                    }
                    ComplaintObservationResponse observation = observationResponses.get(0);
                    OnlineServiceApprovalNotings approvalNotings = onlineServiceApprovalNotingsRepository.findByIntNotingsIdAndBitDeletedFlag(observation.getNotingId(), false);
                    if (approvalNotings != null) {
                        dto.setRemark(approvalNotings.getTxtNoting());
                    }
                }
        }catch (Exception e){
            logger.info("Error Occured: "+e);
        }
        return dto;
    }

    @Override
    public ApplicationCollateralDTO getApplication(Integer intCreatedBy,String status) {
            return ApplicationCollateralMapper.mapToDTO(repo.findByStatusAndIntCreatedByAndBitDeletedFlag(draftStatus.valueOf(status),intCreatedBy, false));
    }

    @Override
    public Page<ApplicationCollateralDTO> findByFilters(Integer intCurrentRole, Integer userId, Status status, String search, String sortColumn, String sortDirection, Pageable pageable) {
        log.info("Inside findByFilters method of TapplicationOfCertificateOfComplianceServiceImpl");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
                sortColumn != null ? sortColumn : "stmUpdatedOn");

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        List<Integer> myStages = getStagesByUserId(intCurrentRole, 0);
        List<Integer> immediateBelowStages = getStagesByUserId(intCurrentRole, -1);
        Optional<Integer> minStage = myStages.stream().min(Integer::compareTo);

        if (minStage.isPresent()) {
            Page<TapplicationOfCertificateOfCompliance> applicationCompliancePage = repo.findByFilters(
                    intCurrentRole, minStage.get(), immediateBelowStages, myStages,
                    status, Status.Pending, Status.Forwarded, Status.Approved, Status.Rejected, Status.OnHold,
                    draftStatus.Payment, userId, search, sortedPageable
            );

            return applicationCompliancePage.map(ApplicationCollateralMapper::mapToDTO);
        }

        return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    @Override
    @Transactional
    public String takeAction(String data) {
        log.info("Inside takeAction method of TapplicationOfCertificateOfComplianceServiceImpl");

        String decodedData = CommonUtil.inputStreamDecoder(data);
        TakeActionRequest<TapplicationOfCertificateOfCompliance> acTakeAction;

        try {
            acTakeAction = objectMapper.readValue(decodedData, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("Inside save method of TapplicationOfCertificateOfComplianceServiceImpl some error occur:" + e.getMessage());
            throw new CustomGeneralException(errorMessages.getInternalServerError());
        }

        String applicationComplianceId = acTakeAction.getData().getTxtApplicationId();
        TapplicationOfCertificateOfCompliance existingCertificate = repo.findById(applicationComplianceId).orElseThrow(() -> new CustomGeneralException(errorMessages.getEntityNotFound()));

        Integer intCurrentRole = acTakeAction.getOfficerRole();

        int approvalStage = existingCertificate.getIntApprovalStage();

        if (existingCertificate.getEnmApprovalStatus().equals(Status.OnHold) && Objects.equals(existingCertificate.getIntCurrentRole(), intCurrentRole)) {
            approvalStage += 1;
        }

        getApprovalDetails(existingCertificate, acTakeAction, approvalStage);

        if (acTakeAction.getAction().equals(Status.Approved)) {

            existingCertificate.setIntCurrentRole(getNextOfficerRole(new CompositeKey(intCurrentRole, approvalStage)));
            existingCertificate.setIntApprovalStage(approvalStage + 1);
            existingCertificate.setEnmApprovalStatus(Status.Pending);

            if (Objects.equals(intCurrentRole, userIdConstants.getCeo())) {
                existingCertificate.setEnmApprovalStatus(Status.Approved);
                try {
    				createTempUser(existingCertificate.getTxtFullName(), existingCertificate.getTxtMobilePhoneNumber(),existingCertificate.getTxtEmail(),existingCertificate.getTxtAddress());
    				List<Tuser> userDb = userService.findByMobileOrEmail(existingCertificate.getTxtMobilePhoneNumber(),existingCertificate.getTxtEmail());
    				Integer m_admin_user_id=0;
    				existingCertificate.setIntAdminUserId(m_admin_user_id);
    				
    			} catch (JsonMappingException e) {
    				// TODO Auto-generated catch block
    				 log.error("Inside save method of TapplicationOfCertificateOfComplianceServiceImpl some error occur:" + e.getMessage());
    		            throw new CustomGeneralException(errorMessages.getInternalServerError());
    				
    			} catch (JsonProcessingException e) {
    				// TODO Auto-generated catch block
    				 log.error("Inside save method of TapplicationOfCertificateOfComplianceServiceImpl some error occur:" + e.getMessage());
    		            throw new CustomGeneralException(errorMessages.getInternalServerError());
    			}
              
            }
                 
        } else if (acTakeAction.getAction().equals(Status.Deferred)) {
            getNextOfficerRole(new CompositeKey(intCurrentRole, existingCertificate.getIntApprovalStage()));

            existingCertificate.setIntCurrentRole(intCurrentRole);
            existingCertificate.setEnmApprovalStatus(Status.OnHold);
            existingCertificate.setIntApprovalStage(existingCertificate.getIntApprovalStage() - 1);
        }
        else if (acTakeAction.getAction().equals(Status.Rejected) && Objects.equals(intCurrentRole, userIdConstants.getCeo())) {
            existingCertificate.setIntCurrentRole(getNextOfficerRole(new CompositeKey(intCurrentRole, approvalStage)));
            existingCertificate.setIntApprovalStage(approvalStage + 1);
            existingCertificate.setEnmApprovalStatus(Status.Rejected);
        }
        else if (acTakeAction.getAction().equals(Status.Draft) || (acTakeAction.getAction().equals(Status.Schedule) || acTakeAction.getAction().equals(Status.Complete))&& Objects.equals(intCurrentRole, userIdConstants.getInspector())) {
            log.info("Inspection draft, no action required.");
        }
        else if (acTakeAction.getAction().equals(Status.Validate) && Objects.equals(intCurrentRole, userIdConstants.getFinanceOfficer())) {
            log.info("Finance Validation, no action required.");
        }
        else {
            throw new CustomGeneralException(errorMessages.getNotAuthorized());
        }

        repo.save(existingCertificate);
        return applicationComplianceId;
    }



    private void getApprovalDetails(TapplicationOfCertificateOfCompliance applicationCompliance, TakeActionRequest<TapplicationOfCertificateOfCompliance> acTakeAction, int approvalStage) {
        int roleId = acTakeAction.getOfficerRole();
        TapplicationOfCertificateOfCompliance submittedCompliance = acTakeAction.getData();

        Map<Integer, Runnable> approvalActions = Map.of(
                userIdConstants.getOicLegal(), () -> {
                    if (approvalStage == 1) {
                        if (submittedCompliance.getVchOicLegalRemark() == null) {
                            throw new CustomGeneralException("Remark is required.");
                        }
                        applicationCompliance.setVchOicLegalRemark(submittedCompliance.getVchOicLegalRemark());
                    }

                    if (approvalStage == 3) {
                        if (submittedCompliance.getInspector() == null) {
                            throw new CustomGeneralException("Inspector must be assigned.");
                        }
                        applicationCompliance.setInspector(submittedCompliance.getInspector());
                    }

                    if (approvalStage == 5) {
                        if (submittedCompliance.getVchOicLegalThreeRemark() == null) {
                            throw new CustomGeneralException("Remark is required.");
                        }
                        applicationCompliance.setVchOicLegalThreeRemark(submittedCompliance.getVchOicLegalThreeRemark());
                    }
                },
                userIdConstants.getOicFinance(), () -> applicationCompliance.setVchOicFinRemark(submittedCompliance.getVchOicFinRemark()),
                userIdConstants.getInspector(), () -> {

                    if (!Objects.equals(applicationCompliance.getInspector().getIntId(), acTakeAction.getUserId())) {
                        throw new CustomGeneralException(errorMessages.getNotAuthorized());
                    }

                    if (submittedCompliance.getInspectionDetails() == null) {
                        throw new CustomGeneralException("Inspection details required.");
                    }

                    TApplicationComplianceInspection inspector = submittedCompliance.getInspectionDetails();

                    if (!(acTakeAction.getAction().equals(Status.Schedule) || acTakeAction.getAction().equals(Status.Complete))) {
                        inspector.setVchReportFilePath(JsonFileExtractorUtil.uploadFile(inspector.getVchReportFilePath(), "AC_Inspector_Report_"+applicationCompliance.getTxtApplicationId(), FolderAndDirectoryConstant.APPLICATION_COMPLIANCE_INSPECTOR_FOLDER));
                    }

                    applicationCompliance.setInspectionDetails(inspector);
                },
                userIdConstants.getApprover(), () -> {
                    if (submittedCompliance.getAproverDetails() == null) {
                        throw new CustomGeneralException("Approval details required.");
                    }
                    applicationCompliance.setAproverDetails(submittedCompliance.getAproverDetails());
                },
                userIdConstants.getCeo(), () -> {
                    if (submittedCompliance.getAproverDetails() == null) {
                        throw new CustomGeneralException("getCeoApprovalDetails details required.");
                    }

                    applicationCompliance.setCeoApprovalDetails(submittedCompliance.getCeoApprovalDetails());
                }
        );

        Runnable action = approvalActions.get(roleId);
        if (action != null) {
            action.run();
        }
    }

    /**
     * Application compliance Role Based
     */
    private final Map<CompositeKey, Integer> applicationComplianceHierarchyMap = new HashMap<>();

    @PostConstruct
    private void initializeHierarchyMap() {
        applicationComplianceHierarchyMap.put(new CompositeKey(userIdConstants.getOicLegal(), 1), userIdConstants.getOicFinance());
        applicationComplianceHierarchyMap.put(new CompositeKey(userIdConstants.getOicFinance(), 2), userIdConstants.getOicLegal());
        applicationComplianceHierarchyMap.put(new CompositeKey(userIdConstants.getOicLegal(), 3), userIdConstants.getInspector());
        applicationComplianceHierarchyMap.put(new CompositeKey(userIdConstants.getInspector(), 4), userIdConstants.getOicLegal());
        applicationComplianceHierarchyMap.put(new CompositeKey(userIdConstants.getOicLegal(), 5), userIdConstants.getApprover());
        applicationComplianceHierarchyMap.put(new CompositeKey(userIdConstants.getApprover(), 6), userIdConstants.getCeo());
        applicationComplianceHierarchyMap.put(new CompositeKey(userIdConstants.getCeo(), 7), userIdConstants.getCeo());
    }

    public int getNextOfficerRole(CompositeKey currentOfficer) {
        if (applicationComplianceHierarchyMap.containsKey(currentOfficer)) {
            return applicationComplianceHierarchyMap.get(currentOfficer);
        } else {
            throw new CustomGeneralException(errorMessages.getNotAuthorized());
        }
    }

    public List<Integer> getStagesByUserId(Integer userId, int direction) {
        List<Integer> stages = new ArrayList<>();

        for (CompositeKey key : applicationComplianceHierarchyMap.keySet()) {
            if (key.getUserId().equals(userId)) {
                stages.add(key.getHierarchyLevel() + direction);
            }
        }

        return stages;
    }

    @Override
    public List<ApplicationCollateralDTO> getApplicationBySubCountyId(Integer subCountyId) {
        List<TapplicationOfCertificateOfCompliance> application = repo.findBySubCounty_intIdAndBitDeletedFlagAndEnmApprovalStatus(subCountyId,false,Status.Approved);
        if (application == null) {
            throw new EntityNotFoundException("Application not found for sub-county ID: " + subCountyId);
        }
        return application.stream().map(ApplicationCollateralMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<SubCountyResponse> findAllsubCounty() {
        List<SubCounty> subcountyObject = repo.findSubCountyByEnmApprovalStatusAndBitDeletedFlag(Status.Approved,false);
        return subcountyObject.stream().map(Mapper::mapToSubCountyResponse)
                .collect(Collectors.toList());
    }
    
    
    
    public ResponseEntity<String> createTempUser(String name,String mobile,String email,String address) throws JsonMappingException, JsonProcessingException {
    	
    
    	List<Tuser> userDb = userService.findByMobileOrEmail(mobile,email);
    	JSONObject object = new JSONObject();
    	if(userDb == null) {
	    	
	    	Tuser user = new Tuser();
	    	user.setTxtUserId(email);
	    	user.setEnPassword(CommonUtil.getHmacMessage("Admin@123"+salt));
	    	user.setTxtMobileNo(mobile);
	    	user.setTxtEmailId(email);
	    	user.setSelRole(46); //Temporary user
	    	user.setTxtFullName(name);
	    	user.setTxtrAddress(address);
	    	user.setSelGender(1);
	    	user.setChkPrevilege(3);
	    	user.setSelDesignation(1);
	    	user.setSelEmployeeType(0);
	    	user.setSelDepartment(1);
	    	user.setSelGroup(0);
	    	user.setSelHierarchy(0);
	    	user.setIntReportingAuth(0);
	    	//user.setUserStatus("NoObjection");
	    	userService.save(objectMapper.writeValueAsString(user));
	    	object.put("status", 200);
	        object.put("msg", "success");
    	}
    	else {
    		object.put("status", 409);
            object.put("msg", "conflict");
    	}
    	
        JSONObject response = new JSONObject();
        response.put("RESPONSE_DATA", Base64.getEncoder().encodeToString(object.toString().getBytes()));
        response.put("RESPONSE_TOKEN", TokenCreaterAndMatcher
                .getHmacMessage(Base64.getEncoder().encodeToString(object.toString().getBytes())));
        return ResponseEntity.ok(response.toString());
    }
    
    
    
}
