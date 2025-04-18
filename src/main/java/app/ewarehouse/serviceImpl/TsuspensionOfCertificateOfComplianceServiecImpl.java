package app.ewarehouse.serviceImpl;

import java.util.*;
import app.ewarehouse.dto.TakeActionRequest;
import app.ewarehouse.entity.*;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.repository.TsuspensionOfCertificateOfComplianceRepository;
import app.ewarehouse.service.TsuspensionOfCertificateOfComplianceService;

@Service
public class TsuspensionOfCertificateOfComplianceServiecImpl implements TsuspensionOfCertificateOfComplianceService {

	@Autowired
	TsuspensionOfCertificateOfComplianceRepository suspensionRepository;
	@Autowired
	ErrorMessages errorMessages;
	@Autowired
	private UserIdConstants userIdConstants;
	@Autowired
	private Validator validator;
	@Autowired
    private ObjectMapper om;
	private static final Logger logger = LoggerFactory.getLogger(TsuspensionOfCertificateOfComplianceServiecImpl.class);

	@Override
	public TsuspensionOfCertificateOfCompliance createSuspension(String complaintFormDto) throws JsonMappingException, JsonProcessingException {
		try {
		logger.info("inside in service Impl");
		String decodedData = CommonUtil.inputStreamDecoder(complaintFormDto);
		   TsuspensionOfCertificateOfCompliance suspensionCompliance  = om.readValue(decodedData, TsuspensionOfCertificateOfCompliance.class);
			suspensionCompliance.setStatus(Status.Pending);
			suspensionCompliance.setSupportingDocument(JsonFileExtractorUtil.uploadFile(suspensionCompliance.getSupportingDocument(), FolderAndDirectoryConstant.SUSPENSION_COMPLIANCE));

			suspensionCompliance.setIntCurrentRole(userIdConstants.getCeo());
			suspensionCompliance.setIntCurrentStage(1);

			Set<ConstraintViolation<TsuspensionOfCertificateOfCompliance>> violations = validator.validate(suspensionCompliance);
			if (!violations.isEmpty()) {
				throw new CustomGeneralException(violations);
			}
			suspensionRepository.save(suspensionCompliance);
		return suspensionCompliance;
		} catch (DataIntegrityViolationException exception) {
			throw new CustomGeneralException(errorMessages.getContactNotUnique());
		} catch (CustomGeneralException exception){
			throw exception;
		}catch (JsonProcessingException e) {
			throw new CustomGeneralException(errorMessages.getInternalServerError());
		}catch (Exception e){
			throw new CustomGeneralException(errorMessages.getUnknownError());
		}
	}

	@Override
	public TsuspensionOfCertificateOfCompliance getSuspensionByComplaintNumber(String id) {
        return suspensionRepository.findByComplaintNumberAndIsDeleted(id,false);
	}

	@Override
	public boolean isContactNumberUnique(String contactNumber) {
        return suspensionRepository.existsByComplainantContactNumberAndIsDeleted(contactNumber,false);
	}

	@Override
	public Page<TsuspensionOfCertificateOfCompliance> getAllSuspensions(int page, int size) {
		 Pageable pageable = PageRequest.of(page, size);
	        return suspensionRepository.findAllByIsDeleted(pageable,false);
	}
	@Override
	public List<TsuspensionOfCertificateOfCompliance> getAllSuspensions() {
		return suspensionRepository.findByIsDeleted(false);
	}

	@Override
	public Page<TsuspensionOfCertificateOfCompliance> findByFilters(Integer intCurrentRole, Status status, String search, String sortColumn, String sortDirection, Pageable pageable) {
		logger.info("Inside findByFilters method of SuspensionCollateralServiceImpl");

		Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
				sortColumn != null ? sortColumn : "updatedAt");

		Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		List<Integer> myStages = getStagesByUserId(intCurrentRole, 0);
		List<Integer> immediateBelowStages = getStagesByUserId(intCurrentRole, -1);
		Optional<Integer> minStage = myStages.stream().min(Integer::compareTo);

		if (minStage.isPresent()) {

            return suspensionRepository.findByFilters(
					intCurrentRole, minStage.get(), immediateBelowStages, myStages,
					status, Status.Pending, Status.Forwarded, Status.Approved,
					Status.Rejected, Status.OnHold, search, sortedPageable
			);
		}

		return new PageImpl<>(new ArrayList<>(), pageable, 0);
	}

	@Override
	public String takeAction(String data) {
		logger.info("Inside takeAction method of SuspensionCollateralServiceImpl");

		String decodedData = CommonUtil.inputStreamDecoder(data);
		TakeActionRequest<TsuspensionOfCertificateOfCompliance> rcTakeAction;

		try {
			rcTakeAction = om.readValue(decodedData, new TypeReference<TakeActionRequest<TsuspensionOfCertificateOfCompliance>>() {});
		} catch (Exception e) {
			logger.error("Inside save method of SuspensionCollateralServiceImpl some error occur:" + e.getMessage());
			throw new CustomGeneralException(errorMessages.getInternalServerError());
		}

		String suspensionComplaintNumber = rcTakeAction.getData().getComplaintNumber();
		TsuspensionOfCertificateOfCompliance existingSuspensionCollateral = suspensionRepository
				.findByComplaintNumberAndIsDeleted(suspensionComplaintNumber, false);

		Integer intCurrentRole = rcTakeAction.getOfficerRole();

		int approvalStage = existingSuspensionCollateral.getIntCurrentStage();

		if (existingSuspensionCollateral.getStatus().equals(Status.OnHold) && Objects.equals(existingSuspensionCollateral.getIntCurrentRole(), intCurrentRole)) {
			approvalStage += 1;
		}

		boolean isSecondCeo = false;

		try {
			isSecondCeo = Objects.equals(getNextOfficerRole(new CompositeKey(existingSuspensionCollateral.getIntCurrentRole(), approvalStage)), userIdConstants.getCeo())
					&& Objects.equals(existingSuspensionCollateral.getIntCurrentRole(), userIdConstants.getCeo());
		}
		catch (CustomGeneralException e) {
			logger.info("Not final approval");
		}

		getApprovalDetails(existingSuspensionCollateral, rcTakeAction, approvalStage);

		if (rcTakeAction.getAction().equals(Status.Approved)) {

			existingSuspensionCollateral.setIntCurrentRole(getNextOfficerRole(new CompositeKey(intCurrentRole, approvalStage)));
			existingSuspensionCollateral.setIntCurrentStage(approvalStage + 1);
			existingSuspensionCollateral.setStatus(Status.Pending);

			if (isSecondCeo) {
				existingSuspensionCollateral.setStatus(Status.Approved);
			}
		} else if (rcTakeAction.getAction().equals(Status.Deferred)) {
			getNextOfficerRole(new CompositeKey(intCurrentRole, existingSuspensionCollateral.getIntCurrentStage()));

			existingSuspensionCollateral.setIntCurrentRole(intCurrentRole);
			existingSuspensionCollateral.setStatus(Status.OnHold);
			existingSuspensionCollateral.setIntCurrentStage(existingSuspensionCollateral.getIntCurrentStage() - 1);
		}
		else if (rcTakeAction.getAction().equals(Status.Rejected) && Objects.equals(intCurrentRole, userIdConstants.getCeo())) {
			existingSuspensionCollateral.setIntCurrentRole(getNextOfficerRole(new CompositeKey(intCurrentRole, approvalStage)));
			existingSuspensionCollateral.setIntCurrentStage(approvalStage + 1);
			existingSuspensionCollateral.setStatus(Status.Rejected);
		}
		else {
			throw new CustomGeneralException(errorMessages.getNotAuthorized());
		}

		suspensionRepository.save(existingSuspensionCollateral);
		return suspensionComplaintNumber;
	}

	private void getApprovalDetails(TsuspensionOfCertificateOfCompliance suspensionCollateral, TakeActionRequest<TsuspensionOfCertificateOfCompliance> rcTakeAction, int approvalStage) {
		int roleId = rcTakeAction.getOfficerRole();
		TsuspensionOfCertificateOfCompliance submittedSuspensionCollateral = rcTakeAction.getData();

		// Map role IDs to their corresponding actions using Runnable instances (lambda expressions)
		Map<Integer, Runnable> approvalActions = Map.of(
				userIdConstants.getCeo(), () -> {

					if (approvalStage == 1) {
						suspensionCollateral.setRemark(submittedSuspensionCollateral.getRemark());
					}
					else {
						suspensionCollateral.setVchCeoSecondRemark(submittedSuspensionCollateral.getVchCeoSecondRemark());
						suspensionCollateral.setVchSuspensionConclusion(submittedSuspensionCollateral.getVchSuspensionConclusion());
					}
				},
				userIdConstants.getOicLegal(), () -> {
					if (approvalStage == 2) {
						suspensionCollateral.setVchOicLegalRemark(submittedSuspensionCollateral.getVchOicLegalRemark());
					}
					else {
						suspensionCollateral.setVchOicLegalTwoRemark(submittedSuspensionCollateral.getVchOicLegalTwoRemark());
					}
				},
				userIdConstants.getInspector(), () -> {
					suspensionCollateral.setVchInspectorRemark(submittedSuspensionCollateral.getVchInspectorRemark());
					suspensionCollateral.setVchInspectionDocument(JsonFileExtractorUtil.uploadFile(submittedSuspensionCollateral.getVchInspectionDocument(), "SC_Inspection_Document_"+suspensionCollateral.getComplaintNumber(), FolderAndDirectoryConstant.SUSPENSION_COLLATERAL_INSPECTION_FOLDER));
				},
				userIdConstants.getApprover(), () -> suspensionCollateral.setVchApproverRemark(submittedSuspensionCollateral.getVchApproverRemark())
		);

		Runnable action = approvalActions.get(roleId);
		if (action != null) {
			action.run();
		}
	}

	/**
	 * Suspension Collateral Role Based
	 */
	private final Map<CompositeKey, Integer> suspensionCollateralHierarchyMap = new HashMap<>();

	@PostConstruct
	private void initializeHierarchyMap() {
		suspensionCollateralHierarchyMap.put(new CompositeKey(userIdConstants.getCeo(), 1), userIdConstants.getOicLegal());
		suspensionCollateralHierarchyMap.put(new CompositeKey(userIdConstants.getOicLegal(), 2), userIdConstants.getInspector());
		suspensionCollateralHierarchyMap.put(new CompositeKey(userIdConstants.getInspector(), 3), userIdConstants.getOicLegal());
		suspensionCollateralHierarchyMap.put(new CompositeKey(userIdConstants.getOicLegal(), 4), userIdConstants.getApprover());
		suspensionCollateralHierarchyMap.put(new CompositeKey(userIdConstants.getApprover(), 5), userIdConstants.getCeo());
		suspensionCollateralHierarchyMap.put(new CompositeKey(userIdConstants.getCeo(), 6), userIdConstants.getCeo());
	}

	public int getNextOfficerRole(CompositeKey currentOfficer) {
		if (suspensionCollateralHierarchyMap.containsKey(currentOfficer)) {
			return suspensionCollateralHierarchyMap.get(currentOfficer);
		} else {
			throw new CustomGeneralException(errorMessages.getNotAuthorized());
		}
	}

	public List<Integer> getStagesByUserId(Integer userId, int direction) {
		List<Integer> stages = new ArrayList<>();

		for (CompositeKey key : suspensionCollateralHierarchyMap.keySet()) {
			if (key.getUserId().equals(userId)) {
				stages.add(key.getHierarchyLevel() + direction);
			}
		}

		return stages;
	}

}
