package app.ewarehouse.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.tika.utils.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Strings;

import app.ewarehouse.dto.AocCompProfDetailsMainDTO;
import app.ewarehouse.dto.AocCompProfDirectorDetDTO;
import app.ewarehouse.dto.AocCompProfileDetDTO;
import app.ewarehouse.dto.AocRemarksDto;
import app.ewarehouse.dto.AppliationConformityCommodityDto;
import app.ewarehouse.dto.ApplicationConformityFormOneADto;
import app.ewarehouse.dto.ApplicationConformityFormOneAListDto;
import app.ewarehouse.dto.ApplicationConformityFormOneBDto;
import app.ewarehouse.dto.ApplicationConformityFormOneBListDto;
import app.ewarehouse.dto.ApplicationConformityMainRemarksDto;
import app.ewarehouse.dto.ApplicationConformityWhLocReqDto;
import app.ewarehouse.dto.ApplicationConformityWhLocationDTO;
import app.ewarehouse.dto.ApplicationOfConformityDTO;
import app.ewarehouse.dto.ApplicationOfConformityDirectorDetailsDTO;
import app.ewarehouse.dto.ApplicationOfConformityDirectorResponseDTO;
import app.ewarehouse.dto.ApplicationOfConformityParticularOfApplicantsDTO;
import app.ewarehouse.dto.ApplicationOfConformityParticularOfApplicantsMapper;
import app.ewarehouse.dto.ApplicationOfConformitySupportingDocumentsDTO;
import app.ewarehouse.dto.ApplicationOfConformityWarehouseOperatorViabilityDTO;
import app.ewarehouse.dto.ApprovedAocIdAndShopDto;
import app.ewarehouse.dto.CommodityDetailDTO;
import app.ewarehouse.dto.CommonResponseModal;
import app.ewarehouse.dto.ConformityCertificateDto;
import app.ewarehouse.dto.DraftedWarehouseForPaymentDto;
import app.ewarehouse.dto.Mail;
import app.ewarehouse.dto.OTPRequestDTO;
import app.ewarehouse.dto.PaymentDataStringDto;
import app.ewarehouse.dto.PaymentInitDto;
import app.ewarehouse.dto.PendingConformityDTO;
import app.ewarehouse.dto.UpdatedStatusRequest;
import app.ewarehouse.dto.WarehouseStorageDTO;
import app.ewarehouse.entity.AocCompProfDirectorDetails;
import app.ewarehouse.entity.AocCompProfSignSeal;
import app.ewarehouse.entity.AocCompProfileDetails;
import app.ewarehouse.entity.AocLevels;
import app.ewarehouse.entity.AocRemarksStatus;
import app.ewarehouse.entity.ApplicationOfConformity;
import app.ewarehouse.entity.ApplicationOfConformityActionHistory;
import app.ewarehouse.entity.ApplicationOfConformityCertificate;
import app.ewarehouse.entity.ApplicationOfConformityCommodityDetails;
import app.ewarehouse.entity.ApplicationOfConformityCommodityStorage;
import app.ewarehouse.entity.ApplicationOfConformityDirectorDetails;
import app.ewarehouse.entity.ApplicationOfConformityFormOneA;
import app.ewarehouse.entity.ApplicationOfConformityFormOneB;
import app.ewarehouse.entity.ApplicationOfConformityLocationDetails;
import app.ewarehouse.entity.ApplicationOfConformityMainRemarks;
import app.ewarehouse.entity.ApplicationOfConformityParticularOfApplicants;
import app.ewarehouse.entity.ApplicationOfConformitySupportingDocuments;
import app.ewarehouse.entity.ApplicationOfConformityWarehouseOperatorViability;
import app.ewarehouse.entity.ApproverStatus;
import app.ewarehouse.entity.ConformityCertificate;
import app.ewarehouse.entity.ConformityMain;
import app.ewarehouse.entity.ConformityTakeActionCc;
import app.ewarehouse.entity.ConformityTakeActionCcDetails;
import app.ewarehouse.entity.ConformityTakeActionInspector;
import app.ewarehouse.entity.ConformityTakeActionInspectorSchedule;
import app.ewarehouse.entity.ConformityTakeActionOic;
import app.ewarehouse.entity.Mrole;
import app.ewarehouse.entity.Nationality;
import app.ewarehouse.entity.PaymentAgainst;
import app.ewarehouse.entity.PaymentData;
import app.ewarehouse.entity.PaymentStatus;
import app.ewarehouse.entity.Recommendation;
import app.ewarehouse.entity.RoutineComplianceConclusion;
import app.ewarehouse.entity.RoutineComplianceInspectorConditionOfGoodsStorage;
import app.ewarehouse.entity.RoutineComplianceInspectorEnvironmentIssues;
import app.ewarehouse.entity.RoutineComplianceInspectorITSystem;
import app.ewarehouse.entity.RoutineComplianceInspectorInsurance;
import app.ewarehouse.entity.RoutineComplianceInspectorLocation;
import app.ewarehouse.entity.RoutineComplianceInspectorPhysicalFireProtection;
import app.ewarehouse.entity.RoutineComplianceInspectorShrink;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.entity.WarehousePersonalEquipment;
import app.ewarehouse.entity.WarehouseStatus;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.master.repository.WardMasterRepo;
import app.ewarehouse.repository.AocCompProfDirectorDetRepository;
import app.ewarehouse.repository.AocCompProfSignSealRepository;
import app.ewarehouse.repository.AocCompProfileDetRepository;
import app.ewarehouse.repository.ApplicationConformityLocDetRepository;
import app.ewarehouse.repository.ApplicationOfConformityActionHistoryRepository;
import app.ewarehouse.repository.ApplicationOfConformityCertificateRepository;
import app.ewarehouse.repository.ApplicationOfConformityCommodityDetailsRepository;
import app.ewarehouse.repository.ApplicationOfConformityCommodityStorageRepo;
import app.ewarehouse.repository.ApplicationOfConformityDirectorDetailsRepository;
import app.ewarehouse.repository.ApplicationOfConformityFormOneARepository;
import app.ewarehouse.repository.ApplicationOfConformityFormOneBRepository;
import app.ewarehouse.repository.ApplicationOfConformityMainRemarksRepository;
import app.ewarehouse.repository.ApplicationOfConformityParticularOfApplicantsRepository;
import app.ewarehouse.repository.ApplicationOfConformityPaymentDetailsRepository;
import app.ewarehouse.repository.ApplicationOfConformityRepository;
import app.ewarehouse.repository.ApplicationOfConformitySupportingDocumentsRepository;
import app.ewarehouse.repository.ApplicationOfConformityWarehouseOperatorViabilityRepository;
import app.ewarehouse.repository.CommodityMasterRepository;
import app.ewarehouse.repository.ConformityCertificateRepository;
import app.ewarehouse.repository.ConformityMainRepository;
import app.ewarehouse.repository.ConformityTakeActionCcDetailsRepository;
import app.ewarehouse.repository.ConformityTakeActionCcRepository;
import app.ewarehouse.repository.ConformityTakeActionInspectorRepository;
import app.ewarehouse.repository.ConformityTakeActionInspectorScheduleRepository;
import app.ewarehouse.repository.ConformityTakeActionOicRepository;
import app.ewarehouse.repository.CountyRepository;
import app.ewarehouse.repository.NationalityMasterRepository;
import app.ewarehouse.repository.PaymentDataRepository;
import app.ewarehouse.repository.RecomendationRepository;
import app.ewarehouse.repository.RoutineComInspecEnvIssuesRepo;
import app.ewarehouse.repository.RoutineComInspecGoodsStorageRepo;
import app.ewarehouse.repository.RoutineComInspecInsuranceRepo;
import app.ewarehouse.repository.RoutineComInspecShrinkRepo;
import app.ewarehouse.repository.RoutineComplianceConclusionRepository;
import app.ewarehouse.repository.RoutineComplianceInspectorAgriculturalCommoditiesRepository;
import app.ewarehouse.repository.RoutineComplianceInspectorITSystemRepository;
import app.ewarehouse.repository.RoutineComplianceInspectorLocationRepository;
import app.ewarehouse.repository.RoutineComplianceInspectorPhysicalFireProtectionRepo;
import app.ewarehouse.repository.SubCountyRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.repository.WarehousePersonalEquipmentRepository;
import app.ewarehouse.service.ConformityParticularService;
import app.ewarehouse.service.OTPService;
import app.ewarehouse.util.ApplicationPropertiesValue;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.DocumentUploadutil;
import app.ewarehouse.util.EmailUtil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.Mapper;
import app.ewarehouse.util.PaymentSecureHashGenerate;
import app.ewarehouse.util.RoleIdConstants;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;

@Service
public class ConformityParticularServiceImpl implements ConformityParticularService {

	private static final String STATUS = "status";
	private static final String AN_UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred: ";
	private static final String ERROR = "error";
	private static final Logger logger = LoggerFactory.getLogger(ConformityParticularServiceImpl.class);
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${finalUpload.path}")
	private String finalUploadPath;

	private final ApplicationOfConformityParticularOfApplicantsRepository conformityParticularRepository;
	private final ApplicationOfConformityDirectorDetailsRepository directorRepository;

	private final AocCompProfDirectorDetRepository compDirectorRepository;
	private final ApplicationOfConformitySupportingDocumentsRepository supportingDocRepository;
	private final ApplicationOfConformityWarehouseOperatorViabilityRepository operatorViabilityRepository;
	private final ApplicationOfConformityPaymentDetailsRepository paymentRepository;
	private final ApplicationOfConformityRepository aocRepository;
	private final ApplicationOfConformityCommodityDetailsRepository aocCommodityDetailsRepo;
	private final ApplicationOfConformityCommodityStorageRepo aocCommodityStorageRepo;
	private final ApplicationOfConformityCertificateRepository certificateRepo;
	private final ApplicationOfConformityMainRemarksRepository remarksRepo;
	private final ApplicationOfConformityFormOneARepository formOneARepository;
	private final ApplicationOfConformityFormOneBRepository formOneBRepository;
	private final ConformityMainRepository conformityMainRepository;
	private final ConformityTakeActionCcRepository conformityTakeActionCcRepository;
	private final ConformityTakeActionCcDetailsRepository conformityTakeActionCcDetailsRepository;
	private final ConformityTakeActionInspectorRepository conformityTakeActionInspectorRepository;
	private final ConformityTakeActionInspectorScheduleRepository conformityInspScheduleRepo;
	private final ConformityTakeActionOicRepository conformityTakeActionOicRepository;
	private final ConformityCertificateRepository certificateRepository;
	private final AocCompProfSignSealRepository signSealRepo;
	
	private final JdbcTemplate jdbcTemplate;
	private final ObjectMapper om;
	private final ApplicationConformityLocDetRepository locationDetailsRepo;
	private final CountyRepository countyRepo;
	private final SubCountyRepository subCountyRepo;
	private final WardMasterRepo wardRepo;
	private final AocCompProfileDetRepository compProfRepo;
	private final CommodityMasterRepository commodityMasterRepository;
	private final PaymentDataRepository paymentDataRepository;
	private final ErrorMessages errorMessages;

	public ConformityParticularServiceImpl(
			ApplicationOfConformityParticularOfApplicantsRepository conformityParticularRepository,
			ApplicationOfConformityDirectorDetailsRepository directorRepository,
			ApplicationOfConformitySupportingDocumentsRepository supportingDocRepository,
			ApplicationOfConformityWarehouseOperatorViabilityRepository operatorViabilityRepository,
			ApplicationOfConformityPaymentDetailsRepository paymentRepository,
			ApplicationOfConformityCommodityDetailsRepository aocCommodityDetailsRepo,
			ApplicationOfConformityRepository aocRepository, JdbcTemplate jdbcTemplate,
			ApplicationOfConformityCertificateRepository certificateRepo,
			ApplicationOfConformityMainRemarksRepository remarksRepo, ErrorMessages errorMessages,
			AocCompProfDirectorDetRepository compDirectorRepository, ObjectMapper om,
			ApplicationConformityLocDetRepository locationDetailsRepo, SubCountyRepository subCountyRepo,
			CountyRepository countyRepo, WardMasterRepo wardRepo, AocCompProfileDetRepository compProfRepo,
			CommodityMasterRepository commodityMasterRepository,
			ApplicationOfConformityCommodityStorageRepo aocCommodityStorageRepo,
			ApplicationOfConformityFormOneARepository formOneARepository,
			ApplicationOfConformityFormOneBRepository formOneBRepository,
			PaymentDataRepository paymentDataRepository,
			ConformityMainRepository conformityMainRepository,
			ConformityTakeActionCcRepository conformityTakeActionCcRepository,
			ConformityTakeActionCcDetailsRepository conformityTakeActionCcDetailsRepository,
			ConformityTakeActionOicRepository conformityTakeActionOicRepository,
			ConformityTakeActionInspectorRepository conformityTakeActionInspectorRepository,ConformityTakeActionInspectorScheduleRepository conformityInspScheduleRepo,
			ConformityCertificateRepository certificateRepository,AocCompProfSignSealRepository signSealRepo
			) {
		this.conformityParticularRepository = conformityParticularRepository;
		this.directorRepository = directorRepository;
		this.supportingDocRepository = supportingDocRepository;
		this.operatorViabilityRepository = operatorViabilityRepository;
		this.paymentRepository = paymentRepository;
		this.aocRepository = aocRepository;
		this.aocCommodityDetailsRepo = aocCommodityDetailsRepo;
		this.jdbcTemplate = jdbcTemplate;
		this.errorMessages = errorMessages;
		this.compDirectorRepository = compDirectorRepository;
		this.certificateRepo = certificateRepo;
		this.remarksRepo = remarksRepo;
		this.om = om;
		this.locationDetailsRepo = locationDetailsRepo;
		this.countyRepo = countyRepo;
		this.subCountyRepo = subCountyRepo;
		this.wardRepo = wardRepo;
		this.compProfRepo = compProfRepo;
		this.aocCommodityStorageRepo = aocCommodityStorageRepo;
		this.commodityMasterRepository = commodityMasterRepository;
		this.formOneARepository = formOneARepository;
		this.formOneBRepository = formOneBRepository;
		this.paymentDataRepository = paymentDataRepository;
		this.conformityMainRepository = conformityMainRepository;
		this.conformityTakeActionCcRepository = conformityTakeActionCcRepository;
		this.conformityTakeActionCcDetailsRepository = conformityTakeActionCcDetailsRepository;
		this.conformityTakeActionInspectorRepository = conformityTakeActionInspectorRepository;
		this.conformityInspScheduleRepo = conformityInspScheduleRepo;
		this.conformityTakeActionOicRepository = conformityTakeActionOicRepository;
		this.certificateRepository = certificateRepository;
		this.signSealRepo = signSealRepo;
	}

	ApplicationOfConformity aoc = new ApplicationOfConformity();

	@Autowired
	private NationalityMasterRepository nationalRepo;
	@Autowired(required = true)
	OTPService otpservices;
	@Autowired
	ApplicationPropertiesValue applicationPropertiesValue;
	@Autowired
	PaymentSecureHashGenerate  paymentSecureHashGenerate;
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
	private TuserRepository tuserRepository;
    
    @Autowired
	private ApplicationOfConformityActionHistoryRepository applicationOfConformityActionHistoryRepository;
   

	@Override
	public Long getCountByCreatedByAndDraftStatus(Integer intId) {
		return conformityParticularRepository.countByCreatedByAndDraftStatus(intId);
	}

	@Override
	public ApplicationOfConformityParticularOfApplicants getAocParticularDataById(Integer intId) {
		return conformityParticularRepository.findById(intId).orElse(null);
	}

	@Override
	public void deleteDirectorById(Integer intId) {
		directorRepository.deleteById(intId);
	}

	@Override
	@Transactional
	public JSONObject saveApplicantData(String data) throws JsonProcessingException {
		JSONObject json = new JSONObject();
		// ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			// Convert JSON string to DTO
			ApplicationOfConformityParticularOfApplicantsDTO apDto = om.readValue(data,
					ApplicationOfConformityParticularOfApplicantsDTO.class);
			// Map DTO to entity
			ApplicationOfConformityParticularOfApplicants apEntity = ApplicationOfConformityParticularOfApplicantsMapper
					.toEntity(apDto);
			// Save entity
			ApplicationOfConformityParticularOfApplicants savedEntity = conformityParticularRepository.save(apEntity);
			Integer id = savedEntity.getId();
//			aoc.setParticularOfApplicantsId(savedEntity);
//			Integer applicantId = savedEntity.getId();
			// Process Commodity type
//			List<AocTypeOfCommodityDto> commodityTypeList = apDto.getTypeOfCommodities();
//			for(AocTypeOfCommodityDto ct : commodityTypeList) {
//				ct.setParticularOfApplicantsId(id);
//				ApplicationOfConformityCommodityType ctEntity = ApplicationOfConformityParticularOfApplicantsMapper.toEntityCommodityType(ct);
//				aocCommodityRepo.save(ctEntity);
//			}

			// Process director list
			List<ApplicationOfConformityDirectorDetailsDTO> directorList = apDto.getDirectors();
			for (ApplicationOfConformityDirectorDetailsDTO di : directorList) {
				di.setParticularOfApplicantsId(id);

				// Map DTO to entity
				ApplicationOfConformityDirectorDetails apEntityDirector = ApplicationOfConformityParticularOfApplicantsMapper
						.toEntityDirector(di);
				// Save director entity
				directorRepository.save(apEntityDirector);
			}
			json.put(STATUS, 200);
			json.put("intAocParticularId", savedEntity.getId());
		} catch (Exception e) {
			logger.error("ConformityParticularServiceImpl:saveApplicantData()", e);
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override
	public Long getDraftStatusOfSupportingDocs(Integer intId) {
		return supportingDocRepository.getDraftStatusOfSupportingDocs(intId);
	}

	@Override
	public ApplicationOfConformitySupportingDocuments getAocSupportindDocDataById(Integer intId) {
		return supportingDocRepository.findById(intId).orElse(null);
	}

	@Override
	public JSONObject saveSupportingDocsData(String data) throws JsonProcessingException {
		JSONObject json = new JSONObject();
		// ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			ApplicationOfConformitySupportingDocumentsDTO supportingDocDto = om.readValue(data,
					ApplicationOfConformitySupportingDocumentsDTO.class);
			ApplicationOfConformitySupportingDocuments supportingDoc = Mapper.toEntity(supportingDocDto);
			ApplicationOfConformitySupportingDocuments savedEntity = supportingDocRepository.save(supportingDoc);
			// supportingDocId = savedEntity.getId();
			// saving on main table
			// aoc.setSupportingDocumentId(supportingDoc);
			json.put(STATUS, 200);
		} catch (Exception e) {
			logger.error("ConformityParticularServiceImpl:saveSupportingDocsData()", e);
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override
	public Long getDraftStatusOfViability(Integer intId) {
		return operatorViabilityRepository.getDraftStatusOfViability(intId);
	}

	@Override
	public ApplicationOfConformityWarehouseOperatorViability getViabilityDataById(Integer intId) {
		return operatorViabilityRepository.findById(intId).orElse(null);
	}

	@Override
	public JSONObject saveOperatorViabilityData(String data) throws JsonMappingException, JsonProcessingException {
		JSONObject json = new JSONObject();
		// ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			ApplicationOfConformityWarehouseOperatorViabilityDTO operatorViabilityDto = om.readValue(data,
					ApplicationOfConformityWarehouseOperatorViabilityDTO.class);
			ApplicationOfConformityWarehouseOperatorViability operatorViability = Mapper.toEntity(operatorViabilityDto);
			ApplicationOfConformityWarehouseOperatorViability savedEntity = operatorViabilityRepository
					.save(operatorViability);
			json.put(STATUS, 200);
		} catch (Exception e) {
			logger.error("ConformityParticularServiceImpl:saveOperatorViabilityData()", e);
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

//	@Transactional
//	@Override
//	public JSONObject savePaymentData(String data) throws JsonProcessingException {
//		JSONObject json = new JSONObject();
//		// ObjectMapper om = new ObjectMapper();
//		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		try {
//			ApplicationOfConformityPaymentDetailsDTO paymentDto = om.readValue(data,
//					ApplicationOfConformityPaymentDetailsDTO.class);
//			ApplicationOfConformityPaymentDetails paymentData = Mapper.toEntity(paymentDto);
//			ApplicationOfConformityParticularOfApplicants particularOfApplicants = conformityParticularRepository
//					.findById(paymentDto.getIntParticularOfApplicantsId())
//					.orElseThrow(() -> new EntityNotFoundException("Particular Of Applicants not found"));
//			paymentData.setParticularOfApplicants(particularOfApplicants);
//			paymentData.setIntCreatedBy(paymentDto.getUserId());
//			// Payment Integration will start from here
//			// for this time i am setting payment status as SUCCESS
//			paymentData.setEnmPaymentStatus(PaymentStatus.UNPAID);
//			ApplicationOfConformityPaymentDetails savedEntity = paymentRepository.save(paymentData);
//
//			if (savedEntity.getEnmPaymentStatus() == PaymentStatus.UNPAID) {
//				// call the procedure
//				// String result = (String)
//				// saveDataInAocMainTable(savedEntity.getIntCreatedBy());
//				json.put(STATUS, 200);
//				json.put("payid", savedEntity.getId());
//				json.put("custId", savedEntity.getParticularOfApplicants().getId());
//				json.put("paymentStatus", PaymentStatus.UNPAID);
//			} else {
//				// payment failed.
//				json.put(STATUS, 403);
//				json.put("id", "Payment Failed");
//			}
//
//		} catch (Exception e) {
//			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
//			json.put(STATUS, 500);
//		}
//		return json;
//	}
	
	@Transactional
	@Override
	public JSONObject savePaymentData(String data) throws JsonProcessingException {
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			
			//String secretKey = applicationPropertiesValue.getSecretKey();
			String secretKey ="a3qLCEMr5kctDsi7";
            String apiClientID = applicationPropertiesValue.getApiClientID();
            String serviceID = applicationPropertiesValue.getServiceID();
			PaymentInitDto dto = om.readValue(data, PaymentInitDto.class);
			
			
			
			PaymentData entity = new PaymentData();
			entity.setDepositorId(dto.getVchWareHouseId());
			entity.setAmountExpected(dto.getPaymentAmount().toString());
			entity.setClientIDNumber(dto.getUserId().toString());
			entity.setCurrency(dto.getCurrency());
			entity.setBillRefNumber(getBillReferenceNumber());
			entity.setBillDesc(dto.getDescription());
			entity.setClientName(dto.getApplicantName());
			entity.setIntCreatedBy(dto.getUserId());
			entity.setEnmPaymentAgainst(PaymentAgainst.COC);
			entity.setIntRoleId(dto.getRoleId());
			entity.setVchEmail(dto.getEmail());
			entity.setVchPhoneNumber(dto.getPhoneNumber());
			entity.setEnmPaymentStatus(PaymentStatus.UNPAID);
			entity.setBitDeletedFlag(false);
			PaymentData savedData = paymentDataRepository.save(entity);
			PaymentDataStringDto pd = new PaymentDataStringDto(savedData.getAmountExpected() , savedData.getClientIDNumber(),savedData.getCurrency(),savedData.getBillRefNumber(),savedData.getBillDesc(),savedData.getClientName());
			String hashKey = paymentSecureHashGenerate.generateSignature(pd);
			
			Double amount=Double.parseDouble(savedData.getAmountExpected());
			Long roundedValue = Math.round(amount);
			String AmountExpected=String.valueOf(roundedValue);
			Map<String , Object> result = new HashMap<>();
			result.put("paymentInitId", entity.getId());			
			result.put("amountExpected", AmountExpected);
			result.put("clientIDNumber", savedData.getClientIDNumber());
			result.put("currency", savedData.getCurrency());
			result.put("clientName", savedData.getClientName());
			result.put("billRefNumber", savedData.getBillRefNumber());
			result.put("billDesc", savedData.getBillDesc());
			result.put("clientEmail" , savedData.getVchEmail());
			result.put("clientMSISDN", dto.getPhoneNumber());
			result.put("secureHash", hashKey);
			result.put("secretKey",secretKey);
			result.put("apiClientID", apiClientID);
			result.put("serviceID", serviceID);
			result.put("callBackURLOnSuccess", "http://172.27.33.194:8088/conformity/paymentReturn"); //can be changed
			result.put("pictureURL", "");
			result.put("notificationURL", "https://www.csm.tech/");
			result.put("format", "");
			result.put("sendSTK" , "");
			json.put("result", result);
			System.out.println(result);
		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}
	
	private String getBillReferenceNumber() {
		return UUID.randomUUID().toString();
	}

//	private Object saveDataInAocMainTable(Integer intCreatedBy) {
//
//		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("confirmity_submition");
//		Map<String, Object> inParams = new HashMap<>();
//		inParams.put("USER_ID", intCreatedBy);
//		Map<String, Object> outParams = simpleJdbcCall.execute(inParams);
//		return outParams.get("RESULT_MSG");
//	}
	
	private Object saveDataInAocMainTable(String warehouseId ,Integer intCreatedBy , Integer roleId) {

		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("confirmity_save_procedure");
		Map<String, Object> inParams = new HashMap<>();
		inParams.put("WAREHOUSE_ID", warehouseId);
		inParams.put("USER_ID", intCreatedBy);
		inParams.put("ROLE_ID", roleId);
		Map<String, Object> outParams = simpleJdbcCall.execute(inParams);
		return outParams.get("RESULT_MSG");
	}
	
	

	@Override
	public List<ApplicationOfConformityDTO> findAll() {
		// List<ApplicationOfConformityDTO> entitiesDto = new ArrayList<>();
		logger.info("inside get all applicationofConformityDTo");
//		try {
//			 List<ApplicationOfConformity> entities = aocRepository.findAll();
//		     System.out.println("inside service"+entities);
//		     entitiesDto = entities.stream().map(ApplicationOfConformityMapper::toDto).collect(Collectors.toList());
//			
//		} catch (StackOverflowError e) {
//			logger.error(AN_UNEXPECTED_ERROR_OCCURRED+e);
//		}
		return null;
	}

	@Override
	public void updateApplicationStatus(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		UpdatedStatusRequest upStatus;
		try {
			upStatus = om.readValue(decodedData, UpdatedStatusRequest.class);
			ApplicationOfConformity application = aocRepository.findById(upStatus.getApplicationId())
					.orElseThrow(() -> new RuntimeException("Application not found"));
			application.setEnmStatus(upStatus.getEnmStatus());

//			application.setRole(upStatus.ge);
			aocRepository.save(application);
		} catch (JsonProcessingException e) {
			logger.error("Failed to process JSON data: {} in ConformityParticularService", e.getMessage());
			throw new RuntimeException("Invalid data format", e);
		}
	}

	@Override
	public void updateApplicationStatus(Status status, Integer role, String appId) {
		ApplicationOfConformity application = aocRepository.findById(appId)
				.orElseThrow(() -> new RuntimeException("Application not found"));

		if (status == Status.Rejected) {
			application.setEnmStatus(status);
		} else {
			application.setRole(role + 1);
		}
		aocRepository.save(application);
	}

	@Override
	public Page<ApplicationOfConformity> getApplicationByStatusAndRole(Date fromDate, Date toDate, Status status,
			Integer pendingAt, Pageable pageable) {
		logger.info("inside getApplicationByStatusAndRole");
		Page<ApplicationOfConformity> applicationsPage = aocRepository.findByFilters(toDate, toDate, status, pendingAt,
				pageable);
		return new PageImpl<>(applicationsPage.getContent(), pageable, applicationsPage.getTotalElements());
	}

	@Override
	@Transactional
	public ApplicationOfConformity findByApplicationIdWithDirectors(String applicationId) {
		logger.info("inside find by id ApplicationOFConformity");
		ApplicationOfConformity application = aocRepository.findByApplicationIdWithDirectors(applicationId)
				.orElse(null);

		if (application != null) {

			ApplicationOfConformityParticularOfApplicants particularOfApplicants = application
					.getParticularOfApplicantsId();
			Integer particularsId = particularOfApplicants.getId();

			Set<ApplicationOfConformityDirectorResponseDTO> directorsdto = directorRepository
					.findDirectorDetailsByParticularOfApplicantsId(particularsId);

			final Set<ApplicationOfConformityDirectorDetails> directors = directorsdto.stream().map(dto -> {
				ApplicationOfConformityDirectorDetails entity = dto.toEntity();
				// entity.setParticularOfApplicants(particularOfApplicants);
				try {
					Nationality nationality = nationalRepo.findById(dto.getNationalityId()).orElseThrow(
							() -> new RuntimeException("Nationality not found with ID: " + dto.getNationalityId()));
					entity.setNationality(nationality);
					// entity = entityManager.merge(entity);
				} catch (Exception e) {
					logger.error("ConformityParticularServiceImpl:findByApplicationIdWithDirectors()", e);
				}
				entity.setParticularOfApplicants(null);
				return entity;
			}).collect(Collectors.toSet());

			particularOfApplicants.getDirectors().addAll(directors);
		}
		return application;
	}

	@Override
	public ApplicationOfConformity findById(String applicationId) {
		return aocRepository.findById(applicationId).orElse(null);
	}

	@Override
	@Transactional
	public void giveRemarks(String data) {
		try {
			// Decode and parse the input data
			String decodedData = CommonUtil.inputStreamDecoder(data);
			// ObjectMapper objectMapper = new ObjectMapper();
			om.registerModule(new JavaTimeModule());
			AocRemarksDto dto = om.readValue(decodedData, AocRemarksDto.class);
			ApplicationOfConformity aocData = aocRepository.findById(dto.getIntApplicantId())
					.orElseThrow(() -> new EntityNotFoundException(
							"ApplicationOfConformity not found with ID: " + dto.getIntApplicantId()));
			ApplicationOfConformityMainRemarks remarks = new ApplicationOfConformityMainRemarks();
			switch (dto.getRoleId()) {
			case RoleIdConstants.OIC:

				aocData.setOicRoleId(dto.getRoleId());
				aocData.setOicUserId(dto.getUserId());
				if (dto.getEnmStatus() != null && "Deferred".equals(dto.getEnmStatus())) {
					aocData.setEnmStatus(Status.Deferred);
					remarks.setApplicationStatus(AocRemarksStatus.DEFERRED);
					aocData.setCurrentLevel(AocLevels.OIC);
				} else {
					aocData.setEnmStatus(Status.Pending);
					if (Boolean.TRUE.equals(aocData.getIsRequiredInspection())) {
						aocData.setVchOicRemarks(dto.getTxtRemark());
						aocData.setCurrentLevel(AocLevels.INSPECTOR);
						aocData.setPendingAt(RoleIdConstants.INSPECTOR);
						remarks.setApplicationStatus(AocRemarksStatus.FORWARDED_TO_INSPECTOR);
					} else {
						aocData.setVchOicFinRemarks(dto.getTxtRemark());
						aocData.setCurrentLevel(AocLevels.APPROVER);
						aocData.setPendingAt(RoleIdConstants.APPROVER);
						remarks.setApplicationStatus(AocRemarksStatus.FORWARDED_TO_APPROVER);
					}
				}
				break;
			case RoleIdConstants.INSPECTOR:
				handleInspectorFormSubmission(dto, aocData, remarks);
				break;
//			case RoleIdConstants.OIC_FINANCE:
//				aocData.setVchOicFinRemarks(dto.getTxtRemark());
//				aocData.setOicFinRoleId(dto.getRoleId());
//				aocData.setOicFinUserId(dto.getUserId());
//				break;
			case RoleIdConstants.APPROVER:
				aocData.setVchApproverRemarks(dto.getTxtRemark());
				aocData.setTime(dto.getTimeOfInspect());
				aocData.setDate(dto.getDateOfInspect());
				byte[] decodedMomFile = Base64.getDecoder().decode(dto.getTxtInspectorFilePath());
				String momFilePath = DocumentUploadutil.uploadFileByte("AOC_MOM_UPLOAD_" + System.currentTimeMillis(),
						decodedMomFile, FolderAndDirectoryConstant.AOC_MOM_UPLOAD_FOLDER);
				aocData.setMomFilePath(momFilePath);
				aocData.setApproverRoleId(dto.getRoleId());
				aocData.setApproverUserId(dto.getUserId());
				aocData.setCurrentLevel(AocLevels.CEO);
				aocData.setPendingAt(RoleIdConstants.CEO);
				remarks.setApplicationStatus(AocRemarksStatus.FORWARDED_TO_CEO);
				break;
			case RoleIdConstants.CEO:
				aocData.setVchCeoRemarks(dto.getTxtRemark());
				aocData.setCeoRoleId(dto.getRoleId());
				aocData.setCeoUserId(dto.getUserId());
				aocData.setEnmStatus("Accepted".equals(dto.getEnmStatus()) ? Status.Accepted : Status.Rejected);
				remarks.setApplicationStatus(
						"Accepted".equals(dto.getEnmStatus()) ? AocRemarksStatus.ACCEPTED : AocRemarksStatus.REJECTED);
				aocData.setDtDateOfIssue("Accepted".equals(dto.getEnmStatus()) ? LocalDate.now() : null);
				aocData.setPendingAt(null);
				break;
			default:
				logger.warn("Invalid username: {}", dto.getUsername());
				throw new IllegalArgumentException("Invalid username: " + dto.getUsername());
			}
			ApplicationOfConformity savedAocData = aocRepository.save(aocData);
			if (!(dto.getRoleId() == RoleIdConstants.INSPECTOR && (dto.getDateOfInspect() != null
					&& dto.getTimeOfInspect() != null && dto.getTxtDescriptionOfInspection() != null))) {

				remarks.setApplicationId(savedAocData);
				remarks.setRemarks(dto.getTxtRemark());
				remarks.setDateOfRemarks(LocalDate.now());
				Tuser user = new Tuser();
				user.setIntId(dto.getUserId());
				remarks.setUser(user);
				Mrole role = new Mrole();
				role.setIntId(dto.getRoleId());
				;
				remarks.setRole(role);
				// remarks.setApplicationStatus(savedAocData.getEnmStatus());
				remarks.setDeletedFlag(false);
				remarksRepo.save(remarks);
			}
			if (savedAocData.getEnmStatus() == Status.Accepted) {
				ApplicationOfConformityCertificate certificate = new ApplicationOfConformityCertificate();
				certificate.setApplication(savedAocData);
				certificate.setCompany(savedAocData.getProfDet());
				certificate.setCertificateNo(getCertificateNo("WRSC/LC/WHOCC/"));
				certificate.setUserId(savedAocData.getIntCreatedBy());
				certificate.setApproverRoleId(dto.getRoleId());
				certificate.setApproverUserId(dto.getUserId());
				certificate.setApproverUserName(dto.getUsername());
				certificate.setValidFrom(savedAocData.getDtDateOfIssue());
				certificate.setValidTo(savedAocData.getDtDateOfIssue().plusYears(1));
				certificate.setEnmPaymentStatus(PaymentStatus.SUCCESS);
				certificate.setEnmWarehouseStatus(WarehouseStatus.Active);
				certificate.setBitDeletedFlag(false);
				certificateRepo.save(certificate);
			}
		} catch (IOException e) {
			logger.error("Failed to process input data", e);
			throw new RuntimeException("Failed to process input data", e);
		} catch (Exception e) {
			logger.error("An unexpected error occurred", e);
			throw new RuntimeException("An unexpected error occurred", e);
		}
	}

	private String getCertificateNo(String id) {
		//String dbCurrentId = certificateRepo.getId();
		String dbCurrentId = certificateRepository.getId();
		if (dbCurrentId == null) {
			return id + Year.now().getValue() + "/" + "1";
		} else {
			// Integer idNum = Integer.parseInt(dbCurrentId.substring(3,
			// dbCurrentId.length()));
			String[] parts = dbCurrentId.split("/");
			String result = parts[parts.length - 1];
			Integer idNum = Integer.parseInt(result);
			AtomicInteger seq = new AtomicInteger(idNum);
			int nextVal = seq.incrementAndGet();
			return id + Year.now().getValue() + "/" + nextVal;
		}
	}

	private void handleInspectorFormSubmission(AocRemarksDto dto, ApplicationOfConformity aocData,
			ApplicationOfConformityMainRemarks remarks) {
		// Handle the first form (dateOfInspect, timeOfInspect,
		// txtDescriptionOfInspection)
		if (dto.getDateOfInspect() != null && dto.getTimeOfInspect() != null
				&& dto.getTxtDescriptionOfInspection() != null) {
			aocData.setDtDateOfInspection(dto.getDateOfInspect());
			aocData.setTmTimeOfInspection(dto.getTimeOfInspect());
			aocData.setVchDescriptionOfInspection(dto.getTxtDescriptionOfInspection());
		}

		// Handle the second form (txtInspectorFilePath, txtRemark)
		if (dto.getTxtInspectorFilePath() != null) {
			byte[] decodedFile = Base64.getDecoder().decode(dto.getTxtInspectorFilePath());
			String filePath = DocumentUploadutil.uploadFileByte(
					"AOC_INSPECTOR_INSPECTION_REPORT_" + System.currentTimeMillis(), decodedFile,
					FolderAndDirectoryConstant.AOC_INSPECTOR_REPORT_FOLDER);
			aocData.setVchInspectionReport(filePath);
		} else {
			aocData.setVchInspectionReport(null);
		}

		if (dto.getTxtRemark() != null && !dto.getTxtRemark().trim().isEmpty()) {
			aocData.setVchInspectorRemarks(dto.getTxtRemark());
			if (Boolean.TRUE.equals(aocData.getIsRequiredInspection())) {
				aocData.setIsRequiredInspection(false);
				aocData.setCurrentLevel(AocLevels.OIC);
				aocData.setPendingAt(RoleIdConstants.OIC);
				remarks.setApplicationStatus(AocRemarksStatus.FORWARDED_TO_OIC);
			}
		} else {
			aocData.setVchInspectorRemarks(null);
		}

		// Set the inspector's user ID and role ID
		aocData.setInspectorUserId(dto.getUserId());
		aocData.setInspectorRoleId(dto.getRoleId());
	}

	// user based view of aoc
	@Override
	public Page<ApplicationOfConformity> getApplicationByUserId(Date fromDate, Date toDate, Integer userId,
			Pageable pageable) {
		logger.info("inside getApplicationByUserId");
		Page<ApplicationOfConformity> applicationsPage = aocRepository.findByUserIdFilters(toDate, toDate, userId,
				pageable);
		return new PageImpl<>(applicationsPage.getContent(), pageable, applicationsPage.getTotalElements());
	}

	@Override
	public String getCommodityTypes(String id) {
		try {
			String conformityList = aocRepository.getCommodityTypes(id, Status.Accepted, false);
			logger.info("Application of conformity commodities are:" + conformityList);
			return conformityList;
		} catch (Exception e) {
			throw new CustomGeneralException("");
		}

	}

	@Override
	public List<ApplicationOfConformityDTO> findByUserIdAndStatus(Integer userId) {
		List<ApplicationOfConformity> aocList = aocRepository.findByIntCreatedByAndEnmStatus(userId, Status.Accepted);

		if (aocList.isEmpty()) {
			throw new CustomGeneralException(errorMessages.getApprovedAocNotFound());
		}

		return aocList.stream().map(aocD -> {
			ApplicationOfConformityDTO aocDto = new ApplicationOfConformityDTO();
			BeanUtils.copyProperties(aocD, aocDto);

			// Map company profile details
			AocCompProfDetailsMainDTO companyDetailsDTO = new AocCompProfDetailsMainDTO();
			AocCompProfileDetDTO profileDTO = new AocCompProfileDetDTO();
			BeanUtils.copyProperties(aocD.getProfDet(), profileDTO);
			companyDetailsDTO.setCompanyProfile(profileDTO);
			companyDetailsDTO.setUserId(aocD.getProfDet().getCreatedBy());

			// Map directors
			List<AocCompProfDirectorDetails> directors = compDirectorRepository
					.findByProfDetAndDeletedFlagFalse(aocD.getProfDet());
			List<AocCompProfDirectorDetDTO> directorDTOs = directors.stream().map(director -> {
				AocCompProfDirectorDetDTO directorDTO = new AocCompProfDirectorDetDTO();
				BeanUtils.copyProperties(director, directorDTO);
				directorDTO.setProfDetId(aocD.getProfDet().getProfDetId());
				return directorDTO;
			}).collect(Collectors.toList());

			companyDetailsDTO.setDirectors(directorDTOs);

			// Set company details
			aocDto.setCompanyDetails(companyDetailsDTO);

			return aocDto;
		}).collect(Collectors.toList());
	}

	@Override
	public ApplicationOfConformityDTO findByUserIdAndApplicationId(Integer userId, String applicationId) {
		List<ApplicationOfConformity> aocList = aocRepository.findByIntCreatedByAndApplicationId(userId, applicationId);

		if (aocList.isEmpty()) {
			throw new CustomGeneralException(errorMessages.getApprovedAocNotFound());
		}

		ApplicationOfConformity aocD = aocList.get(0);

		ApplicationOfConformityDTO aocDto = new ApplicationOfConformityDTO();
		BeanUtils.copyProperties(aocD, aocDto);

		// Map company profile details
		AocCompProfDetailsMainDTO companyDetailsDTO = new AocCompProfDetailsMainDTO();
		AocCompProfileDetDTO profileDTO = new AocCompProfileDetDTO();
		BeanUtils.copyProperties(aocD.getProfDet(), profileDTO);
		companyDetailsDTO.setCompanyProfile(profileDTO);
		companyDetailsDTO.setUserId(aocD.getProfDet().getCreatedBy());

		// Map directors
		List<AocCompProfDirectorDetails> directors = compDirectorRepository
				.findByProfDetAndDeletedFlagFalse(aocD.getProfDet());
		List<AocCompProfDirectorDetDTO> directorDTOs = new ArrayList<>();
		for (AocCompProfDirectorDetails director : directors) {
			AocCompProfDirectorDetDTO directorDTO = new AocCompProfDirectorDetDTO();
			BeanUtils.copyProperties(director, directorDTO);
			directorDTO.setProfDetId(aocD.getProfDet().getProfDetId());
			directorDTOs.add(directorDTO);
		}

		companyDetailsDTO.setDirectors(directorDTOs);

		aocDto.setCompanyDetails(companyDetailsDTO);

		return aocDto;
	}

	@Override
	public List<ApprovedAocIdAndShopDto> getApprovedApplicationIdAndShop(Integer countyId, Integer subCountyId,
			Integer roleId) {
		List<Tuple> shopList = aocRepository.getApprovedApplicationIdAndShop(countyId, subCountyId, roleId);

		List<ApprovedAocIdAndShopDto> dtoList = new ArrayList<>();
		for (Tuple tuple : shopList) {
			ApprovedAocIdAndShopDto dto = new ApprovedAocIdAndShopDto();
			dto.setApplicationId((String) tuple.get("applicationId"));
			dto.setShop((String) tuple.get("shop"));
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public String getOperatorFullName(String applicantId) {
		return aocRepository.getOperatorFullName(applicantId);
	}

	@Override
	public ConformityCertificateDto getCertificate(String applicantId) {
		ApplicationOfConformityCertificate result = certificateRepo.findByApplicationId(applicantId);
		ConformityCertificateDto dto = new ConformityCertificateDto();
		dto.setApplicantName(result.getApplication().getParticularOfApplicantsId().getApplicantFullName());
		dto.setCompanyName(result.getApplication().getParticularOfApplicantsId().getCompanyRegistrationNumber());
		dto.setCountyName(result.getApplication().getParticularOfApplicantsId().getCountyId().getName());
		dto.setPostalCode(result.getApplication().getParticularOfApplicantsId().getPostalCode());
		dto.setLrNumber(result.getApplication().getParticularOfApplicantsId().getLrNumber());
		dto.setApplicationId(result.getApplication().getApplicationId());
		dto.setCertificateId(result.getCertificateNo());
		dto.setApproverName(result.getApproverUserName());
		dto.setSealPath(result.getApplication().getParticularOfApplicantsId().getUploadCompanySealPath());
		dto.setDateOfIssue(result.getApplication().getDtDateOfIssue());
		dto.setValidFrom(result.getValidFrom());
		dto.setValidTo(result.getValidTo());
		return dto;
	}

	@Override
	public List<ApplicationConformityMainRemarksDto> getRemarks(String applicantId) {
		List<ApplicationOfConformityMainRemarks> remarksData = remarksRepo.findByApplicationId(applicantId);

		return remarksData.stream().map(entity -> {
			ApplicationConformityMainRemarksDto dto = new ApplicationConformityMainRemarksDto();

			// Assuming getApplicantId() and getApplicantName() methods exist in
			// ApplicationOfConformity
			dto.setApplicantId(entity.getApplicationId().getApplicationId());
			dto.setApplicantName(entity.getApplicationId().getParticularOfApplicantsId().getApplicantFullName());
			dto.setRemark(entity.getRemarks());
			dto.setDateOfRemark(entity.getDateOfRemarks());
			dto.setUserId(entity.getUser().getIntId());
			dto.setRoleId(entity.getRole().getIntId());
			dto.setUserName(entity.getUser().getTxtFullName());
			dto.setRoleName(entity.getRole().getTxtRoleName());
			dto.setApplicationStatus(entity.getApplicationStatus());

			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	@Override
	public JSONObject generateCertificate(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		// ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			String applicationId = om.readValue(decodedData, String.class);
			ApplicationOfConformity aoc = aocRepository.findById(applicationId).orElse(null);
			if (aoc != null) {
				aoc.setBitCertGenerate(true);
				json.put(STATUS, 200);
				json.put("result", "Certificate Generated.");
			} else {
				json.put(STATUS, 404);
				json.put("result", "No Application found.");
			}

		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;

	}

	@Override
	public List<ApprovedAocIdAndShopDto> getAllApprovedApplicationIdAndShop(String warehouseId) {
		List<Tuple> shopList = aocRepository.getAllApprovedApplicationIdAndShop(warehouseId);
		List<ApprovedAocIdAndShopDto> dtoList = new ArrayList<>();
		for (Tuple tuple : shopList) {
			ApprovedAocIdAndShopDto dto = new ApprovedAocIdAndShopDto();
			dto.setApplicationId((String) tuple.get("applicationId"));
			dto.setShop((String) tuple.get("shop"));
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public JSONObject verifyCertificate(String data) {
		String getcode = data.substring(10, data.length() - 1);
		byte[] decoded = Base64.getDecoder().decode(getcode);
		String decodedData = new String(decoded, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject();
		// ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			String certificateNo = decodedData;
			ApplicationOfConformityCertificate certificate = certificateRepo.findByCertificateNo(certificateNo);
			if (certificate != null) {
				LocalDate currentDate = LocalDate.now();
				if (currentDate.isBefore(certificate.getValidTo())) {
					// String otp = ComplainTokenGenerator.getOtpNumberString();
					OTPRequestDTO otprequestDTO = new OTPRequestDTO();
					otprequestDTO.setEmail("chinmaya.jena@csm.tech");
					otprequestDTO.setMobile("1234567890");
					otprequestDTO.setIp(null);
					otprequestDTO.setUserid(null);
					CommonResponseModal otpreceive = otpservices.getOTP(otprequestDTO);
					String otp = otpreceive.getOtp();
					if (otp != null && !StringUtils.isBlank(otp)) {
						Mail mail = new Mail();
						mail.setMailSubject("Otp Mail.");
						mail.setContentType("text/html");
						//mail.setMailCc("uiidptestmail@gmail.com");
						mail.setTemplate("OTP is : " + otp);
						mail.setMailTo("chinmaya.jena@csm.tech");
						EmailUtil.sendMail(mail.getMailSubject(), mail.getTemplate(), mail.getMailTo());
						json.put(STATUS, 200);
						json.put("result", "OTP Sent");
						json.put("sendStatus", true);
						json.put("email", "chinmaya.jena@csm.tech");
						json.put("mobile", "0");
					} else {
						json.put(STATUS, 200);
						json.put("sendStatus", false);
						json.put("result", "OTP has been sent already. Please Check and Verify");
					}
				} else {
					json.put("msg", "There might some error.");
				}
			} else {
				json.put(STATUS, 404);
				json.put("result", "No Application found.");
			}

		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override

	public List<ApprovedAocIdAndShopDto> getWareHouseIdAndName(Integer countyId, Integer subCountyId) {

		List<Tuple> shopList = aocRepository.getWareHouseIdAndName(countyId, subCountyId);
		List<ApprovedAocIdAndShopDto> dtoList = new ArrayList<>();
		for (Tuple tuple : shopList) {
			ApprovedAocIdAndShopDto dto = new ApprovedAocIdAndShopDto();
			dto.setApplicationId((String) tuple.get("applicationId"));
			dto.setShop((String) tuple.get("shop"));
			dtoList.add(dto);
		}
		return dtoList;

	}

	@Transactional
	public JSONObject saveLocationDetails(String code) {
		JSONObject response = new JSONObject();
		String decodedData = CommonUtil.inputStreamDecoder(code);
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			ApplicationConformityWhLocReqDto dto = om.readValue(decodedData, ApplicationConformityWhLocReqDto.class);
			List<ApplicationConformityWhLocationDTO> list = dto.getLocationDetailsData();

//	        List<ApplicationOfConformityLocationDetails> entities = list.stream().map(singleData -> {
//	            return mapDtoToEntity(singleData);
//	        }).collect(Collectors.toList());
//
//	        locationDetailsRepo.saveAll(entities);
			for (ApplicationConformityWhLocationDTO singleData : list) {
				ApplicationOfConformityLocationDetails entity = mapDtoToEntity(singleData);
				locationDetailsRepo.save(entity);
			}

			response.put("status", "success");
			response.put("message", "Locations saved successfully");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "error");
			response.put("message", e.getMessage());
		}

		return response;
	}

	private ApplicationOfConformityLocationDetails mapDtoToEntity(ApplicationConformityWhLocationDTO singleData) {
		ApplicationOfConformityLocationDetails entity;
		if (singleData.getWhLocationId() != null) {
			entity = locationDetailsRepo.findById(singleData.getWhLocationId()).orElseThrow(
					() -> new EntityNotFoundException("Location not found for ID: " + singleData.getWhLocationId()));
		} else {
			entity = new ApplicationOfConformityLocationDetails();
		}

		entity.setWarehouseName(singleData.getWarehouseName());
		entity.setWhOperatorName(singleData.getWarehouseOperatorName());
		if (entity.getWarehouseId() == null) {
			entity.setWarehouseId(generateWarehouseId());
		} else {
			entity.setWarehouseId(entity.getWarehouseId());
		}
		entity.setEmail(singleData.getEmail());
		entity.setMobileNumber(singleData.getMobileNumber());
		entity.setLrNumber(singleData.getLrNumber());

		entity.setCounty(countyRepo.findById(singleData.getCounty()).orElse(null));
		entity.setSubCounty(subCountyRepo.findById(singleData.getSubCounty()).orElse(null));
		entity.setWard(wardRepo.findById(singleData.getWard()).orElse(null));

		entity.setLongitude(singleData.getLongitude());
		entity.setLatitude(singleData.getLatitude());
		entity.setStreetName(singleData.getStreetName());
		entity.setBuilding(singleData.getBuilding());
		entity.setPolicyNumber(singleData.getPolicyNumber());
		entity.setCreatedBy(singleData.getUserId());
		entity.setCompany(compProfRepo.findById(singleData.getCompanyId()).orElse(null));

		return entity;
	}

//	private String generateWarehouseId() {
//	    String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
//	    Optional<ApplicationOfConformityLocationDetails> latestWarehouse = locationDetailsRepo.findTopByWarehouseIdStartingWithOrderByWarehouseIdDesc("WAR" + currentDate);
//	    int nextId = 1;
//	    if (latestWarehouse.isPresent()) {
//	        String lastWarehouseId = latestWarehouse.get().getWarehouseId();
//	        String lastNumberStr = lastWarehouseId.substring(11);
//	        nextId = Integer.parseInt(lastNumberStr) + 1;
//	    }
//	    String nextIdStr = String.format("%03d", nextId);
//	    String warehouseId = "WAR" + currentDate + nextIdStr;
//	    return warehouseId;
//	}

	private String generateWarehouseId() {
		// Step 1: Get the current date in "yyyyMMdd" format
		String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

		// Step 2: Query the most recent warehouse ID that starts with "WAR" +
		// currentDate
		Optional<ApplicationOfConformityLocationDetails> latestWarehouse = locationDetailsRepo.findTopByWarehouseIdStartingWithOrderByWarehouseIdDesc("WAR" + currentDate);

		// Step 3: Set the next ID to 1 if no previous warehouse IDs exist for today
		int nextId = 1;

		// Step 4: If a previous warehouse ID exists, extract and increment the last
		// number
		if (latestWarehouse.isPresent()) {
			String lastWarehouseId = latestWarehouse.get().getWarehouseId();

			// Ensure the ID has the correct format (i.e., 11 characters: "WARyyyyMMddxxx")
			if (lastWarehouseId.length() >= 11) {
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
		}

		// Step 5: Format the next ID to be a 3-digit number
		String nextIdStr = String.format("%03d", nextId);

		// Step 6: Create the final warehouse ID
		return "WAR" + currentDate + nextIdStr;
	}
	
	@Override
	public JSONObject deleteLocationDetailsByLocationId(Long locationId) {
	    JSONObject response = new JSONObject();

	    try {
	        if (locationId == null) {
	            response.put("status", "Error");
	            response.put("message", "Invalid location ID");
	            return response;
	        }

	        // Check if location is referenced in other tables
	        int countCommodityStorage = aocCommodityStorageRepo.countReferencesInCommodityStorage(locationId);
	        int countCommodityDetails = aocCommodityDetailsRepo.countReferencesInCommodityDetails(locationId);
	        int countFormOneA = formOneARepository.countReferencesInFormOneA(locationId);
	        int countFormOneB = formOneBRepository.countReferencesInFormOneB(locationId);

	        if (countCommodityStorage > 0 || countCommodityDetails > 0 || countFormOneA > 0 || countFormOneB > 0) {
	            response.put("status", "Error");
	            response.put("message", "Cannot delete. Location is referenced in active records.");
	            return response;
	        }

	        // Perform soft delete
	        int updatedRows = locationDetailsRepo.markAsDeleted(locationId);
	        if (updatedRows > 0) {
	            response.put("status", "Success");
	            response.put("message", "Warehouse Removed");
	        } else {
	            response.put("status", "Not Found");
	            response.put("message", "Location not found or already deleted");
	        }

	    } catch (Exception e) {
	        response.put("status", "Error");
	        response.put("message", "An error occurred: " + e.getMessage());
	    }

	    return response;
	}



	@Override
	public JSONArray getDraftedLocationDetails(String companyId) {
		JSONArray arrList = new JSONArray();
		List<Object[]> data = locationDetailsRepo.getDraftedWarehouseDetails(companyId);
		for (Object[] singleData : data) {
			JSONObject obj = new JSONObject();
			obj.put("warehouseId", singleData[0]);
			obj.put("warehouseName", singleData[1]);
			arrList.put(obj);
		}
		return arrList;
	}

	@Override
	@Transactional
	public JSONObject saveCommodityDetails(String code) {
		JSONObject response = new JSONObject();

		try {
			String decodedData = CommonUtil.inputStreamDecoder(code);
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			AppliationConformityCommodityDto dto = om.readValue(decodedData, AppliationConformityCommodityDto.class);	
			AocCompProfileDetails company = compProfRepo.findById(dto.getCompanyId())
					.orElseThrow(() -> new RuntimeException("Company not found with ID: " + dto.getCompanyId()));
			aocCommodityDetailsRepo.deleteByCompanyAndDraftStatusTrueAndDeletedFlagFalse(company);
			aocCommodityStorageRepo.deleteByCompanyAndDraftStatusTrueAndDeletedFlagFalse(company);
			List<ApplicationOfConformityCommodityDetails> commodityDetailsList = new ArrayList<>();
			for (WarehouseStorageDTO singleStorageDetails : dto.getCommodityStorageDetails()) {
				ApplicationOfConformityCommodityStorage storage = new ApplicationOfConformityCommodityStorage();
				storage.setCompany(company);
				storage.setWarehouseLocation(locationDetailsRepo.findById(singleStorageDetails.getWarehouseId()).get());
				storage.setStorageCapacity(singleStorageDetails.getStorageCapacity());
				storage.setCreatedBy(dto.getUserId());
				ApplicationOfConformityCommodityStorage savedEntity = aocCommodityStorageRepo.save(storage);
				for (CommodityDetailDTO singleData : singleStorageDetails.getCommodityDetails()) {
					ApplicationOfConformityCommodityDetails details = new ApplicationOfConformityCommodityDetails();
					details.setCommodityStorage(savedEntity);
					details.setCommodityType(commodityMasterRepository.findById(singleData.getCommodityId())
							.orElseThrow(() -> new RuntimeException(
									"Commodity not found with ID: " + singleData.getCommodityId())));
					details.setWarehouseLocation(
							locationDetailsRepo.findById(singleStorageDetails.getWarehouseId()).get());
					details.setCompany(company);
					details.setStorageCapacity(singleStorageDetails.getStorageCapacity());
					details.setUnit(singleData.getUnit());
					details.setCreatedBy(dto.getUserId());
					commodityDetailsList.add(details);
				}
			}
			// Save all details after the loop
			aocCommodityDetailsRepo.saveAll(commodityDetailsList);
			response.put("message", "Saved Successfully");

		} catch (JsonProcessingException e) {
			response.put("message", "Invalid JSON format");
		} catch (RuntimeException e) {
			response.put("message", e.getMessage());
		} catch (Exception e) {
			response.put("message", "Unable to save due to internal server error!");
		}

		return response;
	}

	@Override
	@Transactional
	public JSONObject saveFormOneADetails(String code) {
		JSONObject response = new JSONObject();

		try {
			String decodedData = CommonUtil.inputStreamDecoder(code);
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			ApplicationConformityFormOneAListDto dto = om.readValue(decodedData,
					ApplicationConformityFormOneAListDto.class);

			List<ApplicationOfConformityFormOneA> entities = new ArrayList<>();

			for (ApplicationConformityFormOneADto singleData : dto.getFormOneAList()) {
				// Validate warehouse & company details
				ApplicationOfConformityFormOneA entity = new ApplicationOfConformityFormOneA();
				entity.setWarehouseLocation(locationDetailsRepo.findById(singleData.getWarehouseId()).orElseThrow(
						() -> new RuntimeException("Warehouse not found with ID: " + singleData.getWarehouseId())));
				entity.setCompany(compProfRepo.findById(singleData.getCompanyId()).orElseThrow(
						() -> new RuntimeException("Company not found with ID: " + singleData.getCompanyId())));
				if(singleData.getFormOneADocId() != null) {
				entity.setFormOneADocId(singleData.getFormOneADocId());
				}
				// Set entity properties
				entity.setBusinessPlan(singleData.getBusinessPlan());
				entity.setCurrentTaxComplianceCertificate(singleData.getCurrentTaxComplianceCertificate());
				entity.setAuthorizedSignatoryCertificateOfGoodConduct(
						singleData.getAuthorizedSignatoryCertificateOfGoodConduct());
				entity.setKraPinOfTheAuthorizedSignatory(singleData.getKraPinOfTheAuthorizedSignatory());
				entity.setNationalIdOfTheAuthorizedSignatory(singleData.getNationalIdOfTheAuthorizedSignatory());
				entity.setInsurancePolicy(singleData.getInsurancePolicy());
				entity.setValidLicenseForTheWarehouse(singleData.getValidLicenseForTheWarehouse());
				entity.setCertificateOfIncorporationRegistration(
						singleData.getCertificateOfIncorporationRegistration());
				entity.setLatestCR12(singleData.getLatestCR12());
				entity.setLatestCertificateOfCompliance(singleData.getLatestCertificateOfCompliance());
				entity.setCreatedBy(singleData.getUserId());

				entities.add(entity);

				// Process file uploads
				List<String> fileUploadList = Arrays.asList(singleData.getBusinessPlan(),
						singleData.getCurrentTaxComplianceCertificate(),
						singleData.getAuthorizedSignatoryCertificateOfGoodConduct(),
						singleData.getKraPinOfTheAuthorizedSignatory(),
						singleData.getNationalIdOfTheAuthorizedSignatory(), singleData.getInsurancePolicy(),
						singleData.getValidLicenseForTheWarehouse(),
						singleData.getCertificateOfIncorporationRegistration(), singleData.getLatestCR12(),
						singleData.getLatestCertificateOfCompliance());

				fileUploadList.stream().filter(file -> !Strings.isNullOrEmpty(file))
						.forEach(file -> copyFileToDestination(file, "certificate-conformity-form-one-a"));
			}

			// Batch insert
			formOneARepository.saveAll(entities);

			response.put("message", "Data saved successfully");
		} catch (Exception e) {
			response.put("message", "Error saving data");
			logger.error("Error processing Form One A details", e);
		}

		return response;
	}

	@Override
	public JSONObject saveFormOneBDetails(String code) {
		JSONObject response = new JSONObject();

		try {
			String decodedData = CommonUtil.inputStreamDecoder(code);
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			ApplicationConformityFormOneBListDto dto = om.readValue(decodedData,
					ApplicationConformityFormOneBListDto.class);

			List<ApplicationOfConformityFormOneB> entities = new ArrayList<>();

			for (ApplicationConformityFormOneBDto singleData : dto.getFormOneBList()) {
				// Validate warehouse & company details
				ApplicationOfConformityFormOneB entity = new ApplicationOfConformityFormOneB();
				entity.setWarehouseLocation(locationDetailsRepo.findById(singleData.getWarehouseId()).orElseThrow(
						() -> new RuntimeException("Warehouse not found with ID: " + singleData.getWarehouseId())));
				entity.setCompany(compProfRepo.findById(singleData.getCompanyId()).orElseThrow(
						() -> new RuntimeException("Company not found with ID: " + singleData.getCompanyId())));

				if(singleData.getFormOneBDocId() != null) {
					entity.setFormOneBDocId(singleData.getFormOneBDocId());
					}
				// Set entity properties
				entity.setStrategicPlan(singleData.getStrategicPlan());
				entity.setLeaseAgreement(singleData.getLeaseAgreement());
				entity.setTitleDeed(singleData.getTitleDeed());
				entity.setAssetRegister(singleData.getAssetRegister());
				entity.setRecommendationLetters(singleData.getRecommendationLetters());
				entity.setAuditedFinancialReports(singleData.getAuditedFinancialReports());
				entity.setRiskAnalysis(singleData.getRiskAnalysis());
				entity.setCreatedBy(singleData.getUserId());

				entities.add(entity);

				// Process file uploads
				List<String> fileUploadList = Arrays.asList(singleData.getStrategicPlan(),
						singleData.getLeaseAgreement(), singleData.getTitleDeed(), singleData.getAssetRegister(),
						singleData.getRecommendationLetters(), singleData.getAuditedFinancialReports(),
						singleData.getRiskAnalysis());

				fileUploadList.stream().filter(file -> !Strings.isNullOrEmpty(file))
						.forEach(file -> copyFileToDestination(file, "certificate-conformity-form-one-b"));
			}

			// Batch insert
			formOneBRepository.saveAll(entities);

			response.put("message", "Data saved successfully");
		} catch (Exception e) {
			response.put("message", "Error saving data");
			logger.error("Error processing Form One B details", e);
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
	public List<ApplicationConformityWhLocationDTO> getDraftedWarehouseLocationList(String companyId) {
		AocCompProfileDetails company = compProfRepo.findById(companyId).orElse(null);
		if (company == null) {
			return Collections.emptyList();
		}

		List<ApplicationOfConformityLocationDetails> entityList = locationDetailsRepo
				.findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(company);

		return entityList.stream().map(entity -> {
			ApplicationConformityWhLocationDTO dto = new ApplicationConformityWhLocationDTO();
			dto.setWhLocationId(entity.getWhLocationId());
			dto.setWarehouseName(entity.getWarehouseName());
			dto.setWarehouseOperatorName(entity.getWhOperatorName());
			dto.setEmail(entity.getEmail());
			dto.setMobileNumber(entity.getMobileNumber());
			dto.setLrNumber(entity.getLrNumber());
			dto.setCounty(entity.getCounty().getId());
			dto.setSubCounty(entity.getSubCounty().getIntId());
			dto.setWard(entity.getWard().getIntWardMasterId());
			dto.setLongitude(entity.getLongitude());
			dto.setLatitude(entity.getLatitude());
			dto.setStreetName(entity.getStreetName());
			dto.setBuilding(entity.getBuilding());
			dto.setPolicyNumber(entity.getPolicyNumber());
			dto.setUserId(entity.getCreatedBy());
			dto.setCompanyId(entity.getCompany().getProfDetId());
			return dto;
		}).collect(Collectors.toList());
	}

	
	//issue need to be fix in this method
//	@Override
//	public JSONObject getDraftedCommodityList(String companyId) {
//		AocCompProfileDetails company = compProfRepo.findById(companyId).orElse(null);
//		if (company == null) {
//			return null;
//		}
//		JSONObject response = new JSONObject();
//		List<ApplicationOfConformityCommodityStorage> storageData = aocCommodityStorageRepo.findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(company);
//		List<ApplicationOfConformityCommodityDetails> commodityDetailsData = aocCommodityDetailsRepo.findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(company);
//		response.put("commodityStorage",
//				storageData);
//		response.put("commodityDetails",
//				commodityDetailsData);
//		System.out.println(storageData);
//		return response;
//	}
	
	@Override
	public List<ApplicationOfConformityCommodityStorage> getDraftedCommodityList(String companyId) {
		AocCompProfileDetails company = compProfRepo.findById(companyId).orElse(null);
		if (company == null) {
			return null;
		}
		JSONObject response = new JSONObject();
		List<ApplicationOfConformityCommodityStorage> storageData = aocCommodityStorageRepo.findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(company);
		//List<ApplicationOfConformityCommodityDetails> commodityDetailsData = aocCommodityDetailsRepo.findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(company);
//		response.put("commodityStorage",
//				storageData);
//		response.put("commodityDetails",
//				commodityDetailsData);
//		System.out.println(storageData);
		return storageData;
	}

	@Override
	public List<ApplicationConformityFormOneADto> getDraftedFormOneADetails(String companyId) {
		AocCompProfileDetails company = compProfRepo.findById(companyId).orElse(null);
		if (company == null) {
			return Collections.emptyList();
		}

		List<ApplicationOfConformityFormOneA> entityList = formOneARepository
				.findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(company);

		return entityList.stream().map(entity -> {
			ApplicationConformityFormOneADto dto = new ApplicationConformityFormOneADto();
			dto.setFormOneADocId(entity.getFormOneADocId());
			dto.setWarehouseId(entity.getWarehouseLocation().getWhLocationId());
			dto.setBusinessPlan(entity.getBusinessPlan());
			dto.setCurrentTaxComplianceCertificate(entity.getCurrentTaxComplianceCertificate());
			dto.setAuthorizedSignatoryCertificateOfGoodConduct(entity.getAuthorizedSignatoryCertificateOfGoodConduct());
			dto.setKraPinOfTheAuthorizedSignatory(entity.getKraPinOfTheAuthorizedSignatory());
			dto.setNationalIdOfTheAuthorizedSignatory(entity.getNationalIdOfTheAuthorizedSignatory());
			dto.setInsurancePolicy(entity.getInsurancePolicy());
			dto.setValidLicenseForTheWarehouse(entity.getValidLicenseForTheWarehouse());
			dto.setCertificateOfIncorporationRegistration(entity.getCertificateOfIncorporationRegistration());
			dto.setLatestCR12(entity.getLatestCR12());
			dto.setLatestCertificateOfCompliance(entity.getLatestCertificateOfCompliance());
			dto.setCompanyId(companyId);
			dto.setUserId(entity.getCreatedBy());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public List<ApplicationConformityFormOneBDto> getDraftedFormOneBDetails(String companyId) {
		AocCompProfileDetails company = compProfRepo.findById(companyId).orElse(null);
		if (company == null) {
			return Collections.emptyList();
		}

		List<ApplicationOfConformityFormOneB> entityList = formOneBRepository
				.findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(company);

		return entityList.stream().map(entity -> {
			ApplicationConformityFormOneBDto dto = new ApplicationConformityFormOneBDto();
			dto.setFormOneBDocId(entity.getFormOneBDocId());
			dto.setWarehouseId(entity.getWarehouseLocation().getWhLocationId());
			dto.setStrategicPlan(entity.getStrategicPlan());
			dto.setLeaseAgreement(entity.getLeaseAgreement());
			dto.setTitleDeed(entity.getTitleDeed());
			dto.setAssetRegister(entity.getAssetRegister());
			dto.setRecommendationLetters(entity.getRecommendationLetters());
			dto.setAuditedFinancialReports(entity.getAuditedFinancialReports());
			dto.setRiskAnalysis(entity.getRiskAnalysis());
			dto.setCompanyId(companyId);
			dto.setUserId(entity.getCreatedBy());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public List<DraftedWarehouseForPaymentDto> getDraftedWarehousesForPayment(String companyId) {
		List<Object[]> objList = locationDetailsRepo.getDraftedWarehousesForPayment(companyId);
		List<DraftedWarehouseForPaymentDto> list = new ArrayList<>();
		for(Object[] objData : objList) {
			DraftedWarehouseForPaymentDto dto = new DraftedWarehouseForPaymentDto();
			dto.setIntWhLocationId((Long) objData[0]);
			dto.setVchWarehouseId((String) objData[1]);
			dto.setVchWarehouseName((String) objData[2]);
			list.add(dto);
		}
		return list;
	}

	@Override
	public JSONObject saveInMainTable(String code) {
	    JSONObject response = new JSONObject();
	    try {
	        String decodedData = CommonUtil.inputStreamDecoder(code);
	        JSONObject obj = new JSONObject(decodedData);

	        String warehouseId = obj.optString("warehouseId", "");
	        Integer userId = obj.optInt("userId", 0);
	        Integer roleId = obj.optInt("roleId", 0);

	        if (warehouseId.isEmpty() || userId == 0 || roleId == 0) {
	            response.put("status", "error");
	            response.put("message", "Invalid input parameters");
	            return response;
	        }

	        // Call stored procedure method
	        String result = (String) saveDataInAocMainTable(warehouseId, userId, roleId);

	        if (result == null || result.equalsIgnoreCase("Error occurred")) {
	            response.put("status", "error");
	            response.put("message", "Failed to save data");
	        } else {
	        	
	        	ConformityMain data = conformityMainRepository.findByWarehouseId(result);        	
	        	Mail mail = new Mail();
				mail.setMailSubject("Application For Certificate of Conformity");
				mail.setContentType("text/html");
				//mail.setMailCc("uiidptestmail@gmail.com");
				mail.setTemplate("You have successfully applied for certificate for conformity. Your Warehouse id is : " + result);
				mail.setMailTo(data.getCompany().getEmail()); // mail should be dynamic
				EmailUtil.sendMail(mail.getMailSubject(), mail.getTemplate(), mail.getMailTo());
	        	
	            response.put("status", "success");
	            response.put("conformityId", result);
	        }
	    } catch (Exception e) {
	    	logger.error("internal server error", e);
	        response.put("status", "error");
	        response.put("message", "An error occurred while processing the request");
	    }
	    return response;
	}
	
	@Override
	public List<ConformityMain> getApplicationsByStatus(String decodedData) throws JsonMappingException, JsonProcessingException {
	JsonNode jsonNode = om.readTree(decodedData);
	String status=jsonNode.get("status").asText();
	String comapnyId=jsonNode.get("companyId").asText();
	ApproverStatus approverStatus = ApproverStatus.valueOf(status);
	List<ConformityMain> listData = conformityMainRepository.findByApplicationStatusAndCompany_ProfDetIdOrderByIdDesc(approverStatus,comapnyId);
	if(approverStatus.equals(ApproverStatus.APPROVED)) {
	listData.forEach(x -> {
		ConformityCertificate certificate = certificateRepository.findByConformityId(x.getId());
		x.setCertificateNo(certificate.getCertificateNo());
	});
	}else if(approverStatus.equals(ApproverStatus.DEFERRED)) {
		listData.forEach(x -> {
			if(x.getIntApplicationStatus() == 105) {
				ConformityTakeActionOic oicData = conformityTakeActionOicRepository.findByConformityId(x.getId());
				x.setCertificateNo(oicData.getRemarkForDeferring());
			}
		});
	}
	listData.forEach(x -> {
		if(x.getIntApplicationStatus() == 100) {
			x.setApplicantStatus("Pending At Oic");
		}else if(x.getIntApplicationStatus() == 101) {
			x.setApplicantStatus("Allocated to CC/Inspector");
		}else if(x.getIntApplicationStatus() == 102) {
			x.setApplicantStatus("Forwarded to Oic");
		}else if(x.getIntApplicationStatus() == 103) {
			x.setApplicantStatus("Forwarded to Ceo");
		}else if(x.getIntApplicationStatus() == 104) {
			x.setApplicantStatus("Approved By Ceo");
		}else if(x.getIntApplicationStatus() == 105) {
			x.setApplicantStatus("Differ Back From Oic");
		}else if(x.getIntApplicationStatus() == 106) {
			x.setApplicantStatus("Differ Back From Ceo");
		}else if(x.getIntApplicationStatus() == 107) {
			x.setApplicantStatus("Rejected from Ceo");
		}
	});
	return listData;
	}

	@Override
	public Page<PendingConformityDTO> getPendingApplications(String data) throws JsonMappingException, JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JsonNode jsonNode = om.readTree(decodedData);
		
		int page = jsonNode.get("page").asInt();
        int size = jsonNode.get("size").asInt();
        int userId = jsonNode.get("pendingAtUserId").asInt();
        int roleId = jsonNode.get("pendingAtRoleId").asInt();
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results;
//        Page<ConformityMain> listData = conformityMainRepository.findByIntApplicationStatusNot(104, pageable);
//        return listData;
        if(roleId == 59) {
        	results = conformityMainRepository.getAllPendingDataForCC(userId,pageable);
        }else if(roleId == 3) {
        	results = conformityMainRepository.getAllPendingDataForInspector(userId, pageable);
        }
        else {
		results = conformityMainRepository.getAllPendingData(pageable);
        }
        return results.map(obj -> {
            int length = obj.length; // Get actual number of columns

            return new PendingConformityDTO(
                (Integer) obj[0],  // intcnfId
                (String) obj[1],   // vchWareHouseId
                (String) obj[2],   // vchWarehouseName
                (String) obj[3],   // vchWhOperatorName
                (String) obj[4],   // vchEmail
                (String) obj[5],   // vchMobileNumber
                (String) obj[6],   // vchLrNumber
                (String) obj[7],   // countyName
                (String) obj[8],   // vchSubCountyName
                (String) obj[9],   // wardName
                (String) (length > 10 ? obj[10] : null), // Handle missing values safely
                (Boolean) (length > 11 ? obj[11] : null),
                (Boolean) (length > 12 ? obj[12] : null),
                (Boolean) (length > 13 ? obj[13] : null),
                (Boolean) (length > 14 ? obj[14] : null),
                (Integer) (length > 15 ? obj[15] : null),
                (Boolean) (length > 16 ? obj[16] : null) // Avoid IndexOutOfBoundsException
            );
        });
	}
	@Override
	public ConformityMain getConfirmityDataById(Integer id) {
		return conformityMainRepository.findByIdAndDeletedFlagFalse(id);
	}

	@Transactional
	@Override
	public JSONObject forwardToCC(String data) throws JsonMappingException, JsonProcessingException {
	    String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();

	    try {
	        JsonNode jsonNode = om.readTree(decodedData);
	        int userId = jsonNode.get("userId").asInt();
	        String userName=jsonNode.get("userName").asText();
	        int roleId=jsonNode.get("roleId").asInt();
	        String roleName=jsonNode.get("roleName").asText();
	        
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        String warehouseId = jsonNode.get("warehouseId").asText();
	        ArrayNode selectedMembers = (ArrayNode) jsonNode.get("selectedMembers");
	        ConformityMain applicantData = conformityMainRepository.findByIdAndDeletedFlagFalse(intApplicantId);
	        if (applicantData == null) {
	            response.put("status", "error");
	            response.put("message", "Applicant data not found.");
	            return response;
	        }
	        ConformityTakeActionOic oicData = conformityTakeActionOicRepository.findByConformityId(intApplicantId);
	        if(oicData != null) {
	        	oicData.setRemarkForDeferring(null);
	        	conformityTakeActionOicRepository.save(oicData);       
	        }
	        for (JsonNode member : selectedMembers) {
	            int memberId = member.get("id").asInt();
	            ConformityTakeActionCc entity = new ConformityTakeActionCc();
	            entity.setConformity(applicantData);
	            entity.setWarehouseId(warehouseId);
	            entity.setCertificateCommitteeId(memberId);
	            entity.setCreatedBy(userId);
	            conformityTakeActionCcRepository.save(entity);
	        }
	        if(applicantData.getApplicationStatus().equals(ApproverStatus.DEFERRED)) {
	        	applicantData.setApplicationStatus(ApproverStatus.APPLIED);
	        }
	        applicantData.setIntApplicationStatus(101);
	        applicantData.setOicOneCC(true);
	        applicantData.setIsEditable(false);
	        conformityMainRepository.save(applicantData);
	        compProfRepo.findById(applicantData.getCompany().getProfDetId()).ifPresent(company -> {
	            company.setIsEditOptionEnabled(false);
	            compProfRepo.save(company);
	        });
	        
	        ApplicationOfConformityActionHistory actionHistory= new ApplicationOfConformityActionHistory();
	        actionHistory.setVchWareHouseId(warehouseId);
	        actionHistory.setIntUserId(userId);
	        actionHistory.setVchUserName(userName);
	        actionHistory.setIntRoleId(roleId);	        
	        actionHistory.setVchRoleName(roleName);
	        actionHistory.setVchStatus("Forward To CC Member");
	        applicationOfConformityActionHistoryRepository.save(actionHistory);
	        
	        response.put("status", "success");
	        response.put("message", "Data saved successfully.");
	    } catch (Exception e) {
	        response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
	    }

	    return response;
	}

	@Transactional
	@Override
	public JSONObject submitCCData(String data) throws JsonMappingException, JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
	    List<String> fileUploadList = new ArrayList<>();
	    try {
	    	JsonNode jsonNode = om.readTree(decodedData);
	    	ArrayNode documents = (ArrayNode) jsonNode.get("documents");
	    	String remarks = jsonNode.get("remarks").asText();
	    	int userId = jsonNode.get("userId").asInt();
	    	String userName=jsonNode.get("userName").asText();
	        int roleId=jsonNode.get("roleId").asInt();
	        String roleName=jsonNode.get("roleName").asText();
	    	int intApplicantId = jsonNode.get("intApplicantId").asInt();
	    	String warehouseId = jsonNode.get("warehouseId").asText();
	    	ConformityMain applicantData = conformityMainRepository.findByIdAndDeletedFlagFalse(intApplicantId);
	    	ConformityTakeActionCc takeActionData = conformityTakeActionCcRepository.findByCertificateCommitteeIdAndConformity(userId, applicantData);
	    	takeActionData.setRemarkByCC(remarks);
	    	takeActionData.setForwardToOicStatus(true);
	    	conformityTakeActionCcRepository.save(takeActionData);
	    	for (JsonNode doc : documents) {
	    		String documentName = doc.get("documentName").asText();
	    		String file = doc.get("file").asText();
	    		ConformityTakeActionCcDetails ccDetails = new ConformityTakeActionCcDetails();
	    		ccDetails.setAllocateCCId(takeActionData);
	    		ccDetails.setConformity(applicantData);
	    		ccDetails.setSupportingDocumentName(documentName);
	    		ccDetails.setSupportingDocumentFile(file);
	    		ccDetails.setWareHouseId(warehouseId);
	    		ccDetails.setCreatedBy(userId);
	    		ConformityTakeActionCcDetails savedCCDetails = conformityTakeActionCcDetailsRepository.save(ccDetails);
	    		fileUploadList.add(savedCCDetails.getSupportingDocumentFile());
	    		}
	    	fileUploadList.stream().filter(file -> !Strings.isNullOrEmpty(file))
			.forEach(file -> copyFileToDestination(file, "conformity-cc-take-action"));
	    	Boolean areAllSubmiitedData = conformityTakeActionCcRepository.areAllRecordsTrue(intApplicantId);
	    	if(Boolean.TRUE.equals(areAllSubmiitedData)) {
	    		applicantData.setOicTwoCC(true);
	    		if(Boolean.TRUE.equals(applicantData.getOicTwoInsp())) {
	    			applicantData.setIntApplicationStatus(102);
	    		}
	    		conformityMainRepository.save(applicantData);
	    	}
	         ApplicationOfConformityActionHistory actionHistory= new ApplicationOfConformityActionHistory();
	         actionHistory.setVchWareHouseId(warehouseId);
	         actionHistory.setIntUserId(userId);
	         actionHistory.setVchUserName(userName);
	         actionHistory.setIntRoleId(roleId);	        
	         actionHistory.setVchRoleName(roleName);   
	         actionHistory.setVchStatus("Forward To OIC");
	         actionHistory.setVchRemarks(remarks);
	         applicationOfConformityActionHistoryRepository.save(actionHistory); 
	    	
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}

	@Transactional
	@Override
	public JSONObject forwardToInsp(String data) throws JsonMappingException, JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
	    try {
	        JsonNode jsonNode = om.readTree(decodedData);
	        int userId = jsonNode.get("userId").asInt();
	        String userName=jsonNode.get("userName").asText();
	        int roleId=jsonNode.get("roleId").asInt();
	        String roleName=jsonNode.get("roleName").asText();
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        int inspectorTeamLeadId = jsonNode.get("inspectorTeamLeadId").asInt();
	        String warehouseId = jsonNode.get("warehouseId").asText();
	        ArrayNode inspectorList = (ArrayNode) jsonNode.get("inspectorList");
	        ConformityMain applicantData = conformityMainRepository.findByIdAndDeletedFlagFalse(intApplicantId);
	        ConformityTakeActionOic oicData = conformityTakeActionOicRepository.findByConformityId(intApplicantId);
	        if(oicData != null) {
	        	oicData.setRemarkForDeferring(null);
	        	conformityTakeActionOicRepository.save(oicData);       
	        }
	        if (applicantData == null) {
	            response.put("status", "error");
	            response.put("message", "Applicant data not found.");
	            return response;
	        }
	        
			ConformityTakeActionInspector entity = new ConformityTakeActionInspector();
			entity.setConformity(applicantData);
			entity.setWareHouseId(warehouseId);
			entity.setInspectorTeamLeadId(inspectorTeamLeadId);
			entity.setInspectorId(inspectorList.toString());
			entity.setCreatedBy(userId);
			conformityTakeActionInspectorRepository.save(entity);
			if(applicantData.getApplicationStatus().equals(ApproverStatus.DEFERRED)) {
	        	applicantData.setApplicationStatus(ApproverStatus.APPLIED);
	        }
	        applicantData.setIntApplicationStatus(101);
	        applicantData.setOicOneInsp(true);
	        applicantData.setIsEditable(false);
	        conformityMainRepository.save(applicantData);
	        compProfRepo.findById(applicantData.getCompany().getProfDetId()).ifPresent(company -> {
	            company.setIsEditOptionEnabled(false);
	            compProfRepo.save(company);
	        });
	        
	        ApplicationOfConformityActionHistory actionHistory= new ApplicationOfConformityActionHistory();
	        actionHistory.setVchWareHouseId(warehouseId);
	        actionHistory.setIntUserId(userId);
	        actionHistory.setVchUserName(userName);
	        actionHistory.setIntRoleId(roleId);	        
	        actionHistory.setVchRoleName(roleName);   
	        actionHistory.setVchStatus("Forward To Inspector");
	        applicationOfConformityActionHistoryRepository.save(actionHistory);
	        
	        response.put("status", "success");
	        response.put("message", "Data saved successfully.");
	    } catch (Exception e) {
	        response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
	    }

	    return response;
	}

	@Override
	public JSONObject checkInspection(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
		try {
			JsonNode jsonNode = om.readTree(decodedData);
	        int userId = jsonNode.get("userId").asInt();
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        ConformityTakeActionInspector inspectordata = conformityTakeActionInspectorRepository.findByConformityIdAndInspectorTeamLeadId(intApplicantId, userId);
	        response.put("status", "success");
	        response.put("allocateInspectorId" , inspectordata.getAllocateInspectorId());
	        response.put("inspectionCompleteStatus", inspectordata.getInspCompleted());
	        response.put("inspectionScheduleStatus", inspectordata.getInspScheduled());
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}

	@Transactional
	@Override
	public JSONObject submitInspectionSchedule(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
	    try {
	    	JsonNode jsonNode = om.readTree(decodedData);
	    	Integer scheduleId = jsonNode.has("scheduleId") && !jsonNode.get("scheduleId").isNull() ? jsonNode.get("scheduleId").asInt() : null; 
	    	int userId = jsonNode.get("userId").asInt();
	        String userName=jsonNode.get("userName").asText();
	        int roleId=jsonNode.get("roleId").asInt();
	        String roleName=jsonNode.get("roleName").asText();
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        String warehouseId = jsonNode.get("warehouseId").asText();
	        int allocatedInspectorId = jsonNode.get("allocatedInspectorId").asInt();
	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
	        LocalDate inspectionDate = LocalDate.parse(jsonNode.get("inspectionDate").asText(), dateFormatter);
	        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	        LocalTime inspectionTime = LocalTime.parse(jsonNode.get("inspectionTime").asText(), timeFormatter);
	        String complianceOfficerName = jsonNode.get("complianceOfficerName").asText();
	        String complianceOfficerOffice = jsonNode.get("complianceOfficerOffice").asText();
	        String facilityType = jsonNode.get("facilityType").asText();
	        String otherFacility = jsonNode.has("otherFacility") && !jsonNode.get("otherFacility").isNull() 
	                && !jsonNode.get("otherFacility").asText().trim().isEmpty() 
	                ? jsonNode.get("otherFacility").asText() 
	                : null;
	        String inspectionRequestedBy = jsonNode.get("inspectionRequestedBy").asText();
	        String inspectionType = jsonNode.get("inspectionType").asText();
	        String remarks = jsonNode.get("remarks").asText();
	        ConformityTakeActionInspector inspData = conformityTakeActionInspectorRepository.findById(allocatedInspectorId).get();
	        ConformityTakeActionInspectorSchedule scheduleData = new ConformityTakeActionInspectorSchedule();
	        if(scheduleId != null) {
	        	scheduleData.setScheduleId(scheduleId);
	        }
	        scheduleData.setAllocateInspector(inspData);
	        scheduleData.setConformity(conformityMainRepository.findByIdAndDeletedFlagFalse(intApplicantId));
	        scheduleData.setWareHouseId(warehouseId);
	        scheduleData.setInspectionDate(inspectionDate);
	        scheduleData.setInspectionTime(inspectionTime);
	        scheduleData.setComplianceOfficerName(complianceOfficerName);
	        scheduleData.setComplianceOfficerOffice(complianceOfficerOffice);
	        scheduleData.setFacilityType(facilityType);
	        scheduleData.setCreatedBy(userId);
	        if(otherFacility != null) {
	        	scheduleData.setOtherFacility(otherFacility);
	        }
	        scheduleData.setInspectionRequestedBy(inspectionRequestedBy);
	        scheduleData.setInspectionType(inspectionType);
	        scheduleData.setRemarks(remarks);
	        conformityInspScheduleRepo.save(scheduleData);
	        inspData.setInspScheduled(true);
	        conformityTakeActionInspectorRepository.save(inspData);
	        
            ApplicationOfConformityActionHistory actionHistory= new ApplicationOfConformityActionHistory();
            if(scheduleId != 0) {
	        actionHistory.setVchWareHouseId(warehouseId);
	        actionHistory.setIntUserId(userId);
	        actionHistory.setVchUserName(userName);
	        actionHistory.setIntRoleId(roleId);	        
	        actionHistory.setVchRoleName(roleName);   
	        actionHistory.setVchStatus("Inspection Reschedule");
	        actionHistory.setVchRemarks(remarks);
	        
            }
            else {
            	    actionHistory.setVchWareHouseId(warehouseId);
         	        actionHistory.setIntUserId(userId);
         	        actionHistory.setVchUserName(userName);
         	        actionHistory.setIntRoleId(roleId);	        
         	        actionHistory.setVchRoleName(roleName);   
         	        actionHistory.setVchStatus("Inspection Schedule");
         	        actionHistory.setVchRemarks(remarks);
                   }
            applicationOfConformityActionHistoryRepository.save(actionHistory);
	        response.put("status", "success");
	        response.put("message", "Data saved successfully.");
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}

	@Transactional
	@Override
	public JSONObject submitCompleteInspection(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
	    om.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
	    om.enable(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS);
	    try {
	    	JsonNode rootNode = om.readTree(decodedData);
	    	int userId = rootNode.get("userId").asInt();
	        String userName=rootNode.get("userName").asText();
	        int roleId=rootNode.get("roleId").asInt();
	        String roleName=rootNode.get("roleName").asText();
	    	int intApplicantId = rootNode.get("intApplicantId").asInt();
	    	int intAllocatedInspectorId = rootNode.get("intAllocatedInspectorId").asInt();
	    	RoutineComplianceInspectorLocation location=om.treeToValue(rootNode.path("data").path("inspectorTwo").path("location"), RoutineComplianceInspectorLocation.class);
            if (location.getIntLocationId() != null) {
            	//location = routineComplianceInspectorLocationRepository.findById(location.getIntLocationId()).orElse(location);
            	location.setIntLocationId(location.getIntLocationId());
            }
            location = routineComplianceInspectorLocationRepository.save(location);
//            JsonNode commoditiesNode = rootNode.path("data").path("inspectorTwo").path("location").path("agriculturalCommodities");
//            if (commoditiesNode.isArray()) {
//                for (JsonNode commodityNode : commoditiesNode) {
//                    RoutineComplianceInspectorAgriculturalCommodity commodity = om.treeToValue(commodityNode, RoutineComplianceInspectorAgriculturalCommodity.class);
//                    // Check if commodity exists
//                    if (commodity.getAgriculturalCommoditiesId() != null) {
//                        commodity = routineComplianceInspectorAgriculturalCommoditiesRepository.findById(commodity.getAgriculturalCommoditiesId()).orElse(commodity);
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
            
            ConformityTakeActionInspector inspData = conformityTakeActionInspectorRepository.findById(intAllocatedInspectorId).get();
            inspData.setIntLocationId(location);
            inspData.setIntWareHousePersonalEquipmentId(warehouseEquipment);
            inspData.setIntphysicalFireProtectionId(physicalSecurityAndFireProtection);
            inspData.setIntConditionOfGoodsStorageId(conditionGoodStorage);
            inspData.setIntEnviromentId(environmentIssues);
            inspData.setIntInsuranceId(insurance);
            inspData.setIntShrinkId(shrink);
            inspData.setIntItSystemId(itSystem);
            inspData.setRecommendationIdLocation(location.getIntLocationId());
            inspData.setConclusionLocationId(location.getIntLocationId());
            inspData.setInspCompleted(true);            
            conformityTakeActionInspectorRepository.save(inspData);
            
            ApplicationOfConformityActionHistory actionHistory= new ApplicationOfConformityActionHistory();
  	        actionHistory.setVchWareHouseId(inspData.getWareHouseId());
  	        actionHistory.setIntUserId(userId);
  	        actionHistory.setVchUserName(userName);
  	        actionHistory.setIntRoleId(roleId);	        
  	        actionHistory.setVchRoleName(roleName);   
  	        actionHistory.setVchStatus("Inspection Completed");
  	        applicationOfConformityActionHistoryRepository.save(actionHistory);
            
	    	response.put("status", "success");
	        response.put("message", "Data saved successfully.");
		} catch (Exception e) {
			logger.error("Internal Server Error", e);
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}

	@Transactional
	@Override
	public JSONObject submitInspectorData(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
	    try {
	    	JsonNode jsonNode = om.readTree(decodedData);
	    	int userId = jsonNode.get("userId").asInt();
	        String userName=jsonNode.get("userName").asText();
	        int roleId=jsonNode.get("roleId").asInt();
	        String roleName=jsonNode.get("roleName").asText();
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        String remarks = jsonNode.get("remarks").asText();
	        String reportFile = (jsonNode.get("reportFile").asText() != null && jsonNode.get("reportFile").asText() != "null") ? jsonNode.get("reportFile").asText() : null;
	        int intAllocatedInspectorId = jsonNode.get("intAllocatedInspectorId").asInt();
	        ConformityTakeActionInspector inspData = conformityTakeActionInspectorRepository.findById(intAllocatedInspectorId).get();
	        inspData.setInspectionFilePath(reportFile);
	        inspData.setRemarkByInspector(remarks);
	        inspData.setForwardToOicStatus(true);
	        conformityTakeActionInspectorRepository.save(inspData);
	        if(reportFile != null) {
	        List<String> fileUploadList = Arrays.asList(reportFile);	        		
	        fileUploadList.stream().filter(file -> !Strings.isNullOrEmpty(file))
	    			.forEach(file -> copyFileToDestination(file, "conformity-inspector-take-action"));
	        }
	        ConformityMain applicantData = conformityMainRepository.findByIdAndDeletedFlagFalse(intApplicantId);
	        applicantData.setOicTwoInsp(true);
	        if(Boolean.TRUE.equals(applicantData.getOicTwoCC())) {
	        	applicantData.setIntApplicationStatus(102);
	        }
	        conformityMainRepository.save(applicantData);
	        
	         ApplicationOfConformityActionHistory actionHistory= new ApplicationOfConformityActionHistory();
		        actionHistory.setVchWareHouseId(applicantData.getWarehouseId());
		        actionHistory.setIntUserId(userId);
		        actionHistory.setVchUserName(userName);
		        actionHistory.setIntRoleId(roleId);	        
		        actionHistory.setVchRoleName(roleName);   
		        actionHistory.setVchStatus("Forward To OIC");
		        actionHistory.setVchRemarks(remarks);		        
		        applicationOfConformityActionHistoryRepository.save(actionHistory); 
		        
	    	response.put("status", "success");
	        response.put("message", "Data saved successfully.");
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}

	@Override
	public JSONObject getInspectorCCData(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
		try {
			JsonNode jsonNode = om.readTree(decodedData);
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        ConformityTakeActionOic oicData = conformityTakeActionOicRepository.findByConformityId(intApplicantId);
	        ConformityTakeActionInspector inspectorData = conformityTakeActionInspectorRepository.findByConformityId(intApplicantId);
	        List<ConformityTakeActionCcDetails> ccData = conformityTakeActionCcDetailsRepository.findByConformityId(intApplicantId);
	        response.put("inspectorReport" , inspectorData.getInspectionFilePath());
	        response.put("inspectorRemarks" , inspectorData.getRemarkByInspector());
	        if(oicData != null) {
	        response.put("oicRemarks" , oicData.getRemarkByOic() == null ? null : oicData.getRemarkByOic());
	        }
	        List<JSONObject> ccDetailsList = new ArrayList<>();
	        Map<Integer, JSONObject> ccMap = new HashMap<>();

	        for (ConformityTakeActionCcDetails singleCCData : ccData) {
	            int ccId = singleCCData.getAllocateCCId().getCertificateCommitteeId();
	            String ccName = tuserRepository.getUserName(ccId);
	            String remarkByCC = singleCCData.getAllocateCCId().getRemarkByCC();
	            String file = singleCCData.getSupportingDocumentFile();
	            String name = singleCCData.getSupportingDocumentName();

	            JSONObject documentNode = new JSONObject();
	            documentNode.put("name", name);
	            documentNode.put("file", file);

	            if (!ccMap.containsKey(ccId)) {
	                JSONObject ccNode = new JSONObject();
	                ccNode.put("remarkByCC", remarkByCC);
	                ccNode.put("certificateCommitteeId", ccId);
	                ccNode.put("certificateCommitteeName" , ccName);
	                ccNode.put("documentDetails", new JSONArray());
	                ccMap.put(ccId, ccNode);
	            }

	            ccMap.get(ccId).getJSONArray("documentDetails").put(documentNode);
	        }

	        ccDetailsList.addAll(ccMap.values());
	        response.put("ccDetails", ccDetailsList);
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}
	
	@Override
	public JSONObject getInspectorCCOicData(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
		try {
			JsonNode jsonNode = om.readTree(decodedData);
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        ConformityTakeActionOic oicData = conformityTakeActionOicRepository.findByConformityId(intApplicantId);
	        ConformityTakeActionInspector inspectorData = conformityTakeActionInspectorRepository.findByConformityId(intApplicantId);
	        List<ConformityTakeActionCcDetails> ccData = conformityTakeActionCcDetailsRepository.findByConformityId(intApplicantId);
	        response.put("inspectorReport" , inspectorData.getInspectionFilePath());
	        response.put("inspectorRemarks" , inspectorData.getRemarkByInspector());
	        response.put("oicRemarks" , oicData.getRemarkByOic());
	        List<JSONObject> ccDetailsList = new ArrayList<>();
	        Map<Integer, JSONObject> ccMap = new HashMap<>();

	        for (ConformityTakeActionCcDetails singleCCData : ccData) {
	            int ccId = singleCCData.getAllocateCCId().getCertificateCommitteeId();
	            String ccName = tuserRepository.getUserName(ccId);
	            String remarkByCC = singleCCData.getAllocateCCId().getRemarkByCC();
	            String file = singleCCData.getSupportingDocumentFile();
	            String name = singleCCData.getSupportingDocumentName();

	            JSONObject documentNode = new JSONObject();
	            documentNode.put("name", name);
	            documentNode.put("file", file);

	            if (!ccMap.containsKey(ccId)) {
	                JSONObject ccNode = new JSONObject();
	                ccNode.put("remarkByCC", remarkByCC);
	                ccNode.put("certificateCommitteeId", ccId);
	                ccNode.put("certificateCommitteeName" , ccName);
	                ccNode.put("documentDetails", new JSONArray());
	                ccMap.put(ccId, ccNode);
	            }

	            ccMap.get(ccId).getJSONArray("documentDetails").put(documentNode);
	        }

	        ccDetailsList.addAll(ccMap.values());
	        response.put("ccDetails", ccDetailsList);
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}
	
	@Transactional
	@Override
	public JSONObject submitOicData(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
		try {
			JsonNode jsonNode = om.readTree(decodedData);
			int userId = jsonNode.get("userId").asInt();
			String userName=jsonNode.get("userName").asText();
	        int roleId=jsonNode.get("roleId").asInt();
	        String roleName=jsonNode.get("roleName").asText();
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        String warehouseId = jsonNode.get("warehouseId").asText();
	        String remarks = jsonNode.get("remarks").asText();
	        ConformityMain applicantData = conformityMainRepository.findByIdAndDeletedFlagFalse(intApplicantId);
	        ConformityTakeActionOic oicData = conformityTakeActionOicRepository.findByConformityId(intApplicantId);
	        if(oicData == null) {
	        	oicData = new ConformityTakeActionOic();
	        	oicData.setConformity(applicantData);
	        	oicData.setWareHouseId(warehouseId);
	        	oicData.setRemarkByOic(remarks);
	        	oicData.setCreatedBy(userId);
	        }else {
	        	oicData.setRemarkByOic(remarks);
	        }
	        conformityTakeActionOicRepository.save(oicData);
	        applicantData.setIntApplicationStatus(103);
	        if(applicantData.getApplicationStatus().equals(ApproverStatus.DEFERRED)) {
	        	applicantData.setApplicationStatus(ApproverStatus.APPLIED);
	        }
	        conformityMainRepository.save(applicantData);
	        
            ApplicationOfConformityActionHistory actionHistory= new ApplicationOfConformityActionHistory();
	        actionHistory.setVchWareHouseId(warehouseId);
	        actionHistory.setIntUserId(userId);
	        actionHistory.setVchUserName(userName);
	        actionHistory.setIntRoleId(roleId);	        
	        actionHistory.setVchRoleName(roleName);   
	        actionHistory.setVchStatus("Forward To CEO");
	        actionHistory.setVchRemarks(remarks);
	        applicationOfConformityActionHistoryRepository.save(actionHistory); 
	        
	        response.put("status", "success");
	        response.put("message", "Data saved successfully.");
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}
	
	@Transactional
	@Override
	public JSONObject generateConformityCertificate(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
		try {
			JsonNode jsonNode = om.readTree(decodedData);
			int userId = jsonNode.get("userId").asInt();
			int roleId = jsonNode.get("roleId").asInt();
			String userName = jsonNode.get("userName").asText();
			String roleName=jsonNode.get("roleName").asText();
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        String warehouseId = jsonNode.get("warehouseId").asText();
	        String remarks = jsonNode.get("remarks").asText();
	        
	        ConformityMain applicantData = conformityMainRepository.findByIdAndDeletedFlagFalse(intApplicantId);
	        applicantData.setIntApplicationStatus(104);
	        applicantData.setApprovedBy(userId);
	        applicantData.setApprovedByRole(roleId);
	        applicantData.setApproverRemarks(remarks);
	        if(applicantData.getDeferCeoRemarks() != null) {
	        	applicantData.setDeferCeoRemarks(null);	        
	        }
	        applicantData.setApproverStatus(ApproverStatus.APPROVED);
	        applicantData.setCnfApprovalDate(LocalDate.now());
	        applicantData.setCertIssueDate(LocalDate.now());
	        applicantData.setApplicationStatus(ApproverStatus.APPROVED);
	        ConformityMain savedApplicantData = conformityMainRepository.save(applicantData);
	        ConformityCertificate certificate = new ConformityCertificate();
	        certificate.setConformity(savedApplicantData);
	        certificate.setCompany(savedApplicantData.getCompany());	        
	        certificate.setWareHouseId(warehouseId);
	        certificate.setApproverRoleId(roleId);
	        certificate.setApproverUserId(userId);
	        certificate.setApproverUserName(userName);
	        certificate.setCertificateNo(getCertificateNo("WRSC/LC/WHOCC/"));
	        certificate.setValidFrom(LocalDate.now());
			certificate.setValidTo(LocalDate.now().plusYears(1));
			certificate.setEnmPaymentStatus(PaymentStatus.SUCCESS);
			certificate.setEnmWarehouseStatus(WarehouseStatus.Active);
			certificate.setBitDeletedFlag(false);
			certificateRepository.save(certificate);
			
			 ApplicationOfConformityActionHistory actionHistory= new ApplicationOfConformityActionHistory();
		     actionHistory.setVchWareHouseId(warehouseId);
		     actionHistory.setIntUserId(userId);
		     actionHistory.setVchUserName(userName);
		     actionHistory.setIntRoleId(roleId);	        
		     actionHistory.setVchRoleName(roleName);   
		     actionHistory.setVchStatus("Conformity Certificate Generated");
		     actionHistory.setVchRemarks(remarks);
		     applicationOfConformityActionHistoryRepository.save(actionHistory); 
		        
	        response.put("status", "success");
	        response.put("message", "Data saved successfully.");
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}
	
	
	@Override
	public ConformityCertificateDto getConformityCertificate(Integer applicantId) {
		ConformityCertificate certificate = certificateRepository.findByConformityId(applicantId);
		ConformityCertificateDto dto = new ConformityCertificateDto();
		dto.setApplicantName(certificate.getConformity().getWarehouseLocation().getWhOperatorName());
		dto.setCompanyName(certificate.getConformity().getWarehouseLocation().getWarehouseName());
		//dto.setCompanyName(certificate.getCompany().getCompany());
		dto.setCountyName(certificate.getConformity().getWarehouseLocation().getCounty().getName());
		dto.setPostalCode(certificate.getCompany().getPostalCode());
		dto.setLrNumber(certificate.getConformity().getWarehouseLocation().getLrNumber());
		dto.setApplicationId(certificate.getWareHouseId());
		dto.setCertificateId(certificate.getCertificateNo());
		dto.setApproverName(certificate.getApproverUserName());
		dto.setFee(certificate.getConformity().getPayment().getAmountExpected());
		AocCompProfSignSeal seal = signSealRepo.findByProfDet(certificate.getCompany());
		dto.setSealPath(seal.getSealPath());
		dto.setDateOfIssue(certificate.getValidFrom());
		dto.setValidFrom(certificate.getValidFrom());
		dto.setValidTo(certificate.getValidTo());
		return dto;
	}
	
	@Transactional
	@Override
	public JSONObject rejectApplication(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
		try {
			JsonNode jsonNode = om.readTree(decodedData);
			int userId = jsonNode.get("userId").asInt();
	        String userName=jsonNode.get("userName").asText();
	        int roleId=jsonNode.get("roleId").asInt();
	        String roleName=jsonNode.get("roleName").asText();
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        String remarks = jsonNode.get("remarks").asText();
	        ConformityMain applicantData = conformityMainRepository.findByIdAndDeletedFlagFalse(intApplicantId);
	        applicantData.setIntApplicationStatus(107);
	        applicantData.setApplicationStatus(ApproverStatus.REJECTED);
	        applicantData.setApproverStatus(ApproverStatus.REJECTED);
	        applicantData.setRejectionRemarks(remarks);
	        conformityMainRepository.save(applicantData);
	        
	        ApplicationOfConformityActionHistory actionHistory= new ApplicationOfConformityActionHistory();
		    actionHistory.setVchWareHouseId(applicantData.getWarehouseId());
		    actionHistory.setIntUserId(userId);
		    actionHistory.setVchUserName(userName);
		    actionHistory.setIntRoleId(roleId);	        
		    actionHistory.setVchRoleName(roleName);   
		    actionHistory.setVchStatus("Conformity Certificate Rejected");
		    actionHistory.setVchRemarks(remarks);
		    applicationOfConformityActionHistoryRepository.save(actionHistory);
		    
	        response.put("status", "success");
	        response.put("message", "Data saved successfully.");
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}
	
	@Transactional
	@Override
	public JSONObject deferCeoApplication(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
		try {
			JsonNode jsonNode = om.readTree(decodedData);
			int userId = jsonNode.get("userId").asInt();
	        String userName=jsonNode.get("userName").asText();
	        int roleId=jsonNode.get("roleId").asInt();
	        String roleName=jsonNode.get("roleName").asText();
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        String remarks = jsonNode.get("remarks").asText();
	        ConformityMain applicantData = conformityMainRepository.findByIdAndDeletedFlagFalse(intApplicantId);
	        applicantData.setIntApplicationStatus(106);
	        applicantData.setApplicationStatus(ApproverStatus.DEFERRED);
	        applicantData.setDeferCeoRemarks(remarks);
	        conformityMainRepository.save(applicantData);
	        
	        ApplicationOfConformityActionHistory actionHistory= new ApplicationOfConformityActionHistory();
		    actionHistory.setVchWareHouseId(applicantData.getWarehouseId());
		    actionHistory.setIntUserId(userId);
		    actionHistory.setVchUserName(userName);
		    actionHistory.setIntRoleId(roleId);	        
		    actionHistory.setVchRoleName(roleName);   
		    actionHistory.setVchStatus("Defer Back To OIC");
		    actionHistory.setVchRemarks(remarks);
		    applicationOfConformityActionHistoryRepository.save(actionHistory);
		    
	        response.put("status", "success");
	        response.put("message", "Data saved successfully.");
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}
	
	@Transactional
	@Override
	public JSONObject deferOicApplication(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
		try {
			JsonNode jsonNode = om.readTree(decodedData);
			int userId = jsonNode.get("userId").asInt();
			String userName=jsonNode.get("userName").asText();
		    int roleId=jsonNode.get("roleId").asInt();
		    String roleName=jsonNode.get("roleName").asText();
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        String warehouseId = jsonNode.get("warehouseId").asText();
	        String remarks = jsonNode.get("remarks").asText();
	        ConformityMain applicantData = conformityMainRepository.findByIdAndDeletedFlagFalse(intApplicantId);
	        ConformityTakeActionOic oicData = conformityTakeActionOicRepository.findByConformityId(intApplicantId);
	        if(oicData == null) {
	        	oicData = new ConformityTakeActionOic();
	        	oicData.setConformity(applicantData);
	        	oicData.setWareHouseId(warehouseId);
	        	oicData.setRemarkForDeferring(remarks);
	        	oicData.setCreatedBy(userId);
	        }else {
	        	oicData.setRemarkForDeferring(remarks);
	        }
	        conformityTakeActionOicRepository.save(oicData);
	        applicantData.setIntApplicationStatus(105);
	        applicantData.setApplicationStatus(ApproverStatus.DEFERRED);
	        conformityMainRepository.save(applicantData);
	        
	        ApplicationOfConformityActionHistory actionHistory= new ApplicationOfConformityActionHistory();
		    actionHistory.setVchWareHouseId(warehouseId);
		    actionHistory.setIntUserId(userId);
		    actionHistory.setVchUserName(userName);
		    actionHistory.setIntRoleId(roleId);	        
		    actionHistory.setVchRoleName(roleName);   
		    actionHistory.setVchStatus("Defer Back To Applicant");
		    actionHistory.setVchRemarks(remarks);
		    applicationOfConformityActionHistoryRepository.save(actionHistory);
		    
	        response.put("status", "success");
	        response.put("message", "Data saved successfully.");
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}
	
	
	@Override
	public Map<String, Object> getInspectorPdf(Integer confId) {
		ConformityTakeActionInspector inspectorData =conformityTakeActionInspectorRepository.findByConformity_Id(confId);
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
        response.put("inspectorApprovalaData", inspectorData);
        response.put("recomendationData", recomendationData);
        response.put("conclusionData", conclusionData);
 
        return response;
	}

	
	//Edit by Jiban
	@Transactional
	@Override
	public JSONObject updateLocationDetails(String updateData) {
		JSONObject response = new JSONObject();
		String decodedData = CommonUtil.inputStreamDecoder(updateData);
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
 
		try {
	        	ApplicationConformityWhLocationDTO dto = om.readValue(decodedData, ApplicationConformityWhLocationDTO.class);
				ApplicationOfConformityLocationDetails entity = mapDtoToEntity(dto);
				locationDetailsRepo.save(entity);
 
			response.put("status", "success");
			response.put("message", "Locations update successfully");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
 
		return response;
	}
	
	@Override
	@Transactional
	public JSONObject updateFormOneADetails(String code) {
		JSONObject response = new JSONObject();
 
		try {
			String decodedData = CommonUtil.inputStreamDecoder(code);
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			ApplicationConformityFormOneADto dto = om.readValue(decodedData,
					ApplicationConformityFormOneADto.class);
 
			ApplicationOfConformityFormOneA entity = formOneARepository.findById(dto.getFormOneADocId())
	                .orElse(new ApplicationOfConformityFormOneA());
			entity.setWarehouseLocation(locationDetailsRepo.findById(dto.getWarehouseId()).orElseThrow(
						() -> new RuntimeException("Warehouse not found with ID: " + dto.getWarehouseId())));
				entity.setCompany(compProfRepo.findById(dto.getCompanyId()).orElseThrow(
						() -> new RuntimeException("Company not found with ID: " + dto.getCompanyId())));
				if(dto.getFormOneADocId() != null) {
				entity.setFormOneADocId(dto.getFormOneADocId());
				}
				// Set entity properties
				entity.setBusinessPlan(dto.getBusinessPlan());
				entity.setCurrentTaxComplianceCertificate(dto.getCurrentTaxComplianceCertificate());
				entity.setAuthorizedSignatoryCertificateOfGoodConduct(
						dto.getAuthorizedSignatoryCertificateOfGoodConduct());
				entity.setKraPinOfTheAuthorizedSignatory(dto.getKraPinOfTheAuthorizedSignatory());
				entity.setNationalIdOfTheAuthorizedSignatory(dto.getNationalIdOfTheAuthorizedSignatory());
				entity.setInsurancePolicy(dto.getInsurancePolicy());
				entity.setValidLicenseForTheWarehouse(dto.getValidLicenseForTheWarehouse());
				entity.setCertificateOfIncorporationRegistration(
						dto.getCertificateOfIncorporationRegistration());
				entity.setLatestCR12(dto.getLatestCR12());
				entity.setLatestCertificateOfCompliance(dto.getLatestCertificateOfCompliance());
				entity.setUpdatedBy(dto.getUserId());
 
				// Process file uploads
				List<String> fileUploadList = Arrays.asList(dto.getBusinessPlan(),
						dto.getCurrentTaxComplianceCertificate(),
						dto.getAuthorizedSignatoryCertificateOfGoodConduct(),
						dto.getKraPinOfTheAuthorizedSignatory(),
						dto.getNationalIdOfTheAuthorizedSignatory(), dto.getInsurancePolicy(),
						dto.getValidLicenseForTheWarehouse(),
						dto.getCertificateOfIncorporationRegistration(), dto.getLatestCR12(),
						dto.getLatestCertificateOfCompliance());
 
				fileUploadList.stream().filter(file -> !Strings.isNullOrEmpty(file))
						.forEach(file -> copyFileToDestination(file, "certificate-conformity-form-one-a"));
			
 
			// Batch insert
			formOneARepository.save(entity);
 
			response.put("message", "Data saved successfully");
		} catch (Exception e) {
			response.put("message", "Error saving data");
			logger.error("Error processing Form One A details", e);
		}
 
		return response;
	}
	
	@Override
	@Transactional
	public JSONObject updateFormOneBDetails(String code) {
		JSONObject response = new JSONObject();
 
		try {
			String decodedData = CommonUtil.inputStreamDecoder(code);
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			ApplicationConformityFormOneBDto dto = om.readValue(decodedData,
					ApplicationConformityFormOneBDto.class);
		
				ApplicationOfConformityFormOneB entity = formOneBRepository.findById(dto.getFormOneBDocId())
		                .orElse(new ApplicationOfConformityFormOneB());
				entity.setWarehouseLocation(locationDetailsRepo.findById(dto.getWarehouseId()).orElseThrow(
						() -> new RuntimeException("Warehouse not found with ID: " + dto.getWarehouseId())));
				entity.setCompany(compProfRepo.findById(dto.getCompanyId()).orElseThrow(
						() -> new RuntimeException("Company not found with ID: " + dto.getCompanyId())));
 
				if(dto.getFormOneBDocId() != null) {
					entity.setFormOneBDocId(dto.getFormOneBDocId());
					}
				// Set entity properties
				entity.setStrategicPlan(dto.getStrategicPlan());
				entity.setLeaseAgreement(dto.getLeaseAgreement());
				entity.setTitleDeed(dto.getTitleDeed());
				entity.setAssetRegister(dto.getAssetRegister());
				entity.setRecommendationLetters(dto.getRecommendationLetters());
				entity.setAuditedFinancialReports(dto.getAuditedFinancialReports());
				entity.setRiskAnalysis(dto.getRiskAnalysis());
				entity.setUpdatedBy(dto.getUserId());
 
				// Process file uploads
				List<String> fileUploadList = Arrays.asList(dto.getStrategicPlan(),
						dto.getLeaseAgreement(), dto.getTitleDeed(), dto.getAssetRegister(),
						dto.getRecommendationLetters(), dto.getAuditedFinancialReports(),
						dto.getRiskAnalysis());
 
				fileUploadList.stream().filter(file -> !Strings.isNullOrEmpty(file))
						.forEach(file -> copyFileToDestination(file, "certificate-conformity-form-one-b"));
 
			// Batch insert
			formOneBRepository.save(entity);
 
			response.put("message", "Data updated successfully");
		} catch (Exception e) {
			response.put("message", "Error saving data");
			logger.error("Error processing Form One B details", e);
		}
 
		return response;
	}
	
	@Override
	@Transactional
	public JSONObject updateCommodityDetails(String code) {
		JSONObject response = new JSONObject();
 
		try {
			String decodedData = CommonUtil.inputStreamDecoder(code);
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			AppliationConformityCommodityDto dto = om.readValue(decodedData, AppliationConformityCommodityDto.class);	
			AocCompProfileDetails company = compProfRepo.findById(dto.getCompanyId())
					.orElseThrow(() -> new RuntimeException("Company not found with ID: " + dto.getCompanyId()));
			aocCommodityDetailsRepo.deleteByCompanyAndDraftStatusTrueAndDeletedFlagFalse(company);
			aocCommodityStorageRepo.deleteByCompanyAndDraftStatusTrueAndDeletedFlagFalse(company);
			List<ApplicationOfConformityCommodityDetails> commodityDetailsList = new ArrayList<>();
			
			for (WarehouseStorageDTO singleStorageDetails : dto.getCommodityStorageDetails()) {
			    aocCommodityDetailsRepo.deleteByWarehouseId(singleStorageDetails.getWarehouseId());
 
			    ApplicationOfConformityCommodityStorage storage =
			        aocCommodityStorageRepo.findByWarehouseId(singleStorageDetails.getWarehouseId());
			    
			    if (storage == null) {
			        storage = new ApplicationOfConformityCommodityStorage();
			    }
			    
			    storage.setCompany(company);
			    storage.setWarehouseLocation(locationDetailsRepo.findById(singleStorageDetails.getWarehouseId()).orElseThrow(
			        () -> new RuntimeException("Warehouse not found with ID: " + singleStorageDetails.getWarehouseId())));
			    storage.setStorageCapacity(singleStorageDetails.getStorageCapacity());
			    storage.setCreatedBy(dto.getUserId());
 
			    ApplicationOfConformityCommodityStorage savedEntity = aocCommodityStorageRepo.save(storage);
 
			    for (CommodityDetailDTO singleData : singleStorageDetails.getCommodityDetails()) {
			        ApplicationOfConformityCommodityDetails details=new ApplicationOfConformityCommodityDetails();
 
			        details.setCommodityStorage(savedEntity);
			        details.setCommodityType(commodityMasterRepository.findById(singleData.getCommodityId())
			            .orElseThrow(() -> new RuntimeException(
			                "Commodity not found with ID: " + singleData.getCommodityId())));
			        details.setWarehouseLocation(storage.getWarehouseLocation());
			        details.setCompany(company);
			        details.setStorageCapacity(singleStorageDetails.getStorageCapacity());
			        details.setUnit(singleData.getUnit());
			        details.setCreatedBy(dto.getUserId());
			        details.setDraftStatus(false);
 
			        commodityDetailsList.add(details);
			    }
			}
			aocCommodityDetailsRepo.saveAll(commodityDetailsList);
			response.put("message", "Updated Successfully");
 
		} catch (JsonProcessingException e) {
			response.put("message", "Invalid JSON format");
		} catch (RuntimeException e) {
			response.put("message", e.getMessage());
		} catch (Exception e) {
			response.put("message", "Unable to update due to internal server error!");
		}
 
		return response;
	}

	@Override
	public List<ConformityMain> getAllApprovedData() {
		return conformityMainRepository.findByIntApplicationStatus(104);
	}
	
	@Override
	public List<ConformityMain> getAllRejectedData() {
		return conformityMainRepository.findByIntApplicationStatus(107);
	}

	@Override
	public JSONObject getInspRequestedBy(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject response = new JSONObject();
		try {
			JsonNode jsonNode = om.readTree(decodedData);
	        int intApplicantId = jsonNode.get("intApplicantId").asInt();
	        ConformityTakeActionInspector inspData = conformityTakeActionInspectorRepository.findByConformityId(intApplicantId);
	        String ccName = tuserRepository.getUserName(inspData.getCreatedBy());
	        response.put("status", "success");
	        response.put("inspReqBy", ccName);
		} catch (Exception e) {
			response.put("status", "error");
	        response.put("message", "An error occurred: " + e.getMessage());
		}
		return response;
	}

	@Override
	public ConformityTakeActionInspectorSchedule getInspectionSchedule(Integer applicantId) {
		return conformityInspScheduleRepo.findByConformityId(applicantId);
	}

	@Override
	public ConformityTakeActionInspector getAllocatedInspectorData(Integer applicantId) {
		ConformityTakeActionInspector inspData = conformityTakeActionInspectorRepository.findByConformityId(applicantId);
		if(inspData.getIntLocationId() != null) {
		inspData.setRecomendationData(recomendationRepository.findByIntLocationIdAndBitDeletedFlagFalse(inspData.getIntLocationId().getIntLocationId()));
		inspData.setConclusionData(routineComplianceConclusionRepository.findByIntLocationIdAndBitDeletedFlagFalse(inspData.getIntLocationId().getIntLocationId()));
		}
		return inspData;
	}

	@Override
	public List<Map<String, Object>> getAllApprovedDataForTakeAction() {
	    List<Object[]> data = conformityMainRepository.findAllApprovedData();
	    List<Map<String, Object>> result = new ArrayList<>();

	    String[] keys = { "cnfId", "warehouseId", "warehouseName", "whOperatorName", "email", 
	                      "mobileNumber", "lrNumber", "countyName", "subCountyName", "wardName", 
	                      "certificateNo" };

	    for (Object[] row : data) {
	        Map<String, Object> map = new HashMap<>();
	        for (int i = 0; i < row.length; i++) {
	            map.put(keys[i], row[i]);
	        }
	        result.add(map);
	    }
	    
	    return result;
	}

	@Override
	public List<Map<String, Object>> getAllDeferedDataForTakeAction() {
		List<Object[]> data = conformityMainRepository.findAllDeferredDataForTakeAction();
		List<Map<String, Object>> result = new ArrayList<>();

		String[] keys = { "intcnfId", "vchWareHouseId", "vchWarehouseName", "vchWhOperatorName", "vchEmail", 
                "vchMobileNumber", "vchLrNumber", "countyName", "vchSubCountyName", "wardName", 
                "vchDeferCeoRemarks", "remarkForDeferring" };

	    for (Object[] row : data) {
	        Map<String, Object> map = new HashMap<>();
	        for (int i = 0; i < row.length; i++) {
	            map.put(keys[i], row[i]);
	        }
	        result.add(map);
	    }
	    
	    return result;
	}

	@Override
	public Page<Map<String, Object>> getAllApprovedDataForTakeAction(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
	    Page<Object[]> dataPage = conformityMainRepository.findAllApprovedData(pageable);

	    String[] keys = { "cnfId", "warehouseId", "warehouseName", "whOperatorName", "email", 
	                      "mobileNumber", "lrNumber", "countyName", "subCountyName", "wardName", 
	                      "certificateNo" };

	    return dataPage.map(row -> {
	        Map<String, Object> map = new HashMap<>();
	        for (int i = 0; i < keys.length; i++) {
	            map.put(keys[i], row[i]);
	        }
	        return map;
	    });
	}

	@Override
	public Page<Map<String, Object>> getAllDeferedDataForTakeAction(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> dataPage = conformityMainRepository.findAllDeferredDataForTakeAction(pageable);

		String[] keys = { "intcnfId", "vchWareHouseId", "vchWarehouseName", "vchWhOperatorName", "vchEmail", 
                "vchMobileNumber", "vchLrNumber", "countyName", "vchSubCountyName", "wardName", 
                "vchDeferCeoRemarks", "remarkForDeferring" };

		return dataPage.map(row -> {
	        Map<String, Object> map = new HashMap<>();
	        for (int i = 0; i < keys.length; i++) {
	            map.put(keys[i], row[i]);
	        }
	        return map;
	    });
	}

	@Override
	public Page<Map<String, Object>> getAllRejectedDataForTakeAction(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Object[]> dataPage = conformityMainRepository.findAllRejectedDataForTakeAction(pageable);
		String[] keys = { "intcnfId", "vchWareHouseId", "vchWarehouseName", "vchWhOperatorName", "vchEmail", 
                "vchMobileNumber", "vchLrNumber", "countyName", "vchSubCountyName", "wardName", 
                "vchRejectionRemarks"};

		return dataPage.map(row -> {
	        Map<String, Object> map = new HashMap<>();
	        for (int i = 0; i < keys.length; i++) {
	            map.put(keys[i], row[i]);
	        }
	        return map;
	    });
	}

	@Override
	public List<ApplicationOfConformityActionHistory> getConformityActionHistoryByWareHouseId(String wareHouseId) {
		return applicationOfConformityActionHistoryRepository.findConformityActionHistoryByWareHouseId(wareHouseId);
	}


//	recomendationData = recomendationRepository.findByIntLocationIdAndBitDeletedFlagFalse(locationData.getIntLocationId());
//    conclusionData = routineComplianceConclusionRepository.findByIntLocationIdAndBitDeletedFlagFalse(locationData.getIntLocationId());
//    List<Recommendation> recomendationData = new ArrayList<>();
//    List<RoutineComplianceConclusion> conclusionData = new ArrayList<>();
	

	

	

	

	

	




//	@Override
//	@Transactional
//	public JSONObject saveFormOneADetails(String code) {
//		JSONObject response = new JSONObject();
//		List<String> fileUploadList = new ArrayList<>();
//		try {
//			String decodedData = CommonUtil.inputStreamDecoder(code);
//	        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//	        ApplicationConformityFormOneAListDto dto = om.readValue(decodedData, ApplicationConformityFormOneAListDto.class);
//	        for(ApplicationConformityFormOneADto singleData : dto.getFormOneAList()) {
//	        	
//	        	fileUploadList.add(singleData.getBusinessPlan());
//	        	fileUploadList.add(singleData.getCurrentTaxComplianceCertificate());
//	        	fileUploadList.add(singleData.getAuthorizedSignatoryCertificateOfGoodConduct());
//	        	fileUploadList.add(singleData.getKraPinOfTheAuthorizedSignatory());
//	        	fileUploadList.add(singleData.getNationalIdOfTheAuthorizedSignatory());
//	        	fileUploadList.add(singleData.getInsurancePolicy());
//	        	fileUploadList.add(singleData.getValidLicenseForTheWarehouse());
//	        	fileUploadList.add(singleData.getCertificateOfIncorporationRegistration());
//	        	fileUploadList.add(singleData.getLatestCR12());
//	        	fileUploadList.add(singleData.getLatestCertificateOfCompliance());
//	        	
//	        	ApplicationOfConformityFormOneA entity = new ApplicationOfConformityFormOneA();
//	        	entity.setWarehouseLocation(locationDetailsRepo.findById(singleData.getWarehouseId()).get());
//	        	AocCompProfileDetails company = compProfRepo.findById(singleData.getCompanyId())
//		                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + singleData.getCompanyId()));
//	        	entity.setCompany(company);
//	        	entity.setBusinessPlan(singleData.getBusinessPlan());
//	        	entity.setCurrentTaxComplianceCertificate(singleData.getCurrentTaxComplianceCertificate());
//	        	entity.setAuthorizedSignatoryCertificateOfGoodConduct(singleData.getAuthorizedSignatoryCertificateOfGoodConduct());
//	        	entity.setKraPinOfTheAuthorizedSignatory(singleData.getKraPinOfTheAuthorizedSignatory());
//	        	entity.setNationalIdOfTheAuthorizedSignatory(singleData.getNationalIdOfTheAuthorizedSignatory());
//	        	entity.setInsurancePolicy(singleData.getInsurancePolicy());
//	        	entity.setValidLicenseForTheWarehouse(singleData.getValidLicenseForTheWarehouse());
//	        	entity.setCertificateOfIncorporationRegistration(singleData.getCertificateOfIncorporationRegistration());
//	        	entity.setLatestCR12(singleData.getLatestCR12());
//	        	entity.setLatestCertificateOfCompliance(singleData.getLatestCertificateOfCompliance());
//	        	entity.setCreatedBy(singleData.getUserId());
//	        	formOneARepository.save(entity);
//	        	fileUploadList.forEach(fileUpload -> {
//	        	    if (!Strings.isNullOrEmpty(fileUpload)) {
//	        	        String srcPath = tempUploadPath + fileUpload;
//	        	        String destDir = finalUploadPath + "certificate-conformity-form-one-a/";
//	        	        String destPath = destDir + fileUpload;
//
//	        	        logger.info("Source Path: {}", srcPath);
//	        	        logger.info("Destination Path: {}", destPath);
//
//	        	        File f = new File(srcPath);
//	        	        if (f.exists()) {
//	        	            File src = new File(srcPath);
//	        	            File dest = new File(destPath);
//	        	            try {
//	        	                Files.createDirectories(Paths.get(destDir));
//	        	                Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
//	        	                Files.delete(src.toPath());
//	        	            } catch (IOException e) {
//	        	                logger.error("Internal Server error", e);
//	        	            }
//	        	        } else {
//	        	            logger.error("File not found: {}", srcPath);
//	        	        }
//	        	    }
//	        	});
//	        }
//	        response.put("message", "Data saved");
//	        
//		} catch (Exception e) {
//			response.put("message", "There might be some error");
//			logger.error("Internal Server error", e);
//		}
//		return response;
//	}

}
