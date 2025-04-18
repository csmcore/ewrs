package app.ewarehouse.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import app.ewarehouse.dto.ComplaintDetailsComprehensive;
import app.ewarehouse.dto.ComplaintmanagementResponse;
import app.ewarehouse.dto.NewComplaintManagementDTO;
import app.ewarehouse.dto.UserDetailsResponseDto;
import app.ewarehouse.entity.ApplicationOfConformity;
import app.ewarehouse.entity.ApplicationOfConformityLocationDetails;
import app.ewarehouse.entity.ApproverStatus;
import app.ewarehouse.entity.Commodity;
import app.ewarehouse.entity.ComplaintApplicationStatus;
import app.ewarehouse.entity.ComplaintIcm;
import app.ewarehouse.entity.ComplaintManagementNewEntity;
import app.ewarehouse.entity.ComplaintMgmtInspScheduleDocs;
import app.ewarehouse.entity.ComplaintObservation;
import app.ewarehouse.entity.ComplaintObservationResponse;
import app.ewarehouse.entity.ComplaintType;
import app.ewarehouse.entity.Complaint_managment;
import app.ewarehouse.entity.ConformityMain;
import app.ewarehouse.entity.County;
import app.ewarehouse.entity.OnlineServiceApprovalNotings;
import app.ewarehouse.entity.OperatorLicenceEntity;
import app.ewarehouse.entity.RevocateICM;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.SubCounty;
import app.ewarehouse.entity.Supporting_document;
import app.ewarehouse.entity.TOnlineServiceApplicationH;
import app.ewarehouse.entity.TOnlineServiceApproval;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.master.repository.WardMasterRepo;
import app.ewarehouse.repository.ApplicationConformityLocDetRepository;
import app.ewarehouse.repository.ApplicationOfConformityRepository;
import app.ewarehouse.repository.ComplaintIcmRepository;
import app.ewarehouse.repository.ComplaintManagementNewRepository;
import app.ewarehouse.repository.ComplaintMgmtInspScheduleDocsRepository;
import app.ewarehouse.repository.ComplaintMgmtInspScheduleRepository;
import app.ewarehouse.repository.ComplaintObservationRepository;
import app.ewarehouse.repository.ComplaintObservationResponseRepository;
import app.ewarehouse.repository.ComplaintTypeRepository;
import app.ewarehouse.repository.Complaint_managmentRepository;
import app.ewarehouse.repository.ConformityMainRepository;
import app.ewarehouse.repository.CountyRepository;
import app.ewarehouse.repository.OnlineServiceApprovalNotingsRepository;
import app.ewarehouse.repository.Operator_LicenceRepository;
import app.ewarehouse.repository.RevocateICMRepository;
import app.ewarehouse.repository.SubCountyRepository;
import app.ewarehouse.repository.Supporting_documentRepository;
import app.ewarehouse.repository.TOnlineServiceApplicationHRepository;
import app.ewarehouse.repository.TOnlineServiceApprovalRepository;
import app.ewarehouse.repository.TSetAuthorityRepository;
import app.ewarehouse.repository.TapplicationOfCertificateOfComplianceRepository;
import app.ewarehouse.repository.TmenulinksRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.Complaint_managmentService;
import app.ewarehouse.service.TuserService;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.FnAuthority;
import app.ewarehouse.util.Mapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Transactional
@Service

public class Complaint_managmentServiceImpl implements Complaint_managmentService {
	@Autowired
	private Complaint_managmentRepository complaint_managmentRepository;
	@Autowired
	TapplicationOfCertificateOfComplianceRepository repo;
	@Autowired
	EntityManager entityManager;
	@Autowired
	private ComplaintObservationResponseRepository complaintObservationResponseRepository;
	@Autowired
	private ApplicationOfConformityRepository applicationOfConformityRepository;

	@Autowired
	ErrorMessages errorMessages;

	@Autowired
	ComplaintObservationRepository observationRepo;
	@Autowired
	TOnlineServiceApprovalRepository tOnlineServiceApprovalRepository;
	@Autowired
	private TOnlineServiceApplicationHRepository tOnlineServiceApplicationHRepository;
	@Autowired
	TmenulinksRepository tmenulinksRepository;
	@Autowired
	TSetAuthorityRepository tSetAuthorityRepository;
	@Autowired
	private FnAuthority fnAuthority;
	@Autowired
	private TuserRepository tuserRepository;

	@Autowired
	private CountyRepository countyRepo;

	@Autowired
	private SubCountyRepository subCountyRepo;

	@Autowired
	private ApplicationOfConformityRepository appRepo;

	@Autowired
	private ComplaintTypeRepository ctRepo;

	@Autowired
	private ComplaintMgmtInspScheduleDocsRepository docRepo;

	@Autowired
	private OnlineServiceApprovalNotingsRepository onlineServiceApprovalNotingsRepository;

	@Autowired
	private ComplaintObservationResponseRepository resRepo;

	@Autowired
	Operator_LicenceRepository newoperatorLicenceRepository;

	@Autowired
	WardMasterRepo wardmasterRepo;
	@Autowired
	ComplaintManagementNewRepository newcomplaint_managmentRepository;
	@Autowired
	private TuserService tuserService;
	@Autowired
	private ComplaintMgmtInspScheduleRepository compMgmtRepo;
	public static final String PENDING_AUTHORITIES = "pendingAuthorities";
	public static final String STAGENO = "stageNo";
	public static final String ATAPROCESSID = "intATAProcessId";
	@Autowired
	private ObjectMapper om;
	@Autowired
	private Supporting_documentRepository supporting_documentRepository;
	@Autowired
	WorkflowServiceImpl workflowServiceImpl;
	@Autowired
	private ComplaintIcmRepository complaintIcmRepository;
	@Autowired
	ApplicationConformityLocDetRepository confirmityLocDetails;
	@Autowired
	ConformityMainRepository conformityMainRepository;
	
	@Autowired
	private RevocateICMRepository revocateIcmRepository;

	JSONObject selCounty = new JSONObject("{'1':'County1','2':'County2','3':'County3'}");
	JSONObject selSubCounty = new JSONObject("{'1':'Sub County1','2':'Sub County2','3':'Sub County3'}");
	JSONObject rdoComplaintAgainst = new JSONObject(
			"{'100':'Warehouse Operator License','101':'Warehouse Operator Certificate','102':'Collateral Manager','103':'Inspector','104':'Grader'}");
	JSONObject selCountyofWarehouse = new JSONObject(
			"{'1':'County of Warehouse1','2':'County of Warehouse2','3':'County of Warehouse3'}");
	JSONObject selSubCountyofWarehouse = new JSONObject(
			"{'1':'Sub-County of Warehouse1','2':'Sub-County of Warehouse2','3':'Sub-County of Warehouse3'}");
	JSONObject selWarehouseShopName = new JSONObject(
			"{'1':'Warehouse Shop Name1','2':'Warehouse Shop Name2','3':'Warehouse Shop Name3'}");
	JSONObject selNameofGrader = new JSONObject(
			"{'1':' Name of Grader1','2':' Name of Grader2','3':' Name of Grader3'}");
	JSONObject selNameofCollateralManager = new JSONObject(
			"{'1':'Name of Collateral Manager1','2':'Name of Collateral Manager2','3':'Name of Collateral Manager3'}");
	JSONObject selNameofInspector = new JSONObject(
			"{'1':'Name of Inspector1','2':'Name of Inspector2','3':'Name of Inspector3'}");
	JSONObject selTypeofComplain = new JSONObject(
			"{'1':'Type of Complain1','2':'Type of Complain2','3':'Type of Complain3'}");

	Integer parentId = 0;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(Complaint_managmentServiceImpl.class);
	JSONObject json = new JSONObject();
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${finalUpload.path}")
	private String finalUploadPath;

	@Override
	public JSONObject save(String data) throws Exception {
		ComplaintManagementNewEntity saveData = null;
		Integer processId = 160; // it important id complete flow
		logger.info("Inside save method of Complaint_managmentServiceImpl");
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Complaint_managment complaint_managment = om.readValue(data, Complaint_managment.class);
		List<String> fileUploadList = new ArrayList<String>();
		if (!Objects.isNull(complaint_managment.getIntId()) && complaint_managment.getIntId() > 0) {
			ComplaintManagementNewEntity newcomp = newcomplaint_managmentRepository
					.findByIdAndCreatedByAndDeletedFlagFalse(complaint_managment.getIntId(),
							complaint_managment.getIntCreatedBy());
			List<Map<String, Object>> mapObjlist = workflowServiceImpl.getAllComplaintStatus();
			String appicationStatus = mapObjlist.stream().filter(x -> Integer.parseInt(x.get("intId").toString()) == 15)
					.collect(Collectors.toList()).get(0).get("complaintStatus").toString();

			int complaint_aginast_id = Optional.ofNullable(complaint_managment.getRdoComplaintAgainst()).orElse(0);
			int complaint_type_id = Integer.parseInt(complaint_managment.getSelTypeofComplain());

			if (complaint_aginast_id == 101) {

				if (complaint_type_id == 20) {
					newcomp.setComplaintAgainst(101);
				} else if (complaint_type_id == 21) {
					newcomp.setComplaintAgainst(100);
				}

			} else {

				newcomp.setComplaintAgainst(complaint_aginast_id);
			}

			newcomp.setUserId(complaint_managment.getIntCreatedBy());
			newcomp.setComplaintType(complaint_type_id);

			if (complaint_aginast_id == 100) {
				newcomp.setComplaintAgainstUserId(Integer.parseInt(complaint_managment.getSelWarehouseShopName()));
			} else if (complaint_aginast_id == 101) {
				newcomp.setComplaintAgainstUserId(Integer.parseInt(complaint_managment.getSelWarehouseShopName()));
			} else if (complaint_aginast_id == 102) {

				Tuser user = tuserRepository.findByIntIdAndBitDeletedFlag(
						Integer.valueOf(complaint_managment.getSelNameofCollateralManager()), false);

				user.setSelCounty(complaint_managment.getSelCountyofWarehouse());
				user.setSelSubCounty(complaint_managment.getSelSubCountyofWarehouse());
				user.setSelWard(complaint_managment.getSelWardofWarehouse());
				tuserRepository.save(user);
				newcomp.setComplaintAgainstUserId(
						Integer.parseInt(complaint_managment.getSelNameofCollateralManager()));
			} else if (complaint_aginast_id == 103) {
				newcomp.setComplaintAgainstUserId(Integer.parseInt(complaint_managment.getSelNameofInspector()));
			} else if (complaint_aginast_id == 104) {
				newcomp.setComplaintAgainstUserId(Integer.parseInt(complaint_managment.getSelNameofGrader()));
			}

			newcomp.setComplaintType(Integer.parseInt(complaint_managment.getSelTypeofComplain()));
			newcomp.setIncidentDate(complaint_managment.getTxtDateofIncident());
			newcomp.setDescriptionIncident(complaint_managment.getTxtrDescriptionofIncident());
			newcomp.setActionOnApplication("noAction");
			newcomp.setActionCondition("noCondition");
			newcomp.setCreatedOn(LocalDateTime.now());
			newcomp.setApplicationStatus(appicationStatus);
			newcomp.setCreatedBy(complaint_managment.getIntCreatedBy());
			newcomp.setComplaintNo(newcomp.getComplaintNo());
			saveData = newcomplaint_managmentRepository.saveAndFlush(newcomp);
			// saveData = complaint_managmentRepository.save(getEntity);

			if (saveData == null) {
				// throw new IllegalArgumentException("saveData cannot be null");
				json.put("complaintNo", 0);
				json.put("message", "Record is not Updated");
				json.put("status", 203);
			}

			parentId = saveData.getId();
			json.put("complaintNo", newcomp.getComplaintNo());
			json.put("message", "Record is Updated");
			json.put("status", 202);
			supporting_documentRepository.deleteAllByIntParentId(parentId);
			
		} else {

			ComplaintManagementNewEntity newcomp = new ComplaintManagementNewEntity();
			List<Map<String, Object>> mapObjlist = workflowServiceImpl.getAllComplaintStatus();
			String appicationStatus = mapObjlist.stream().filter(x -> Integer.parseInt(x.get("intId").toString()) == 15)
					.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
			complaint_managment.setVchActionOnApplication("noAction");
			complaint_managment.setActionCondition("noCondition");
			complaint_managment.setApplicationStatus(appicationStatus);
			String compNo = getComplaintNo("WRS");
			complaint_managment.setComplaintNo(compNo);

			int complaint_aginast_id = Optional.ofNullable(complaint_managment.getRdoComplaintAgainst()).orElse(0);
			int complaint_type_id = Integer.parseInt(complaint_managment.getSelTypeofComplain());

			if (complaint_aginast_id == 101) {

				if (complaint_type_id == 20) {
					newcomp.setComplaintAgainst(101);
				} else if (complaint_type_id == 21) {
					newcomp.setComplaintAgainst(100);
				}
			} else {

				newcomp.setComplaintAgainst(complaint_aginast_id);
			}

			newcomp.setUserId(complaint_managment.getIntCreatedBy());
			newcomp.setComplaintType(complaint_type_id);

			if (complaint_aginast_id == 100) {
				newcomp.setComplaintAgainstUserId(Integer.parseInt(complaint_managment.getSelWarehouseShopName()));
			} else if (complaint_aginast_id == 101) {
				newcomp.setComplaintAgainstUserId(Integer.parseInt(complaint_managment.getSelWarehouseShopName()));
			} else if (complaint_aginast_id == 102) {
				Tuser user = tuserRepository.findByIntIdAndBitDeletedFlag(
						Integer.valueOf(complaint_managment.getSelNameofCollateralManager()), false);
				user.setSelCounty(complaint_managment.getSelCountyofWarehouse());
				user.setSelSubCounty(complaint_managment.getSelSubCountyofWarehouse());
				user.setSelWard(complaint_managment.getSelWardofWarehouse());
				tuserRepository.save(user);
				newcomp.setComplaintAgainstUserId(
						Integer.parseInt(complaint_managment.getSelNameofCollateralManager()));
				newcomp.setComplaintAgainstUserId(
						Integer.parseInt(complaint_managment.getSelNameofCollateralManager()));
			} else if (complaint_aginast_id == 103) {
				newcomp.setComplaintAgainstUserId(Integer.parseInt(complaint_managment.getSelNameofInspector()));
			} else if (complaint_aginast_id == 104) {
				newcomp.setComplaintAgainstUserId(Integer.parseInt(complaint_managment.getSelNameofGrader()));
			}

			newcomp.setComplaintType(Integer.parseInt(complaint_managment.getSelTypeofComplain()));
			newcomp.setIncidentDate(complaint_managment.getTxtDateofIncident());
			newcomp.setDescriptionIncident(complaint_managment.getTxtrDescriptionofIncident());
			newcomp.setActionOnApplication("noAction");
			newcomp.setActionCondition("noCondition");
			newcomp.setCreatedOn(LocalDateTime.now());
			newcomp.setApplicationStatus(appicationStatus);
			newcomp.setCreatedBy(complaint_managment.getIntCreatedBy());
			newcomp.setComplaintNo(compNo);
			saveData = newcomplaint_managmentRepository.saveAndFlush(newcomp);
			// saveData = complaint_managmentRepository.save(complaint_managment);
			if (saveData == null) {
				// throw new IllegalArgumentException("saveData cannot be null");
				json.put("complaintNo", "Appication is not submitted");
				json.put("status", 201);
			}
			parentId = saveData.getId();
			json.put("parentId", parentId);
			json.put("complaintNo", saveData.getComplaintNo());
			json.put("status", 200);
		}

		if (saveData != null) {

			List<Supporting_document> supporting_documentList = complaint_managment.getAddMoreSupportingDocuments();
			supporting_documentList.forEach(t -> {
				t.setIntParentId(parentId);
				fileUploadList.add(t.getAmfileUploadDocuments());
			});
			supporting_documentRepository.saveAll(supporting_documentList);
			fileUploadList.forEach(fileUpload -> {
				if (!Strings.isNullOrEmpty(fileUpload)) {

					File f = new File(tempUploadPath + fileUpload);
					if (f.exists()) {
						File src = new File(tempUploadPath + fileUpload);
						Path path = Paths.get(finalUploadPath + "complaint-management");

						if (!Files.exists(path) && !Files.isDirectory(path)) {
							try {
								Files.createDirectories(path);
							} catch (IOException e) {
								logger.info("Inside getById method of Complaint_managmentServiceImpl" + e.getMessage());

							}
						}

						try {
							File dest = new File(finalUploadPath + "complaint-management/" + fileUpload);
							Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
							Files.delete(src.toPath());
						} catch (IOException e) {
							logger.error(
									"Inside save method of Complaint_managmentServiceImpl some error occur in file moving to destination folder:"
											+ e);
						}

						/*
						 * File f = new File(tempUploadPath + fileUpload); if (f.exists()) { File src =
						 * new File(tempUploadPath + fileUpload); File dest = new File(finalUploadPath +
						 * "complaint-management/" + fileUpload); try { Files.copy(src.toPath(),
						 * dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
						 * Files.delete(src.toPath()); } catch (IOException e) { logger.error(
						 * "Inside save method of Complaint_managmentServiceImpl some error occur in file moving to destination folder:"
						 * + e); } }
						 */
					}
				}
			});

		}

		// FOR WORKFLOW

		int complaint_aginast_id = Optional.ofNullable(saveData.getComplaintAgainst()).orElse(0);
		int complaint_type_id = saveData.getComplaintType();

		Integer intLabelId = 0;

		if (complaint_aginast_id == 101 || complaint_aginast_id == 100) {

			if (complaint_type_id == 20) {
				intLabelId = 101;
			} else if (complaint_type_id == 21) {
				intLabelId = 100;
			}

		} else if (complaint_aginast_id == 102) {
			intLabelId = 102;
		} else if (complaint_aginast_id == 103) {
			intLabelId = 103;
		} else if (complaint_aginast_id == 104) {
			intLabelId = 104;
		}

		List<Map<String, Object>> mapObjlist = workflowServiceImpl.getAllComplaintStatus();
		String appicationStatus = mapObjlist.stream().filter(x -> Integer.parseInt(x.get("intId").toString()) == 13)
				.collect(Collectors.toList()).get(0).get("complaintStatus").toString();

		JSONObject tsetAuthority = fnAuthority.getAuthority(processId, 1, intLabelId);
		if (tsetAuthority.length() > 0) {

			String pendingAt = tsetAuthority.getString(PENDING_AUTHORITIES);
			int stageNo = tsetAuthority.getInt(STAGENO);
			int intATAProcessId = tsetAuthority.getInt(ATAPROCESSID);
			TOnlineServiceApproval tOnlineServiceApprovalDelete = tOnlineServiceApprovalRepository.getByIntOnlineServiceIdAndIntProcessIdAndBitDeletedFlag(parentId, processId, false);
			if (tOnlineServiceApprovalDelete != null) {
				tOnlineServiceApprovalRepository.deleteByIntOnlineServiceIdAndIntProcessIdAndBitDeletedFlag(parentId, processId, false);
				//tOnlineServiceApprovalDelete.setBitDeletedFlag(true);
				//tOnlineServiceApprovalRepository.save(tOnlineServiceApprovalDelete);
			}
			
			OnlineServiceApprovalNotings notings =onlineServiceApprovalNotingsRepository.findByIntOnlineServiceIdAndBitDeletedFlag(parentId,false);
			
			if(notings!=null) {
				 onlineServiceApprovalNotingsRepository.deleteNotings(parentId,processId);
			}
			
			TOnlineServiceApproval tOnlineServiceApproval = new TOnlineServiceApproval();
			tOnlineServiceApproval.setIntOnlineServiceId(parentId);
			tOnlineServiceApproval.setIntStageNo(stageNo);
			tOnlineServiceApproval.setIntProcessId(processId);
			tOnlineServiceApproval.setIntPendingAt(pendingAt);
			tOnlineServiceApproval.setIntATAProcessId(intATAProcessId);
			tOnlineServiceApproval.setIntLabelId(intLabelId);
			tOnlineServiceApproval.setVchForwardAllAction(appicationStatus);
			tOnlineServiceApprovalRepository.save(tOnlineServiceApproval);
			
			OnlineServiceApprovalNotings newNotings = new OnlineServiceApprovalNotings();

			newNotings.setIntOnlineServiceId(parentId);
			newNotings.setIntProcessId(processId);
			newNotings.setIntFromAuthority(complaint_managment.getIntCreatedByRole());
			newNotings.setIntToAuthority(pendingAt);
			newNotings.setIntStatus(10);
			newNotings.setTxtNoting("Complaint has been submitted.");
			newNotings.setTinStageCtr(stageNo);
			newNotings.setIntCreatedBy(complaint_managment.getIntCreatedBy());
			newNotings.setIntActionTakenId(10);
			newNotings.setVchActionTaken("Complaint has been submitted.");
			onlineServiceApprovalNotingsRepository.save(newNotings);
			 			

		}

		json.put("processId", processId);
		json.put("labelId", intLabelId);
		json.put("id", parentId);
		return json;
	}

	private String getComplaintNo(String id) {
		String dbCurrentId = newcomplaint_managmentRepository.getId();
		if (dbCurrentId == null) {
			return id + "100000";
		} else {
			Integer idNum = Integer.parseInt(dbCurrentId.substring(3, dbCurrentId.length()));
			AtomicInteger seq = new AtomicInteger(idNum);
			int nextVal = seq.incrementAndGet();
			return id + nextVal;
		}
	}

	@Override
	public JSONObject getById(Integer id) throws Exception {
		logger.info("Inside getById method of Complaint_managmentServiceImpl");

		// Complaint_managment entity =
		// complaint_managmentRepository.findByIntIdAndBitDeletedFlag(id, false);
		NewComplaintManagementDTO newcomplaintDto = new NewComplaintManagementDTO();
		try {
			
			ComplaintManagementNewEntity entity = newcomplaint_managmentRepository.findByIdAndDeletedFlag(id, false);

			

			newcomplaintDto.setIntId(entity.getId());
			newcomplaintDto.setComplaintNo(entity.getComplaintNo());
			Integer userIdValue = entity.getUserId();
			Integer compuserIdValue = entity.getComplaintAgainstUserId();

			UserDetailsResponseDto userdto = tuserService.getUserDetails(userIdValue);

			newcomplaintDto.setTxtFullName(userdto.getFullName());
			newcomplaintDto.setTxtrAddress(userdto.getAddress());
			newcomplaintDto.setTxtContactNumber(userdto.getContactNumber());
			newcomplaintDto.setTxtEmailAddress(userdto.getEmail());
			newcomplaintDto.setSelCounty(entity.getUser().getSelCounty());
			newcomplaintDto.setSelSubCounty(entity.getUser().getSelSubCounty());
			newcomplaintDto.setSelCountyVal(userdto.getCountyName());
			newcomplaintDto.setSelSubCountyVal(userdto.getSubCountyName());
			newcomplaintDto.setSelWardVal(userdto.getWardName());
			newcomplaintDto.setRdoComplaintAgainst(entity.getComplaintAgainst());

			dynamicValue = (!Objects.isNull(entity.getComplaintAgainst())
					&& rdoComplaintAgainst.has(entity.getComplaintAgainst().toString()))
							? rdoComplaintAgainst.get(entity.getComplaintAgainst().toString())
							: "--";

			newcomplaintDto.setRdoComplaintAgainstVal((String) dynamicValue);

			UserDetailsResponseDto complaintuserdto = tuserService.getUserDetails(compuserIdValue);
			newcomplaintDto.setComplaintAgainstUserId(compuserIdValue);
			newcomplaintDto.setApplicationStatus(entity.getApplicationStatus());
			newcomplaintDto.setTxtDateofIncident(entity.getIncidentDate());
			newcomplaintDto.setSelCountyofWarehouse(Integer.parseInt(complaintuserdto.getCountyId()));
			newcomplaintDto.setSelCountyofWarehouseVal(complaintuserdto.getCountyName());
			newcomplaintDto.setSelSubCountyofWarehouse(Integer.parseInt(complaintuserdto.getSubCountyId()));
			newcomplaintDto.setSelSubCountyofWarehouseVal(complaintuserdto.getSubCountyName());
			newcomplaintDto.setSelWardofWarehouse(Integer.valueOf(complaintuserdto.getWardId()));
			newcomplaintDto.setSelWardofWarehouseVal(complaintuserdto.getWardName());
			newcomplaintDto.setTxtrDescriptionofIncident(entity.getDescriptionIncident());

			if (entity.getComplaintAgainst() == 100) {
				ApplicationOfConformityLocationDetails locationDetails = confirmityLocDetails
						.findByWarehouseId(entity.getUser().getWarehouseId());
				newcomplaintDto.setTxtWarehouseOperator(entity.getUser().getTxtFullName());
				newcomplaintDto.setSelWarehouseShopName(entity.getComplaintAgainstUserId());
				newcomplaintDto.setSelWarehouseShopNameVal(locationDetails.getWarehouseName());
			} else if (entity.getComplaintAgainst() == 101) {
				ApplicationOfConformityLocationDetails locationDetails = confirmityLocDetails
						.findByWarehouseId(entity.getUser().getWarehouseId());
				newcomplaintDto.setTxtWarehouseOperator(entity.getUser().getTxtFullName());
				newcomplaintDto.setSelWarehouseShopName(entity.getComplaintAgainstUserId());
				newcomplaintDto.setSelWarehouseShopNameVal(locationDetails.getWarehouseName());
			} else if (entity.getComplaintAgainst() == 102) {
				newcomplaintDto.setSelNameofCollateralManager(entity.getComplaintAgainstUserId());
				newcomplaintDto.setSelNameofCollateralManagerVal(complaintuserdto.getFullName());
			} else if (entity.getComplaintAgainst() == 103) {
				newcomplaintDto.setSelNameofInspector(entity.getComplaintAgainstUserId());
				newcomplaintDto.setSelNameofInspectorVal(complaintuserdto.getFullName());
			} else if (entity.getComplaintAgainst() == 104) {

				newcomplaintDto.setSelNameofGrader(entity.getComplaintAgainstUserId());
				newcomplaintDto.setSelNameofGraderVal(complaintuserdto.getFullName());
			}

			if (!entity.getComplaintType().toString().equals("0")) {
				ComplaintType ct = ctRepo.findById(entity.getComplaintType()).get();
				newcomplaintDto.setSelTypeofComplain(ct.getComplaintId());
				newcomplaintDto.setSelTypeofComplainVal(ct.getComplaintType());
			} else {
				newcomplaintDto.setSelTypeofComplainVal("--");
			}
			if (entity.getComplaintNo().isEmpty()) {
				newcomplaintDto.setComplaintNo("--");
			}
			if (entity.getComplaintAgainst() == 0) {
				newcomplaintDto.setComplaintNo("--");
			}

			// ComplaintType ct =
			// ctRepo.findById(Integer.parseInt(entity.getTypeofComplain())).get();
			// entity.setSelTypeofComplainVal(ct.getComplaintType());
			List<Supporting_document> supporting_documentList = supporting_documentRepository
					.findByIntParentIdAndBitDeletedFlag(id, false);
			newcomplaintDto.setAddMoreSupportingDocuments(supporting_documentList);
			
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}

		

		return new JSONObject(newcomplaintDto);
	}

	@Override
	public JSONObject getAll(String formParams) throws Exception {
		logger.info("Inside getAll method of Complaint_managmentServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		String txtFullName = "0";
		String txtContactNumber = "0";
		Integer intCreatedBy = 0;
		Integer totalDataPresent = 0;
		List<NewComplaintManagementDTO> allComplaintDataList = new ArrayList<NewComplaintManagementDTO>();
		try {
		
			if (jsonData.has("txtFullName") && !jsonData.isNull("txtFullName")
					&& !jsonData.getString("txtFullName").equals("")) {
				txtFullName = jsonData.getString("txtFullName");
			}
			if (jsonData.has("txtContactNumber") && !jsonData.isNull("txtContactNumber")
					&& !jsonData.getString("txtContactNumber").equals("")) {
				txtContactNumber = jsonData.getString("txtContactNumber");
			}

			if (jsonData.has("intCreatedBy") && !jsonData.isNull("intCreatedBy")
					&& !jsonData.getString("intCreatedBy").equals("")) {
				intCreatedBy = jsonData.getInt("intCreatedBy");
			}
			if (intCreatedBy == 0) {
				// totalDataPresent =
				// complaint_managmentRepository.countByBitDeletedFlagAndIntCreatedBy(false,
				// txtFullName,txtContactNumber);
				totalDataPresent = newcomplaint_managmentRepository.countSearchByDeletedFlagAndMobileAndComplaintNo(false,txtFullName, txtContactNumber,intCreatedBy);
			} else {
				//totalDataPresent = newcomplaint_managmentRepository.countByDeletedFlagAndCreatedBy(false, intCreatedBy);
				totalDataPresent = newcomplaint_managmentRepository.countSearchByDeletedFlagAndMobileAndComplaintNo(false,txtFullName, txtContactNumber,intCreatedBy);
			}
			Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
					jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent,
					Sort.by(Sort.Direction.DESC, "createdOn"));
//			List<Complaint_managment> complaint_managmentResp = complaint_managmentRepository.findAllByBitDeletedFlagAndIntInsertStatus(false, pageRequest, txtFullName, txtContactNumber,intCreatedBy);

			
			List<ComplaintManagementNewEntity> complaint_managmentResp = newcomplaint_managmentRepository.findAllComplaintByDeletedFlag(false, pageRequest, txtFullName, txtContactNumber, intCreatedBy);

			complaint_managmentResp.forEach(entity -> {
				NewComplaintManagementDTO newcomplaintDto = new NewComplaintManagementDTO();

				Integer count = onlineServiceApprovalNotingsRepository.getCountedData(12, entity.getId());
				newcomplaintDto.setNotingCount(count);
				TOnlineServiceApproval approval = tOnlineServiceApprovalRepository
						.findByIntOnlineServiceIdAndIntStageNoAndBitDeletedFlag(160, entity.getId(), false);
				if (approval != null) {
					newcomplaintDto.setTinStatus(approval.getTinStatus());
					newcomplaintDto.setIntProcessId(approval.getIntProcessId());
					newcomplaintDto.setResubmitCount(onlineServiceApprovalNotingsRepository
							.countByIntOnlineServiceIdAndIntProcessIdAndIntStatusAndBitDeletedFlag(entity.getId(),
									approval.getIntProcessId(), 3, false));
					newcomplaintDto.setStageNo(approval.getIntStageNo());

					String cm = tuserRepository.getPendignAtUser(Integer.parseInt(approval.getIntPendingAt()));
					// entity.setPendingATUser(approval.getIntPendingAt());
					newcomplaintDto.setPendingATUser(cm);
					// entity.setForwardedToUser(approval.getIntForwardTo());
					// entity.setSentFromUser(approval.getIntSentFrom().toString());
				}

				OnlineServiceApprovalNotings remarksList = onlineServiceApprovalNotingsRepository
						.getCEoOrCLCRemark(approval.getIntProcessId(), entity.getId());

				if (remarksList != null) {
					String remarks = remarksList.getTxtNoting();
					newcomplaintDto.setRemarks(remarks);
					newcomplaintDto.setSusReason(remarksList.getVchSuspensionReason());
				} else {
					newcomplaintDto.setRemarks("--");
					newcomplaintDto.setSusReason("--");
				}

				Integer userIdValue = entity.getUserId();

				newcomplaintDto.setIntId(entity.getId());
				newcomplaintDto.setComplaintNo(entity.getComplaintNo());

				Integer compuserIdValue = entity.getComplaintAgainstUserId();
				UserDetailsResponseDto userdto = tuserService.getUserDetails(userIdValue);

				newcomplaintDto.setTxtFullName(userdto.getFullName());

				dynamicValue = (!Objects.isNull(entity.getUser().getSelCounty())
						&& selCounty.has(entity.getUser().getSelCounty().toString())) ? userdto.getCountyName().toString()
								: "--";
				newcomplaintDto.setSelCountyVal((String) dynamicValue);
				dynamicValue = (!Objects.isNull(entity.getUser().getSelSubCounty())
						&& selSubCounty.has(entity.getUser().getSelSubCounty().toString()))
								? userdto.getSubCountyName().toString()
								: "--";
				newcomplaintDto.setSelSubCountyVal((String) dynamicValue);
				dynamicValue = (!Objects.isNull(entity.getComplaintAgainst())
						&& rdoComplaintAgainst.has(entity.getComplaintAgainst().toString()))
								? rdoComplaintAgainst.get(entity.getComplaintAgainst().toString())
								: "--";
				newcomplaintDto.setSelWard(Integer.parseInt(userdto.getWardId()));
				newcomplaintDto.setRdoComplaintAgainstVal((String) dynamicValue);
				newcomplaintDto.setRdoComplaintAgainst(entity.getComplaintAgainst());

				UserDetailsResponseDto complaintuserdto = tuserService.getUserDetails(compuserIdValue);
				newcomplaintDto.setComplaintAgainstUserId(compuserIdValue);
				newcomplaintDto.setApplicationStatus(entity.getApplicationStatus());
				newcomplaintDto.setTxtDateofIncident(entity.getIncidentDate());
				newcomplaintDto.setSelCountyofWarehouse(Integer.parseInt(complaintuserdto.getCountyId()));
				newcomplaintDto.setSelCountyofWarehouseVal(complaintuserdto.getCountyName());
				newcomplaintDto.setSelSubCountyofWarehouse(Integer.parseInt(complaintuserdto.getSubCountyId()));
				newcomplaintDto.setSelSubCountyofWarehouseVal(complaintuserdto.getSubCountyName());
				newcomplaintDto.setSelWardofWarehouse(Integer.valueOf(complaintuserdto.getWardId()));
				newcomplaintDto.setSelWardofWarehouseVal(complaintuserdto.getWardName());

				if (entity.getComplaintAgainst() == 100) {
					ApplicationOfConformityLocationDetails locationDetails = confirmityLocDetails
							.findByWarehouseId(entity.getUser().getWarehouseId());
					newcomplaintDto.setCompanyProfId(locationDetails.getCompany().getProfDetId());
					newcomplaintDto.setTxtWarehouseOperator(entity.getUser().getTxtFullName());
					newcomplaintDto.setSelWarehouseShopName(entity.getComplaintAgainstUserId());
					newcomplaintDto.setSelWarehouseShopNameVal(locationDetails.getWarehouseName());
					newcomplaintDto.setComplaintAgainstUserName(entity.getUser().getTxtFullName());					
							
				} else if (entity.getComplaintAgainst() == 101) {
					ApplicationOfConformityLocationDetails locationDetails = confirmityLocDetails
							.findByWarehouseId(entity.getUser().getWarehouseId());
					newcomplaintDto.setCompanyProfId(locationDetails.getCompany().getProfDetId());

					System.out.println("****************************" + locationDetails.getCompany().getProfDetId());
					newcomplaintDto.setTxtWarehouseOperator(entity.getUser().getTxtFullName());
					newcomplaintDto.setSelWarehouseShopName(entity.getComplaintAgainstUserId());
					newcomplaintDto.setSelWarehouseShopNameVal(locationDetails.getWarehouseName());
					newcomplaintDto.setComplaintAgainstUserName(entity.getUser().getTxtFullName());	
				} else if (entity.getComplaintAgainst() == 102) {
					newcomplaintDto.setSelNameofCollateralManager(entity.getComplaintAgainstUserId());
					newcomplaintDto.setSelNameofCollateralManagerVal(complaintuserdto.getFullName());
					newcomplaintDto.setComplaintAgainstUserName(complaintuserdto.getFullName());	
				} else if (entity.getComplaintAgainst() == 103) {
					newcomplaintDto.setSelNameofInspector(entity.getComplaintAgainstUserId());
					newcomplaintDto.setSelNameofInspectorVal(complaintuserdto.getFullName());
					newcomplaintDto.setComplaintAgainstUserName(complaintuserdto.getFullName());
				} else if (entity.getComplaintAgainst() == 104) {
					newcomplaintDto.setSelNameofGrader(entity.getComplaintAgainstUserId());
					newcomplaintDto.setSelNameofGraderVal(complaintuserdto.getFullName());
					newcomplaintDto.setComplaintAgainstUserName(complaintuserdto.getFullName());
				}

				if (!entity.getComplaintType().toString().equals("0")) {
					ComplaintType ct = ctRepo.findById(entity.getComplaintType()).get();
					newcomplaintDto.setSelTypeofComplainVal(ct.getComplaintType());
				} else {
					newcomplaintDto.setSelTypeofComplainVal("--");
				}
				if (entity.getComplaintNo().isEmpty()) {
					newcomplaintDto.setComplaintNo("--");
				}
				if (entity.getComplaintAgainst() == 0) {
					newcomplaintDto.setComplaintNo("--");
				}

				allComplaintDataList.add(newcomplaintDto);
			});

			if (allComplaintDataList.size() == 0) {
				totalDataPresent = 0;
			}
			
			
		}
		catch(Exception e) {
			
		}
		
		
		json.put("status", 200);
		json.put("result", new JSONArray(allComplaintDataList));
		json.put("count", totalDataPresent);
		logger.info("json contians:" + json);
		return json;
	}

	@Override
	public JSONObject getByActionWise(String data) throws Exception {
		
		logger.info("Inside getByActionWise method of Complaint_managmentServiceImpl");
		JSONObject jsonObject = new JSONObject(data);
		JSONArray jsonArray = new JSONArray();
		Integer intId = jsonObject.getInt("intId");
		Integer roleId = jsonObject.getInt("roleId");
		Integer userId = jsonObject.getInt("userId");
		JSONObject jsonData = jsonObject.getJSONObject("formData");
		String applicationStatus = jsonObject.getString("applicationStatus");
		String applicationType = jsonObject.optString("applicationType");
		String txtFullName = "0";
		String txtContactNumber = "0";
		List<Map<String, Object>> mergedContent = new ArrayList<Map<String, Object>>();
		Page<Map<String, Object>> pageobj = null;
		try {
			if (jsonData.has("txtFullName") && !jsonData.isNull("txtFullName")
					&& !jsonData.getString("txtFullName").equals("")) {
				txtFullName = jsonData.getString("txtFullName");
			}
			if (jsonData.has("txtContactNumber") && !jsonData.isNull("txtContactNumber")
					&& !jsonData.getString("txtContactNumber").equals("")) {
				txtContactNumber = jsonData.getString("txtContactNumber");
			}
			
			String vchAuths = tSetAuthorityRepository.getVchAuthsByProcessIdAndRoleIdup(intId, roleId);
			boolean isPresent = false;
			boolean isGenerate = false;
			if (vchAuths != null) {
				String[] vchAuthList = vchAuths.split(",");
				for (String num : vchAuthList) {
					int currentNumber = Integer.parseInt(num.trim());
					if (currentNumber == 13) {
						isPresent = true;
					}
					if (currentNumber == 14) {
						isGenerate = true;
					}
				}
			}
			if (isPresent && isGenerate) {
				json.put("draftStatus", 1);
			} else if (isGenerate) {
				json.put("draftStatus", 1);
			} else {
				json.put("draftStatus", 0);
			}

			
			Pageable pageRequest = PageRequest.of(jsonObject.has("pageNo") ? jsonObject.getInt("pageNo") - 1 : 0,
					jsonObject.optInt("size"));

			if (applicationStatus.equals("pending")) {

				List<Integer> intList = null;
				List<Object[]> committeeRoleList = complaint_managmentRepository.getCommitteeRoleId(userId, roleId);

				boolean allNull = true;

				boolean allNullArrays = committeeRoleList.stream().filter(Objects::nonNull) // Ignore null list elements
						.allMatch(arr -> arr == null || Arrays.stream(arr).allMatch(Objects::isNull));

				if (allNullArrays) {
					allNull = true;
				} else {
					allNull = false;
				}

				intList = new ArrayList<Integer>();
				if (!allNull) {
					intList = new ArrayList<Integer>();
					if (committeeRoleList.get(0)[0] != null) {
						Integer icmroleId = 0;
						for (Object[] roleList : committeeRoleList) {

							if (roleList[0] != null) {
								icmroleId = Integer.parseInt(roleList[0].toString());
								intList.add(icmroleId);
							}

						}
					}

					if (committeeRoleList.get(0)[1] != null) {
						Integer icmCLCRoleId = 0;
						for (Object[] roleList : committeeRoleList) {

							if (roleList[1] != null) {
								icmCLCRoleId = Integer.parseInt(roleList[1].toString());
							}

							if (icmCLCRoleId != 0) {
								intList.add(icmCLCRoleId);
							}
						}
					}

					if (intList != null && intList.size() > 0) {

						//for (Integer committeeRole : intList) {
//							  Integer appcount = complaint_managmentRepository.checkIcmApplicationAssign(committeeRole, roleId, userId);
//							  if (appcount > 0) {
//								  Page<Map<String, Object>>	committeepageobj = complaint_managmentRepository.getICMData(intId, committeeRole, roleId, userId, txtFullName,txtContactNumber, pageRequest);
//								  for (Map<String, Object> item : committeepageobj.getContent()) {
//							            mergedContent.add(item);
//							        }
//								}

//							Page<Map<String, Object>> committeepageobj = complaint_managmentRepository.getICMData(intId,committeeRole, roleId, userId, txtFullName, txtContactNumber, pageRequest);
//							
//							for (Map<String, Object> item : committeepageobj.getContent()) {
//								mergedContent.add(item);
//							}
							
							//Page<Map<String, Object>> getRevokeICMData = complaint_managmentRepository.getICMData(intId,committeeRole, roleId, userId, txtFullName, txtContactNumber, pageRequest);
							
							
						pageobj = complaint_managmentRepository.getRevokeAndICMData(intId, roleId, userId, txtFullName, txtContactNumber, pageRequest);
							
//							if(getRevokeICMData!=null&&!getRevokeICMData.isEmpty()) {
//								for (Map<String, Object> item : getRevokeICMData.getContent()) {
//									 mergedContent.add(item);
//									/*
//									 * Integer applicationId = (Integer)item.get("intId"); boolean actionTaken =
//									 * (boolean)item.get("icmActionTaken");
//									 * 
//									 * Integer RevokeCountStatus=
//									 * complaint_managmentRepository.checkIcmApplicationRevokeAssign(applicationId);
//									 * 
//									 * if(RevokeCountStatus>0) { Page<Map<String, Object>> getRevokeData =
//									 * complaint_managmentRepository.getRevokeData(intId,committeeRole, roleId,
//									 * userId,applicationId,txtFullName, txtContactNumber, pageRequest);
//									 * if(getRevokeData!=null) { for (Map<String, Object> revokeitem :
//									 * getRevokeData.getContent()) { mergedContent.add(revokeitem); } }
//									 * Page<Map<String, Object>> getRevokeAssignData
//									 * =complaint_managmentRepository.getAssignRevokeData(intId,applicationId,
//									 * txtFullName, txtContactNumber, pageRequest); for (Map<String, Object>
//									 * revoke_assign_item : getRevokeAssignData.getContent()) {
//									 * mergedContent.add(revoke_assign_item); }
//									 * 
//									 * } else { mergedContent.add(item); }
//									 */
//											
//								}	
//							}
						//}

//						if (mergedContent != null && mergedContent.size() > 0) {
//							pageobj = new PageImpl<>(mergedContent, PageRequest.of(0, mergedContent.size()),mergedContent.size());
//						}

					}
				}

				else {
					// pageobj = complaint_managmentRepository.getAllReviewData(intId, roleId,
					// userId, txtFullName,txtContactNumber, pageRequest);

					pageobj = complaint_managmentRepository.getAllReviewData(intId, userId, txtFullName, txtContactNumber,pageRequest);

				}

			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		

		List<NewComplaintManagementDTO> complaint_managmentList = new ArrayList<>();
		
		try {
			
			if (pageobj != null && !pageobj.isEmpty()) {
				for (Map<String, Object> obdata : pageobj) {
					JSONObject jsonOb = new JSONObject();
					jsonOb.put("intId", (Integer) obdata.get("intId"));
					jsonOb.put("dtmCreatedOn", obdata.get("dtmCreatedOn"));
					jsonOb.put("dtmStatusDate",
							new SimpleDateFormat("dd MMM yyyy ,hh.mm a").format((Date) obdata.get("dtmStatusDate")));
					jsonOb.put("tinStatus", (Integer) obdata.get("tinStatus"));
					if ((Integer) obdata.get("tinStatus") == 4) {
						jsonOb.put("userName", obdata.get("userName").toString());
					}
//					if (obdata.get("intPendingAt") != null) {
//						jsonOb.put("intPendingAt", obdata.get("vchRolename"));  
//					}
					jsonOb.put("applicationRoleId", obdata.get("userRoleId"));
					jsonOb.put("rolePendingAt", obdata.get("rolePendingAt"));

					if (obdata.get("intPendingAt") != null) {

						if (Integer.parseInt((String) obdata.get("intPendingAt")) == 52) {
							jsonOb.put("intPendingAt", "Internal Committee Member");
						} else {
							jsonOb.put("intPendingAt", tuserRepository
									.getPendignAtUser(Integer.parseInt((String) obdata.get("intPendingAt"))));
						}

					}
					jsonOb.put("stageno", (Integer) obdata.get("intStageNo"));
					jsonOb.put("intLableId", (Integer) obdata.get("intLabelId"));
					jsonOb.put("vchActionOnApplication", obdata.get("vchActionOnApplication"));
					jsonOb.put("ActionCondition", obdata.get("ActionCondition"));
					jsonOb.put("username", obdata.get("user_name"));
					jsonOb.put("contactno", obdata.get("contact_no"));
					jsonOb.put("status", obdata.get("vchForwardAllAction"));
					jsonOb.put("icmStatus", obdata.get("icmStatus"));
					jsonOb.put("icmActionTaken", obdata.get("icmActionTaken"));
					if (applicationStatus.equals("pending")) {
						Integer tinVerifiedBy = Integer.parseInt(obdata.get("tinVerifiedBy").toString());
						boolean isAction = false;
						if (roleId.equals(tinVerifiedBy)) {
							isAction = true;
						}
						if (isAction) {
							jsonOb.put("action", 0);
						} else {
							jsonOb.put("action", 1);
						}
					}
					Integer tinVerifyLetterGenStatus = (Integer) obdata.get("tinVerifyLetterGenStatus");
					jsonOb.put("tinVerifyLetterGenStatus", tinVerifyLetterGenStatus);
					if (tinVerifyLetterGenStatus == 1) {
						jsonOb.put("vchApprovalDoc", obdata.get("vchApprovalDoc").toString());
						JSONArray jsArr = new JSONArray(obdata.get("vchApprovalDoc").toString());
						jsonOb.put("verifyLetter", jsArr);
					}

					NewComplaintManagementDTO newcomplaintDto = new NewComplaintManagementDTO();
					ComplaintManagementNewEntity entity = newcomplaint_managmentRepository
							.findByIdAndDeletedFlag((Integer) obdata.get("intId"), false);

					Integer userIdValue = entity.getUserId();

					Integer compuserIdValue = entity.getComplaintAgainstUserId();

					newcomplaintDto.setIntId(entity.getId());
					newcomplaintDto.setComplaintNo(entity.getComplaintNo());
					UserDetailsResponseDto userdto = tuserService.getUserDetails(userIdValue);
					newcomplaintDto.setTxtFullName(userdto.getFullName());

					dynamicValue = (!Objects.isNull(entity.getUser().getSelCounty())
							&& selCounty.has(entity.getUser().getSelCounty().toString()))
									? userdto.getCountyName().toString()
									: "--";
					newcomplaintDto.setSelCountyVal((String) dynamicValue);
					dynamicValue = (!Objects.isNull(entity.getUser().getSelSubCounty())
							&& selSubCounty.has(entity.getUser().getSelSubCounty().toString()))
									? userdto.getSubCountyName().toString()
									: "--";
					newcomplaintDto.setSelSubCountyVal((String) dynamicValue);
					newcomplaintDto.setSelWard(Integer.parseInt(userdto.getWardId()));
					dynamicValue = (!Objects.isNull(entity.getComplaintAgainst())
							&& rdoComplaintAgainst.has(entity.getComplaintAgainst().toString()))
									? rdoComplaintAgainst.get(entity.getComplaintAgainst().toString())
									: "--";
					newcomplaintDto.setRdoComplaintAgainstVal((String) dynamicValue);
					newcomplaintDto.setRdoComplaintAgainst(entity.getComplaintAgainst());
					UserDetailsResponseDto complaintuserdto = tuserService.getUserDetails(compuserIdValue);
					newcomplaintDto.setComplaintAgainstUserId(compuserIdValue);
					newcomplaintDto.setApplicationStatus(entity.getApplicationStatus());
					newcomplaintDto.setTxtDateofIncident(entity.getIncidentDate());
					newcomplaintDto.setSelCountyofWarehouse(Integer.parseInt(complaintuserdto.getCountyId()));
					newcomplaintDto.setSelCountyofWarehouseVal(complaintuserdto.getCountyName());
					newcomplaintDto.setSelSubCountyofWarehouse(Integer.parseInt(complaintuserdto.getSubCountyId()));
					newcomplaintDto.setSelSubCountyofWarehouseVal(complaintuserdto.getSubCountyName());
					if (entity.getComplaintAgainst() == 100) {
						ApplicationOfConformityLocationDetails locationDetails = confirmityLocDetails.findByWarehouseId(entity.getUser().getWarehouseId());
						OperatorLicenceEntity operatorLicenceEntity = newoperatorLicenceRepository
								.findByWarehouseIdAndLicenseGenTrueAndLicenceCertGenTrueAndDeletedFlagFalse(
										locationDetails.getWarehouseId());
						
						System.out.println(operatorLicenceEntity.toString());
						newcomplaintDto.setPaymentId(operatorLicenceEntity.getPaymentId());
						newcomplaintDto.setWareHouseId(entity.getUser().getWarehouseId());
						newcomplaintDto.setCompanyProfId(locationDetails.getCompany().getProfDetId());
						newcomplaintDto.setTxtWarehouseOperator(entity.getUser().getTxtFullName());
						newcomplaintDto.setSelWarehouseShopName(entity.getComplaintAgainstUserId());
						newcomplaintDto.setSelWarehouseShopNameVal(locationDetails.getWarehouseName());
					} else if (entity.getComplaintAgainst() == 101) {
						ApplicationOfConformityLocationDetails locationDetails = confirmityLocDetails
								.findByWarehouseId(entity.getUser().getWarehouseId());
						// OperatorLicenceEntity
						// operatorLicenceEntity=newoperatorLicenceRepository.findByWarehouseIdAndLicenseGenTrueAndLicenceCertGenTrueAndDeletedFlagFalse(locationDetails.getWarehouseId());
						ConformityMain confMain = conformityMainRepository
								.findByWarehouseIdAndApplicationStatusAndDeletedFlagFalse(entity.getUser().getWarehouseId(),
										ApproverStatus.APPROVED);
						
						System.out.println(confMain.toString());
						newcomplaintDto.setPaymentId(confMain.getPayment().getId().intValue());
						// newcomplaintDto.setPaymentId(operatorLicenceEntity.getPaymentId());
						newcomplaintDto.setWareHouseId(entity.getUser().getWarehouseId());
						newcomplaintDto.setCompanyProfId(locationDetails.getCompany().getProfDetId());
						newcomplaintDto.setTxtWarehouseOperator(entity.getUser().getTxtFullName());
						newcomplaintDto.setSelWarehouseShopName(entity.getComplaintAgainstUserId());
						newcomplaintDto.setSelWarehouseShopNameVal(locationDetails.getWarehouseName());
					} else if (entity.getComplaintAgainst() == 102) {
						newcomplaintDto.setSelNameofCollateralManagerVal(complaintuserdto.getFullName());
					} else if (entity.getComplaintAgainst() == 103) {

						newcomplaintDto.setSelNameofInspectorVal(complaintuserdto.getFullName());
					} else if (entity.getComplaintAgainst() == 104) {

						newcomplaintDto.setSelNameofGraderVal(complaintuserdto.getFullName());
					}

					if (!entity.getComplaintType().toString().equals("0")) {
						ComplaintType ct = ctRepo.findById(entity.getComplaintType()).get();
						newcomplaintDto.setSelTypeofComplainVal(ct.getComplaintType());
					} else {
						newcomplaintDto.setSelTypeofComplainVal("--");
					}
					if (entity.getComplaintNo().isEmpty()) {
						newcomplaintDto.setComplaintNo("--");
					}
					if (entity.getComplaintAgainst() == 0) {
						newcomplaintDto.setComplaintNo("--");
					}

					complaint_managmentList.add(newcomplaintDto);
					jsonOb.put("result1", new JSONArray(complaint_managmentList));
					complaint_managmentList.remove(0);
					jsonArray.put(jsonOb);
				}
			}
			
		}
		catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		

		long totalitems = 0;
		if (pageobj != null) {
			totalitems = pageobj.getTotalElements();
		}

		JSONObject resultJson = new JSONObject();
		resultJson.put("status", 200);
		resultJson.put("result", jsonArray);
		resultJson.put("count", totalitems);
		return resultJson;

	}

	@Transactional
	@Override
	public JSONObject getEventTakeActionDetails(String data) throws Exception {
		logger.info("Inside getEventTakeActionDetails method of ContactformServiceImpl");
		JSONObject getAuthArr = new JSONObject();
		JSONObject requestedData = new JSONObject(data);
		Long currentTime = System.currentTimeMillis();
		Integer intId = requestedData.optInt("intId", 0);
		Integer onlineServiceId = requestedData.optInt("onlineServiceId", 0);
		Integer stageNo = requestedData.optInt("stageNo", 0);
		Integer action = requestedData.optInt("action", 0);
		String remark = requestedData.optString("remark", "");
		Integer roleId = requestedData.optInt("roleId", 0);
		Integer userId = requestedData.optInt("userId", 0);
		Integer forwardAuthority = requestedData.optInt("forwardAuthority", 0);
		Integer labelId = requestedData.getInt("labelId");
		String allActions = requestedData.optString("allActions", "");
		String actionType = requestedData.optString("actionType", "0");
		String conditionActions = requestedData.optString("actionCondition", "nocondition");
		JSONArray internalMembers = requestedData.optJSONArray("internalMembers");
		Integer applicationRoleId = requestedData.optInt("applicationRoleId", 0);
		String suspensionReason = requestedData.optString("SespensioReason", null);
		JSONArray jsonarr = requestedData.optJSONArray("addMoreSupportingDocuments");
		String ActionTaken = requestedData.optString("ActionTaken");

		String status = "";
		String outMsg = "";
		Integer prevRoleId = roleId;
		try {
			TOnlineServiceApproval actionDetails = tOnlineServiceApprovalRepository
					.getAllDataByUsingIntIdAndOnlineServiceId(intId, onlineServiceId);
			if (actionDetails.getIntForwardedUserId() > 0) {
				prevRoleId = actionDetails.getIntSentFrom();
			}

			String vchAuths = "";
			if (applicationRoleId == 52 || applicationRoleId == 12) {

				if (stageNo == 2 || stageNo == 3) {
					vchAuths = tSetAuthorityRepository.getVchAuthsByProcessIdAndRoleId(intId, applicationRoleId, 2,labelId);
				} 
				
				else if (stageNo == 5 || stageNo == 6) {
					vchAuths = tSetAuthorityRepository.getVchAuthsByProcessIdAndRoleId(intId, applicationRoleId, 5,labelId);
				}
				else {
					vchAuths = tSetAuthorityRepository.getVchAuthsByProcessIdAndRoleId(intId, applicationRoleId,
							actionDetails.getIntStageNo(), labelId);
				}

			} else {
				vchAuths = tSetAuthorityRepository.getVchAuthsByProcessIdAndRoleId(intId, prevRoleId,
						actionDetails.getIntStageNo(), labelId);
			}

			String[] vchAuthList = vchAuths.split(",");
			boolean isPresent = false;
			boolean isGenerate = false;
			for (String num : vchAuthList) {
				int currentNumber = Integer.parseInt(num.trim());
				if (currentNumber == 13) {
					isPresent = true;

				}
				if (currentNumber == 14) {
					isGenerate = true;
				}
			}
			Date date = new Date(currentTime);

			String[] pendingData = actionDetails.getIntPendingAt().split(",");
			List<String> intPending = new ArrayList<>();
			for (String value : pendingData) {
				intPending.add(value);
			}
			List<Map<String, Object>> mapObjlist = workflowServiceImpl.getAllComplaintStatus();
			if (actionDetails != null) {
				OnlineServiceApprovalNotings notings = new OnlineServiceApprovalNotings();
				notings.setIntProcessId(intId);
				notings.setIntProfileId(0);
				notings.setIntOnlineServiceId(onlineServiceId);
				notings.setIntFromAuthority(roleId);
				notings.setDtActionTaken(date);
				notings.setIntStatus(action);
				notings.setTinStageCtr(stageNo);
				notings.setTxtNoting(remark);
				notings.setVchSuspensionReason(suspensionReason);
				notings.setIntCreatedBy(userId);
				notings.setIntActionTakenId(action);
				notings.setVchActionTaken(ActionTaken);
				TOnlineServiceApproval onlineServiceApproval = tOnlineServiceApprovalRepository
						.findByIntProcessIdAndIntOnlineServiceIdAndBitDeletedFlag(intId, onlineServiceId, labelId);

				onlineServiceApproval.setTinStatus(action);
				onlineServiceApproval.setDtmStatusDate(date);
				onlineServiceApproval.setIntCreatedBy(userId);
				onlineServiceApproval.setIntUpdatedBy(userId);
				if (action == 1) { // Markup action
					if (intPending.size() > 1) {
						intPending.remove(roleId.toString());
						String intPendingString = String.join(",", intPending);

						notings.setIntToAuthority(intPendingString);

						onlineServiceApproval.setIntStageNo(stageNo);
						onlineServiceApproval.setIntATAProcessId(actionDetails.getIntATAProcessId());
						onlineServiceApproval.setIntSentFrom(roleId);
						onlineServiceApproval.setVchForwardAllAction(null);

						if (isPresent || isGenerate) {
							onlineServiceApproval.setIntPendingAt(onlineServiceApproval.getIntPendingAt());
							onlineServiceApproval.setIntForwardTo(onlineServiceApproval.getIntForwardTo());
						} else {
							onlineServiceApproval.setIntPendingAt(intPendingString);
							onlineServiceApproval.setIntForwardTo(intPendingString);
							onlineServiceApproval.setIntForwardedUserId(0);
						}
					} else {

						if (isPresent || isGenerate) {
							getAuthArr = fnAuthority.getAuthorityByCondition(intId, stageNo, labelId, conditionActions);
							String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
							Integer newStageNo = getAuthArr.getInt(STAGENO);
							Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);
							notings.setIntToAuthority(pendingAt.toString());

							onlineServiceApproval.setIntStageNo(newStageNo);
							onlineServiceApproval.setIntATAProcessId(intATAProcessId);
							onlineServiceApproval.setIntForwardTo(pendingAt);
							onlineServiceApproval.setTinVerifiedBy(roleId);
							onlineServiceApproval.setVchForwardAllAction(null);

						} else {

							getAuthArr = fnAuthority.getAuthority(intId, stageNo + 1, labelId);
							if (getAuthArr != null) {
								String appicationStatus = mapObjlist.stream()
										.filter(x -> Integer.parseInt(x.get("intId").toString()) == 14)
										.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
								String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
								Integer newStageNo = getAuthArr.getInt(STAGENO);
								Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);
								notings.setIntToAuthority(pendingAt.toString());
								onlineServiceApproval.setIntStageNo(newStageNo);
								onlineServiceApproval.setIntPendingAt(pendingAt.toString());
								onlineServiceApproval.setIntATAProcessId(intATAProcessId);
								onlineServiceApproval.setIntForwardTo(pendingAt);
								onlineServiceApproval.setTinVerifiedBy(0);
								onlineServiceApproval.setIntSentFrom(roleId);
								onlineServiceApproval.setVchForwardAllAction(appicationStatus);
								onlineServiceApproval.setIntForwardedUserId(0);

								if (labelId == 104 || labelId == 103 || labelId == 102
										|| labelId == 101 && stageNo == 1 && roleId == 5 && internalMembers != null) {
									List<ComplaintIcm> complaintIcmList = new ArrayList<>();

									// Iterate over each object in the internalMembers array
									for (int i = 0; i < internalMembers.length(); i++) {
										JSONObject member = internalMembers.getJSONObject(i);
										ComplaintIcm complaintIcm = new ComplaintIcm();
										// Map the values from the JSON to the ComplaintIcm entity
										complaintIcm.setIcmRoleId(member.optInt("icMemberRoleId"));
										complaintIcm.setIcmUserRoleId(member.optInt("roleId"));
										complaintIcm.setIcmUserId(member.optInt("userId"));
										complaintIcm.setIcmUserName(member.optString("fullName"));
										complaintIcm.setIcmActionTaken(false);
										complaintIcm.setIcmStatus(ComplaintIcm.ComplaintStatus.APPLICATION_ASSIGNED);
										complaintIcm.setIntCreatedBy(userId); // Replace with the actual user ID
										complaintIcm.setBitDeletedFlag(false);
										complaintIcm.setIcmActionTaken(false);// Set to false initially
										complaintIcm.setIcmComplaintAppId(onlineServiceId);// Replace with the actual
										complaintIcm.setLableId(labelId);
										complaintIcm.setProcessId(intId);
										complaintIcm.setSentMailId(0);
										complaintIcm.setSentNotificationId(0);
										complaintIcm.setSentSMSId(0);
										complaintIcm.setNotificationFlag(false);
										complaintIcm.setSMSFlag(false);
										complaintIcm.setMailFlag(false);
										complaintIcm.setIcmActionTaken(false); // Set to false initially
										complaintIcmList.add(complaintIcm);
									}
									complaintIcmRepository.saveAll(complaintIcmList);
									ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository
											.findByIdAndDeletedFlag(onlineServiceId, false);
									comp_man_entity.setApplicationStatus(appicationStatus);
									newcomplaint_managmentRepository.save(comp_man_entity);
								}
							}
						}
					}
					status = "200";
					outMsg = "Markup Action Taken Successfully!";
				} else if (action == 2) {// MarkDown
					if (intPending.size() > 1) {
						intPending.remove(roleId.toString());
						String intPendingString = String.join(",", intPending);
						notings.setIntToAuthority(intPendingString);

						onlineServiceApproval.setIntStageNo(stageNo);
						onlineServiceApproval.setIntPendingAt(intPendingString);
						onlineServiceApproval.setIntATAProcessId(actionDetails.getIntATAProcessId());
						onlineServiceApproval.setIntForwardTo(intPendingString);
						onlineServiceApproval.setIntSentFrom(roleId);
						onlineServiceApproval.setIntForwardedUserId(0);
					} else {
						getAuthArr = fnAuthority.getAuthority(intId, stageNo - 1, labelId);
						if (getAuthArr.length() != 0) {
							String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
							Integer newStageNo = getAuthArr.getInt(STAGENO);
							Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);
							notings.setIntToAuthority(pendingAt.toString());
							onlineServiceApproval.setIntStageNo(newStageNo);
							onlineServiceApproval.setIntPendingAt(pendingAt.toString());
							onlineServiceApproval.setIntATAProcessId(intATAProcessId);
							onlineServiceApproval.setIntForwardTo(pendingAt);
							onlineServiceApproval.setTinVerifiedBy(roleId);
							onlineServiceApproval.setIntSentFrom(roleId);
							onlineServiceApproval.setIntForwardedUserId(0);
						}
					}
					status = "200";
					outMsg = "MarkDown Action Taken Successfully!";
				} else if (action == 3) {// Resubmit
					notings.setIntToAuthority("0");
					notings.setTinStageCtr(0);
					OnlineServiceApprovalNotings onlineServiceApprovalNotings = onlineServiceApprovalNotingsRepository
							.saveAndFlush(notings);

					onlineServiceApproval.setIntStageNo(0);
					onlineServiceApproval.setIntPendingAt("0");
					onlineServiceApproval.setIntATAProcessId(0);
					onlineServiceApproval.setIntForwardTo("0");
					onlineServiceApproval.setTinResubmitStatus(1);
					onlineServiceApproval.setIntSentFrom(roleId);
					onlineServiceApproval.setVchForwardAllAction(null);
					onlineServiceApproval.setIntForwardedUserId(0);
					onlineServiceApproval.setVchForwardAllAction(null);
					onlineServiceApproval.setIntForwardedUserId(0);
					TOnlineServiceApplicationH tOnlineServiceApplicationH = new TOnlineServiceApplicationH();
					tOnlineServiceApplicationH.setIntOnlineServiceId(onlineServiceId);
					tOnlineServiceApplicationH.setIntNotingId(onlineServiceApprovalNotings.getIntNotingsId());
					tOnlineServiceApplicationH.setIntProcessId(intId);
					tOnlineServiceApplicationH.setIntCreatedBy(userId);
					tOnlineServiceApplicationH.setTinResubmitStatus((short) 1);
					tOnlineServiceApplicationHRepository.save(tOnlineServiceApplicationH);
					status = "200";
					outMsg = "Resubmit Action Taken Successfully!";
				} else if (action == 4) {// Forward
					getAuthArr = fnAuthority.getAuthority(intId, stageNo, labelId);
					Integer forwardedRoleId = tuserRepository.findByRoleIdByUserId(forwardAuthority);
					if (getAuthArr != null) {
						String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
						Integer newStageNo = getAuthArr.getInt(STAGENO);
						Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);
						notings.setIntFromAuthority(roleId);
						notings.setIntToAuthority(forwardedRoleId.toString());
						notings.setTinQueryTo(forwardAuthority);
						onlineServiceApproval.setIntStageNo(newStageNo);
						onlineServiceApproval.setIntPendingAt(forwardedRoleId.toString());
						onlineServiceApproval.setIntATAProcessId(intATAProcessId);
						onlineServiceApproval.setIntForwardTo(forwardedRoleId.toString());
						onlineServiceApproval.setVchForwardAllAction(allActions);
						onlineServiceApproval.setIntForwardedUserId(forwardAuthority);
						onlineServiceApproval.setTinVerifiedBy(roleId);
						onlineServiceApproval.setIntSentFrom(roleId);

					}

					status = "200";
					outMsg = "Forward Action Taken Successfully!";
				} else if (action == 7) {// Reject

					notings.setIntToAuthority("0");
					notings.setTinStageCtr(4);
					onlineServiceApproval.setIntStageNo(4);
					notings.setIntActionTakenId(action);
					/*
					 * onlineServiceApproval.setIntPendingAt("0");
					 * onlineServiceApproval.setIntATAProcessId(0);
					 * onlineServiceApproval.setIntForwardTo("0");
					 * onlineServiceApproval.setTinResubmitStatus(0);
					 * onlineServiceApproval.setIntForwardedUserId(0);
					 */

					if (labelId == 104 || labelId == 103 || labelId == 102 || labelId == 101
							|| labelId == 100 && roleId == 5 || roleId == 13 && stageNo == 3) {
						String appicationStatus = mapObjlist.stream()
								.filter(x -> Integer.parseInt(x.get("intId").toString()) == 27)
								.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
						ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository
								.findByIdAndDeletedFlag(onlineServiceId, false);
						comp_man_entity.setApplicationStatus(appicationStatus);
						newcomplaint_managmentRepository.save(comp_man_entity);
						onlineServiceApproval.setVchForwardAllAction(appicationStatus);
						notings.setVchSuspensionReason(appicationStatus);
					}
//					else if(labelId == 105 || labelId == 106 && roleId == 5 || roleId == 13 && stageNo == 6) {
//						String appicationStatus = mapObjlist.stream()
//								.filter(x -> Integer.parseInt(x.get("intId").toString()) == 27)
//								.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
//						ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository.findByIdAndDeletedFlag(onlineServiceId, false);
//						comp_man_entity.setApplicationStatus(appicationStatus);
//						newcomplaint_managmentRepository.save(comp_man_entity);
//						onlineServiceApproval.setVchForwardAllAction(appicationStatus);
//						onlineServiceApproval.setVchForwardAllAction(appicationStatus);
//						notings.setVchSuspensionReason(appicationStatus);
//					}
					else if(labelId == 106 && roleId == 5 && stageNo == 6) {
						String appicationStatus = mapObjlist.stream()
								.filter(x -> Integer.parseInt(x.get("intId").toString()) == 27)
								.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
						ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository.findByIdAndDeletedFlag(onlineServiceId, false);
						comp_man_entity.setApplicationStatus(appicationStatus);
						newcomplaint_managmentRepository.save(comp_man_entity);
						onlineServiceApproval.setVchForwardAllAction(appicationStatus);
						onlineServiceApproval.setVchForwardAllAction(appicationStatus);
						notings.setVchSuspensionReason(appicationStatus);
					}
					else if(labelId == 105 && roleId == 13 && stageNo == 6) {
						String appicationStatus = mapObjlist.stream()
								.filter(x -> Integer.parseInt(x.get("intId").toString()) == 12)
								.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
						ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository.findByIdAndDeletedFlag(onlineServiceId, false);
						comp_man_entity.setApplicationStatus(appicationStatus);
						newcomplaint_managmentRepository.save(comp_man_entity);
						onlineServiceApproval.setVchForwardAllAction(appicationStatus);
						onlineServiceApproval.setVchForwardAllAction(appicationStatus);
						notings.setVchSuspensionReason(appicationStatus);
					}
					status = "200";
					outMsg = "Reject Action Taken Successfully!";
				} else if (action == 8) {// Approve
					if (isPresent || isGenerate) {
						notings.setTinStageCtr(4);
						onlineServiceApproval.setIntStageNo(stageNo);
						onlineServiceApproval.setIntForwardTo("0");
					} else {

					}
					
					
					if ((labelId == 104 || labelId == 103 || labelId == 102 || labelId == 101|| labelId == 100) && (roleId == 5 || roleId == 13) && stageNo == 3) {


						String appicationStatus = mapObjlist.stream()
								.filter(x -> Integer.parseInt(x.get("intId").toString()) == 12)
								.collect(Collectors.toList()).get(0).get("complaintStatus").toString();

						ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository
								.findByIdAndDeletedFlag(onlineServiceId, false);
						comp_man_entity.setApplicationStatus(appicationStatus);
						newcomplaint_managmentRepository.save(comp_man_entity);
						
						notings.setIntToAuthority("0");
						notings.setTinStageCtr(4);
						onlineServiceApproval.setIntStageNo(4);
						onlineServiceApproval.setVchATAAuths(""+roleId);
						onlineServiceApproval.setIntPendingAt(""+roleId);
						onlineServiceApproval.setIntATAProcessId(2);
						onlineServiceApproval.setIntForwardTo(""+roleId);
						onlineServiceApproval.setVchForwardAllAction(appicationStatus);
						onlineServiceApproval.setTinQueryTo(0);
						onlineServiceApproval.setTinResubmitStatus(0);
						onlineServiceApproval.setVchForwardAllAction(appicationStatus);

						Optional<Tuser> userOpt = tuserRepository.findById(comp_man_entity.getComplaintAgainstUserId());
						if (userOpt.isPresent()) {
							Tuser user = userOpt.get();
							// user.setBitDeletedFlag(true);
							if (labelId == 100) {
								user.setOplsuspStatus(true);
								if (user.isOpcsuspStatus() == true) {
									user.setVchUserStatus("Suspend");
								}
							}
							if (labelId == 101) {
								user.setOpcsuspStatus(true);
								if (user.isOplsuspStatus() == true) {
                                     user.setVchUserStatus("Suspend");
								}
							}
							
							tuserRepository.save(user);
							onlineServiceApproval.setVchForwardAllAction(appicationStatus);
						}

						if (roleId == 13 && labelId == 100) {
							int labelId1 = 105;
							getAuthArr = fnAuthority.getAuthority(intId, stageNo + 1, labelId1);

							String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
							Integer newStageNo = getAuthArr.getInt(STAGENO);
							Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);

							notings.setIntToAuthority(pendingAt);

							onlineServiceApproval.setIntStageNo(newStageNo);

							onlineServiceApproval.setIntPendingAt(pendingAt);

							onlineServiceApproval.setIntATAProcessId(intATAProcessId);
							notings.setTinStageCtr(newStageNo);
							notings.setIntToAuthority("" + roleId);
							onlineServiceApproval.setIntForwardTo("" + roleId);
							onlineServiceApproval.setTinResubmitStatus(0);
							onlineServiceApproval.setIntForwardedUserId(userId);
							onlineServiceApproval.setIntLabelId(labelId1);
							onlineServiceApproval.setIntSentFrom(roleId);

						}
						
						if (roleId == 5 && labelId == 102) {
							int labelId1 = 106;
							getAuthArr = fnAuthority.getAuthority(intId, stageNo + 1, labelId1);

							String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
							Integer newStageNo = getAuthArr.getInt(STAGENO);
							Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);

							notings.setIntToAuthority(pendingAt);

							onlineServiceApproval.setIntStageNo(newStageNo);

							onlineServiceApproval.setIntPendingAt(pendingAt);

							onlineServiceApproval.setIntATAProcessId(intATAProcessId);
							notings.setTinStageCtr(newStageNo);
							notings.setIntToAuthority("" + roleId);
							onlineServiceApproval.setIntForwardTo("" + roleId);
							onlineServiceApproval.setTinResubmitStatus(0);
							onlineServiceApproval.setIntForwardedUserId(userId);
							onlineServiceApproval.setIntLabelId(labelId1);
							onlineServiceApproval.setIntSentFrom(roleId);
							Tuser user = userOpt.get();
							// user.setBitDeletedFlag(true);
							user.setVchUserStatus("Suspend");
							tuserRepository.save(user);
						}
						
						if(labelId == 103) {
							Tuser user = userOpt.get();
							// user.setBitDeletedFlag(true);
							user.setVchUserStatus("Suspend");
							tuserRepository.save(user);
						}
						if(labelId == 104) {
							Tuser user = userOpt.get();
							// user.setBitDeletedFlag(true);
							user.setVchUserStatus("Suspend");
							tuserRepository.save(user);
						}
						
						OnlineServiceApprovalNotings onlineServiceApprovalNotings = onlineServiceApprovalNotingsRepository.saveAndFlush(notings);
						
						status = "200";
						outMsg = "Approve Action Taken Successfully!";

					}	
				}
				else if (action == 20) {// Revoke
					if (isPresent || isGenerate) {
						notings.setTinStageCtr(7);

						onlineServiceApproval.setIntStageNo(stageNo);
						onlineServiceApproval.setIntForwardTo("0");
					} else {
											
	
					}

					if (labelId == 105 || labelId == 106 && roleId == 5||roleId == 13 && stageNo == 6) {

						ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository.findByIdAndDeletedFlag(onlineServiceId, false);
						 comp_man_entity.setApplicationStatus("Revoked");
						newcomplaint_managmentRepository.save(comp_man_entity);
						onlineServiceApproval.setVchForwardAllAction("Revoked");	
						Optional<Tuser> userOpt = tuserRepository.findById(comp_man_entity.getComplaintAgainstUserId());
						if (userOpt.isPresent()) {
							Tuser user = userOpt.get();
							//user.setBitDeletedFlag(true);
							user.setVchUserStatus("Suspend");
							user.setRevokeStatus(true);							
							tuserRepository.save(user);
							onlineServiceApproval.setVchForwardAllAction("Revoked");
						}
					}
					status = "200";
					outMsg = "Revoke Action Taken Successfully!";
				} 
				
				else if (action == 6) {// Query
					Integer intATAProcessId = actionDetails.getIntATAProcessId();
					Integer selAuthority = 0;
					if (intATAProcessId == 1) {

					} else {
						selAuthority = 0;
					}

					String appicationStatus = mapObjlist.stream()
							.filter(x -> Integer.parseInt(x.get("intId").toString()) == 16).collect(Collectors.toList())
							.get(0).get("complaintStatus").toString();

					ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository
							.findByIdAndDeletedFlag(onlineServiceId, false);
					comp_man_entity.setApplicationStatus(appicationStatus);
					newcomplaint_managmentRepository.save(comp_man_entity);

					onlineServiceApproval.setVchForwardAllAction(appicationStatus);

					notings.setIntToAuthority(selAuthority.toString());
					notings.setTinStageCtr(4);
					// notings.setTinStageCtr(4);
					onlineServiceApproval.setIntStageNo(4);
					onlineServiceApproval.setVchATAAuths(onlineServiceApproval.getIntPendingAt());
					onlineServiceApproval.setIntPendingAt(selAuthority.toString());
					onlineServiceApproval.setIntATAProcessId(0);
					onlineServiceApproval.setIntForwardTo("0");
					onlineServiceApproval.setVchForwardAllAction(appicationStatus);
					onlineServiceApproval.setTinQueryTo(intATAProcessId);
					onlineServiceApproval.setTinResubmitStatus(0);
					OnlineServiceApprovalNotings onlineServiceApprovalNotings = onlineServiceApprovalNotingsRepository
							.saveAndFlush(notings);

					status = "200";
					outMsg = "Query Action Taken Successfully!";
				} else if (action == 11) {// Verify action

					if (intPending.size() > 1) {
						intPending.remove(roleId.toString());
						String intPendingString = String.join(",", intPending);
						notings.setIntToAuthority(intPendingString);
						onlineServiceApproval.setIntStageNo(stageNo);
						onlineServiceApproval.setVchForwardAllAction(null);
						if (isPresent || isGenerate) {
							onlineServiceApproval.setIntPendingAt(onlineServiceApproval.getIntPendingAt());
							onlineServiceApproval.setIntForwardTo(onlineServiceApproval.getIntForwardTo());
							onlineServiceApproval.setTinVerifiedBy(roleId);
						} else {
							onlineServiceApproval.setIntPendingAt(intPendingString);
							onlineServiceApproval.setIntForwardTo(intPendingString);
							onlineServiceApproval.setTinVerifiedBy(roleId);
							onlineServiceApproval.setIntForwardedUserId(0);
						}
						onlineServiceApproval.setIntATAProcessId(actionDetails.getIntATAProcessId());
						// onlineServiceApproval.setTinVerifiedBy(roleId);
						onlineServiceApproval.setIntSentFrom(roleId);
					} else {

						if (isPresent || isGenerate) {
							getAuthArr = fnAuthority.getAuthority(intId, stageNo, labelId);
							String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
							Integer newStageNo = getAuthArr.getInt(STAGENO);
							Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);
							notings.setIntToAuthority(pendingAt.toString());

							onlineServiceApproval.setIntStageNo(newStageNo);
							onlineServiceApproval.setIntATAProcessId(intATAProcessId);
							onlineServiceApproval.setVchForwardAllAction(null);
							onlineServiceApproval.setTinVerifiedBy(roleId);

						} else {

							if (labelId == 104 || labelId == 103 || labelId == 102 || labelId == 101 || labelId == 100
									&& stageNo == 2 && actionDetails.getIntStageNo() == 2 && applicationRoleId == 52
									|| applicationRoleId == 12) {

								getAuthArr = fnAuthority.getAuthority(intId, stageNo + 1, labelId);
								ComplaintIcm icmApplicationDetails = complaintIcmRepository
										.findByIcmComplaintAppIdAndIcmUserIdAndIcmUserRoleIdAndIcmActionTaken(
												onlineServiceId, userId, roleId, false);
								icmApplicationDetails.setIcmStatus(ComplaintIcm.ComplaintStatus.APPLICATION_REVIEWED);
								icmApplicationDetails.setIcmActionTaken(true);

								complaintIcmRepository.saveAndFlush(icmApplicationDetails);
							} else {

								ComplaintIcm icmApplicationDetails = complaintIcmRepository
										.findByIcmComplaintAppIdAndIcmUserIdAndIcmUserRoleIdAndIcmActionTaken(
												onlineServiceId, userId, roleId, false);
								icmApplicationDetails.setIcmStatus(ComplaintIcm.ComplaintStatus.APPLICATION_REVIEWED);
								icmApplicationDetails.setIcmActionTaken(true);
								complaintIcmRepository.saveAndFlush(icmApplicationDetails);
							}

							OnlineServiceApprovalNotings onlineServiceApprovalNotings = onlineServiceApprovalNotingsRepository
									.saveAndFlush(notings);

							int notingId = onlineServiceApprovalNotings.getIntNotingsId();
							List<String> fileUploadList = new ArrayList<String>();

							List<ComplaintMgmtInspScheduleDocs> supporting_documentList = new ArrayList();

							// Multiple file uploading

							if (jsonarr != null && jsonarr.length() > 0) {

								for (int i = 0; i <= jsonarr.length() - 1; i++) {
									JSONObject jsonobj = (JSONObject) jsonarr.get(i);
									String docName = jsonobj.get("amtxtDocumentName").toString();
									String docPathName = jsonobj.get("amfileUploadDocuments").toString();
									ComplaintMgmtInspScheduleDocs doc = new ComplaintMgmtInspScheduleDocs();
									doc.setDocPath(docPathName);
									doc.setDocName(docName);
									doc.setParentId(notingId);
									doc.setBitDeletedFlag(false);
									supporting_documentList.add(doc);
									fileUploadList.add(docPathName);
								}

								fileUploadList.forEach(fileUpload -> {
									if (!Strings.isNullOrEmpty(fileUpload)) {
										File f = new File(tempUploadPath + fileUpload);
										if (f.exists()) {
											File src = new File(tempUploadPath + fileUpload);
											File dest = new File(
													finalUploadPath + "complaint-management/" + fileUpload);
											try {
												Files.copy(src.toPath(), dest.toPath(),
														StandardCopyOption.REPLACE_EXISTING);
												Files.delete(src.toPath());
											} catch (IOException e) {
												logger.error(
														"Inside save method of Complaint_managmentServiceImpl some error occur in file moving to destination folder:"
																+ e);
											}
										}
									}
								});

								// List<OnlineServiceApprovalNotings> notingsting_documentList = new
								// ArrayList<OnlineServiceApprovalNotings>();
								List<ComplaintMgmtInspScheduleDocs> savedData = docRepo
										.saveAll(supporting_documentList);

//								for (ComplaintMgmtInspScheduleDocs object : savedData) {
//
//									OnlineServiceApprovalNotings copyNotings = (OnlineServiceApprovalNotings) notings
//											.clone();
//									copyNotings.setInspectionDocId(object.getDocId());
//									notingsting_documentList.add(copyNotings);
//								}
//
//								onlineServiceApprovalNotingsRepository.saveAll(notingsting_documentList);

							} else {
								notings.setInspectionDocId(null);
							}

							if (getAuthArr != null && getAuthArr.length() != 0) {

								String appicationStatus = mapObjlist.stream()
										.filter(x -> Integer.parseInt(x.get("intId").toString()) == 14)
										.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
								String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
								Integer newStageNo = getAuthArr.getInt(STAGENO);
								Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);

								notings.setIntToAuthority(pendingAt);

								onlineServiceApproval.setIntStageNo(newStageNo);

								onlineServiceApproval.setIntPendingAt(pendingAt);

								onlineServiceApproval.setIntATAProcessId(intATAProcessId);

								onlineServiceApproval.setIntSentFrom(roleId);
								onlineServiceApproval.setVchForwardAllAction(appicationStatus);
								onlineServiceApproval.setTinVerifiedBy(0);
								onlineServiceApproval.setIntForwardedUserId(0);
								ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository
										.findByIdAndDeletedFlag(onlineServiceId, false);
								comp_man_entity.setApplicationStatus(appicationStatus);
								newcomplaint_managmentRepository.save(comp_man_entity);
							}
						}

					}
					status = "200";
					outMsg = "Verify Action Taken Successfully!";

				}

				else if(action == 111) {//label id 105 106
					if (intPending.size() > 1) {
						intPending.remove(roleId.toString());
						String intPendingString = String.join(",", intPending);

						notings.setIntToAuthority(intPendingString);
//						TOnlineServiceApproval onlineServiceApproval = tOnlineServiceApprovalRepository
//								.findByIntProcessIdAndIntOnlineServiceIdAndBitDeletedFlag(intId, onlineServiceId, labelId);
						onlineServiceApproval.setIntStageNo(stageNo);
						onlineServiceApproval.setIntATAProcessId(actionDetails.getIntATAProcessId());
						onlineServiceApproval.setIntSentFrom(roleId);
						
						onlineServiceApproval.setVchForwardAllAction("Under Revocation");

						if (isPresent || isGenerate) {
							onlineServiceApproval.setIntPendingAt(onlineServiceApproval.getIntPendingAt());
							onlineServiceApproval.setIntForwardTo(onlineServiceApproval.getIntForwardTo());
						} else {
							onlineServiceApproval.setIntPendingAt(intPendingString);
							onlineServiceApproval.setIntForwardTo(intPendingString);
							onlineServiceApproval.setIntForwardedUserId(0);
						}
					} else {

						if (isPresent || isGenerate) {
							getAuthArr = fnAuthority.getAuthorityByCondition(intId, stageNo, labelId, conditionActions);
							String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
							Integer newStageNo = getAuthArr.getInt(STAGENO);
							Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);
							notings.setIntToAuthority(pendingAt.toString());

							onlineServiceApproval.setIntStageNo(newStageNo);
							onlineServiceApproval.setIntATAProcessId(intATAProcessId);
							onlineServiceApproval.setIntForwardTo(pendingAt);
							onlineServiceApproval.setTinVerifiedBy(roleId);
							onlineServiceApproval.setVchForwardAllAction("Under Revocation");

						} else {

							getAuthArr = fnAuthority.getAuthority(intId, stageNo + 1, labelId);
							if (getAuthArr != null) {
								String appicationStatus = mapObjlist.stream()
										.filter(x -> Integer.parseInt(x.get("intId").toString()) == 14)
										.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
								String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
								Integer newStageNo = getAuthArr.getInt(STAGENO);
								Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);
								notings.setIntToAuthority(pendingAt.toString());
								onlineServiceApproval.setIntStageNo(newStageNo);
								onlineServiceApproval.setIntPendingAt(pendingAt.toString());
								onlineServiceApproval.setIntATAProcessId(intATAProcessId);
								onlineServiceApproval.setIntForwardTo(pendingAt);
								onlineServiceApproval.setTinVerifiedBy(0);
								onlineServiceApproval.setIntSentFrom(roleId);
								onlineServiceApproval.setVchForwardAllAction("Under Revocation");
								onlineServiceApproval.setIntForwardedUserId(0);

								if ((labelId == 106 || labelId == 105) && stageNo == 4 && (roleId == 5 || roleId == 13) && internalMembers != null) {
									List<RevocateICM> complaintIcmList = new ArrayList<>();

									// Iterate over each object in the internalMembers array
									for (int i = 0; i < internalMembers.length(); i++) {
										JSONObject member = internalMembers.getJSONObject(i);
										RevocateICM complaintIcm = new RevocateICM();
										// Map the values from the JSON to the ComplaintIcm entity
										complaintIcm.setIcmRoleId(member.optInt("icMemberRoleId"));
										complaintIcm.setIcmUserRoleId(member.optInt("roleId"));
										complaintIcm.setIcmUserId(member.optInt("userId"));
										complaintIcm.setIcmUserName(member.optString("fullName"));
										complaintIcm.setIcmActionTaken(false);
										complaintIcm.setIcmStatus(RevocateICM.ComplaintStatus.APPLICATION_ASSIGNED); 
										complaintIcm.setIntCreatedBy(userId); // Replace with the actual user ID
										complaintIcm.setBitDeletedFlag(false);
										complaintIcm.setIcmActionTaken(false);// Set to false initially
										complaintIcm.setIcmComplaintAppId(onlineServiceId);// Replace with the actual
										complaintIcm.setLableId(labelId);
										complaintIcm.setProcessId(intId);
										complaintIcm.setSentMailId(0);
										complaintIcm.setSentNotificationId(0);
										complaintIcm.setSentSMSId(0);
										complaintIcm.setNotificationFlag(false);
										complaintIcm.setSMSFlag(false);
										complaintIcm.setMailFlag(false);
										complaintIcm.setIcmActionTaken(false); // Set to false initially
										complaintIcmList.add(complaintIcm);
									}
									revocateIcmRepository.saveAll(complaintIcmList);
									String appicationStatus1 = mapObjlist.stream()
											.filter(x -> Integer.parseInt(x.get("intId").toString()) == 14)
											.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
									ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository.findByIdAndDeletedFlag(onlineServiceId, false);
									// comp_man_entity.setApplicationStatus(appicationStatus);
									 comp_man_entity.setApplicationStatus("Under Revocation");
									newcomplaint_managmentRepository.save(comp_man_entity);
								}
							}
						}
					}
					status = "200";
					outMsg = "Markup Action Taken Successfully!";
				}
				
				else if(action == 40) { //label id 105 106
					if (intPending.size() > 1) {
						intPending.remove(roleId.toString());
						String intPendingString = String.join(",", intPending);
						notings.setIntToAuthority(intPendingString);
						onlineServiceApproval.setIntStageNo(stageNo);
						onlineServiceApproval.setVchForwardAllAction("Under Revocation");
						if (isPresent || isGenerate) {
							onlineServiceApproval.setIntPendingAt(onlineServiceApproval.getIntPendingAt());
							onlineServiceApproval.setIntForwardTo(onlineServiceApproval.getIntForwardTo());
							onlineServiceApproval.setTinVerifiedBy(roleId);
						} else {
							onlineServiceApproval.setIntPendingAt(intPendingString);
							onlineServiceApproval.setIntForwardTo(intPendingString);
							onlineServiceApproval.setTinVerifiedBy(roleId);
							onlineServiceApproval.setIntForwardedUserId(0);
						}
						onlineServiceApproval.setIntATAProcessId(actionDetails.getIntATAProcessId());
						// onlineServiceApproval.setTinVerifiedBy(roleId);
						onlineServiceApproval.setIntSentFrom(roleId);
					} else {

						if (isPresent || isGenerate) {
							getAuthArr = fnAuthority.getAuthority(intId, stageNo, labelId);
							String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
							Integer newStageNo = getAuthArr.getInt(STAGENO);
							Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);
							notings.setIntToAuthority(pendingAt.toString());

							onlineServiceApproval.setIntStageNo(newStageNo);
							onlineServiceApproval.setIntATAProcessId(intATAProcessId);
							onlineServiceApproval.setVchForwardAllAction("Under Revocation");
							onlineServiceApproval.setTinVerifiedBy(roleId);

						} else {

							if (labelId == 105 || labelId == 106 && stageNo == 5 && actionDetails.getIntStageNo()==5 && applicationRoleId == 52 || applicationRoleId == 12) {

								getAuthArr = fnAuthority.getAuthority(intId, stageNo + 1, labelId);
								RevocateICM icmApplicationDetails = revocateIcmRepository
										.findByIcmComplaintAppIdAndIcmUserIdAndIcmUserRoleIdAndIcmActionTaken(
												onlineServiceId, userId, roleId, false);
								icmApplicationDetails.setIcmStatus(RevocateICM.ComplaintStatus.APPLICATION_REVIEWED);
								icmApplicationDetails.setIcmActionTaken(true);

								revocateIcmRepository.saveAndFlush(icmApplicationDetails);
							} else {

								RevocateICM icmApplicationDetails = revocateIcmRepository.findByIcmComplaintAppIdAndIcmUserIdAndIcmUserRoleIdAndIcmActionTaken(onlineServiceId, userId, roleId, false);
								icmApplicationDetails.setIcmStatus(RevocateICM.ComplaintStatus.APPLICATION_REVIEWED);
								icmApplicationDetails.setIcmActionTaken(true);
								revocateIcmRepository.saveAndFlush(icmApplicationDetails);
							} 
							 
							OnlineServiceApprovalNotings onlineServiceApprovalNotings = onlineServiceApprovalNotingsRepository.saveAndFlush(notings);

							  int notingId=onlineServiceApprovalNotings.getIntNotingsId();
							List<String> fileUploadList = new ArrayList<String>();

							List<ComplaintMgmtInspScheduleDocs> supporting_documentList = new ArrayList();

							// Multiple file uploading

							if (jsonarr != null && jsonarr.length() > 0) {

								for (int i = 0; i <= jsonarr.length() - 1; i++) {
									JSONObject jsonobj = (JSONObject) jsonarr.get(i);
									String docName = jsonobj.get("amtxtDocumentName").toString();
									String docPathName = jsonobj.get("amfileUploadDocuments").toString();
									ComplaintMgmtInspScheduleDocs doc = new ComplaintMgmtInspScheduleDocs();
									doc.setDocPath(docPathName);
									doc.setDocName(docName);
									doc.setParentId(notingId);
									doc.setBitDeletedFlag(false);
									supporting_documentList.add(doc);
									fileUploadList.add(docPathName);
								}

								fileUploadList.forEach(fileUpload -> {
									if (!Strings.isNullOrEmpty(fileUpload)) {
										File f = new File(tempUploadPath + fileUpload);
										if (f.exists()) {
											File src = new File(tempUploadPath + fileUpload);
											File dest = new File(
													finalUploadPath + "complaint-management/" + fileUpload);
											try {
												Files.copy(src.toPath(), dest.toPath(),
														StandardCopyOption.REPLACE_EXISTING);
												Files.delete(src.toPath());
											} catch (IOException e) {
												logger.error(
														"Inside save method of Complaint_managmentServiceImpl some error occur in file moving to destination folder:"
																+ e);
											}
										}
									}
								});
								
								List<ComplaintMgmtInspScheduleDocs> savedData = docRepo.saveAll(supporting_documentList);
	
							} else {
								notings.setInspectionDocId(null);
							}

							if (getAuthArr!=null&&getAuthArr.length() != 0) {
								
								String appicationStatus = mapObjlist.stream()
										.filter(x -> Integer.parseInt(x.get("intId").toString()) == 14)
										.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
								String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
								Integer newStageNo = getAuthArr.getInt(STAGENO);
								Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);

								notings.setIntToAuthority(pendingAt);

								onlineServiceApproval.setIntStageNo(newStageNo);

								onlineServiceApproval.setIntPendingAt(pendingAt);

								onlineServiceApproval.setIntATAProcessId(intATAProcessId);

								onlineServiceApproval.setIntSentFrom(roleId);
								onlineServiceApproval.setVchForwardAllAction("Under Revocation");
								onlineServiceApproval.setTinVerifiedBy(0);
								onlineServiceApproval.setIntForwardedUserId(0);
								ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository.findByIdAndDeletedFlag(onlineServiceId, false);
								 comp_man_entity.setApplicationStatus("Under Revocation");
								newcomplaint_managmentRepository.save(comp_man_entity);
							}
						}

					}
					status = "200";
					outMsg = "Verify Action Taken Successfully!";
				}

				else if (action == 16) { // CLC action stageNo 1
					if (intPending.size() > 1) {
						intPending.remove(roleId.toString());
						String intPendingString = String.join(",", intPending);

						notings.setIntToAuthority(intPendingString);

						onlineServiceApproval.setIntStageNo(stageNo);
						onlineServiceApproval.setIntATAProcessId(actionDetails.getIntATAProcessId());
						onlineServiceApproval.setIntSentFrom(roleId);
						onlineServiceApproval.setVchForwardAllAction(null);

						if (isPresent || isGenerate) {
							onlineServiceApproval.setIntPendingAt(onlineServiceApproval.getIntPendingAt());
							onlineServiceApproval.setIntForwardTo(onlineServiceApproval.getIntForwardTo());
						} else {
							onlineServiceApproval.setIntPendingAt(intPendingString);
							onlineServiceApproval.setIntForwardTo(intPendingString);
							onlineServiceApproval.setIntForwardedUserId(0);
						}
					} else {

						if (isPresent || isGenerate) {
							getAuthArr = fnAuthority.getAuthorityByCondition(intId, stageNo, labelId, conditionActions);
							String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
							Integer newStageNo = getAuthArr.getInt(STAGENO);
							Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);
							notings.setIntToAuthority(pendingAt.toString());

							onlineServiceApproval.setIntStageNo(newStageNo);
							onlineServiceApproval.setIntATAProcessId(intATAProcessId);
							onlineServiceApproval.setIntForwardTo(pendingAt);
							onlineServiceApproval.setTinVerifiedBy(roleId);
							onlineServiceApproval.setVchForwardAllAction(null);

						} else {

							getAuthArr = fnAuthority.getAuthority(intId, stageNo + 1, labelId);
							if (getAuthArr != null) {
								String appicationStatus = mapObjlist.stream()
										.filter(x -> Integer.parseInt(x.get("intId").toString()) == 13)
										.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
								String pendingAt = getAuthArr.getString(PENDING_AUTHORITIES);
								Integer newStageNo = getAuthArr.getInt(STAGENO);
								Integer intATAProcessId = getAuthArr.getInt(ATAPROCESSID);
								notings.setIntToAuthority(pendingAt.toString());
								onlineServiceApproval.setIntStageNo(newStageNo);
								onlineServiceApproval.setIntPendingAt(pendingAt.toString());
								onlineServiceApproval.setIntATAProcessId(intATAProcessId);
								onlineServiceApproval.setIntForwardTo(pendingAt);
								onlineServiceApproval.setTinVerifiedBy(0);
								onlineServiceApproval.setIntSentFrom(roleId);
								onlineServiceApproval.setVchForwardAllAction(appicationStatus);
								onlineServiceApproval.setIntForwardedUserId(0);

								if (labelId == 100 && stageNo == 1 && roleId == 13 && internalMembers != null) {
									List<ComplaintIcm> complaintIcmList = new ArrayList<>();

									// Iterate over each object in the internalMembers array
									for (int i = 0; i < internalMembers.length(); i++) {
										JSONObject member = internalMembers.getJSONObject(i);
										ComplaintIcm complaintIcm = new ComplaintIcm();
										// Map the values from the JSON to the ComplaintIcm entity
										complaintIcm.setIcmRoleId(member.optInt("icMemberRoleId"));
										complaintIcm.setIcmUserRoleId(member.optInt("roleId"));
										complaintIcm.setIcmUserId(member.optInt("userId"));
										complaintIcm.setIcmUserName(member.optString("fullName"));
										complaintIcm.setIcmStatus(ComplaintIcm.ComplaintStatus.APPLICATION_ASSIGNED);
										complaintIcm.setIntCreatedBy(userId); // Replace with the actual user ID
										complaintIcm.setBitDeletedFlag(false); // Set to false initially
										complaintIcm.setIcmComplaintAppId(onlineServiceId); // Replace with the actual
										complaintIcm.setLableId(labelId);
										complaintIcm.setProcessId(intId);
										complaintIcm.setSentMailId(0);
										complaintIcm.setSentNotificationId(0);
										complaintIcm.setSentSMSId(0);
										complaintIcm.setNotificationFlag(false);
										complaintIcm.setSMSFlag(false);
										complaintIcm.setMailFlag(false);
										complaintIcm.setIcmActionTaken(false);// Set to false initially
										complaintIcmList.add(complaintIcm);
									}
									complaintIcmRepository.saveAll(complaintIcmList);
									String appicationStatus1 = mapObjlist.stream()
											.filter(x -> Integer.parseInt(x.get("intId").toString()) == 14)
											.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
									ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository
											.findByIdAndDeletedFlag(onlineServiceId, false);
									comp_man_entity.setApplicationStatus(appicationStatus1);
									newcomplaint_managmentRepository.save(comp_man_entity);
								}
							}
						}
					}
					status = "200";
					outMsg = "Markup Action Taken Successfully!";

				}

				else if (action == 17 || action == 18 || action == 19) {// Warning action

					notings.setIntToAuthority("0");
					notings.setTinStageCtr(4);
					onlineServiceApproval.setIntStageNo(4);
					/*
					 * onlineServiceApproval.setIntPendingAt("0");
					 * onlineServiceApproval.setIntATAProcessId(0);
					 * onlineServiceApproval.setIntForwardTo("0");
					 * onlineServiceApproval.setTinResubmitStatus(0);
					 * onlineServiceApproval.setIntForwardedUserId(0);
					 */

					if (labelId == 100 && roleId == 13 && stageNo == 3) {
						String appicationStatus = mapObjlist.stream()
								.filter(x -> Integer.parseInt(x.get("intId").toString()) == 28)
								.collect(Collectors.toList()).get(0).get("complaintStatus").toString();
						ComplaintManagementNewEntity comp_man_entity = newcomplaint_managmentRepository.findByIdAndDeletedFlag(onlineServiceId, false);
						comp_man_entity.setApplicationStatus(appicationStatus);
						newcomplaint_managmentRepository.save(comp_man_entity);
						onlineServiceApproval.setVchForwardAllAction(appicationStatus);
						notings.setVchSuspensionReason(appicationStatus);						
					}
					status = "200";
					outMsg = "Warning Action Taken Successfully!";
				}

				if (action != 3 && action != 11 && action != 6) {
					OnlineServiceApprovalNotings onlineServiceApprovalNotings = onlineServiceApprovalNotingsRepository
							.saveAndFlush(notings);
				}
				tOnlineServiceApprovalRepository.save(onlineServiceApproval);
			}
		} catch (Exception e) {
			logger.error("Complaint_managmentServiceImpl:getEventTakeActionDetails()", e);
			status = "500";
			outMsg = "Error occurred during action!";
		}

		JSONObject jsObj = new JSONObject();
		jsObj.put("status", status);
		jsObj.put("outMsg", outMsg);
		jsObj.put("lableId", labelId);
		jsObj.put("id", intId);
		jsObj.put("result", "");
		return jsObj;

	}

	public JSONObject getPreviewDetails(Integer intId) throws Exception {
		JSONArray arrDetails = new JSONArray();
		String status = "";
		String outMsg = "";

		try {
			Complaint_managment complaint_managment = complaint_managmentRepository.findByIntIdAndBitDeletedFlag(intId,
					false);

			if (complaint_managment != null) {
				dynamicValue = (selCounty.has(complaint_managment.getSelCounty().toString()))
						? selCounty.get(complaint_managment.getSelCounty().toString())
						: "--";
				County county = countyRepo.findById(complaint_managment.getSelCounty()).get();
				complaint_managment.setSelCountyVal(county.getName());
				dynamicValue = (selSubCounty.has(complaint_managment.getSelSubCounty().toString()))
						? selSubCounty.get(complaint_managment.getSelSubCounty().toString())
						: "--";
				SubCounty subCounty = subCountyRepo.findById(complaint_managment.getSelSubCounty()).get();
				complaint_managment.setSelSubCountyVal(subCounty.getTxtSubCountyName());
				dynamicValue = (rdoComplaintAgainst.has(complaint_managment.getRdoComplaintAgainst().toString()))
						? rdoComplaintAgainst.get(complaint_managment.getRdoComplaintAgainst().toString())
						: "--";

				// wardmasterRepo.findById(complaint_managment.getw).get();
				complaint_managment.setRdoComplaintAgainstVal(dynamicValue.toString());
				dynamicValue = (selCountyofWarehouse.has(complaint_managment.getSelCountyofWarehouse().toString()))
						? selCountyofWarehouse.get(complaint_managment.getSelCountyofWarehouse().toString())
						: "--";

				if (!complaint_managment.getSelCountyofWarehouse().toString().equals("0")) {
					County selcounty = countyRepo.findById(complaint_managment.getSelCountyofWarehouse()).get();
					complaint_managment.setSelCountyofWarehouseVal(selcounty.getName());
				}

				dynamicValue = (selSubCountyofWarehouse
						.has(complaint_managment.getSelSubCountyofWarehouse().toString()))
								? selSubCountyofWarehouse
										.get(complaint_managment.getSelSubCountyofWarehouse().toString())
								: "--";
				if (!complaint_managment.getSelSubCountyofWarehouse().toString().equals("0")) {
					SubCounty selsubCounty = subCountyRepo.findById(complaint_managment.getSelSubCountyofWarehouse())
							.get();
					complaint_managment.setSelSubCountyofWarehouseVal(selsubCounty.getTxtSubCountyName());
				}

				dynamicValue = (selWarehouseShopName.has(complaint_managment.getSelWarehouseShopName().toString()))
						? selWarehouseShopName.get(complaint_managment.getSelWarehouseShopName().toString())
						: "--";
				if (!complaint_managment.getSelWarehouseShopName().toString().equals("0")) {
					ApplicationOfConformity app = appRepo.findById(complaint_managment.getSelWarehouseShopName()).get();
					complaint_managment.setSelWarehouseShopNameVal(app.getParticularOfApplicantsId().getShop());
				}

				dynamicValue = (selNameofGrader.has(complaint_managment.getSelNameofGrader().toString()))
						? selNameofGrader.get(complaint_managment.getSelNameofGrader().toString())
						: "--";
				if (!complaint_managment.getSelNameofGrader().toString().equals("0")) {
					Tuser cm = tuserRepository.findById(Integer.parseInt(complaint_managment.getSelNameofGrader()))
							.get();
					complaint_managment.setSelNameofGraderVal(cm.getTxtFullName());
				} else {
					complaint_managment.setSelNameofGraderVal(dynamicValue.toString());
				}
				dynamicValue = (selNameofCollateralManager
						.has(complaint_managment.getSelNameofCollateralManager().toString()))
								? selNameofCollateralManager
										.get(complaint_managment.getSelNameofCollateralManager().toString())
								: "--";
				if (!complaint_managment.getSelNameofCollateralManager().toString().equals("0")) {
					Tuser cm = tuserRepository
							.findById(Integer.parseInt(complaint_managment.getSelNameofCollateralManager())).get();
					complaint_managment.setSelNameofCollateralManagerVal(cm.getTxtFullName());
				} else {
					complaint_managment.setSelNameofCollateralManagerVal(dynamicValue.toString());
				}
				dynamicValue = (selNameofInspector.has(complaint_managment.getSelNameofInspector().toString()))
						? selNameofInspector.get(complaint_managment.getSelNameofInspector().toString())
						: "--";
				if (!complaint_managment.getSelNameofInspector().toString().equals("0")) {
					Tuser insp = tuserRepository.findById(Integer.parseInt(complaint_managment.getSelNameofInspector()))
							.get();
					complaint_managment.setSelNameofInspectorVal(insp.getTxtFullName());
				} else {
					complaint_managment.setSelNameofInspectorVal(dynamicValue.toString());
				}
				dynamicValue = (selTypeofComplain.has(complaint_managment.getSelTypeofComplain().toString()))
						? selTypeofComplain.get(complaint_managment.getSelTypeofComplain().toString())
						: "--";
				ComplaintType ct = ctRepo.findById(Integer.parseInt(complaint_managment.getSelTypeofComplain())).get();
				complaint_managment.setSelTypeofComplainVal(ct.getComplaintType());

// arrDetails.put(new JSONObject().put("label", "Complainants Information").put("value", complaint_managment.getHedComplainantsInformation()));

				arrDetails.put(
						new JSONObject().put("label", "Full Name").put("value", complaint_managment.getTxtFullName()));

				arrDetails.put(new JSONObject().put("label", "Contact Number").put("value",
						complaint_managment.getTxtContactNumber()));

				arrDetails.put(new JSONObject().put("label", "Email Address").put("value",
						complaint_managment.getTxtEmailAddress()));

				arrDetails.put(
						new JSONObject().put("label", "Address").put("value", complaint_managment.getTxtrAddress()));

				arrDetails.put(
						new JSONObject().put("label", "County").put("value", complaint_managment.getSelCountyVal()));

				arrDetails.put(new JSONObject().put("label", "Sub County").put("value",
						complaint_managment.getSelSubCountyVal()));

// arrDetails.put(new JSONObject().put("label", "Complaint Information").put("value", complaint_managment.getHedComplaintInformation()));

				arrDetails.put(new JSONObject().put("label", "Complaint Against").put("value",
						complaint_managment.getRdoComplaintAgainstVal()));

				if (complaint_managment.getSelCountyofWarehouseVal() != null
						&& !("--".equals(complaint_managment.getSelCountyofWarehouseVal()))
						&& !("".equals(complaint_managment.getSelCountyofWarehouseVal()))
						&& !("0".equals(complaint_managment.getSelCountyofWarehouseVal()))) {
					arrDetails.put(new JSONObject().put("label", "County of Warehouse").put("value",
							complaint_managment.getSelCountyofWarehouseVal()));
				}

				if (complaint_managment.getSelSubCountyofWarehouseVal() != null
						&& !("--".equals(complaint_managment.getSelSubCountyofWarehouseVal()))
						&& !("".equals(complaint_managment.getSelSubCountyofWarehouseVal()))
						&& !("0".equals(complaint_managment.getSelSubCountyofWarehouseVal()))) {
					arrDetails.put(new JSONObject().put("label", "Sub-County of Warehouse").put("value",
							complaint_managment.getSelSubCountyofWarehouseVal()));
				}

				if (complaint_managment.getSelWarehouseShopNameVal() != null
						&& !("--".equals(complaint_managment.getSelWarehouseShopNameVal()))
						&& !("".equals(complaint_managment.getSelWarehouseShopNameVal()))
						&& !("0".equals(complaint_managment.getSelWarehouseShopNameVal()))) {
					arrDetails.put(new JSONObject().put("label", "Warehouse Shop Name").put("value",
							complaint_managment.getSelWarehouseShopNameVal()));
				}

				if (complaint_managment.getTxtWarehouseOperator() != null
						&& !("--".equals(complaint_managment.getTxtWarehouseOperator()))
						&& !("".equals(complaint_managment.getTxtWarehouseOperator()))
						&& !("0".equals(complaint_managment.getTxtWarehouseOperator()))) {
					arrDetails.put(new JSONObject().put("label", "Warehouse Operator").put("value",
							complaint_managment.getTxtWarehouseOperator()));
				}

				if (complaint_managment.getSelNameofGraderVal() != null
						&& !("--".equals(complaint_managment.getSelNameofGraderVal()))
						&& !("".equals(complaint_managment.getSelNameofGraderVal()))
						&& !("0".equals(complaint_managment.getSelNameofGraderVal()))) {
					arrDetails.put(new JSONObject().put("label", " Name of Grader").put("value",
							complaint_managment.getSelNameofGraderVal()));
				}

				if (complaint_managment.getSelNameofCollateralManagerVal() != null
						&& !("--".equals(complaint_managment.getSelNameofCollateralManagerVal()))
						&& !("".equals(complaint_managment.getSelNameofCollateralManagerVal()))
						&& !("0".equals(complaint_managment.getSelNameofCollateralManagerVal()))) {
					arrDetails.put(new JSONObject().put("label", "Name of Collateral Manager").put("value",
							complaint_managment.getSelNameofCollateralManagerVal()));
				}

				if (complaint_managment.getSelNameofInspectorVal() != null
						&& !("--".equals(complaint_managment.getSelNameofInspectorVal()))
						&& !("".equals(complaint_managment.getSelNameofInspectorVal()))
						&& !("0".equals(complaint_managment.getSelNameofInspectorVal()))) {
					arrDetails.put(new JSONObject().put("label", "Name of Inspector").put("value",
							complaint_managment.getSelNameofInspectorVal()));
				}

				if (complaint_managment.getSelTypeofComplainVal() != null
						&& !("--".equals(complaint_managment.getSelTypeofComplainVal()))
						&& !("".equals(complaint_managment.getSelTypeofComplainVal()))
						&& !("0".equals(complaint_managment.getSelTypeofComplainVal()))) {
					arrDetails.put(new JSONObject().put("label", "Type of Complain").put("value",
							complaint_managment.getSelTypeofComplainVal()));
				}

				arrDetails.put(new JSONObject().put("label", "Date of Incident ").put("value", complaint_managment
						.getTxtDateofIncident().toLocalDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))));

				arrDetails.put(new JSONObject().put("label", "Description of Incident").put("value",
						complaint_managment.getTxtrDescriptionofIncident()));
				List<Supporting_document> supporting_documentList = supporting_documentRepository
						.findByIntParentIdAndBitDeletedFlag(complaint_managment.getIntId(), false);
				arrDetails.put(new JSONObject().put("label", "Supporting Documents").put("SupportingDoc",
						supporting_documentList));

				status = "200";
				outMsg = "Success!!!";
			}

		} catch (Exception e) {
			logger.error("Complaint_managmentServiceImpl:getPreviewDetails()");
			status = "400";
			outMsg = "Something went Wrong!!!";
		}
		JSONObject result = new JSONObject();
		result.put("status", status);
		result.put("msg", outMsg);
		result.put("result", arrDetails);
		return result;
	}

	@Override
	public JSONObject deleteById(Integer id) throws Exception {
		logger.info("Inside deleteById method of Complaint_managmentServiceImpl");
		// Complaint_managment entity =
		// complaint_managmentRepository.findByIntIdAndBitDeletedFlag(id, false);

		ComplaintManagementNewEntity entity = newcomplaint_managmentRepository.findByIdAndDeletedFlag(id, false);

		entity.setDeletedFlag(true);
		newcomplaint_managmentRepository.save(entity);
		List<Supporting_document> supporting_documentList = supporting_documentRepository
				.findByIntParentIdAndBitDeletedFlag(id, false);
		supporting_documentList.forEach(t -> t.setBitDeletedFlag(true));
		supporting_documentRepository.saveAll(supporting_documentList);
		json.put("status", 200);
		return json;
	}

	@Override
	public Page<ComplaintmanagementResponse> getFilteredComplaint(String search, String sortColumn,
			String sortDirection, Pageable sortedPageable) {
		logger.info("Inside getFilteredComplaint method of Complaint_managementServiceImpl");

		Integer totalDataPresent = complaint_managmentRepository.countByBitDeletedFlag(false);
		logger.info("totalDataPresent" + totalDataPresent);

		List<ComplaintApplicationStatus> applicationStatuses = Arrays.asList(ComplaintApplicationStatus.SUSPEND,
				ComplaintApplicationStatus.REVOKE_LICENSE);
		Page<Complaint_managment> response = complaint_managmentRepository.findByFilters(search, sortedPageable,
				applicationStatuses);
		logger.info("page contents" + response.getContent());
		logger.info("Total pages" + response.getTotalPages());
		logger.info("Total elements" + response.getTotalElements());

		List<ComplaintmanagementResponse> complaintManagementResponse = response.getContent().stream().map(entity -> {
			ComplaintmanagementResponse dto = Mapper.mapToComplaintmanagementResponse(entity);
			String AOCno = entity.getSelWarehouseShopName();
			String commodityTypes = applicationOfConformityRepository.getCommodityTypes(AOCno, Status.Accepted, false);

			if (commodityTypes == null || commodityTypes.isEmpty()) {
				logger.warn("No commodity types found for AOCno: " + AOCno);
				dto.setTypeOfCommodities("---");
				dto.setOtherCommodities(Collections.emptyList());
			} else {
				logger.info("commodity types" + commodityTypes);
				try {
					List<Commodity> commodities = om.readValue(commodityTypes, new TypeReference<List<Commodity>>() {
					});
					logger.info("list of commodity" + commodities);
					if (!commodities.isEmpty()) {
						dto.setTypeOfCommodities(commodities.get(0).getName());
						logger.info("commodity inside commodity" + dto.getTypeOfCommodities());
						List<String> otherCommodities = new ArrayList<>();
						for (int i = 1; i < commodities.size(); i++) {
							otherCommodities.add(commodities.get(i).getName());
						}
						dto.setOtherCommodities(otherCommodities);
						logger.info("otherCommodities" + dto.getOtherCommodities());
					}
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}

			if (entity.getSelWarehouseShopName() != null && !entity.getSelWarehouseShopName().toString().equals("0")) {
				dto.setSelWarehouseShopName(entity.getSelWarehouseShopName());
			} else {
				dto.setSelWarehouseShopName("---");
			}

			if (entity.getTxtWarehouseOperator() != null && !entity.getTxtWarehouseOperator().toString().isEmpty()) {
				dto.setTxtWarehouseOperator(entity.getTxtWarehouseOperator());
			} else {
				dto.setTxtWarehouseOperator("---");
			}

			Integer count = onlineServiceApprovalNotingsRepository.getCountedData(12, entity.getIntId());
			dto.setNotingCount(count);

			TOnlineServiceApproval approval = tOnlineServiceApprovalRepository
					.findByIntOnlineServiceIdAndIntStageNoAndBitDeletedFlag(160, entity.getIntId(), false);
			if (approval != null) {
				dto.setTinStatus(approval.getTinStatus());
				dto.setIntProcessId(approval.getIntProcessId());
				dto.setResubmitCount(onlineServiceApprovalNotingsRepository
						.countByIntOnlineServiceIdAndIntProcessIdAndIntStatusAndBitDeletedFlag(entity.getIntId(),
								approval.getIntProcessId(), 3, false));
				String pendingAtUser = tuserRepository.getPendignAtUser(Integer.parseInt(approval.getIntPendingAt()));
				dto.setPendingATUser(pendingAtUser);
			}
			dto.setSelCountyVal(selCounty.has(entity.getSelCounty().toString())
					? (String) selCounty.get(entity.getSelCounty().toString())
					: "--");
			dto.setSelSubCountyVal(selSubCounty.has(entity.getSelSubCounty().toString())
					? (String) selSubCounty.get(entity.getSelSubCounty().toString())
					: "--");
			return dto;
		}).collect(Collectors.toList());
		return new PageImpl<>(complaintManagementResponse, sortedPageable, response.getTotalElements());
	}

	@Override
	public ComplaintDetailsComprehensive findById(int id) {
		Complaint_managment entity = complaint_managmentRepository.findByIntIdAndBitDeletedFlag(id, false);
		if (entity == null) {
			throw new CustomGeneralException(errorMessages.getFailedToFetch());
		}

		ComplaintmanagementResponse dto = Mapper.mapToComplaintmanagementResponse(entity);

		String AOCno = entity.getSelWarehouseShopName();
		String commodityTypes = applicationOfConformityRepository.getCommodityTypes(AOCno, Status.Accepted, false);

		if (commodityTypes == null || commodityTypes.isEmpty()) {
			logger.warn("No commodity types found for AOCno: " + AOCno);
			dto.setTypeOfCommodities("---");
			dto.setOtherCommodities(Collections.emptyList());
		} else {
			logger.info("commodity types" + commodityTypes);
			try {
				List<Commodity> commodities = om.readValue(commodityTypes, new TypeReference<List<Commodity>>() {
				});
				logger.info("list of commodity" + commodities);
				if (!commodities.isEmpty()) {
					dto.setTypeOfCommodities(commodities.get(0).getName());
					logger.info("commodity inside commodity" + dto.getTypeOfCommodities());
					List<String> otherCommodities = new ArrayList<>();
					for (int i = 1; i < commodities.size(); i++) {
						otherCommodities.add(commodities.get(i).getName());
					}
					dto.setOtherCommodities(otherCommodities);
					logger.info("otherCommodities" + dto.getOtherCommodities());
				}
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}

		if (entity.getSelWarehouseShopName() != null && !entity.getSelWarehouseShopName().toString().equals("0")) {
			dto.setSelWarehouseShopName(entity.getSelWarehouseShopName());
		} else {
			dto.setSelWarehouseShopName("---");
		}

		if (entity.getTxtWarehouseOperator() != null && !entity.getTxtWarehouseOperator().toString().isEmpty()) {
			dto.setTxtWarehouseOperator(entity.getTxtWarehouseOperator());
		} else {
			dto.setTxtWarehouseOperator("---");
		}

		List<ComplaintObservationResponse> observationResponse = complaintObservationResponseRepository
				.findByObResAPIdAndObResStageAndDeleteFlag(id, 7, false);
		ComplaintObservationResponse observation = observationResponse.get(0);
		OnlineServiceApprovalNotings approvalNotings = onlineServiceApprovalNotingsRepository
				.findByIntNotingsIdAndBitDeletedFlag(observation.getNotingId(), false);

		return ComplaintDetailsComprehensive.builder().complaintManagementResponse(dto).approvalNotings(approvalNotings)
				.observationResponses(observationResponse).build();
	}

	@Override
	public List<ComplaintObservation> getComplaintOservation(Integer lableId, Integer RoleId) {

		return observationRepo.findByIntLableIdAndIntRoleId(lableId, RoleId);
	}

	@Override
	public String documentRemoved(int DocId) {
		supporting_documentRepository.deleteById(DocId);
		return "Document Removed";
	}

	public void main() throws ExecutionException, InterruptedException {
		// Create WebSocket client
		StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
		WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
		stompClient.setMessageConverter(new StringMessageConverter());

		// WebSocket server URL (Replace with your WebSocket URL)
		String url = "ws://localhost:8088/websocket";

		// Connect to the WebSocket
		StompSession session = stompClient.connect(url, new StompSessionHandlerAdapter() {
		}).get();

		// Subscribe to a topic
		session.subscribe("/user/" + 5 + "/topic/getNotifications'", new StompSessionHandlerAdapter() {
		});

		// Send a message
		session.send("/app/sendNotification", "Hello, WebSocket!");

		System.out.println("Message sent to WebSocket server");
	}

	@Override
	public JSONObject checkOperatorSusStatus(Integer lableId, Integer userId) throws Exception {
		// TODO Auto-generated method stub

		JSONObject jsonObj = new JSONObject();
		try {
			Optional<Tuser> userOpt = tuserRepository.findById(userId);
			Tuser user = userOpt.get();

			if (lableId == 100) {
				boolean susStatus = user.isOplsuspStatus();
				jsonObj.put("msg", "the warehouse operator license Suspended");
				jsonObj.put("suspStatus", susStatus);
				jsonObj.put("status", 200);
				jsonObj.put("lableId", 100);

			} else if (lableId == 101) {
				boolean susStatus = user.isOpcsuspStatus();
				jsonObj.put("msg", "the warehouse operator certificate Suspended");
				jsonObj.put("suspStatus", susStatus);
				jsonObj.put("status", 200);
				jsonObj.put("lableId", 101);
			} else {
				jsonObj.put("msg", "A complaint can be raised.");
				jsonObj.put("suspStatus", false);
				jsonObj.put("lableId", 0);
				jsonObj.put("status", 200);
			}

		} catch (Exception e) {
			jsonObj.put("msg", "Internal Problem Occured");
			jsonObj.put("suspStatus", true);
			jsonObj.put("status", 201);
		}

		return jsonObj;
	}

	@Override
	public String getICMemberList(Integer appId,Integer lableId) throws Exception {
		JSONObject response = new JSONObject();
		List<Map<String, Object>> jsonList = new ArrayList<>();
		List<Map<String, Object>> jsonList1 = new ArrayList<>();
		try {
			
			List<Object[]> results=null;
			List<Object[]> results1=null;
			if(lableId==105||lableId==106) {
				results1 = complaint_managmentRepository.findByRevokeICMemberList(appId, lableId);
				if(lableId==105)
				  results = complaint_managmentRepository.findByICMemberList(appId, 100);
				else if(lableId==106)
					results = complaint_managmentRepository.findByICMemberList(appId, 102);
					
			}
			else {
				results = complaint_managmentRepository.findByICMemberList(appId, lableId);
			}
			
			if (jsonList != null) {
				if(results1 != null) {
				
					for (Object[] row : results1) {
						Map<String, Object> jsonRecord1 = new HashMap<>();

						Integer userId=(Integer)row[2];
					
						if(userId!=0) {
							Tuser tuser=tuserRepository.findByIntIdAndBitDeletedFlag(userId,false);
							jsonRecord1.put("userMobile", tuser.getTxtMobileNo());
							jsonRecord1.put("useEmail", tuser.getTxtEmailId());
						}
						jsonRecord1.put("icmRoleId", row[0]);
						jsonRecord1.put("icmUserRoleId", row[1]);
						jsonRecord1.put("icmUserId", row[2]);
						jsonRecord1.put("icmUserName", row[3]);
						jsonRecord1.put("dtmCreatedOn", row[4]);
						jsonRecord1.put("dtmUpdatedOn", row[5]);
						jsonRecord1.put("icmComplaintAppId", row[6]);
						jsonList1.add(jsonRecord1);
					}
				}
				for (Object[] row : results) {
					Map<String, Object> jsonRecord = new HashMap<>();

					Integer userId=(Integer)row[2];
					
					if(userId!=0) {
						Tuser tuser=tuserRepository.findByIntIdAndBitDeletedFlag(userId,false);
						jsonRecord.put("userMobile", tuser.getTxtMobileNo());
						jsonRecord.put("useEmail", tuser.getTxtEmailId());
					}
					jsonRecord.put("icmRoleId", row[0]);
					jsonRecord.put("icmUserRoleId", row[1]);
					jsonRecord.put("icmUserId", row[2]);
					jsonRecord.put("icmUserName", row[3]);
					jsonRecord.put("dtmCreatedOn", row[4]);
					jsonRecord.put("dtmUpdatedOn", row[5]);
					jsonRecord.put("icmComplaintAppId", row[6]);
					jsonList.add(jsonRecord);
				}
			}

		} catch (Exception e) {

			
		}
		response.put("result", jsonList);
		response.put("result1", jsonList1);
		return response.toString();
	}

}