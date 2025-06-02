package app.ewarehouse.serviceImpl;

import app.ewarehouse.dto.InspectorSuspensionComplaintRequest;
import app.ewarehouse.dto.InspectorSuspensionComplaintResponse;
import app.ewarehouse.entity.InspectorSuspensionComplaint;
import app.ewarehouse.entity.Status;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.InspectorSuspensionComplaintRepository;
import app.ewarehouse.service.InspectorSuspensionComplaintService;
import app.ewarehouse.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class InspectorSuspensionComplaintServiceImpl implements InspectorSuspensionComplaintService {
    private final InspectorSuspensionComplaintRepository inspectorSuspensionComplaintRepository;
    private final Validator validator;
    private final ErrorMessages errorMessages;
    private final ObjectMapper om;


    public InspectorSuspensionComplaintServiceImpl(InspectorSuspensionComplaintRepository inspectorSuspensionComplaintRepository, Validator validator, ErrorMessages errorMessages,ObjectMapper om) {
        this.inspectorSuspensionComplaintRepository = inspectorSuspensionComplaintRepository;
        this.validator = validator;
        this.errorMessages = errorMessages;
        this.om = om;
    }

    @Override
    @Transactional
    public String save(String complaintData) {
        log.info("Inside save method of InspectorSuspensionComplaintServiceImpl");
        try {
            InspectorSuspensionComplaintRequest inspectorSuspensionComplaintRequest = om.readValue(CommonUtil.inputStreamDecoder(complaintData), InspectorSuspensionComplaintRequest.class);

            Set<ConstraintViolation<InspectorSuspensionComplaintRequest>> violations = validator.validate(inspectorSuspensionComplaintRequest);
            if (!violations.isEmpty()) {
                throw new CustomGeneralException(violations);
            }

            byte[] decode = Base64.getDecoder().decode(inspectorSuspensionComplaintRequest.getSupportingDocument().getBytes());
            String uniqueFileName = "Inspector_Suspension_Complaint_" + UUID.randomUUID();

            String file_url = DocumentUploadutil.uploadFileByte(uniqueFileName, decode, FolderAndDirectoryConstant.INSP_SUSPENSION_FOLDER);

            if (file_url.startsWith("Document")) {
                throw new RuntimeException(file_url);
            }

            String filePath = file_url.substring(file_url.indexOf(FolderAndDirectoryConstant.INSP_SUSPENSION_FOLDER));

            InspectorSuspensionComplaint inspectorSuspensionComplaint = InspectorSuspensionComplaint.builder()
                    .complainantName(inspectorSuspensionComplaintRequest.getComplainantName())
                    .complainantContactNumber(inspectorSuspensionComplaintRequest.getComplainantContactNumber())
                    .complainantEmail(inspectorSuspensionComplaintRequest.getComplainantEmail())
                    .complainantAddress(inspectorSuspensionComplaintRequest.getComplainantAddress())
                    .complaintType(inspectorSuspensionComplaintRequest.getComplaintType())
                    .dateOfIncident(inspectorSuspensionComplaintRequest.getDateOfIncident())
                    .descriptionOfIncident(inspectorSuspensionComplaintRequest.getDescriptionOfIncident())
                    .locationOfIncident(inspectorSuspensionComplaintRequest.getLocationOfIncident())
                    .isDeclared(inspectorSuspensionComplaintRequest.getIsDeclared())
                    .isDeleted(false)
                    .status(Status.Pending)
                    .supportingDocument(filePath)
                    .actionTakenBy(1)
                    .build();
            InspectorSuspensionComplaint complaint = inspectorSuspensionComplaintRepository.save(inspectorSuspensionComplaint);
            return complaint.getComplaintId();
        } catch (Exception e) {
            log.error("Error occurred in save method of InspectorSuspensionComplaintServiceImpl: {} ", e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }


    @Override
    public InspectorSuspensionComplaintResponse getById(String id) {
        try {
            log.info("Inside getById method of InspectorSuspensionComplaintServiceImpl");
            InspectorSuspensionComplaint existingInspectorSuspensionComplaint = inspectorSuspensionComplaintRepository.findComplaintById(id, false)
                    .orElseThrow(() -> new CustomGeneralException(errorMessages.getEntityNotFound()));
            return Mapper.mapToComplaintResponse(existingInspectorSuspensionComplaint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<InspectorSuspensionComplaintResponse> getAll() {
        log.info("Inside getAll method of InspectorSuspensionComplaintServiceImpl");
        try {
            return inspectorSuspensionComplaintRepository.findAllComplaints(false).stream()
                    .map(Mapper::mapToComplaintResponse)
                    .toList();
        } catch (Exception e) {
            log.error("Error occurred in getAll method of InspectorSuspensionComplaintServiceImpl: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<InspectorSuspensionComplaintResponse> getAll(Integer pageNumber, Integer pageSize, String sortCol, String sortDir, String search) {
        log.info("Inside getAll paginated method of InspectorSuspensionComplaintServiceImpl");
        try {
/*<<<<<<< HEAD
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
            Page<InspectorSuspensionComplaint> complaintPage = inspectorSuspensionComplaintRepository.findAllComplaints(false, pageable);
            List<InspectorSuspensionComplaintResponse> inspectorSuspensionComplaintResponse = complaintPage.getContent().stream().map(Mapper::mapToComplaintResponse).collect(Collectors.toList());
=======*/

            Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "DESC"),
                    sortCol != null ? sortCol : "createdAt");
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

            Page<InspectorSuspensionComplaint> page;
            if (StringUtils.hasText(search)) {
                page = inspectorSuspensionComplaintRepository.findByFilters(search, pageable);
            } else {
                page = inspectorSuspensionComplaintRepository.findAllComplaints(false,pageable);
            }

            List<InspectorSuspensionComplaintResponse> inspectorSuspensionComplaintResponse = page.getContent()
                    .stream()
                    .map(Mapper::mapToComplaintResponse)
                    .toList();


            return new PageImpl<>(inspectorSuspensionComplaintResponse, pageable, page.getTotalElements());
        } catch (Exception e) {
            log.error("Error occurred in getAll method of InspectorSuspensionComplaintServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<InspectorSuspensionComplaint> getComplaintList(String status, Integer actionTakenBy, Pageable pageable) {
    	List<Integer> actionTakenByList = actionTakenBy==1||actionTakenBy==3?List.of(1,3):List.of(actionTakenBy);
        if (status == null || "null".equals(status))
            return inspectorSuspensionComplaintRepository.findByActionTakenBy(actionTakenByList, pageable);
        else
            return inspectorSuspensionComplaintRepository.findByStatusAndActionTakenBy(Status.valueOf(status), actionTakenByList, pageable);
    }

    @Override
    public void forwardComplaint(String id, Integer actionTakenBy, String remarks, Status status, Integer userId) {
        InspectorSuspensionComplaint c = inspectorSuspensionComplaintRepository.findById(id).get();
        c.setStatus(status);
        c.setActionTakenBy(actionTakenBy);
        c.setPendingAtUser(userId);
        if (actionTakenBy == 2)
            c.setCeoRemarks(remarks);
        if (actionTakenBy == 3)
            c.setOicRemarks(remarks);
        if (actionTakenBy == 4)
            c.setCeo2Remarks(remarks);
        if (actionTakenBy == 5)
            c.setApproverRemarks(remarks);
        inspectorSuspensionComplaintRepository.save(c);
    }
}
