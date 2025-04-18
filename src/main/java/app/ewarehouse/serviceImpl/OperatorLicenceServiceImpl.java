package app.ewarehouse.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import app.ewarehouse.dto.FormOneCRequestDto;
import app.ewarehouse.dto.FormOneCResponseDto;
import app.ewarehouse.dto.Mail;
import app.ewarehouse.dto.OPLApplicationStatusDTO;
import app.ewarehouse.dto.OPLFormDetailsDTO;
import app.ewarehouse.dto.OperatorLicenceApprovalResponseDto;
import app.ewarehouse.dto.OperatorLicenceDTO;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.dto.StakeHolderActionRequestData;
import app.ewarehouse.entity.Action;
import app.ewarehouse.entity.AocCompProfileDetails;
import app.ewarehouse.entity.ApplicationOfConformityLocationDetails;
import app.ewarehouse.entity.CompanyWarehouseDetails;
import app.ewarehouse.entity.ConfirmityFormOneCEntity;
import app.ewarehouse.entity.ConformityMain;
import app.ewarehouse.entity.CreatedStatus;
import app.ewarehouse.entity.InspReportDoc;
import app.ewarehouse.entity.InspectionMemberOPL;
import app.ewarehouse.entity.MFinalOperatorLicense;
import app.ewarehouse.entity.OperatorLicence;
import app.ewarehouse.entity.OperatorLicenceEntity;
import app.ewarehouse.entity.OperatorLicenceFiles;
import app.ewarehouse.entity.OperatorLicenceRemarks;
import app.ewarehouse.entity.OperatorLicenceStatus;
import app.ewarehouse.entity.OplCLC;
import app.ewarehouse.entity.OplInspectorIcm;
import app.ewarehouse.entity.OplServiceApproval;
import app.ewarehouse.entity.OplServiceApprovalNotings;
import app.ewarehouse.entity.OplSetAuthority;
import app.ewarehouse.entity.PaymentAgainst;
import app.ewarehouse.entity.PaymentData;
import app.ewarehouse.entity.PaymentStatus;
import app.ewarehouse.entity.SaveStatus;
import app.ewarehouse.entity.Stakeholder;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.entity.WRSC_OPL_Main;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.AocCompProfileDetRepository;
import app.ewarehouse.repository.ApplicationConformityLocDetRepository;
import app.ewarehouse.repository.ApplicationOfConformityRepository;
import app.ewarehouse.repository.CompanyWarehouseDetailsRepository;
import app.ewarehouse.repository.ConfirmityFormOneCRepo;
import app.ewarehouse.repository.ConformityMainRepository;
import app.ewarehouse.repository.InspectionMemberOPLRepository;
import app.ewarehouse.repository.MFinalOperatorlicenseRepository;
import app.ewarehouse.repository.MroleRepository;
import app.ewarehouse.repository.OPLCLCRepository;
import app.ewarehouse.repository.OPLInspReportDocRepository;
import app.ewarehouse.repository.OPLInspectorIcmRepository;
import app.ewarehouse.repository.OperatorLicenceRemarkRepository;
import app.ewarehouse.repository.OperatorLicenceRepository;
import app.ewarehouse.repository.Operator_LicenceRepository;
import app.ewarehouse.repository.OplServiceApprovalNotingsRepository;
import app.ewarehouse.repository.OplServiceApprovalRepository;
import app.ewarehouse.repository.OplSetAuthorityRepository;
import app.ewarehouse.repository.PaymentDataRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.repository.WRSCOPLMainRepository;
import app.ewarehouse.service.OperatorLicenceService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.EmailUtil;
import app.ewarehouse.util.JsonFileExtractorUtil;
import app.ewarehouse.util.Mapper;
import app.ewarehouse.util.TupleToJsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperatorLicenceServiceImpl implements OperatorLicenceService {

	private final OperatorLicenceRepository operatorLicenceRepository;

	private final MFinalOperatorlicenseRepository mFinalOperatorlicenseRepository;

	private final OperatorLicenceRemarkRepository operatorLicenceRemarkRepository;

	private final ObjectMapper objectMapper;

	private final ApplicationOfConformityRepository aocRepository;

	private final JdbcTemplate jdbcTemplate;

	private final TuserRepository userRepo;

	private OperatorLicence operatorLicence1;
	ResponseDTO<String> responseDTO = new ResponseDTO<>();

	@Autowired
	ConfirmityFormOneCRepo documentRepository;
	@Autowired
	Operator_LicenceRepository newoperatorLicenceRepository;
	@Autowired
	OplSetAuthorityRepository setAuthorityRepository;
	@Autowired
	OplServiceApprovalRepository OplServiceApprovalRepository;
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${finalUpload.path}")
	private String finalUploadPath;
	@Autowired
	WorkflowServiceImpl workflowServiceImpl;
	@Autowired
	PaymentDataRepository paymentRepo;
	@Autowired
	OplServiceApprovalNotingsRepository approvalNotingsRepo;
	@Autowired
	OPLCLCRepository oplRepository;
	@Autowired
	OPLInspectorIcmRepository oplInspectorIcmRepository;
	@Autowired
	InspectionMemberOPLRepository inspectionMemberOPLRepository;
	@Autowired
	OPLInspReportDocRepository oplInspReportDocRepository;
	@Autowired
	MroleRepository mroleRepository;
    @Autowired
    WRSCOPLMainRepository wrsoplMainRepository;

    @Autowired
    ConformityMainRepository conformityMainRepository;
    @Autowired
    ApplicationConformityLocDetRepository    confirmityLocDetails;
    @Autowired
    CompanyWarehouseDetailsRepository companyWarehouseDetailsRepository;
    @Autowired
    AocCompProfileDetRepository  aocCompProfileDetRepository;
    @Autowired
    OplServiceApprovalNotingsRepository oplServiceApprovalNotingsRepository;

	@Transactional
	@Override
	public String handleAction(String actionRequestData, Stakeholder forwardedTo, Stakeholder actionTakenBy,
			Action action, Status status) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(actionRequestData);
		StakeHolderActionRequestData actionRequest;
		try {
			actionRequest = objectMapper.readValue(decodedData, StakeHolderActionRequestData.class);
			System.out.println("StakeHolder Action Request data...." + actionRequest);
		} catch (Exception e) {
			throw new CustomGeneralException("Invalid data format: " + e);
		}
		System.out.println(actionRequest.getId());

		OperatorLicence operatorLicence = operatorLicenceRepository.findById(actionRequest.getId())
				.orElseThrow(() -> new RuntimeException("Operator Licence not found"));

		System.out.println("Fetched Operator Licence by id data ..." + operatorLicence);

		OperatorLicenceRemarks remark = new OperatorLicenceRemarks();
		remark.setOperatorLicence(operatorLicence);
		remark.setRemark(actionRequest.getRemark());
		remark.setCreatedBy(actionRequest.getCreatedBy());
		remark.setStakeholder(actionTakenBy);
//		System.out.println("Remark"+remark);
		operatorLicenceRemarkRepository.save(remark);

		if (actionRequest.getInspectionReport() != null
				&& !actionRequest.getInspectionReport().getFilePath().isBlank()) {
			OperatorLicenceFiles file = actionRequest.getInspectionReport();
			file.setFilePath(JsonFileExtractorUtil.uploadFile(file.getFilePath()));
			file.setOperatorLicence(operatorLicence);

			if (operatorLicence.getFiles() == null) {
				operatorLicence.setFiles(new ArrayList<>());
			}
			operatorLicence.getFiles().add(file);
		}

		if ("forward".equalsIgnoreCase(actionRequest.getAction())) {
			operatorLicence.setForwardedTo(forwardedTo);
			operatorLicence.setAction(action);
			operatorLicence.setStatus(status);
			System.out.println("Current action..." + action);
			System.out.println("Current Status..." + status);
		} else if ("reject".equalsIgnoreCase(actionRequest.getAction())) {
			operatorLicence.setStatus(Status.Rejected);
			operatorLicence.setForwardedTo(Stakeholder.APPLICANT);
			operatorLicence.setAction(Action.Rejected);
		}
		Status newStatus = operatorLicence.getStatus();
		System.out.println("Final Operator Licence data I am saving...." + operatorLicence);

		OperatorLicence savedOperatorLicense = operatorLicenceRepository.save(operatorLicence);

		if (Status.Approved.equals(newStatus)) {
			Tuser user = userRepo.findById(operatorLicence.getIntCreatedBy()).get();
			user.setSelRole(6); // Warehouse Operator
			user.setWarehouseId(aocRepository
					.findByIntCreatedByAndEnmStatus(savedOperatorLicense.getIntCreatedBy(), Status.Accepted).get(0)
					.getApplicationId());
			userRepo.save(user);
		}

		if (status.equals(Status.Approved) && savedOperatorLicense.getEnmSaveStatus().equals(SaveStatus.New)) {
			System.out.println("Approving");
			MFinalOperatorLicense finalOperatorLicense = new MFinalOperatorLicense();
			finalOperatorLicense.setOperatorLicenceApplication(savedOperatorLicense);
			finalOperatorLicense.setEnmStatus(CreatedStatus.Created);
			finalOperatorLicense.setIntApprovedBy(actionRequest.getApprovedBy());
			finalOperatorLicense.setApplicationOfConformity(aocRepository
					.findByIntCreatedByAndEnmStatus(savedOperatorLicense.getIntCreatedBy(), Status.Accepted).get(0));
			mFinalOperatorlicenseRepository.save(finalOperatorLicense);
			System.out.println("Approved");
		}

		ResponseDTO<String> responseDTO = new ResponseDTO<>();
		responseDTO.setStatus(HttpStatus.OK.value());
		responseDTO.setResult("Action successfully completed");
		responseDTO.setMessage("Operator Licence action processed successfully");

		return CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString();
	}

	@Override
	public Page<OperatorLicenceDTO> getApplications(Pageable pageable, Status status, Stakeholder stakeholder,
			Action action, String search) {
		Page<OperatorLicence> licences = operatorLicenceRepository.findByStatusAndForwardedToAndAction(status,
				stakeholder, action, pageable, search);
//        System.out.println(licences.getContent());
		return licences.map(Mapper::convertToDto);
	}

	@Transactional
	@Override
	public String saveOperatorLicence(String operatorLicence) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(operatorLicence);
		System.out.println(decodedData);
		try {
			operatorLicence1 = objectMapper.readValue(decodedData, OperatorLicence.class);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomGeneralException("Invalid data format");
		}
		System.out.println(operatorLicence1);
		operatorLicence1.setFiles(operatorLicence1.getFiles().stream().map(this::saveFile).toList());
		System.out.println(operatorLicence1);
		operatorLicence1.setStatus(Status.Pending);
		operatorLicence1.setForwardedTo(Stakeholder.TECHNICAL);
		operatorLicence1.setAction(Action.Pending);
		if (!operatorLicence1.getEnmSaveStatus().equals(SaveStatus.Renew)) {
			operatorLicence1.setVchApplicationNo(generateUniqueNo("t_operator_licence", "vchApplicationNo"));
		}
		OperatorLicence savedOperatorLicence = operatorLicenceRepository.save(operatorLicence1);
		if (savedOperatorLicence.getEnmSaveStatus().equals(SaveStatus.Renew)) {
			operatorLicenceRemarkRepository.deleteAllByOperatorLicence(savedOperatorLicence);
		}
		Integer operatorId = savedOperatorLicence.getId();

		responseDTO.setStatus(HttpStatus.CREATED.value());
		responseDTO.setResult(savedOperatorLicence.getVchApplicationNo());
		responseDTO.setMessage("Your Operator Licence Number is " + operatorId);
		return CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString();
	}

	private OperatorLicenceFiles saveFile(OperatorLicenceFiles operatorLicenceFiles) {
		operatorLicenceFiles.setFilePath(JsonFileExtractorUtil.uploadFile(operatorLicenceFiles.getFilePath()));
		operatorLicenceFiles.setOperatorLicence(this.operatorLicence1);
		return operatorLicenceFiles;
	}

	@Override
	public Page<OperatorLicence> getOperatorLicences(Status status, Integer userId, PageRequest pageRequest) {
		return operatorLicenceRepository.findByFilters(status, userId, pageRequest);
	}

	@Override
	public List<OperatorLicence> getOperatorLicences(Status status, Integer userId) {
		return operatorLicenceRepository.findByFilters(status, userId);
	}

	@Override
	public Page<OperatorLicenceDTO> getAllApplications(Pageable pageable, Stakeholder forwardedTo, String search) {
		Page<OperatorLicence> licences = operatorLicenceRepository.findByForwardedTo(pageable, forwardedTo, search);
		return licences.map(Mapper::convertToDto);
	}

	@Override
	public OperatorLicence getOperatorLicence(Integer id) {
		return operatorLicenceRepository.findById(id).get();
	}

	public String generateUniqueNo(String tableName, String idName) {
		return jdbcTemplate.execute((Connection connection) -> {
			try (CallableStatement callableStatement = connection.prepareCall("{CALL GenerateCustomID(?, ?, ?)}")) {
				callableStatement.setString(1, tableName);
				callableStatement.setString(2, idName);
				callableStatement.registerOutParameter(3, Types.VARCHAR);
				callableStatement.execute();
				return callableStatement.getString(3);
			} catch (SQLException e) {
				throw new RuntimeException("Error generating custom ID", e);
			}
		});
	}

	@Override
	public String getOperatorWarehouseName(String compId) {

		return new TupleToJsonConverter()
				.convertListTupleToJsonArray(operatorLicenceRepository.getOperatorWarehouseByCompanyId(compId));
	}
@Transactional
	@Override
	public JSONObject saveDocuments(List<FormOneCRequestDto> documentRequestDtos) {

		JSONObject object = new JSONObject();
		try {
			String warehouseId = documentRequestDtos.get(0).getWarehouseId();
			String formOnecId = documentRequestDtos.get(0).getFormOneCId();
			Integer userId = documentRequestDtos.get(0).getUserid();
			Integer roleId = documentRequestDtos.get(0).getRoleid();
			String actionType = documentRequestDtos.get(0).getActionType();
			boolean isDraftEDit = documentRequestDtos.get(0).isDraftEDit();
			Integer valueId = documentRepository.checkFormStatus(warehouseId);

			if (valueId == 0) {
				String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
				Optional<ConfirmityFormOneCEntity> latestWarehouse = documentRepository.findTopByFormOneCIdStartingWithOrderByFormOneCIdDesc("FormOneC");

				String lastWarehouseId = null;
				if (latestWarehouse.isPresent()) {
					lastWarehouseId = latestWarehouse.get().getFormOneCId();
				}
				//String formOneCId = generateFormOneCId(lastWarehouseId, "FormOneC");
				
				String formOneCId="FormOneC"+generateUniqueId(3);
				
				List<ConfirmityFormOneCEntity> documents = documentRequestDtos.stream().map(dto -> {
					ConfirmityFormOneCEntity document = new ConfirmityFormOneCEntity();
					document.setCreatedBy(dto.getUserid());
					document.setWareHouseId(dto.getWarehouseId());
					document.setDocName(dto.getFileName());
					document.setFileName(dto.getUploadedFileName());
					document.setCreatedOn(LocalDateTime.now());
					document.setDeletedFlag(false);
					document.setFormOneCId(formOneCId);
					return document;
				}).collect(Collectors.toList());
				List<ConfirmityFormOneCEntity> saveDocuments = documentRepository.saveAllAndFlush(documents);

				if (saveDocuments != null && saveDocuments.size() > 0) {
					documentRequestDtos.forEach(fileUpload -> {
						if (!Strings.isNullOrEmpty(fileUpload.getUploadedFileName())) {

							File f = new File(tempUploadPath + fileUpload.getUploadedFileName());
							if (f.exists()) {
								File src = new File(tempUploadPath + fileUpload.getUploadedFileName());
								Path path = Paths.get(finalUploadPath + "operator_licence");

								if (!Files.exists(path) && !Files.isDirectory(path)) {
									try {
										Files.createDirectories(path);
									} catch (IOException e) {
										log.info("Inside getById method of Complaint_managmentServiceImpl"
												+ e.getMessage());

									}
								}

								try {
									File dest = new File(finalUploadPath + "operator_licence/" + fileUpload.getUploadedFileName());
									Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
									Files.delete(src.toPath());
								} catch (IOException e) {
									log.error(
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

				if (saveDocuments != null && saveDocuments.size() > 0) {
					object.put("status", 200);
					object.put("Msg", "Form 1C Submitted");
					object.put("formOneCId", formOneCId);
				} else {
					object.put("status", 201);
					object.put("Msg", "Unable to process the request");
					object.put("formOneCId", "0");
				}
			} else {
				 documentRepository.findByWareHouseIdAndDeletedFlagFalse(warehouseId).stream()
					.map(doc -> new FormOneCResponseDto(doc.getDocName(), doc.getFileName(), doc.getFormOneCId(),doc.getId()))
					.collect(Collectors.toList()).forEach(fileUpload -> {
						if (!Strings.isNullOrEmpty(fileUpload.getUploadedFileName())) {

							try {
								
								File src = new File(finalUploadPath + "operator_licence/" + fileUpload.getUploadedFileName());
								if(src.exists()) {
									File dest = new File(tempUploadPath + fileUpload.getUploadedFileName());
									Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
									Files.delete(src.toPath());
								}
								
							} catch (IOException e) {
								log.error("Inside save method of Complaint_managmentServiceImpl some error occur in file moving to destination folder:"+ e);
							}
						}
					});
				 documentRepository.deleteByWareHouseId(warehouseId);
				 
				 
					List<ConfirmityFormOneCEntity> documents = documentRequestDtos.stream().map(dto -> {
						ConfirmityFormOneCEntity document = new ConfirmityFormOneCEntity();
						document.setCreatedBy(dto.getUserid());
						document.setWareHouseId(dto.getWarehouseId());
						document.setDocName(dto.getFileName());
						document.setFileName(dto.getUploadedFileName());
						document.setCreatedOn(LocalDateTime.now());
						document.setDeletedFlag(false);
						document.setFormOneCId(formOnecId);
						return document;
					}).collect(Collectors.toList());
					List<ConfirmityFormOneCEntity> saveDocuments = documentRepository.saveAllAndFlush(documents);

					if (saveDocuments != null && saveDocuments.size() > 0) {
						documentRequestDtos.forEach(fileUpload -> {
							if (!Strings.isNullOrEmpty(fileUpload.getUploadedFileName())) {
								File f = new File(tempUploadPath + fileUpload.getUploadedFileName());
								if (f.exists()) {
									File src = new File(tempUploadPath + fileUpload.getUploadedFileName());
									Path path = Paths.get(finalUploadPath + "operator_licence");

									if (!Files.exists(path) && !Files.isDirectory(path)) {
										try {
											Files.createDirectories(path);
										} catch (IOException e) {
											log.info("Inside getById method of Complaint_managmentServiceImpl"
													+ e.getMessage());

										}
									}

									try {
										File dest = new File(finalUploadPath + "operator_licence/" + fileUpload.getUploadedFileName());
										Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
										Files.delete(src.toPath());
									} catch (IOException e) {
										log.error(
												"Inside save method of Complaint_managmentServiceImpl some error occur in file moving to destination folder:"
														+ e);
									}
									
								}
							}
						});
				 
				 
					}
					
					
					if(isDraftEDit) {
						
						List<Map<String, Object>> mapObjlist = workflowServiceImpl.getAllComplaintStatus();
						String appicationStatus = mapObjlist.stream()
								.filter(x -> Integer.parseInt(x.get("intId").toString()) == 19).collect(Collectors.toList())
								.get(0).get("complaintStatus").toString();
						
						OperatorLicenceEntity licenseEntity=newoperatorLicenceRepository.findByWarehouseIdAndFormOneCIdAndDeletedFlagFalse(warehouseId,formOnecId);
						
						licenseEntity.setApplicationStatus(appicationStatus);
						licenseEntity.setApprovalStatus(OperatorLicenceStatus.PENDING);
						licenseEntity.setApprovedBy(0);
						licenseEntity.setApprovedByRole(0);
						licenseEntity.setCnfApprovalDate(LocalDate.now());
						licenseEntity.setApproverRemarks(null);
						licenseEntity.setFormOneCId(formOnecId);
						licenseEntity=newoperatorLicenceRepository.saveAndFlush(licenseEntity);
						OplSetAuthority setAuthority = setAuthorityRepository.findByProcessIdAndStageNoAndLabelId(200, 1, 301);
						Integer setauthId = setAuthority.getSetAuthId();
						Integer authId = setAuthority.getPrimaryAuth();
						Integer introleId = setAuthority.getRoleId();
						Integer stageNo = setAuthority.getStageNo();
						Integer lableId = setAuthority.getLabelId();
						Integer processId = setAuthority.getProcessId();
						Integer serviceId = licenseEntity.getLicenceSno();
						
//						OplServiceApproval serviceApproval = OplServiceApprovalRepository
//								.findByOnlineServiceIdAndProcessIdAndStageNoAndLabelId(serviceId, processId, stageNo, lableId);
//						
//						if (serviceApproval != null) {
//							serviceApproval.setDeletedFlag(true);
//							OplServiceApprovalRepository.save(serviceApproval);
//						}
						
						OplServiceApprovalRepository.deleteByOnlineServiceId(serviceId);
						
						OplServiceApproval oplServiceApproval = new OplServiceApproval();

						oplServiceApproval.setOnlineServiceId(serviceId);
						oplServiceApproval.setStageNo(stageNo);
						oplServiceApproval.setPendingAt(authId);
						oplServiceApproval.setForwardTo(introleId);
						oplServiceApproval.setForwardedUserId(userId);
						oplServiceApproval.setLabelId(lableId);
						oplServiceApproval.setProcessId(processId);
						oplServiceApproval.setAtaProcessId(setauthId);
						oplServiceApproval.setDeletedFlag(false);
						oplServiceApproval.setInspAllocated(false);
						oplServiceApproval.setClcAllocated(false);
						OplServiceApprovalRepository.saveAndFlush(oplServiceApproval);
						licenseEntity.setFormOneCId(formOnecId);
						licenseEntity=newoperatorLicenceRepository.saveAndFlush(licenseEntity);
						object.put("status", 200);
						object.put("Msg", "Form 1C Submitted");
						object.put("formOneCId", licenseEntity.getFormOneCId());
					}
					else {
						OperatorLicenceEntity licenseEntity=newoperatorLicenceRepository.findByWarehouseIdAndFormOneCIdAndDeletedFlagFalse(warehouseId,formOnecId);
						if(licenseEntity!=null) {
							licenseEntity.setFormOneCId(formOnecId);
							licenseEntity=newoperatorLicenceRepository.saveAndFlush(licenseEntity);
							object.put("status", 200);
							object.put("Msg", "Form 1C Updated");
							object.put("formOneCId", licenseEntity.getFormOneCId());
						}
						object.put("status", 200);
						object.put("Msg", "Form 1C Updated");
						object.put("formOneCId",formOnecId);
						
					}
				
			}

		} catch (Exception e) {
			log.info("Inside Form One C Service Impl" + e.getMessage());
		}

		return object;
	}

	@Override
	public List<FormOneCResponseDto> getFilesByWarehouseId(String warehouseId) {
		return documentRepository.findByWareHouseIdAndDeletedFlagFalse(warehouseId).stream()
				.map(doc -> new FormOneCResponseDto(doc.getDocName(), doc.getFileName(), doc.getFormOneCId(),doc.getId()))
				.collect(Collectors.toList());
	}
	
	
	 public  String generateUniqueId(int digits) {
		 // Get current date & time in YYYYMMDDHHmmss format
	        String dateTime = new SimpleDateFormat("yyyyMMdd").format(new Date());

	        // Generate a random number with the specified number of digits
	        int min = (int) Math.pow(10, digits - 1); // Minimum value (e.g., 100000 for 6 digits)
	        int max = (int) Math.pow(10, digits) - 1; // Maximum value (e.g., 999999 for 6 digits)
	        int randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);

	        // Combine date-time and random number
	        return dateTime + randomNumber;
	    }
	 

	private String generateFormOneCId(String lastWarehouseId, String Startwith) {

		// Step 2: Query the most recent warehouse ID that starts with "WAR" +
		// currentDate

		String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		// Step 3: Set the next ID to 1 if no previous warehouse IDs exist for today
		int nextId = 1;

		// Step 4: If a previous warehouse ID exists, extract and increment the last
		// number

		// Ensure the ID has the correct format (i.e., 11 characters: "WARyyyyMMddxxx")
		if (lastWarehouseId != null && lastWarehouseId.length() >= 11) {
			String lastNumberStr = lastWarehouseId.substring(11);
			try {
				// Try to parse the last part of the warehouse ID
				int lastNumber = Integer.parseInt(lastNumberStr);
				nextId = lastNumber + 1;
			} catch (NumberFormatException e) {
				// If parsing fails, log the error and default to ID 1
				System.err.println("Error parsing warehouse ID number: " + e.getMessage());
				nextId = 1;
			}
		} else {
			// If the warehouse ID format is invalid, default to ID 1
			System.err.println("Invalid warehouse ID format: " + lastWarehouseId);
			nextId = 1;
		}
		// Step 5: Format the next ID to be a 3-digit number
		String nextIdStr = String.format("%03d", nextId);

		// Step 6: Create the final warehouse ID
		return Startwith +currentDate+nextIdStr;
	}

	@Transactional
	@Override
	public JSONObject saveLicenceApplication(JSONObject data) {

		JSONObject object = new JSONObject();

		try {

			String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
			Optional<OperatorLicenceEntity> latestWarehouse = newoperatorLicenceRepository.findTopByOplIdStartingWithOrderByOplIdDesc("WRSOPL"+currentDate);
			String lastWarehouseId = null;
			if (latestWarehouse.isPresent()) {
				lastWarehouseId = latestWarehouse.get().getOplId();
			}
			//String OPLId = generateFormOneCId(lastWarehouseId, "WRSOPL");
			
			String OPLId ="WRSOPL"+generateUniqueId(3);

			String selectedWareHouseId = data.getString("selectedWareHouseId");
			Integer confirmityId = Integer.parseInt(data.getString("confirmityId"));
			String formOneCId = data.getString("formOneCId");
			Integer paymentId = data.getInt("paymentId");
			Integer oplSno = data.getInt("oplSno");
			String companyId = data.getString("companyId");
			Integer userId = data.getInt("userId");
			Integer roleId = data.getInt("roleId");
			OperatorLicenceEntity LicenceEntity = null;
			OperatorLicenceEntity saveData = null;
			if (oplSno != 0) {

				LicenceEntity = newoperatorLicenceRepository.findByLicenceSnoAndDeletedFlagFalse(oplSno);
				LicenceEntity.setPaymentId(paymentId);
				LicenceEntity.setPaymentStatus("PAID");
				LicenceEntity.setUpdatedOn(LocalDateTime.now());
				LicenceEntity.setDeletedFlag(false);
				LicenceEntity.setLicenseGen(false);
				LicenceEntity.setLicenceCertGen(false);
				ConformityMain	conformityMain = conformityMainRepository.findByWarehouseId(selectedWareHouseId);
				conformityMain.setLicenceCerti(false);
				conformityMain.setLicenseGen(false);
				conformityMain.setBitOPLApplied(true);
				conformityMain.setIntOPLAppId(oplSno);				
				
				conformityMainRepository.saveAndFlush(conformityMain);
				LicenceEntity.setApprovalStatus(OperatorLicenceStatus.PENDING);
				saveData = newoperatorLicenceRepository.saveAndFlush(LicenceEntity);
				PaymentData payment_data = paymentRepo.findByIdAndEnmPaymentStatusAndBitDeletedFlag(
						Long.valueOf(paymentId), PaymentStatus.UNPAID, false);
				payment_data.setEnmPaymentStatus(PaymentStatus.PAID);
				payment_data.setEnmPaymentAgainst(PaymentAgainst.OPL);
				paymentRepo.saveAndFlush(payment_data);
				
				Mail mail = new Mail();
				mail.setMailSubject("Submission Confirmation – Operator License Application");
				mail.setContentType("text/html");
				//mail.setMailCc("uiidptestmail@gmail.com");
				mail.setTemplate("Your application for an Operator License has been submitted. The application number is "+LicenceEntity.getOplId());
				mail.setMailTo(conformityMain.getCompany().getEmail());
				EmailUtil.sendMail(mail.getMailSubject(), mail.getTemplate(),mail.getMailTo());

			} // update
			else {

				List<Map<String, Object>> mapObjlist = workflowServiceImpl.getAllComplaintStatus();
				String appicationStatus = mapObjlist.stream()
						.filter(x -> Integer.parseInt(x.get("intId").toString()) == 19).collect(Collectors.toList())
						.get(0).get("complaintStatus").toString();
				
				String formStatus = mapObjlist.stream()
						.filter(x -> Integer.parseInt(x.get("intId").toString()) == 15).collect(Collectors.toList())
						.get(0).get("complaintStatus").toString();
				LicenceEntity = new OperatorLicenceEntity();
				LicenceEntity.setWarehouseId(selectedWareHouseId);
				LicenceEntity.setCompanyId(companyId);
				LicenceEntity.setConfirmityId(confirmityId);
				LicenceEntity.setFormOneCId(formOneCId);
				LicenceEntity.setPaymentId(paymentId);
				LicenceEntity.setCreatedBy(userId);
				LicenceEntity.setCreatedRoleId(roleId);
				LicenceEntity.setCreatedOn(LocalDateTime.now());
				LicenceEntity.setDeletedFlag(false);
				LicenceEntity.setLicenseGen(false);
				LicenceEntity.setLicenceCertGen(false);
				LicenceEntity.setApprovalStatus(OperatorLicenceStatus.PENDING);
				LicenceEntity.setPaymentStatus("UNPAID");
				LicenceEntity.setApplicationStatus(appicationStatus);
				LicenceEntity.setFormStatus(formStatus);
				LicenceEntity.setOplId(OPLId);
				saveData = newoperatorLicenceRepository.saveAndFlush(LicenceEntity);
				OplSetAuthority setAuthority = setAuthorityRepository.findByProcessIdAndStageNoAndLabelId(200, 1, 301);
				Integer setauthId = setAuthority.getSetAuthId();
				Integer authId = setAuthority.getPrimaryAuth();
				Integer introleId = setAuthority.getRoleId();
				Integer stageNo = setAuthority.getStageNo();
				Integer lableId = setAuthority.getLabelId();
				Integer processId = setAuthority.getProcessId();
				Integer serviceId = saveData.getLicenceSno();
				OplServiceApproval serviceApproval = OplServiceApprovalRepository.findByOnlineServiceIdAndProcessIdAndStageNoAndLabelId(serviceId, processId, stageNo, lableId);

				if (serviceApproval != null) {
					serviceApproval.setDeletedFlag(true);
					OplServiceApprovalRepository.save(serviceApproval);
				}
				OplServiceApproval oplServiceApproval = new OplServiceApproval();

				oplServiceApproval.setOnlineServiceId(serviceId);
				oplServiceApproval.setStageNo(stageNo);
				oplServiceApproval.setPendingAt(authId);
				oplServiceApproval.setForwardTo(introleId);
				oplServiceApproval.setForwardedUserId(userId);
				oplServiceApproval.setLabelId(lableId);
				oplServiceApproval.setProcessId(processId);
				oplServiceApproval.setAtaProcessId(setauthId);
				oplServiceApproval.setDeletedFlag(false);
				oplServiceApproval.setInspAllocated(false);
				oplServiceApproval.setClcAllocated(false);
				OplServiceApprovalRepository.saveAndFlush(oplServiceApproval);

			}

			if (saveData != null) {
				object.put("status", 200);
				object.put("Msg", "Form Submitted");
				object.put("OPLSno", saveData.getLicenceSno());
				object.put("OPLId", saveData.getOplId());
			} else {
				object.put("status", 201);
				object.put("Msg", "Unable to process the request");
				object.put("OPLSno", "0");
			}

		} catch (Exception e) {
			object.put("status", 201);
			object.put("Msg", "Unable to process the request");
			object.put("OPLSno", "0");
			log.info("inside Operator Application save and update Method" + e.getMessage());
		}
		return object;
	}

	@Override
	public JSONObject checkPaymentStatus(String whid) {
		JSONObject object = new JSONObject();
		String status = newoperatorLicenceRepository.checkPaymentStatus(whid);

		try {
			if (status == null) {
				status = "norecord";
				object.put("status", 200);
				object.put("paymentStatus", status);
			} else {
				object.put("status", 200);
				object.put("paymentStatus", status);
			}
		} catch (Exception e) {

		}

		return object;
	}

	@Override
	public Page<OperatorLicenceApprovalResponseDto> getAllApprovalData(String status, String search, Integer roleId,
			Integer applicationRoleId, Integer userid, Pageable pageable) {

		Page<Map<String, Object>> pageobj = null;
		List<OperatorLicenceApprovalResponseDto> buyerResponses = new ArrayList<OperatorLicenceApprovalResponseDto>();
		Long totalElements = 0l;

		OplServiceApproval serviceApproval = OplServiceApprovalRepository
				.findByPendingAtAndProcessIdAndLabelIdAndDeletedFlagFalse(200, applicationRoleId, 301);
		if (applicationRoleId == 10 && serviceApproval != null && serviceApproval.getStageNo() == 1) {
			Integer stageOneStatus = newoperatorLicenceRepository.checkStageOneCompeleted();

			if (stageOneStatus == 0) {
				//pageobj = newoperatorLicenceRepository.getAllApprovalData(200, roleId, status, search, pageable);
				pageobj = newoperatorLicenceRepository.getAllApprovalData(200,status, search, pageable);
			}

		} else if (applicationRoleId == 3) {
			/*
			 * pageobj = newoperatorLicenceRepository.getAllReportingAuthData(200, roleId,
			 * userid, status, search, pageable);
			 */
			
			pageobj = newoperatorLicenceRepository.getAllReportingAuthData(200,userid,applicationRoleId,status,search, pageable);
		} else if (applicationRoleId == 12) {
			
			//pageobj = newoperatorLicenceRepository.getAllReportingAuthData(200, applicationRoleId, userid, status,search, pageable);
			
			pageobj = newoperatorLicenceRepository.getAllReportingAuthData(200,userid,applicationRoleId,status,search, pageable);
		} else {
			pageobj = newoperatorLicenceRepository.getAllApprovalData(200,status,search, pageable);
		}

		if (pageobj != null) {

			buyerResponses = pageobj.getContent().stream().map(Mapper::mapToOperatorLicenceApprovalDto).collect(Collectors.toList());

		}

		if (pageobj != null) {
			totalElements = pageobj.getTotalElements();
		}

		return new PageImpl<>(buyerResponses, pageable, totalElements);
	}

	@Override
	public PaymentData getPaymentDetailsById(Long id) {
		// TODO Auto-generated method stub

		PaymentData payment = paymentRepo.findByIdAndEnmPaymentStatusAndBitDeletedFlag(id, PaymentStatus.PAID, false);

		if (payment == null) {
			payment = new PaymentData();
			payment.setAmountExpected("0");
			payment.setEnmPaymentStatus(PaymentStatus.FAILED);
		}

		return payment;
	}

	@Transactional
	@Override
	public JSONObject AllTakeAction(String takeactionData) {
		// TODO Auto-generated method stub
		JSONObject getAuthArr = new JSONObject();
		JSONObject requestedData = new JSONObject(takeactionData);
		Long currentTime = System.currentTimeMillis();
		Integer processId = requestedData.optInt("processId", 0);
		Integer onlineServiceId = requestedData.optInt("onlineServiceId", 0);
		Integer stageNo = requestedData.optInt("stageNo", 0);
		Integer labelId = requestedData.getInt("labelId");
		Integer action = requestedData.optInt("action", 0);
		String remark = requestedData.optString("remark", "");
		Integer roleId = requestedData.optInt("roleId", 0);
		Integer userId = requestedData.optInt("userId", 0);
		Integer teamLeadRoleId = requestedData.optInt("teamLeadRoleId", 0);
		Integer teamLeadId = requestedData.optInt("teamLeadId", 0);
		String teamLeadName = requestedData.optString("teamLeadName", null);
		String conditionActions = requestedData.optString("actionCondition", "nocondition");
		JSONArray internalMembers = requestedData.optJSONArray("internalMembers");
		JSONArray inspectionMembers = requestedData.optJSONArray("inspectionMembers");
		Integer applicationRoleId = requestedData.optInt("applicationRoleId", 0);
		String suspensionReason = requestedData.optString("SespensioReason", null);
		JSONArray supportingDocumentsList = requestedData.optJSONArray("addMoreSupportingDocuments");
		String actionType = requestedData.optString("actionType", null);
		String enPassword=requestedData.optString("enPassword", null);
		String status = "";
		String outMsg = "";
		String id="";

		try {

			Date date = new Date(currentTime);
			List<Map<String, Object>> mapObjlist = workflowServiceImpl.getAllComplaintStatus();
			OplServiceApprovalNotings notings = new OplServiceApprovalNotings();
			notings.setProcessId(processId);
			notings.setProfileId(0);
			notings.setOnlineServiceId(onlineServiceId);
			notings.setFromAuthority(applicationRoleId);
			//notings.setActionTakenDate(LocalDate.now());
			notings.setStatus(action);
			notings.setStageNo(stageNo);
			notings.setNoting(remark);
			notings.setSuspensionReason(suspensionReason);
			notings.setCreatedBy(userId);
			notings.setActionTakenId(action);
			notings.setActionTaken(actionType);
			notings.setDeletedFlag(false);

			OplServiceApproval serviceApproval = new OplServiceApproval();
			if (stageNo == 1 || stageNo == 3) {
				serviceApproval = OplServiceApprovalRepository.findByOnlineServiceIdAndProcessIdAndStageNoAndLabelId(
						onlineServiceId, processId, stageNo, labelId);
				Integer pendingAt = serviceApproval.getPendingAt();
				serviceApproval.setStatusDate(LocalDateTime.now());
				serviceApproval.setCreatedBy(userId);
				serviceApproval.setUpdatedBy(userId);
			}

			if (action == 1) {// forwarded to Inspector

				OplInspectorIcm oplInspectorIcm = new OplInspectorIcm();
				// Map the values from the JSON to the ComplaintIcm entity
				oplInspectorIcm.setIcmRoleId(teamLeadRoleId);
				oplInspectorIcm.setIcmUserRoleId(teamLeadRoleId);
				oplInspectorIcm.setIcmUserId(teamLeadId);
				oplInspectorIcm.setIcmUserName(teamLeadName);
				oplInspectorIcm.setIcmActionTaken(false);
				oplInspectorIcm.setIcmStatus(OplInspectorIcm.ComplaintStatus.APPLICATION_ASSIGNED);
				oplInspectorIcm.setIntCreatedBy(userId); // Replace with the actual user ID
				oplInspectorIcm.setBitDeletedFlag(false);
				oplInspectorIcm.setIcmActionTaken(false);// Set to false initially
				oplInspectorIcm.setIcmComplaintAppId(onlineServiceId);// Replace with the actual
				oplInspectorIcm.setLableId(labelId);
				oplInspectorIcm.setProcessId(processId);
				oplInspectorIcm.setPendingAt(teamLeadRoleId);
				oplInspectorIcm.setStageNo(stageNo + 1);
				oplInspectorIcm.setSentMailId(0);
				oplInspectorIcm.setSentNotificationId(0);
				oplInspectorIcm.setSentSMSId(0);
				oplInspectorIcm.setNotificationFlag(false);
				oplInspectorIcm.setSMSFlag(false);
				oplInspectorIcm.setMailFlag(false);
				oplInspectorIcm.setIcmActionTaken(false); // Set to false initially

				oplInspectorIcmRepository.saveAndFlush(oplInspectorIcm);

				List<InspectionMemberOPL> memberList = new ArrayList<>();

				// Iterate over each object in the internalMembers array
				for (int i = 0; i < inspectionMembers.length(); i++) {
					JSONObject member = inspectionMembers.getJSONObject(i);
					InspectionMemberOPL memberOpl = new InspectionMemberOPL();
					memberOpl.setInspMemberRoleId(member.optInt("roleId"));
					;
					memberOpl.setInspMemberUserId(member.optInt("id"));
					memberOpl.setInspMemberName(member.optString("name"));
					memberOpl.setTeamLeadId(teamLeadId);
					memberList.add(memberOpl);
				}

				inspectionMemberOPLRepository.saveAllAndFlush(memberList);

				serviceApproval.setInspAllocated(true);

				String appicationStatus1 = mapObjlist.stream()
						.filter(x -> Integer.parseInt(x.get("intId").toString()) == 14).collect(Collectors.toList())
						.get(0).get("complaintStatus").toString();
				OperatorLicenceEntity LicenceEntity1 = newoperatorLicenceRepository
						.findByLicenceSnoAndDeletedFlagFalse(onlineServiceId);
				LicenceEntity1.setApplicationStatus(appicationStatus1);
				newoperatorLicenceRepository.save(LicenceEntity1);
				OplServiceApprovalRepository.save(serviceApproval);
				status = "200";
				outMsg = "Inspector Allocated Successfully!";

			} else if (action == 2) { // forwarded to CLC
				
			

				if (labelId == 301 && stageNo == 1 && roleId == 10 && internalMembers != null) {
					List<OplCLC> complaintIcmList = new ArrayList<>();

					// Iterate over each object in the internalMembers array
					for (int i = 0; i < internalMembers.length(); i++) {
						JSONObject member = internalMembers.getJSONObject(i);
						OplCLC complaintIcm = new OplCLC();
						// Map the values from the JSON to the ComplaintIcm entity
						complaintIcm.setIcmRoleId(member.optInt("icMemberRoleId"));
						complaintIcm.setIcmUserRoleId(member.optInt("roleId"));
						complaintIcm.setIcmUserId(member.optInt("userId"));
						complaintIcm.setIcmUserName(member.optString("fullName"));
						complaintIcm.setIcmActionTaken(false);
						complaintIcm.setIcmStatus(OplCLC.ComplaintStatus.APPLICATION_ASSIGNED);
						complaintIcm.setIntCreatedBy(userId); // Replace with the actual user ID
						complaintIcm.setBitDeletedFlag(false);
						complaintIcm.setIcmActionTaken(false);// Set to false initially
						complaintIcm.setIcmComplaintAppId(onlineServiceId);// Replace with the actual
						complaintIcm.setLableId(labelId);
						complaintIcm.setProcessId(processId);
						complaintIcm.setPendingAt(member.optInt("icMemberRoleId"));
						complaintIcm.setStageNo(stageNo + 1);
						complaintIcm.setSentMailId(0);
						complaintIcm.setSentNotificationId(0);
						complaintIcm.setSentSMSId(0);
						complaintIcm.setNotificationFlag(false);
						complaintIcm.setSMSFlag(false);
						complaintIcm.setMailFlag(false);
						complaintIcm.setIcmActionTaken(false); // Set to false initially
						complaintIcmList.add(complaintIcm);
					}

					oplRepository.saveAll(complaintIcmList);
					String appicationStatus = mapObjlist.stream()
							.filter(x -> Integer.parseInt(x.get("intId").toString()) == 20).collect(Collectors.toList())
							.get(0).get("complaintStatus").toString();					
					
					
					OplSetAuthority setAuthority = setAuthorityRepository.findByProcessIdAndStageNoAndLabelId(200, stageNo+1, 301);
					Integer setauthId = setAuthority.getSetAuthId();
					Integer authId = setAuthority.getPrimaryAuth();
					Integer introleId = setAuthority.getRoleId();
					Integer stageNoValue = setAuthority.getStageNo();
					Integer lableId = setAuthority.getLabelId();
					Integer processIdvalue = setAuthority.getProcessId();
					serviceApproval = OplServiceApprovalRepository.findByProcessIdAndOnlineServiceIdAndDeletedFlag(processId, onlineServiceId, labelId);
					
					
					notings.setToAuthority(authId);
					serviceApproval.setStageNo(stageNoValue);
					serviceApproval.setPendingAt(authId);
					serviceApproval.setAtaProcessId(serviceApproval.getAtaProcessId());
					serviceApproval.setSentFrom(roleId);
					serviceApproval.setForwardAllAction(appicationStatus);
					serviceApproval.setVerifiedBy(userId);
					serviceApproval.setForwardedUserId(userId);
					OplServiceApprovalRepository.save(serviceApproval);
					
					OperatorLicenceEntity LicenceEntity1 = newoperatorLicenceRepository
							.findByLicenceSnoAndDeletedFlagFalse(onlineServiceId);
					LicenceEntity1.setApplicationStatus(appicationStatus);
					newoperatorLicenceRepository.save(LicenceEntity1);
					approvalNotingsRepo.saveAndFlush(notings);
					serviceApproval.setClcAllocated(true);
					OplServiceApprovalRepository.save(serviceApproval);
					status = "200";
					outMsg = "forwarded successfully.";
				}
			}

			else if (action == 3) {

				String appicationStatus = mapObjlist.stream()
						.filter(x -> Integer.parseInt(x.get("intId").toString()) == 16).collect(Collectors.toList())
						.get(0).get("complaintStatus").toString();

				notings.setToAuthority(0);
				notings.setActionTaken(appicationStatus);
				notings.setStageNo(5);
				serviceApproval.setStageNo(5);
				serviceApproval.setPendingAt(0);
				serviceApproval.setAtaProcessId(0);
				serviceApproval.setForwardTo(0);
				serviceApproval.setVerifiedBy(null);
				serviceApproval.setSentFrom(roleId);
				serviceApproval.setForwardAllAction(appicationStatus);
				serviceApproval.setForwardedUserId(userId);

				OperatorLicenceEntity LicenceEntity1 = newoperatorLicenceRepository.findByLicenceSnoAndDeletedFlagFalse(onlineServiceId);
				LicenceEntity1.setApplicationStatus(appicationStatus);
				LicenceEntity1.setApprovalStatus(OperatorLicenceStatus.DEFERRED);
				LicenceEntity1.setApprovedBy(userId);
				LicenceEntity1.setApprovedByRole(applicationRoleId);
				LicenceEntity1.setCnfApprovalDate(LocalDate.now());
				LicenceEntity1.setApproverRemarks(remark);				
				newoperatorLicenceRepository.save(LicenceEntity1);
				approvalNotingsRepo.saveAndFlush(notings);
				OplServiceApprovalRepository.save(serviceApproval);
				status = "200";
				outMsg = "deferred successfully.";

			}

			else if (action == 11) {// Inception action

				serviceApproval = OplServiceApprovalRepository
						.findByProcessIdAndOnlineServiceIdAndDeletedFlag(processId, onlineServiceId, labelId);

				Integer stage_No = serviceApproval.getStageNo();
				if (stage_No == 2 && applicationRoleId == 3 || stage_No == 2 && applicationRoleId == 12) {

					String appicationStatus = mapObjlist.stream()
							.filter(x -> Integer.parseInt(x.get("intId").toString()) == 19).collect(Collectors.toList())
							.get(0).get("complaintStatus").toString();

					OplSetAuthority setAuthority = setAuthorityRepository.findByProcessIdAndStageNoAndLabelId(200, stageNo+1, 301);
					Integer setauthId = setAuthority.getSetAuthId();
					Integer authId = setAuthority.getPrimaryAuth();
					Integer introleId = setAuthority.getRoleId();
					Integer stageNoValue = setAuthority.getStageNo();
					Integer lableId = setAuthority.getLabelId();
					Integer processIdvalue = setAuthority.getProcessId();
					serviceApproval = OplServiceApprovalRepository.findByProcessIdAndOnlineServiceIdAndDeletedFlag(processId, onlineServiceId, labelId);
					
					
					notings.setToAuthority(authId);
					notings.setActionTaken(appicationStatus);
					serviceApproval.setStageNo(stageNoValue);
					serviceApproval.setPendingAt(authId);
					serviceApproval.setAtaProcessId(serviceApproval.getAtaProcessId());
					serviceApproval.setSentFrom(roleId);
					serviceApproval.setForwardAllAction(appicationStatus);
					serviceApproval.setVerifiedBy(userId);
					serviceApproval.setForwardedUserId(userId);
					OplServiceApprovalRepository.save(serviceApproval);
					OperatorLicenceEntity LicenceEntity1 = newoperatorLicenceRepository
							.findByLicenceSnoAndDeletedFlagFalse(onlineServiceId);
					LicenceEntity1.setApplicationStatus(appicationStatus);
					newoperatorLicenceRepository.save(LicenceEntity1);
					OplCLC icmApplicationDetails = oplRepository
							.findByIcmComplaintAppIdAndIcmUserIdAndIcmUserRoleIdAndIcmActionTaken(onlineServiceId,
									userId, roleId, false);
					icmApplicationDetails.setIcmStatus(OplCLC.ComplaintStatus.APPLICATION_REVIEWED);
					icmApplicationDetails.setIcmActionTaken(true);
					oplRepository.saveAndFlush(icmApplicationDetails);

				} else {
					
					String appicationStatus = mapObjlist.stream()
							.filter(x -> Integer.parseInt(x.get("intId").toString()) == 19).collect(Collectors.toList())
							.get(0).get("complaintStatus").toString();
					notings.setActionTaken(appicationStatus);
					notings.setToAuthority(serviceApproval.getPendingAt());
					OplCLC icmApplicationDetails = oplRepository
							.findByIcmComplaintAppIdAndIcmUserIdAndIcmUserRoleIdAndIcmActionTaken(onlineServiceId,
									userId, roleId, false);
					icmApplicationDetails.setIcmStatus(OplCLC.ComplaintStatus.APPLICATION_REVIEWED);
					icmApplicationDetails.setIcmActionTaken(true);
					oplRepository.saveAndFlush(icmApplicationDetails);
					OperatorLicenceEntity LicenceEntity1 = newoperatorLicenceRepository
							.findByLicenceSnoAndDeletedFlagFalse(onlineServiceId);
					LicenceEntity1.setApplicationStatus(appicationStatus);
					newoperatorLicenceRepository.save(LicenceEntity1);
				}

				if (supportingDocumentsList != null && supportingDocumentsList.length() > 0) {
					List<String> fileUploadList = new ArrayList<String>();

					List<InspReportDoc> supporting_documentList = new ArrayList();

					// Multiple file uploading inspObservation
					String docId = generateUniqueID();
					notings.setInspObservation(docId);
					OplServiceApprovalNotings notingsObj = approvalNotingsRepo.saveAndFlush(notings);

					for (int i = 0; i <= supportingDocumentsList.length() - 1; i++) {
						JSONObject jsonobj = (JSONObject) supportingDocumentsList.get(i);
						String docName = jsonobj.get("amtxtDocumentName").toString();
						String docPathName = jsonobj.get("amfileUploadDocuments").toString();
						InspReportDoc doc = new InspReportDoc();
						doc.setDocPath(docPathName);
						doc.setDocName(docName);
						doc.setIntParentId(notingsObj.getNotingsId());
						doc.setVchInsObserId(docId);
						doc.setCreatedUserId(userId);
						doc.setCreatedRoleId(roleId);
						doc.setBitDeletedFlag(false);
						supporting_documentList.add(doc);
						fileUploadList.add(docPathName);
					}

					fileUploadList.forEach(fileUpload -> {
						if (!Strings.isNullOrEmpty(fileUpload)) {
							File f = new File(tempUploadPath + fileUpload);
							if (f.exists()) {
								File src = new File(tempUploadPath + fileUpload);
								Path path = Paths.get(finalUploadPath + "OPL_Report_Docs");

								if (!Files.exists(path) && !Files.isDirectory(path)) {
									try {
										Files.createDirectories(path);
									} catch (IOException e) {
										log.info("Inside getById method of Complaint_managmentServiceImpl"
												+ e.getMessage());

									}
								}

								try {
									File dest = new File(finalUploadPath + "OPL_Report_Docs/" + fileUpload);
									Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
									Files.delete(src.toPath());
								} catch (IOException e) {
									log.error(
											"Inside save method of Complaint_managmentServiceImpl some error occur in file moving to destination folder:"
													+ e);
								}

							}
						}
					});

					List<InspReportDoc> savedData = oplInspReportDocRepository.saveAll(supporting_documentList);

				} else {
					notings.setInspectionDocId(null);
					notings.setInspObservation(null);
				}

				id="";
				status = "200";
				outMsg = "Application Inpection Report Submitted to Technical Successfully!";

			}

			else if (action == 12) {// technical Verify action

				serviceApproval = OplServiceApprovalRepository.findByProcessIdAndOnlineServiceIdAndDeletedFlag(processId, onlineServiceId, labelId);

				Integer stage_No = serviceApproval.getStageNo();
				if (stage_No == 3 && applicationRoleId == 10) {
					
					String appicationStatus = mapObjlist.stream()
							.filter(x -> Integer.parseInt(x.get("intId").toString()) == 21).collect(Collectors.toList())
							.get(0).get("complaintStatus").toString();

					OplSetAuthority setAuthority = setAuthorityRepository.findByProcessIdAndStageNoAndLabelId(200, stageNo+1, 301);
					Integer setauthId = setAuthority.getSetAuthId();
					Integer authId = setAuthority.getPrimaryAuth();
					Integer introleId = setAuthority.getRoleId();
					Integer stageNoValue = setAuthority.getStageNo();
					Integer lableId = setAuthority.getLabelId();
					Integer processIdvalue = setAuthority.getProcessId();
					serviceApproval = OplServiceApprovalRepository.findByProcessIdAndOnlineServiceIdAndDeletedFlag(processId, onlineServiceId, labelId);
					
					
					notings.setToAuthority(authId);
					notings.setActionTaken(appicationStatus);
					serviceApproval.setStageNo(stageNoValue);
					serviceApproval.setPendingAt(authId);
					serviceApproval.setAtaProcessId(serviceApproval.getAtaProcessId());
					serviceApproval.setSentFrom(roleId);
					serviceApproval.setForwardAllAction(appicationStatus);
					serviceApproval.setVerifiedBy(userId);
					serviceApproval.setForwardedUserId(userId);
					OplServiceApprovalRepository.save(serviceApproval);
					OperatorLicenceEntity LicenceEntity1 = newoperatorLicenceRepository
							.findByLicenceSnoAndDeletedFlagFalse(onlineServiceId);
					LicenceEntity1.setApplicationStatus(appicationStatus);
					newoperatorLicenceRepository.save(LicenceEntity1);
					
					approvalNotingsRepo.saveAndFlush(notings);

				}

				id="";
				status = "200";
				outMsg = "Inpection Report verify  and  Forwarded to CECM Successfully!";

			}

			else if (action == 13) {// technical Defer

				serviceApproval = OplServiceApprovalRepository
						.findByProcessIdAndOnlineServiceIdAndDeletedFlag(processId, onlineServiceId, labelId);

				Integer stage_No = serviceApproval.getStageNo();
				if (stage_No == 4 && applicationRoleId == 13) {

					String appicationStatus = mapObjlist.stream()
							.filter(x -> Integer.parseInt(x.get("intId").toString()) == 14).collect(Collectors.toList())
							.get(0).get("complaintStatus").toString();

					notings.setToAuthority(0);
					serviceApproval.setStageNo(serviceApproval.getStageNo() + 1);
					serviceApproval.setPendingAt(0);
					serviceApproval.setAtaProcessId(0);
					serviceApproval.setSentFrom(roleId);
					serviceApproval.setForwardAllAction(appicationStatus);
					serviceApproval.setVerifiedBy(userId);
					serviceApproval.setForwardedUserId(userId);
					OplServiceApprovalRepository.save(serviceApproval);
					approvalNotingsRepo.saveAndFlush(notings);

				}
				
				status = "200";
				outMsg = "Verify Action Taken Successfully!";

			}
			else if (action == 101) {// Issue License  action By CECM

				serviceApproval = OplServiceApprovalRepository.findByProcessIdAndOnlineServiceIdAndDeletedFlag(processId, onlineServiceId, labelId);

				Integer stage_No = serviceApproval.getStageNo();
				if (stage_No == 4 && applicationRoleId == 13) {
					
					String appicationStatus = mapObjlist.stream()
							.filter(x -> Integer.parseInt(x.get("intId").toString()) == 18).collect(Collectors.toList())
							.get(0).get("complaintStatus").toString();

					/*
					 * OplSetAuthority setAuthority =
					 * setAuthorityRepository.findByProcessIdAndStageNoAndLabelId(200, stageNo+1,
					 * 301); Integer setauthId = setAuthority.getSetAuthId(); Integer authId =
					 * setAuthority.getPrimaryAuth(); Integer introleId = setAuthority.getRoleId();
					 * Integer stageNoValue = setAuthority.getStageNo(); Integer lableId =
					 * setAuthority.getLabelId(); Integer processIdvalue =
					 * setAuthority.getProcessId(); serviceApproval =
					 * OplServiceApprovalRepository.findByProcessIdAndOnlineServiceIdAndDeletedFlag(
					 * processId, onlineServiceId, labelId);
					 */
					notings.setActionTaken(appicationStatus);
					notings.setToAuthority(0);
					serviceApproval.setStageNo(0);
					serviceApproval.setPendingAt(0);
					serviceApproval.setAtaProcessId(0);
					serviceApproval.setSentFrom(roleId);
					serviceApproval.setForwardAllAction(appicationStatus);
					serviceApproval.setVerifiedBy(userId);
					serviceApproval.setForwardedUserId(userId);		
					
					
					String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
					
					Integer currentYear=LocalDate.now().getYear();
					   
					String OPLIdStr="SC/LC/WHOCC/"+String.valueOf(currentYear)+"/";
					String OPLId =OPLIdStr+generateUniqueId(3);
					String certificateIdStr="WRSC/WOL/MIGORI/"+currentYear+"/";
					String certificateId =certificateIdStr+generateUniqueId(3);
					Optional<WRSC_OPL_Main> latestWarehouse = wrsoplMainRepository.findTopByVchOplIdStartingWithOrderByVchOplIdDesc(OPLIdStr);
					String lastWarehouseId = null;
					if (latestWarehouse.isPresent()) {
						lastWarehouseId = latestWarehouse.get().getVchOplId();
					}
					//String OPLId = generateFormOneCId(lastWarehouseId, OPLIdStr);
					
					OperatorLicenceEntity LicenceEntity1 = newoperatorLicenceRepository.findByLicenceSnoAndDeletedFlagFalse(onlineServiceId);
					LicenceEntity1.setApprovalStatus(OperatorLicenceStatus.APPROVED);
					LicenceEntity1.setCnfApprovalDate(LocalDate.now());
					LicenceEntity1.setApprovedBy(userId);
					LicenceEntity1.setApprovedByRole(applicationRoleId);
					LicenceEntity1.setDeletedFlag(false);  
					LicenceEntity1.setCnfApprovalDate(LocalDate.now());
					LicenceEntity1.setCreatedOn(LocalDateTime.now());
					LicenceEntity1.setApprovedBy(userId);
					LicenceEntity1.setApproverRemarks(remark);
					LicenceEntity1.setLicenseGen(true);
					LicenceEntity1.setCertId(certificateId);
					LicenceEntity1.setCertGen(true);					
					LicenceEntity1.setLicenceCertGen(true);
					LicenceEntity1.setCertGen(true);
					LicenceEntity1.setCertIssueDate(LocalDate.now());					
					LicenceEntity1.setApplicationStatus(appicationStatus);
					OperatorLicenceEntity LicenceEntityfinal=newoperatorLicenceRepository.save(LicenceEntity1);
					
					WRSC_OPL_Main oplfinalEntity=new WRSC_OPL_Main(); 
					oplfinalEntity.setVchOplId(OPLId);
					oplfinalEntity.setIntOplAppSno(onlineServiceId);
					oplfinalEntity.setVchOplAppId(LicenceEntityfinal.getOplId());
					oplfinalEntity.setIntCreatedBy(userId);	
					oplfinalEntity.setIntCreatedRoleId(roleId);
					oplfinalEntity.setBitDeletedFlag(false);  
					oplfinalEntity.setDtCnfApproval(LocalDate.now());
					oplfinalEntity.setDtmCreatedOn(LocalDateTime.now());
					oplfinalEntity.setIntApprovedBy(userId);
					oplfinalEntity.setIntApprovedByRole(applicationRoleId);
					oplfinalEntity.setVchApprovalStatus(OperatorLicenceStatus.APPROVED);
					oplfinalEntity.setVchApproverRemarks(remark);
					oplfinalEntity.setBitLicenseGen(true);  
					oplfinalEntity.setVchCertId(certificateId);
					oplfinalEntity.setBitCertGen(true);					
					oplfinalEntity.setBitLicenceCertGen(true);
					oplfinalEntity.setBitCertGen(true);
					oplfinalEntity.setDtCertIssue(LocalDate.now());
					oplfinalEntity.setVchPaymentStatus(LicenceEntityfinal.getPaymentStatus());
					 
					ConformityMain	conformityMain = conformityMainRepository.findByWarehouseId(LicenceEntityfinal.getWarehouseId());
					conformityMain.setLicenceCerti(true);
					conformityMain.setLicenseGen(true);				
					conformityMainRepository.saveAndFlush(conformityMain);
					ApplicationOfConformityLocationDetails locationDetails=confirmityLocDetails.findByWarehouseId(LicenceEntityfinal.getWarehouseId());
					CompanyWarehouseDetails companyWarehousRoutineCom=new CompanyWarehouseDetails();
					AocCompProfileDetails profile=aocCompProfileDetRepository.findByProfDetIdAndDeletedFlagFalse(LicenceEntityfinal.getCompanyId());
					companyWarehousRoutineCom.setCompanyId(LicenceEntityfinal.getCompanyId());
					companyWarehousRoutineCom.setCompanyName(profile.getCompany());
					companyWarehousRoutineCom.setWarehouseId(LicenceEntityfinal.getWarehouseId());
					companyWarehousRoutineCom.setWarehouseName(locationDetails.getWarehouseName());					
					companyWarehouseDetailsRepository.saveAndFlush(companyWarehousRoutineCom);					
					WRSC_OPL_Main saveData=wrsoplMainRepository.saveAndFlush(oplfinalEntity);
					OplServiceApprovalRepository.save(serviceApproval);
					approvalNotingsRepo.saveAndFlush(notings);
					Tuser userEntity = new Tuser();
					userEntity.setTxtFullName(locationDetails.getWhOperatorName());
					userEntity.setSelGender(2);
					userEntity.setTxtMobileNo(locationDetails.getMobileNumber());
					userEntity.setTxtEmailId(locationDetails.getEmail());
					userEntity.setTxtDateOfJoining(new Date());
					userEntity.setTxtrAddress(locationDetails.getStreetName()+","+locationDetails.getBuilding());
					userEntity.setSelRole(6);
					userEntity.setSelCounty(locationDetails.getCounty().getId());
					userEntity.setSelSubCounty(locationDetails.getSubCounty().getIntId());
					userEntity.setSelWard(locationDetails.getWard().getIntWardMasterId());
					userEntity.setWarehouseId(locationDetails.getWarehouseId());
					String userid= locationDetails.getEmail();
					userEntity.setTxtUserId(userid);
					String pwd=generatePassword(6);
					userEntity.setTxtPassword("6e95001cee8e57360ffd1ed3ca37035b413a3f198d71df367e3a47a3d822e4df");
					userEntity.setSelIcmRoleId(0);
					userEntity.setSelCLCRoleId(0);
					userEntity.setChkPrevilege(3);
					Tuser updateData = userRepo.save(userEntity);
					

					/*
					 * Mail mail = new Mail();
					 * mail.setMailSubject("Application Submitted for Operator License.");
					 * mail.setContentType("text/html"); mail.setMailCc("uiidptestmail@gmail.com");
					 * mail.setTemplate("Your Operator License application No."+LicenceEntity1.
					 * getOplId()+" has been approved, and your License ID-"
					 * +OPLId+". Login credential is User Id -"+userid+"Password -"+"Admin@123");
					 * mail.setMailTo("warehouse@csmpl.com");
					 * EmailUtil.sendMail(mail.getMailSubject(),
					 * mail.getTemplate(),mail.getMailTo());
					 */

					Mail mail = new Mail();
					mail.setMailSubject("Application Submitted for Operator License.");
					mail.setContentType("text/html");
					//mail.setMailCc("uiidptestmail@gmail.com");
					mail.setTemplate("Your Operator License application No."+LicenceEntity1.getOplId()+" has been approved, and your License ID-"+OPLId+". Login credential is User Id -"+userid+"Password -"+"Admin@123");
					mail.setMailTo(locationDetails.getEmail());
					EmailUtil.sendMail(mail.getMailSubject(), mail.getTemplate(),mail.getMailTo());

					id=saveData.getVchOplId();
					status = "200";

					//outMsg = "Warehouse Operator License Generated Successfully!";
					
					outMsg="Your Operator License application No."+LicenceEntity1.getOplId()+" has been approved, and your License ID-"+OPLId+". Login credential is User Id -"+userid+"Password -"+"Admin@123";

				}
								
			}
			else if (action == 102) {// Reject application  action By CECM

				serviceApproval = OplServiceApprovalRepository.findByProcessIdAndOnlineServiceIdAndDeletedFlag(processId, onlineServiceId, labelId);

				Integer stage_No = serviceApproval.getStageNo();
				if (stage_No == 4 && applicationRoleId == 13) {
					
					String appicationStatus = mapObjlist.stream()
							.filter(x -> Integer.parseInt(x.get("intId").toString()) == 3).collect(Collectors.toList())
							.get(0).get("complaintStatus").toString();

					
					notings.setActionTaken(appicationStatus);
					notings.setToAuthority(0);
					serviceApproval.setStageNo(0);
					serviceApproval.setPendingAt(0);
					serviceApproval.setAtaProcessId(0);
					serviceApproval.setSentFrom(roleId);
					serviceApproval.setForwardAllAction(appicationStatus);
					serviceApproval.setVerifiedBy(userId);
					serviceApproval.setForwardedUserId(userId);	
					
					OperatorLicenceEntity LicenceEntity1 = newoperatorLicenceRepository.findByLicenceSnoAndDeletedFlagFalse(onlineServiceId);
					LicenceEntity1.setApprovalStatus(OperatorLicenceStatus.REJECTED);
					LicenceEntity1.setCnfApprovalDate(LocalDate.now());
					LicenceEntity1.setApprovedBy(userId);
					LicenceEntity1.setApprovedByRole(applicationRoleId);
					LicenceEntity1.setDeletedFlag(false);  
					LicenceEntity1.setCnfApprovalDate(LocalDate.now());
					LicenceEntity1.setCreatedOn(LocalDateTime.now());
					LicenceEntity1.setApprovedBy(userId);
					LicenceEntity1.setApproverRemarks(remark);
					LicenceEntity1.setLicenseGen(false);
					LicenceEntity1.setCertId(null);
					LicenceEntity1.setCertGen(false);					
					LicenceEntity1.setLicenceCertGen(false);					
					LicenceEntity1.setApplicationStatus(appicationStatus);
					OperatorLicenceEntity LicenceEntityfinal=newoperatorLicenceRepository.save(LicenceEntity1);
					ConformityMain	conformityMain = conformityMainRepository.findByWarehouseId(LicenceEntityfinal.getWarehouseId());
					conformityMain.setLicenceCerti(false);
					conformityMain.setLicenseGen(false);				
					conformityMainRepository.saveAndFlush(conformityMain);
					OplServiceApprovalRepository.save(serviceApproval);
					approvalNotingsRepo.saveAndFlush(notings);
					status = "200";
					outMsg = "Operator License Application Rejected Successfully!";
				}
				
				
			}

			else if (action == 103) {// Deferred  action By CECM

				serviceApproval = OplServiceApprovalRepository.findByProcessIdAndOnlineServiceIdAndDeletedFlag(processId, onlineServiceId, labelId);

				Integer stage_No = serviceApproval.getStageNo();
				if (stage_No == 4 && applicationRoleId == 13) {
					
					String appicationStatus = mapObjlist.stream()
							.filter(x -> Integer.parseInt(x.get("intId").toString()) == 19).collect(Collectors.toList())
							.get(0).get("complaintStatus").toString();

					OplSetAuthority setAuthority = setAuthorityRepository.findByProcessIdAndStageNoAndLabelId(200, stageNo-1, 301);
					Integer setauthId = setAuthority.getSetAuthId();
					Integer authId = setAuthority.getPrimaryAuth();
					Integer introleId = setAuthority.getRoleId();
					Integer stageNoValue = setAuthority.getStageNo();
					Integer lableId = setAuthority.getLabelId();
					Integer processIdvalue = setAuthority.getProcessId();
					serviceApproval = OplServiceApprovalRepository.findByProcessIdAndOnlineServiceIdAndDeletedFlag(processId, onlineServiceId, labelId);
					
					
					notings.setToAuthority(authId);
					serviceApproval.setStageNo(stageNoValue);
					serviceApproval.setPendingAt(authId);
					serviceApproval.setAtaProcessId(serviceApproval.getAtaProcessId());
					serviceApproval.setSentFrom(roleId);
					serviceApproval.setForwardAllAction(appicationStatus);
					serviceApproval.setVerifiedBy(userId);
					serviceApproval.setForwardedUserId(userId);
					OplServiceApprovalRepository.save(serviceApproval);
					
					String appicationStatus1 = mapObjlist.stream()
							.filter(x -> Integer.parseInt(x.get("intId").toString()) == 16).collect(Collectors.toList())
							.get(0).get("complaintStatus").toString();

					OperatorLicenceEntity LicenceEntity1 = newoperatorLicenceRepository.findByLicenceSnoAndDeletedFlagFalse(onlineServiceId);
					LicenceEntity1.setApplicationStatus(appicationStatus);
					LicenceEntity1.setApprovalStatus(OperatorLicenceStatus.DEFERRED);
					LicenceEntity1.setApprovedBy(userId);
					LicenceEntity1.setApprovedByRole(applicationRoleId);
					LicenceEntity1.setCnfApprovalDate(LocalDate.now());
					LicenceEntity1.setApproverRemarks(remark);
					newoperatorLicenceRepository.save(LicenceEntity1);
					
					approvalNotingsRepo.saveAndFlush(notings);
					id="";
					status = "200";
					outMsg = "Application Deffered to Technical Successfully!";
					
				}
			}

		} catch (Exception e) {

			log.error("Operator License Approval Process:Technical TakeActionDetails()", e);
			status = "500";
			outMsg = "Error occurred during action!";

		}

		JSONObject jsObj = new JSONObject();
		jsObj.put("status", status);
		jsObj.put("outMsg", outMsg);
		jsObj.put("ProcessId", processId);
		jsObj.put("result", "");
		return jsObj;
	}

	public static String generateUniqueID() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String timestamp = sdf.format(new Date());
		String randomUUID = UUID.randomUUID().toString().substring(0, 3); // Get first 8 characters
		return timestamp + "_" + randomUUID;
	}

	@Override
	public JSONObject getAllActionRemarks(String takeactionData) {

		JSONObject result = new JSONObject();
		JSONObject requestedData = new JSONObject(takeactionData);
		Integer processId = requestedData.optInt("processId", 0);
		Integer onlineServiceId = requestedData.optInt("onlineServiceId", 0);
		Integer stageNo = requestedData.optInt("stageNo", 0);
		Integer labelId = requestedData.getInt("labelId");
		Integer roleId = requestedData.optInt("roleId", 0);
		Integer userId = requestedData.optInt("userId", 0);
		List<OplServiceApprovalNotings> notingsList = approvalNotingsRepo
				.findByOnlineServiceIdAndProcessId(onlineServiceId, processId);

		if (notingsList != null) {

			List<OplServiceApprovalNotings> InspectorRemarklist = notingsList.stream()
					.filter(x -> x.getFromAuthority() == 3).collect(Collectors.toList());

			if (InspectorRemarklist != null && InspectorRemarklist.size() > 0) {
				JSONArray jsonarr = new JSONArray();

				InspectorRemarklist.forEach(x -> {
					JSONObject remarksObj = new JSONObject();
					remarksObj.put("remarks", x.getNoting());
					remarksObj.put("susReason", x.getSuspensionReason());  
					remarksObj.put("ActionTaken", x.getActionTaken());
					remarksObj.put("susReason", x.getSuspensionReason());
					remarksObj.put("remarkRoleName",
							mroleRepository.findByIntIdAndBitDeletedFlag(x.getFromAuthority(), false).getTxtRoleName());
					remarksObj.put("remarkUserName",
							userRepo.findByIntIdAndBitDeletedFlag(x.getCreatedBy(), false).getTxtFullName());

					if (x.getInspObservation() != null) {
						List<InspReportDoc> supportingDoc = oplInspReportDocRepository
								.findByVchInsObserIdAndBitDeletedFlagFalse(x.getInspObservation());
						remarksObj.put("label", "Supporting Documents");
						remarksObj.put("SupportingDoc", supportingDoc);
					} else {
						remarksObj.put("remarksDoc", "not found");
					}

					jsonarr.put(remarksObj);
				});
				result.put("status", 200);
				result.put("InspectorRemarks", jsonarr);
			} else {
				result.put("status", 200);
				result.put("InspectorRemarks", "Inspector Remark not Found");
			}

			List<OplServiceApprovalNotings> ClcRemarklist = notingsList.stream().filter(x -> x.getFromAuthority() == 12)
					.collect(Collectors.toList());

			if (ClcRemarklist != null && ClcRemarklist.size() > 0) {
				JSONArray jsonarr = new JSONArray();
				ClcRemarklist.forEach(x -> {
					JSONObject remarksObj = new JSONObject();
					remarksObj.put("remarks", x.getNoting());
					remarksObj.put("susReason", x.getSuspensionReason());
					remarksObj.put("ActionTaken", x.getActionTaken());
					remarksObj.put("susReason", x.getSuspensionReason());
					remarksObj.put("remarkRoleName",
							mroleRepository.findByIntIdAndBitDeletedFlag(x.getFromAuthority(), false).getTxtRoleName());
					remarksObj.put("remarkUserName",
							userRepo.findByIntIdAndBitDeletedFlag(x.getCreatedBy(), false).getTxtFullName());

					if (x.getInspObservation() != null) {
						List<InspReportDoc> supportingDoc = oplInspReportDocRepository
								.findByVchInsObserIdAndBitDeletedFlagFalse(x.getInspObservation());
						remarksObj.put("label", "Supporting Documents");
						remarksObj.put("SupportingDoc", supportingDoc);
					} else {
						remarksObj.put("remarksDoc", "not found");
					}

					jsonarr.put(remarksObj);
				});

				result.put("status", 200);
				result.put("CLCRemarks", jsonarr);
			} else {
				result.put("status", 200);
				result.put("CLCRemarks", "0");
			}

			List<OplServiceApprovalNotings> technicalRemarklist = notingsList.stream()
					.filter(x -> x.getFromAuthority() == 10).collect(Collectors.toList());

			if (technicalRemarklist != null && technicalRemarklist.size() > 0) {
				JSONArray jsonarr = new JSONArray();
				technicalRemarklist.forEach(x -> {
					JSONObject remarksObj = new JSONObject();
					remarksObj.put("remarks", x.getNoting());
					remarksObj.put("susReason", x.getSuspensionReason());
					remarksObj.put("ActionTaken", x.getActionTaken());
					remarksObj.put("susReason", x.getSuspensionReason());
					remarksObj.put("remarkRoleName",
							mroleRepository.findByIntIdAndBitDeletedFlag(x.getFromAuthority(), false).getTxtRoleName());
					remarksObj.put("remarkUserName",
							userRepo.findByIntIdAndBitDeletedFlag(x.getCreatedBy(), false).getTxtFullName());

					if (x.getInspObservation() != null) {
						List<InspReportDoc> supportingDoc = oplInspReportDocRepository
								.findByVchInsObserIdAndBitDeletedFlagFalse(x.getInspObservation());
						remarksObj.put("label", "Supporting Documents");
						remarksObj.put("SupportingDoc", supportingDoc);
					} else {
						remarksObj.put("remarksDoc", "not found");
					}

					jsonarr.put(remarksObj);
				});

				result.put("status", 200);
				result.put("technicalRemarks", jsonarr);
			} else {
				result.put("status", 200);
				result.put("technicalRemarks", "0");
			}

			List<OplServiceApprovalNotings> cecmRemarklist = notingsList.stream()
					.filter(x -> x.getFromAuthority() == 13).collect(Collectors.toList());

			if (cecmRemarklist != null && cecmRemarklist.size() > 0) {
				JSONArray jsonarr = new JSONArray();
				cecmRemarklist.forEach(x -> {
					JSONObject remarksObj = new JSONObject();
					remarksObj.put("remarks", x.getNoting());
					remarksObj.put("susReason", x.getSuspensionReason());
					remarksObj.put("ActionTaken", x.getActionTaken());
					remarksObj.put("susReason", x.getSuspensionReason());
					remarksObj.put("remarkRoleName",
							mroleRepository.findByIntIdAndBitDeletedFlag(x.getFromAuthority(), false).getTxtRoleName());
					remarksObj.put("remarkUserName",
							userRepo.findByIntIdAndBitDeletedFlag(x.getCreatedBy(), false).getTxtFullName());

					if (x.getInspObservation() != null) {
						List<InspReportDoc> supportingDoc = oplInspReportDocRepository
								.findByVchInsObserIdAndBitDeletedFlagFalse(x.getInspObservation());
						remarksObj.put("label", "Supporting Documents");
						remarksObj.put("SupportingDoc", supportingDoc);
					} else {
						remarksObj.put("remarksDoc", "not found");
					}

					jsonarr.put(remarksObj);
				});

				result.put("status", 200);
				result.put("cecmRemarks", jsonarr);
			} else {
				result.put("status", 200);
				result.put("cecmRemarks", "0");
			}
		}

		else {
			result.put("status", 700);
			result.put("defaultRemarks", "0");
		}

		return result;
	}

	@Override
	public JSONObject getAllActionRemarksList(String takeactionData) {

		JSONObject result = new JSONObject();
		JSONObject requestedData = new JSONObject();
		Integer processId = requestedData.optInt("processId", 0);
		Integer onlineServiceId = requestedData.optInt("onlineServiceId", 0);
		Integer stageNo = requestedData.optInt("stageNo", 0);
		Integer labelId = requestedData.getInt("labelId");
		Integer roleId = requestedData.optInt("roleId", 0);
		Integer userId = requestedData.optInt("userId", 0);
		List<OplServiceApprovalNotings> notingsList = approvalNotingsRepo
				.findByOnlineServiceIdAndProcessId(onlineServiceId, processId);

		if (notingsList != null) {
			JSONArray jsonarr = new JSONArray();

			notingsList.forEach(x -> {
				JSONObject remarksObj = new JSONObject();
				remarksObj.put("remarks", x.getNoting());
				remarksObj.put("susReason", x.getSuspensionReason());
				remarksObj.put("ActionTaken", x.getActionTaken());
				remarksObj.put("susReason", x.getSuspensionReason());
				remarksObj.put("remarkRoleName",
						mroleRepository.findByIntIdAndBitDeletedFlag(x.getFromAuthority(), false).getTxtRoleName());
				remarksObj.put("remarkUserName",
						userRepo.findByIntIdAndBitDeletedFlag(x.getCreatedBy(), false).getTxtFullName());

				if (x.getInspObservation() != null) {
					List<InspReportDoc> supportingDoc = oplInspReportDocRepository
							.findByVchInsObserIdAndBitDeletedFlagFalse(x.getInspObservation());
					remarksObj.put("label", "Supporting Documents");
					remarksObj.put("SupportingDoc", supportingDoc);
				} else {
					remarksObj.put("remarksDoc", "not found");
				}

				jsonarr.put(remarksObj);
			});
			result.put("status", 200);
			result.put("AllRemarks", jsonarr);
		}

		else {
			result.put("status", 700);
			result.put("remarksValue", "noRecord Found");
		}

		return result;
	}

	@Override
	public Page<OPLApplicationStatusDTO> getAllApprovalStatusData(Integer roleId,Integer userid,String status, String search, Pageable pageable) {
		
		Page<Map<String, Object>> pageobj = null;
		List<OPLApplicationStatusDTO> resultResponses = new ArrayList<OPLApplicationStatusDTO>();
		Long totalElements = 0l;

		if(status!=null&&status.equals("APPROVED")) {
			pageobj = newoperatorLicenceRepository.getAllApprovedAndRejectApplicationStatus(status, true, search, pageable);
		}
		else if(status!=null&&status.equals("REJECTED")) {
			//pageobj = newoperatorLicenceRepository.getAllApprovedAndRejectApplicationStatus(status, false, search, pageable);
			pageobj = newoperatorLicenceRepository.getAllDeferredApplicationStatus(status,search, pageable);
		}
		else if(status!=null&&status.equals("DEFERRED")) {
//			pageobj = newoperatorLicenceRepository.getAllDeferredApplicationStatus(roleId,userid,status, search, pageable);
			pageobj = newoperatorLicenceRepository.getAllDeferredApplicationStatus(status, search, pageable);
		}
		
		if (pageobj != null) {

			resultResponses = pageobj.getContent().stream().map(Mapper::mapOPLApplicationStatusDTO).collect(Collectors.toList());

		}

		if (pageobj != null) {
			totalElements = pageobj.getTotalElements();
		}

		return new PageImpl<>(resultResponses, pageable, totalElements);
	}

	@Transactional
	@Override
	public JSONObject generateCertificate(String certData) {
		
		JSONObject result=new JSONObject(certData);
		JSONObject requestedData=new JSONObject(certData);
		String vchWareHouseId = requestedData.optString("vchWareHouseId", null);
		String vchOPlId = requestedData.optString("vchOPlId", null);
		Boolean bitLicenceCertGen = requestedData.optBoolean("bitLicenceCertGen", true);
		
		Integer currentYear=LocalDate.now().getYear();
		 String certificateIdStr="WRSC/WOL/MIGORI/"+currentYear+"/";
		 String certificateId =certificateIdStr+generateUniqueId(3);
		Optional<WRSC_OPL_Main> latestWarehouse = wrsoplMainRepository.findTopByVchCertIdStartingWithOrderByVchCertIdDesc(certificateIdStr);
		String lastWarehouseId = null;
		if (latestWarehouse.isPresent()) {
			lastWarehouseId = latestWarehouse.get().getVchOplId();
		}
		//String certificateId = generateFormOneCId(lastWarehouseId, certificateIdStr);
		WRSC_OPL_Main oplfinalEntity=wrsoplMainRepository.findByVchOplAppIdAndBitLicenceCertGenAndBitDeletedFlagFalse(vchOPlId,bitLicenceCertGen);
		OperatorLicenceEntity licenseEntity=newoperatorLicenceRepository.findByOplIdAndLicenceCertGenAndDeletedFlagFalse(vchOPlId,bitLicenceCertGen);
		
		if(wrsoplMainRepository!=null&&newoperatorLicenceRepository!=null) {
			//oplfinalEntity.setVchCertId(certificateId);
			oplfinalEntity.setBitCertGen(true);					
			oplfinalEntity.setBitLicenceCertGen(true);
			oplfinalEntity.setBitCertGen(true);
			oplfinalEntity.setDtCertIssue(LocalDate.now());
			wrsoplMainRepository.saveAndFlush(oplfinalEntity);
			//licenseEntity.setCertId(certificateId);
			licenseEntity.setCertGen(true);					
			licenseEntity.setLicenceCertGen(true);
			licenseEntity.setCertGen(true);
			licenseEntity.setCertIssueDate(LocalDate.now());
			licenseEntity=newoperatorLicenceRepository.saveAndFlush(licenseEntity);
			ConformityMain	conformityMain = conformityMainRepository.findByWarehouseId(licenseEntity.getWarehouseId());
			conformityMain.setLicenceCerti(true);
			conformityMain.setLicenseGen(true);
			result.put("status", 200);
			result.put("certId", licenseEntity.getCertId());
	
		}
		else {
			
			result.put("status", 200);
			result.put("certId", "0");
			
		}
	
		return result;
	}
	
	 // Method to add ordinal suffix
    private static String addOrdinalSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return day + "th";
        }
        switch (day % 10) {
            case 1: return day + "st";
            case 2: return day + "nd";
            case 3: return day + "rd";
            default: return day + "th";
        }
    }

	@Override
	public JSONObject getCertificateDate(String certId) {
		
		JSONObject resultObj=new JSONObject();
		
		WRSC_OPL_Main oplfinalEntity=wrsoplMainRepository.findByVchOplAppIdAndBitLicenceCertGenAndBitDeletedFlagFalse(certId,true);
		
		
		try{
			if(oplfinalEntity!=null) {
				
				LocalDate certIssueDate=oplfinalEntity.getDtCertIssue();
				
		        // Format month and year
		        String formattedDate = certIssueDate.format(DateTimeFormatter.ofPattern("MMMM d yyyy"));

		        // Add ordinal suffix to the day
		        String validFrom = formattedDate.replaceFirst("\\d+", addOrdinalSuffix(certIssueDate.getDayOfMonth()));
		        
		        LocalDate givenDate = certIssueDate;

		        // Get the next year's date
		        LocalDate nextYearDate = givenDate.plusYears(1);

		        // Format month and year
		        String formattedDateTo = nextYearDate.format(DateTimeFormatter.ofPattern("MMMM d yyyy"));

		        // Add ordinal suffix to the day
		        String vaidTo = formattedDateTo.replaceFirst("\\d+", addOrdinalSuffix(nextYearDate.getDayOfMonth()));
		        
		        resultObj.put("certValidFromDt", validFrom);
		        resultObj.put("certValidToDt", vaidTo);
		        resultObj.put("certIssueDt", certIssueDate);
		        resultObj.put("whcertID", oplfinalEntity.getVchCertId());
		        resultObj.put("status", 200);
			}
			else {
				 resultObj.put("certValidFromDt", 0);
			     resultObj.put("certValidToDt", 0);
			     resultObj.put("certIssueDt", 0);
			     resultObj.put("status", 201);   
			}
		}
		catch(Exception e)
		{
			log.info("inside Operator License service IMpl"+e);
			resultObj.put("certValidFromDt", 0);
		     resultObj.put("certValidToDt", 0);
		     resultObj.put("certIssueDt", 0);
		     resultObj.put("status", 500);
		}
		
		return resultObj;
	}

	@Override
	public Page<OPLFormDetailsDTO> getOPLFormDetailsData(String userInfo) {
		
		JSONObject json=new JSONObject(userInfo);
		Integer userId=json.optInt("userId",0);
		Integer roleId=json.optInt("roleId",0);
		String status=json.optString("status",null); 
		String sortDirection="DESC";
		String sortColumn="dtmCreatedOn";
		Integer intCreatedBy = 0;
		Integer totalDataPresent = 0;
		Integer size=json.optInt("size",0);
		Integer pageNo=json.optInt("pageNo",0);
		Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
				sortColumn != null ? sortColumn : "dtmCreatedOn");
		
		totalDataPresent = newoperatorLicenceRepository.countByDeletedFlagAndCreatedBy(false, intCreatedBy);
		Pageable pageRequest = PageRequest.of(json.has("pageNo") ? json.getInt("pageNo") - 1 : 0,
				json.has("size") ? json.getInt("size") : totalDataPresent,sort);// Sort.by(Sort.Direction.DESC, "intId")
		
		Page<Map<String, Object>> pageobj = null;
		List<OPLFormDetailsDTO> resultResponses = new ArrayList<>();

		    pageobj=newoperatorLicenceRepository.getOPLFormDetialsDataValue(userId,roleId,status,pageRequest);
		
		if (pageobj != null) {

			resultResponses = pageobj.getContent().stream().map(Mapper::convertToFormDTO).collect(Collectors.toList());

		}

		if (pageobj != null) {
			totalDataPresent =Math.toIntExact( pageobj.getTotalElements());
		}

		return new PageImpl<>(resultResponses, pageRequest, totalDataPresent);

	}

	@Override
	public Page<OPLApplicationStatusDTO> getActionTakenOnApplication(String formdata) {
		JSONObject json=new JSONObject(formdata);
		Integer userId=json.optInt("userId",0);
		Integer roleId=json.optInt("roleId",0);
		String status=json.optString("status",null); 
		String sortDirection="DESC";
		String sortColumn="dtmCreatedOn";
		Integer intCreatedBy = 0;
		Integer totalDataPresent = 0;
		Integer size=json.optInt("size",0);
		Integer pageNo=json.optInt("pageNo",0);
		Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
				sortColumn != null ? sortColumn : "dtmCreatedOn");
		
		totalDataPresent = newoperatorLicenceRepository.countByDeletedFlagAndCreatedBy(false, intCreatedBy);
		Pageable pageRequest = PageRequest.of(json.has("pageNo") ? json.getInt("pageNo") - 1 : 0,
				json.has("size") ? json.getInt("size") : totalDataPresent,sort);// Sort.by(Sort.Direction.DESC, "intId")
		
		Page<Map<String, Object>> pageobj = null;
		List<OPLApplicationStatusDTO> resultResponses = new ArrayList<OPLApplicationStatusDTO>();

		if(status!=null&&status.equals("APPROVED")) {
			pageobj = newoperatorLicenceRepository.getActionTakenApplicationStatus(userId,roleId,status, true,  pageRequest);
		}
		else if(status!=null&&status.equals("REJECTED")) {
			pageobj = newoperatorLicenceRepository.getActionTakenApplicationStatus(userId,roleId,status, false,  pageRequest);
		}
		else if(status!=null&&status.equals("DEFFERED")) {
			pageobj = newoperatorLicenceRepository.getActionTakenApplicationStatus(userId,roleId,status, false,  pageRequest);
		}
		
		if (pageobj != null) {

			resultResponses = pageobj.getContent().stream().map(Mapper::mapOPLApplicationStatusDTO).collect(Collectors.toList());

		}

		if (pageobj != null) {
			totalDataPresent =Math.toIntExact( pageobj.getTotalElements());
		}

		return new PageImpl<>(resultResponses, pageRequest, totalDataPresent);
	}
	

	

	    public static String generatePassword(int length) {

	  	    final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	  	    final SecureRandom RANDOM = new SecureRandom();
	        StringBuilder password = new StringBuilder(length);
	        for (int i = 0; i < length; i++) {
	            int index = RANDOM.nextInt(ALPHA_NUMERIC.length());
	            password.append(ALPHA_NUMERIC.charAt(index));
	        }
	        return password.toString();
	    }

	    @Override
	    public List<Map<String, Object>> getOperatorLicenceActionHistory(Integer intLicenceSno) {
	    	 return oplServiceApprovalNotingsRepository.getOperatorLicenceActionHistory(intLicenceSno);
	    }



//	@Override
//	public String getOperatorWarehouseNameByWareHouse(String compId) {
//		return new TupleToJsonConverter()
//				.convertListTupleToJsonArray(operatorLicenceRepository.getOperatorWarehouseNameByWareHouse(compId));
//	}

}
