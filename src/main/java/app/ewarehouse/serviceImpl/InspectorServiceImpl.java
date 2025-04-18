package app.ewarehouse.serviceImpl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.InspectorDTO;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.dto.StakeHolderActionRequestData;
import app.ewarehouse.entity.Action;
import app.ewarehouse.entity.CheckList;
import app.ewarehouse.entity.Inspector;
import app.ewarehouse.entity.Remark;
import app.ewarehouse.entity.Stakeholder;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.InspectorRepository;
import app.ewarehouse.repository.RemarkRepository;
import app.ewarehouse.service.InspectorService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.JsonFileExtractorUtil;
import app.ewarehouse.util.UserIdConstants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class InspectorServiceImpl implements InspectorService {

	@Autowired
	private InspectorRepository inspectorRepository;
	@Autowired
	private RemarkRepository remarkRepository;
	
	@Autowired
    private EntityManager entityManager;
	
	@Autowired
	private UserIdConstants userIdConstants;

	@Autowired
	private ObjectMapper objectMapper;

	ResponseDTO<String> responseDTO = new ResponseDTO<>();

	private Inspector inspector;
	
	@Override
	public Page<InspectorDTO> getFilteredApplications(Date fromDate, Date toDate, Status status, Action action, Stakeholder stakeholder,String search, Stakeholder forwardedTo, Pageable pageable) {
		if (toDate != null) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(toDate);
	        calendar.add(Calendar.HOUR, 24);
	        toDate = calendar.getTime();
	    }
	    Page<Inspector> inspectors = inspectorRepository.findByFilters(fromDate, toDate, status, action, stakeholder,search, forwardedTo, pageable);
	    return inspectors.map(this::convertToDto);
	}
	
	public Inspector getInspectorById(Integer id) {
		Optional<Inspector> optionalInspector = inspectorRepository.findById(id);
		return optionalInspector.orElse(null);
	}

	public List<InspectorDTO> getAllInspectors() {
		List<Inspector> inspectors = inspectorRepository.findAll();
		return inspectors.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public Page<InspectorDTO> getAllInspectors(Pageable pageable) {
		Page<Inspector> inspectors = inspectorRepository.findAll(pageable);
		return inspectors.map(this::convertToDto);
	}

	private InspectorDTO convertToDto(Inspector inspector) {
		InspectorDTO dto = new InspectorDTO();
		dto.setId(inspector.getId());
		dto.setName(inspector.getName());
		dto.setEmail(inspector.getEmail());
		dto.setMobileNumber(inspector.getMobileNumber());
		dto.setStatus(inspector.getStatus());
		dto.setCheckList(inspector.getChecklist());
		dto.setRemarks(inspector.getRemarks());
		dto.setAddress(inspector.getAddress());
		dto.setForwardedTo(inspector.getForwardedTo());
		dto.setForwardedBy(inspector.getForwardedBy());
		dto.setIntId(inspector.getIntId());
		dto.setDtmCreatedOn(inspector.getDtmCreatedOn());
		dto.setStmUpdatedOn(inspector.getStmUpdatedOn());
		return dto;
	}

	public String generateCustomId(String tableName, String idName) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GenerateCustomID")
                .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
                .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
                .registerStoredProcedureParameter(3, String.class, ParameterMode.OUT)
                .setParameter(1, tableName)
                .setParameter(2, idName);

        query.execute();
        return (String) query.getOutputParameterValue(3);
    }
	
	@Override
	@Transactional
	public String createInspector(String inspectorData) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(inspectorData);
		try {
			inspector = objectMapper.readValue(decodedData, Inspector.class);
		} catch (Exception e) {
			throw new CustomGeneralException("Invalid data format: " + e);
		}
		inspector.setChecklist(inspector.getChecklist().stream().map(this::saveFile).toList());
		inspector.setStatus(Status.Pending);
		inspector.setForwardedTo(Stakeholder.CEO);
		inspector.setForwardedBy(Stakeholder.APPLICANT);
		inspector.setCurrentAction(Action.Pending);
		
		if (inspector.getIntId() == null) {
            String customId = generateCustomId("t_inspector", "intId");
            inspector.setIntId(customId);
            System.out.println(inspector.getIntId());
        }

		Inspector inspectorRes = inspectorRepository.save(inspector);
		System.out.println(inspectorRes);
		String generatedId = inspectorRes.getIntId();

		return CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(generatedId)).toString();

	}

	private CheckList saveFile(CheckList checkList) {
		checkList.setFilePath(JsonFileExtractorUtil.uploadFile(checkList.getFilePath(),
				FolderAndDirectoryConstant.ONBOARDING_EXTERNAL_INSPECTOR_FOLDER));
		checkList.setInspector(this.inspector);
		return checkList;
	}
	
	@Override
	public Page<InspectorDTO> getPendingApplicationsForCeo(Date fromDate, Date toDate, Status status, Action action, Stakeholder stakeholder, String search, Stakeholder forwardedTo, Pageable pageable) {
	    List<Status> statuses = Arrays.asList(Status.Pending, Status.InProgress);
	    List<Stakeholder> stakeholders = Arrays.asList(Stakeholder.CEO, Stakeholder.CEO_SECOND);
	    return inspectorRepository.findFilteredApplications(fromDate, toDate, status, action, stakeholder, search, forwardedTo, statuses, stakeholders, pageable)
	                              .map(this::convertToDto);
	}

	@Override
	public Page<InspectorDTO> getPendingApplicationsForOicLegal(Date fromDate, Date toDate, Status status, Action action, Stakeholder stakeholder, String search, Stakeholder forwardedTo, Pageable pageable) {
	    return inspectorRepository.findFilteredApplications(fromDate, toDate, Status.InProgress, action, Stakeholder.OIC_LEGAL, search, Stakeholder.OIC_LEGAL, null, null, pageable)
	                              .map(this::convertToDto);
	}


	@Override
	public Page<InspectorDTO> getPendingApplicationsForApprover(Date fromDate, Date toDate, Status status, Action action, Stakeholder stakeholder, String search, Stakeholder forwardedTo, Pageable pageable) {
	    return inspectorRepository.findFilteredApplications(fromDate, toDate, Status.InProgress, action, Stakeholder.APPROVER, search, Stakeholder.APPROVER, null, null, pageable)
	                              .map(this::convertToDto);
	}


	@Override
	public Page<InspectorDTO> findApplicationsByStatusAndForwardedTo(Date fromDate, Date toDate, String search, Status status, Stakeholder forwardedTo, Pageable pageable) {
	    return inspectorRepository.findFilteredApplications(fromDate, toDate, status, null, null, search, forwardedTo, null, null, pageable)
	                              .map(this::convertToDto);
	}


	@Override
	public Page<InspectorDTO> findApplicationsByActionAndForwardedTo(Date fromDate, Date toDate, String search, Action action, Stakeholder forwardedTo, Pageable pageable) {
	    return inspectorRepository.findFilteredApplications(fromDate, toDate, null, action, null, search, forwardedTo, null, null, pageable)
	                              .map(this::convertToDto);
	}

	@Override
	@Transactional
	public String handleCeoAction(String ceoActionRequestData) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(ceoActionRequestData);
		StakeHolderActionRequestData ceoActionRequest;
		try {
			ceoActionRequest = objectMapper.readValue(decodedData, StakeHolderActionRequestData.class);
			System.out.println(ceoActionRequest);
		} catch (Exception e) {
			throw new CustomGeneralException("Invalid data format: " + e);
		}

		Inspector inspector = inspectorRepository.findById(ceoActionRequest.getId())
				.orElseThrow(() -> new RuntimeException("Inspector not found"));

		Remark remark = new Remark();
		remark.setInspector(inspector);
		remark.setRemark(ceoActionRequest.getRemark());
		remark.setCreatedBy(ceoActionRequest.getCreatedBy());
		remark.setStakeholder(Stakeholder.CEO);
		remarkRepository.save(remark);

		if ("forward".equalsIgnoreCase(ceoActionRequest.getAction())) {
			inspector.setForwardedTo(Stakeholder.OIC_LEGAL);
			inspector.setForwardedBy(Stakeholder.CEO);
			inspector.setCurrentAction(Action.Forwarded);
			inspector.setStatus(Status.InProgress);
		} else if ("reject".equalsIgnoreCase(ceoActionRequest.getAction())) {
			inspector.setStatus(Status.Rejected);
			inspector.setForwardedTo(Stakeholder.APPLICANT);
			inspector.setCurrentAction(Action.Rejected);
		}

		return CommonUtil
				.inputStreamEncoder(objectMapper.writeValueAsString(inspectorRepository.save(inspector).getIntId()))
				.toString();
	}

	@Override
	@Transactional
	public String handleOicLegalAction(String oicLegalActionRequestData) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(oicLegalActionRequestData);
		StakeHolderActionRequestData oicLegalActionRequest;
		try {
			oicLegalActionRequest = objectMapper.readValue(decodedData, StakeHolderActionRequestData.class);
			System.out.println(oicLegalActionRequest);
		} catch (Exception e) {
			throw new CustomGeneralException("Invalid data format: " + e);
		}

		Inspector inspector = inspectorRepository.findById(oicLegalActionRequest.getId())
				.orElseThrow(() -> new RuntimeException("Inspector not found"));

		Remark remark = new Remark();
		remark.setInspector(inspector);
		remark.setRemark(oicLegalActionRequest.getRemark());
		remark.setStakeholder(Stakeholder.OIC_LEGAL);
		remark.setCreatedBy(oicLegalActionRequest.getCreatedBy());
		System.out.println(oicLegalActionRequest.getCreatedBy());
		remarkRepository.save(remark);

		if ("forward".equalsIgnoreCase(oicLegalActionRequest.getAction())) {
			inspector.setForwardedTo(Stakeholder.APPROVER);
			inspector.setForwardedBy(Stakeholder.OIC_LEGAL);
			inspector.setCurrentAction(Action.Forwarded);
			inspector.setStatus(Status.InProgress);
		} else if ("reject".equalsIgnoreCase(oicLegalActionRequest.getAction())) {
			inspector.setStatus(Status.InProgress);
			inspector.setForwardedTo(Stakeholder.CEO);
			inspector.setCurrentAction(Action.OnHold);
		}

		return CommonUtil
				.inputStreamEncoder(objectMapper.writeValueAsString(inspectorRepository.save(inspector).getIntId()))
				.toString();
	}

	@Override
	@Transactional
	public String handleApproverAction(String approverActionRequestData) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(approverActionRequestData);
		StakeHolderActionRequestData approverActionRequest;
		try {
			approverActionRequest = objectMapper.readValue(decodedData, StakeHolderActionRequestData.class);
			System.out.println(approverActionRequest);
		} catch (Exception e) {
			throw new CustomGeneralException("Invalid data format: " + e);
		}

		Inspector inspector = inspectorRepository.findById(approverActionRequest.getId())
				.orElseThrow(() -> new RuntimeException("Inspector not found"));

		Remark remark = new Remark();
		remark.setInspector(inspector);
		remark.setRemark(approverActionRequest.getRemark());
		remark.setCreatedBy(approverActionRequest.getCreatedBy());
		remark.setStakeholder(Stakeholder.APPROVER);

		remarkRepository.save(remark);

		if ("forwardToCeoSecond".equalsIgnoreCase(approverActionRequest.getAction())) {
			inspector.setForwardedTo(Stakeholder.CEO_SECOND);
			inspector.setForwardedBy(Stakeholder.APPROVER);
			inspector.setCurrentAction(Action.Forwarded);
			inspector.setStatus(Status.InProgress);
		} else if ("reject".equalsIgnoreCase(approverActionRequest.getAction())) {
			inspector.setStatus(Status.InProgress);
			inspector.setForwardedTo(Stakeholder.OIC_LEGAL);
			inspector.setCurrentAction(Action.OnHold);
		}

		return CommonUtil
				.inputStreamEncoder(objectMapper.writeValueAsString(inspectorRepository.save(inspector).getIntId()))
				.toString();
	}

	@Override
	@Transactional
	public String handleCeoSecondAction(String ceoSecondActionRequestData) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(ceoSecondActionRequestData);
		StakeHolderActionRequestData ceoSecondActionRequest;
		try {
			ceoSecondActionRequest = objectMapper.readValue(decodedData, StakeHolderActionRequestData.class);
			System.out.println(ceoSecondActionRequest);
		} catch (Exception e) {
			throw new CustomGeneralException("Invalid data format: " + e);
		}

		Inspector inspector = inspectorRepository.findById(ceoSecondActionRequest.getId())
				.orElseThrow(() -> new RuntimeException("Inspector not found"));

		Remark remark = new Remark();
		remark.setInspector(inspector);
		remark.setRemark(ceoSecondActionRequest.getRemark());
		remark.setCreatedBy(ceoSecondActionRequest.getCreatedBy());
		remark.setStakeholder(Stakeholder.CEO);

		remarkRepository.save(remark);

		if ("approve".equalsIgnoreCase(ceoSecondActionRequest.getAction())) {
			inspector.setForwardedTo(Stakeholder.APPLICANT);
			inspector.setCurrentAction(Action.Approved);
			inspector.setStatus(Status.Approved);
			// Create and associate the admin user(To be changed)
			Tuser adminUser = createAdminUserFromInspector(inspector, ceoSecondActionRequest);
			inspector.setAdminUser(adminUser);
		}
		return CommonUtil
				.inputStreamEncoder(objectMapper.writeValueAsString(inspectorRepository.save(inspector).getIntId()))
				.toString();
	}

	private Tuser createAdminUserFromInspector(Inspector inspector, StakeHolderActionRequestData requestData) {
		Tuser adminUser = new Tuser();
		adminUser.setTxtFullName(inspector.getName());
		adminUser.setTxtEmailId(inspector.getEmail());
		adminUser.setTxtMobileNo(inspector.getMobileNumber());
		adminUser.setTxtrAddress(inspector.getAddress());
		adminUser.setSelRole(userIdConstants.getInspector()); 
		adminUser.setTxtUserId(inspector.getEmail());
		adminUser.setTxtPassword(CommonUtil.getHmacMessage("Inspector@123"+9763)); 
		return adminUser;
	}
}
