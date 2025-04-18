package app.ewarehouse.serviceImpl;


import app.ewarehouse.dto.SuspensionOfGraderResolutionDto;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TsuspensionOfGrader;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.TsuspensionOfGraderRepository;
import app.ewarehouse.service.TsuspensionOfGraderService;
import app.ewarehouse.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class TsuspensionOfGraderServiceImpl implements TsuspensionOfGraderService {
    private static final Logger logger = LoggerFactory.getLogger(TsuspensionOfGrader.class);

    private final TsuspensionOfGraderRepository repo;
    private final Validator validator;
    private final UserIdConstants userIdConstants;
    private final ObjectMapper objectMapper;
    private final ErrorMessages errorMessages;

    public TsuspensionOfGraderServiceImpl(TsuspensionOfGraderRepository repo, Validator validator, UserIdConstants userIdConstants, ObjectMapper objectMapper, ErrorMessages errorMessages) {
        this.repo = repo;
        this.validator = validator;
        this.userIdConstants = userIdConstants;
        this.objectMapper = objectMapper;
        this.errorMessages = errorMessages;
    }

    @Override
    public Page<TsuspensionOfGrader> getAllSuspensions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findByIsDeleted(pageable, false);
    }

    @Override
    public TsuspensionOfGrader createSuspension(String complaintFormDto) throws JsonProcessingException {
        try {
            String decodedData = CommonUtil.inputStreamDecoder(complaintFormDto);
            TsuspensionOfGrader suspensionGrader = objectMapper.readValue(decodedData, TsuspensionOfGrader.class);
            suspensionGrader.setStatus(Status.Pending);
            suspensionGrader.setSupportingDocument(JsonFileExtractorUtil.uploadFile(suspensionGrader.getSupportingDocument(), FolderAndDirectoryConstant.SUSPENSION_COMPLIANCE));
            suspensionGrader.setForwardedBy(userIdConstants.getComplaint());
            suspensionGrader.setPendingAtUser(userIdConstants.getCeo());
            suspensionGrader.setIsDeleted(false);

            Set<ConstraintViolation<TsuspensionOfGrader>> violations = validator.validate(suspensionGrader);
            if (!violations.isEmpty()) {
                logger.error("Inside save method of TsuspensionOfGraderImpl Validation errors: {}", violations);
                throw new CustomGeneralException(violations);
            }

            repo.save(suspensionGrader);
            return suspensionGrader;

        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                logger.error("Unique constraint violation: Contact Number: {}", e.getMessage(), e);
                throw new CustomGeneralException("A record with the same contact number already exists.");
            }
            logger.error("Data integrity violation: {}", e.getMessage(), e);
            throw new RuntimeException(e);

        } catch (JsonProcessingException e) {
            logger.error("Inside save method of TsuspensionOfGraderImpl some error occur: {}", e.getMessage());
            throw new CustomGeneralException("Invalid data format.");
        } catch (CustomGeneralException exception) {
            logger.error("Inside save method of TsuspensionOfGraderImpl some error occur: {}", exception.getMessage());
            throw new RuntimeException(exception);
        } catch (Exception e) {
            logger.info("Unexpected error: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public TsuspensionOfGrader getSuspensionByGraderId(String graderComplaintId) {
        return repo.findById(graderComplaintId).orElseThrow(() -> new CustomGeneralException(errorMessages.getEntityNotFound()));
    }

    @Override
    public boolean isContactNumberUnique(String contactNumber) {
        return repo.existsByComplainantContactNumber(contactNumber);
    }

    @Override
    public List<TsuspensionOfGrader> getAll() {
        return repo.getByIsDeleted(false);
    }


    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPendingComplaintsForUser(Integer pageNumber, Integer pageSize, Integer roleId) {
        logger.info("Inside getAllPending paginated method of TsuspensionOfGraderServiceImpl");
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
            Page<TsuspensionOfGrader> tsuspensionOfGraderPage = repo.findComplaintsByUser(Status.Pending, roleId, pageable);
            List<TsuspensionOfGrader> graderList = tsuspensionOfGraderPage.getContent();
            return Map.of("status", "200", "payload", graderList, "totalPendingComplaints", tsuspensionOfGraderPage.getTotalElements(), "totalPages", tsuspensionOfGraderPage.getTotalPages());
        } catch (Exception e) {
            logger.info("Error occurred in getPendingComplaintsForUser method of TsuspensionOfGraderServiceImpl : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void forwardComplaint(String data) {
        try {
            String decodedData = CommonUtil.inputStreamDecoder(data);
            SuspensionOfGraderResolutionDto resolutionDto = objectMapper.readValue(decodedData, SuspensionOfGraderResolutionDto.class);

            String complaintNumber = new String(Base64.getDecoder().decode(resolutionDto.getComplaintNumber()), StandardCharsets.UTF_8);

            TsuspensionOfGrader tsuspension = repo.findById(complaintNumber)
                    .orElseThrow(() -> new CustomGeneralException(errorMessages.getEntityNotFound()));

            Integer roleId = resolutionDto.getRoleId();
            String remark = resolutionDto.getRemark();

            if (roleId.equals(userIdConstants.getCeo())) {
                handleCeoRole(tsuspension, remark);
            } else if (roleId.equals(userIdConstants.getOic())) {
                handleOicLegalRole(tsuspension, remark);
            } else if (roleId.equals(userIdConstants.getApprover())) {
                handleApproverRole(tsuspension, remark);
            } else {
                throw new CustomGeneralException(errorMessages.getNotAuthorized());
            }
            repo.save(tsuspension);
        } catch (JsonProcessingException ex) {
            logger.error("Error processing JSON data: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } catch (CustomGeneralException exception) {
            throw new CustomGeneralException(exception.getMessage());
        } catch (Exception e) {
            logger.info("Error occurred in forwardComplaint method of TsuspensionOfGraderServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    private void handleCeoRole(TsuspensionOfGrader tsuspension, String remark) {
        if (!(tsuspension.getStatus().equals(Status.Pending) && tsuspension.getForwardedBy().equals(userIdConstants.getComplaint()))) {
            throw new CustomGeneralException(errorMessages.getAlreadyForwarded());
        }
        tsuspension.setCeoInitialRemark(remark);
        tsuspension.setPendingAtUser(userIdConstants.getOic());
        tsuspension.setForwardedBy(userIdConstants.getCeo());
    }

    private void handleOicLegalRole(TsuspensionOfGrader tsuspension, String remark) {
        if (!(tsuspension.getStatus().equals(Status.Pending) && tsuspension.getForwardedBy().equals(userIdConstants.getCeo()) && tsuspension.getCeoInitialRemark() != null)) {
            throw new CustomGeneralException(errorMessages.getAlreadyForwarded());
        }
        tsuspension.setForwardedBy(userIdConstants.getOic());
        tsuspension.setPendingAtUser(userIdConstants.getApprover());
        tsuspension.setOicLegalRemark(remark);
    }

    private void handleApproverRole(TsuspensionOfGrader tsuspension, String remark) {
        if (!(tsuspension.getStatus().equals(Status.Pending) && tsuspension.getForwardedBy().equals(userIdConstants.getOic()) && tsuspension.getOicLegalRemark() != null)) {
            throw new CustomGeneralException(errorMessages.getAlreadyForwarded());
        }
        tsuspension.setForwardedBy(userIdConstants.getApprover());
        tsuspension.setPendingAtUser(userIdConstants.getCeo());
        tsuspension.setApproverRemark(remark);
    }

    @Override
    public Map<String, Object> getForwardedComplaints(Integer pageNumber, Integer pageSize, Integer roleId) {
        logger.info("Inside getForwardedComplaints paginated method of TsuspensionOfGraderServiceImpl");
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending());
            Page<TsuspensionOfGrader> tsuspensionOfGraderPage;
            if (roleId.equals(userIdConstants.getOic())) {
                tsuspensionOfGraderPage = repo.findForwardedComplaintsForOicLegal(Status.Pending, roleId, pageable);
            } else if (roleId.equals(userIdConstants.getApprover())) {
                tsuspensionOfGraderPage = repo.findForwardedComplaintsForApprover(Status.Pending, roleId, pageable);
            } else {
                throw new CustomGeneralException(errorMessages.getNotAuthorized());
            }
            List<TsuspensionOfGrader> graderList = tsuspensionOfGraderPage.getContent();
            return Map.of("status", "200", "payload", graderList, "totalForwardedComplaints", tsuspensionOfGraderPage.getTotalElements(), "totalPages", tsuspensionOfGraderPage.getTotalPages());
        } catch (Exception e) {
            logger.info("Error occurred in getForwardedComplaints method of TsuspensionOfGraderServiceImpl : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void verdict(String data) {
        logger.info("Inside verdict method of TsuspensionOfGraderServiceImpl");
        try {
            SuspensionOfGraderResolutionDto resolutionDto = objectMapper.readValue(CommonUtil.inputStreamDecoder(data), SuspensionOfGraderResolutionDto.class);
            String complaintNumber = new String(Base64.getDecoder().decode(resolutionDto.getComplaintNumber()), StandardCharsets.UTF_8);
            TsuspensionOfGrader tsuspension = repo.findById(complaintNumber)
                    .orElseThrow(() -> new CustomGeneralException(errorMessages.getEntityNotFound()));

            if (!(tsuspension.getStatus().equals(Status.Pending) && tsuspension.getForwardedBy().equals(userIdConstants.getApprover()) && tsuspension.getApproverRemark() != null)) {
                throw new CustomGeneralException(errorMessages.getAlreadyResolved());
            }

            Integer roleId = resolutionDto.getRoleId();
            String remark = resolutionDto.getRemark();
            String verdict = resolutionDto.getVerdict();

            if (!roleId.equals(userIdConstants.getCeo())) {
                throw new CustomGeneralException(errorMessages.getNotAuthorized());
            }

            if (verdict.equals("approved")) {
                tsuspension.setStatus(Status.Approved);
            } else if (verdict.equals("rejected")) {
                tsuspension.setStatus(Status.Rejected);
            } else {
                throw new CustomGeneralException(errorMessages.getInvalidVerdict());
            }

            tsuspension.setCeoFinalRemark(remark);
            tsuspension.setRemark(remark);
            tsuspension.setForwardedBy(userIdConstants.getCeo());
            repo.save(tsuspension);
        } catch (Exception e) {
            logger.info("Error occurred in verdict method of TsuspensionOfGraderServiceImpl : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public Map<String, Object> getResolvedComplaints(Integer pageNumber, Integer pageSize, Integer roleId, Status status) {
        logger.info("Inside getResolvedComplaints paginated method of TsuspensionOfGraderServiceImpl");
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending());
            Page<TsuspensionOfGrader> tsuspensionOfGraderPage;
            if (!roleId.equals(userIdConstants.getCeo())){
                throw new CustomGeneralException(errorMessages.getNotAuthorized());
            }

            if(status.equals(Status.Approved)){
                tsuspensionOfGraderPage = repo.findResolvedComplaints(Status.Approved, pageable);
            } else if (status.equals(Status.Rejected)) {
                tsuspensionOfGraderPage = repo.findResolvedComplaints(Status.Rejected, pageable);
            } else {
                throw new CustomGeneralException(errorMessages.getInvalidVerdict());
            }
            List<TsuspensionOfGrader> graderList = tsuspensionOfGraderPage.getContent();
            return Map.of("status", "200", "payload", graderList, "totalResolvedComplaints", tsuspensionOfGraderPage.getTotalElements(), "totalPages", tsuspensionOfGraderPage.getTotalPages());
        } catch (Exception e) {
            logger.info("Error occurred in getResolvedComplaints method of TsuspensionOfGraderServiceImpl : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
