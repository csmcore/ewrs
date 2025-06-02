package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.DisputeDeclarationResponse;
import app.ewarehouse.dto.TakeActionRequest;
import app.ewarehouse.entity.DisputeDeclaration;
import app.ewarehouse.entity.DisputeDeclarationCommitteeDetails;
import app.ewarehouse.entity.DisputeDeclarationCommitteeMembers;
import app.ewarehouse.entity.DisputeDeclarationOic;
import app.ewarehouse.entity.DisputeDeclarationSupportingDocument;
import app.ewarehouse.entity.Status;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.DisputeDeclarationRepository;
import app.ewarehouse.service.DisputeDeclarationService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.CompositeKey;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.JsonFileExtractorUtil;
import app.ewarehouse.util.Mapper;
import app.ewarehouse.util.UserIdConstants;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DisputeDeclarationServiceImpl implements DisputeDeclarationService {
    private final DisputeDeclarationRepository disputeDeclarationRepository;
    private final UserIdConstants userIdConstants;
    final Validator validator;
    private final ObjectMapper om;
    private final ErrorMessages errorMessages;


    public DisputeDeclarationServiceImpl(DisputeDeclarationRepository disputeDeclarationRepository, UserIdConstants userIdConstants, Validator validator, ErrorMessages errorMessages,ObjectMapper om) {
        this.disputeDeclarationRepository = disputeDeclarationRepository;
        this.userIdConstants = userIdConstants;
        this.validator = validator;
        this.errorMessages = errorMessages;
        this.om = om;
    }

    @Override
    @Transactional
    public String create(String data) {
        log.info("Inside create method of DisputeDeclarationServiceImpl");

        try {
            DisputeDeclaration disputeDeclaration = om.readValue(CommonUtil.inputStreamDecoder(data), DisputeDeclaration.class);
            DisputeDeclaration finalDisputeDeclaration = disputeDeclaration;
            disputeDeclaration.setSupportingDocuments(
                    disputeDeclaration.getSupportingDocuments().stream().peek(
                            (DisputeDeclarationSupportingDocument item) ->
                            {
                                item.setDocumentPath(
                                        JsonFileExtractorUtil.uploadFile(item.getDocumentPath(), item.getDocumentName() + "_" + System.currentTimeMillis(), FolderAndDirectoryConstant.DISPUTE_DECLARATION_SUPPORTING_DOCUMENTS)
                                );
                                item.setDisputeDeclaration(finalDisputeDeclaration);
                            }).collect(Collectors.toList()));

            Set<ConstraintViolation<DisputeDeclaration>> violations = validator.validate(disputeDeclaration);
            if (!violations.isEmpty()) {
                log.error("Inside save method of DisputeDeclarationServiceImpl Validation errors: " + violations);
                throw new CustomGeneralException(violations);
            }

            disputeDeclaration.setStatus(Status.Pending);
            disputeDeclaration.setIntCurrentStage(1);
            disputeDeclaration.setIntCurrentRole(userIdConstants.getCeo());

            disputeDeclaration = disputeDeclarationRepository.save(disputeDeclaration);

            return disputeDeclaration.getDisputeId();
        }
        catch (CustomGeneralException exception) {
            log.error("Inside save method of DisputeDeclarationServiceImpl some error occur:" + exception.getMessage());
            throw exception;
        } catch (Exception e) {
            log.error("Inside save method of DisputeDeclarationServiceImpl some error occur:" + e.getMessage());
            throw new CustomGeneralException(errorMessages.getUnknownError());
        }
    }

    @Override
    public DisputeDeclarationResponse getById(String id) {
        try {
            log.info("Inside getById method of DisputeDeclarationServiceImpl");
            DisputeDeclaration dispute = disputeDeclarationRepository.findById(id).orElseThrow(() ->  new CustomGeneralException(errorMessages.getEntityNotFound()));
            return Mapper.mapToDisputeDeclarationResponse(dispute);
        } catch (CustomGeneralException e) {
            log.error("Error occurred in getById method of BuyerServiceImpl: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred in getById method of BuyerServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisputeDeclarationResponse> getAll() {
        log.info("Inside getAll method of DisputeDeclarationServiceImpl");
        return disputeDeclarationRepository.findAll().stream()
                .map(Mapper::mapToDisputeDeclarationResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisputeDeclarationResponse> getAll(Pageable pageable) {
        log.info("Inside getAll paginated method of DisputeDeclarationServiceImpl");
        try {
            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
            Page<DisputeDeclaration> disputeDeclarationPage = disputeDeclarationRepository.findAllDisputeDeclarations(sortedPageable);

            return disputeDeclarationPage.map(Mapper::mapToDisputeDeclarationResponse);

        } catch (Exception e) {
            log.error("Error occurred in paginated getAll method of DisputeDeclarationServiceImpl: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<DisputeDeclarationResponse> findByFilters(Integer roleId, Date fromDate, Date toDate, String search, String sortColumn, String sortDirection, Integer userId, Integer action, Pageable pageable) {
        log.info("Inside findByFilters method of DisputeDeclarationServiceImpl");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
                sortColumn != null ? sortColumn : "updatedAt");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        List<Integer> myStages = getStagesByUserId(roleId, 0);

        Page<DisputeDeclaration> disputeDeclarations = disputeDeclarationRepository.findByFilters(fromDate, toDate, search, userId, myStages.get(0), roleId, userIdConstants.getComplaint(), sortedPageable);

        return disputeDeclarations.map(Mapper::mapToDisputeDeclarationResponse);
    }

    @Override
    public String takeAction(String data) {
        log.info("Inside takeAction method of DisputeDeclarationServiceImpl");

        String decodedData = CommonUtil.inputStreamDecoder(data);
        TakeActionRequest<DisputeDeclaration> disputeTakeAction;

        try {
            disputeTakeAction = om.readValue(decodedData, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("Inside save method of DisputeDeclarationServiceImpl some error occur:" + e.getMessage());
            throw new CustomGeneralException(errorMessages.getInternalServerError());
        }

        String disputeId = disputeTakeAction.getData().getDisputeId();
        DisputeDeclaration existingDispute = disputeDeclarationRepository
                .findById(disputeId)
                .orElseThrow(() -> new CustomGeneralException(errorMessages.getEntityNotFound()));

        Integer intCurrentRole = disputeTakeAction.getOfficerRole();

        int approvalStage = existingDispute.getIntCurrentStage();

        if (existingDispute.getStatus().equals(Status.OnHold) && Objects.equals(existingDispute.getIntCurrentRole(), intCurrentRole)) {
            approvalStage += 1;
        }

        boolean isSecondOicLegal = false;

        try {
            isSecondOicLegal = Objects.equals(getNextDisputeOfficer(new CompositeKey(existingDispute.getIntCurrentRole(), approvalStage)), userIdConstants.getOicLegal())
                    && Objects.equals(existingDispute.getIntCurrentRole(), userIdConstants.getOicLegal());
        }
        catch (CustomGeneralException e) {
            log.info("Not final approval");
        }

        getApprovalDetails(existingDispute, disputeTakeAction, approvalStage);

        if (disputeTakeAction.getAction().equals(Status.Approved)) {
            existingDispute.setIntCurrentRole(getNextDisputeOfficer(new CompositeKey(intCurrentRole, approvalStage)));
            existingDispute.setIntCurrentStage(approvalStage + 1);
            existingDispute.setStatus(Status.Pending);

            if (isSecondOicLegal) {
                existingDispute.setStatus(Status.Approved);
            }
        }
        else if (disputeTakeAction.getAction().equals(Status.Rejected) && Objects.equals(intCurrentRole, userIdConstants.getCeo())) {
            existingDispute.setIntCurrentRole(getNextDisputeOfficer(new CompositeKey(intCurrentRole, approvalStage)));
            existingDispute.setIntCurrentStage(approvalStage + 1);
            existingDispute.setStatus(Status.Rejected);
        }
        else if (disputeTakeAction.getAction().equals(Status.Draft)) {
            log.info("Inspection draft, no action required.");
        }
        else {
            throw new CustomGeneralException(errorMessages.getNotAuthorized());
        }

        disputeDeclarationRepository.save(existingDispute);
        return disputeId;
    }

    private void getApprovalDetails(DisputeDeclaration existingDispute, TakeActionRequest<DisputeDeclaration> disputeTakeAction, int approvalStage) {
        int roleId = disputeTakeAction.getOfficerRole();
        DisputeDeclaration submittedDispute = disputeTakeAction.getData();

        // Map role IDs to their corresponding actions using Runnable instances (lambda expressions)
        Map<Integer, Runnable> approvalActions = Map.of(
                userIdConstants.getCeo(), () -> {
                    if (submittedDispute.getCeoApproval() == null) {
                        throw new CustomGeneralException("Ceo approval details required.");
                    }

                    existingDispute.setCeoApproval(submittedDispute.getCeoApproval());
                },
                userIdConstants.getOicLegal(), () -> {

                    if (approvalStage == 2) {
                        if (submittedDispute.getCommitteeDetails() == null) {
                            throw new CustomGeneralException("Committee details required.");
                        }

                        DisputeDeclarationCommitteeDetails committeeDetails = submittedDispute.getCommitteeDetails();

                        committeeDetails.setCommitteeMembers(
                                committeeDetails.getCommitteeMembers().stream().peek(
                                        (DisputeDeclarationCommitteeMembers item) -> item.setCommitteeDetails(committeeDetails)
                                ).collect(Collectors.toList())
                        );

                        existingDispute.setCommitteeDetails(committeeDetails);
                    }
                    else {
                        if (submittedDispute.getOicLegalTwo() == null) {
                            throw new CustomGeneralException("Oic legal approval details required.");
                        }

                        existingDispute.setOicLegalTwo(submittedDispute.getOicLegalTwo());
                    }
                },
                userIdConstants.getOic(), () -> {

                    if (submittedDispute.getOicApproval() == null) {
                        throw new CustomGeneralException("Oic Cert approval details required.");
                    }

                    DisputeDeclarationOic oic = submittedDispute.getOicApproval();
                    oic.setReportPath(JsonFileExtractorUtil.uploadFile(oic.getReportPath(), "Oic_Dispute_Report_"+existingDispute.getDisputeId(), FolderAndDirectoryConstant.DISPUTE_DECLARATION_APPROVAL_DOCUMENTS));
                    existingDispute.setOicApproval(oic);
                }
        );

        Runnable action = approvalActions.get(roleId);
        if (action != null) {
            action.run();
        }
    }

    /**
     * Dispute Declaration Role Based
     */
    private final Map<CompositeKey, Integer> disputeDeclarationHierarchyMap = new HashMap<>();

    @PostConstruct
    public void setDisputeDeclarationHierarchyMap() {
        disputeDeclarationHierarchyMap.put(new CompositeKey(userIdConstants.getComplaint(), 0), userIdConstants.getCeo());
        disputeDeclarationHierarchyMap.put(new CompositeKey(userIdConstants.getCeo(), 1), userIdConstants.getOicLegal());
        disputeDeclarationHierarchyMap.put(new CompositeKey(userIdConstants.getOicLegal(),2), userIdConstants.getOic());
        disputeDeclarationHierarchyMap.put(new CompositeKey(userIdConstants.getOic(), 3), userIdConstants.getOicLegal());
        disputeDeclarationHierarchyMap.put(new CompositeKey(userIdConstants.getOicLegal(), 4), userIdConstants.getOicLegal());
    }

    public int getNextDisputeOfficer(CompositeKey currentOfficer) {
        if (disputeDeclarationHierarchyMap.containsKey(currentOfficer)) {
            return disputeDeclarationHierarchyMap.get(currentOfficer);
        } else {
            throw new CustomGeneralException(errorMessages.getNotAuthorized());
        }
    }

    public List<Integer> getStagesByUserId(Integer userId, int direction) {
        List<Integer> stages = new ArrayList<>();

        for (CompositeKey key : disputeDeclarationHierarchyMap.keySet()) {
            if (key.getUserId().equals(userId)) {
                stages.add(key.getHierarchyLevel() + direction);
            }
        }

        return stages;
    }
}


