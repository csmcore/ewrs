package app.ewarehouse.serviceImpl;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import app.ewarehouse.dto.PledgeDischargeEmploymentDetailsDTO;
import app.ewarehouse.dto.PledgeDischargeResidentialDTO;
import app.ewarehouse.dto.PledgeDischargeStepFiveDTO;
import app.ewarehouse.dto.PledgingDischargeBankDetailsDTO;
import app.ewarehouse.dto.PledgingDischargeDepositorLoanAppDTO;
import app.ewarehouse.dto.PledgingDischargeDepositorWarehouseDTO;
import app.ewarehouse.dto.PledgingDischargeWarehouseReceiptRequest;
import app.ewarehouse.dto.PledgingDischargeWarehouseReceiptResponse;
import app.ewarehouse.entity.Bank;
import app.ewarehouse.entity.BuyerDepositor;
import app.ewarehouse.entity.EmploymentDetails;
import app.ewarehouse.entity.MLoanPurpose;
import app.ewarehouse.entity.MPledgingDischargeWarehouseReceipt;
import app.ewarehouse.entity.MScheme;
import app.ewarehouse.entity.PledgingDischargeBankDetails;
import app.ewarehouse.entity.PledgingDischargeDepositorLoanApp;
import app.ewarehouse.entity.PledgingDischargeDepositorWarehouse;
import app.ewarehouse.entity.PledgingDischargeMain;
import app.ewarehouse.entity.PledgingDischargeResidential;
import app.ewarehouse.entity.PledgingDischargeUploadDocs;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TwarehouseReceipt;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.BankRepository;
import app.ewarehouse.repository.BuyerDepositorRepository;
import app.ewarehouse.repository.EmploymentDetailsRepository;
import app.ewarehouse.repository.LoanPurposeRepository;
import app.ewarehouse.repository.PledgingDischargeBankDetailsRepository;
import app.ewarehouse.repository.PledgingDischargeDepositorLoanAppRepository;
import app.ewarehouse.repository.PledgingDischargeDepositorWarehouseRepository;
import app.ewarehouse.repository.PledgingDischargeMainRepository;
import app.ewarehouse.repository.PledgingDischargeResidentialRepository;
import app.ewarehouse.repository.PledgingDischargeUploadDocsRepository;
import app.ewarehouse.repository.PledgingDischargeWarehouseReceiptRepository;
import app.ewarehouse.repository.SchemeRepository;
import app.ewarehouse.repository.TwarehouseReceiptRepository;
import app.ewarehouse.service.PledgingDischargeWarehouseReceiptService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.DocumentUploadutil;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.Mapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PledgingDischargeWarehouseReceiptServiceImpl implements PledgingDischargeWarehouseReceiptService {
	private final PledgingDischargeWarehouseReceiptRepository repository;
	private final EmploymentDetailsRepository employmentDetailsRepository;
	private final EntityManager entityManager;
	private final BuyerDepositorRepository bdRepo;
	private final TwarehouseReceiptRepository wrRepo;
	private final PledgingDischargeDepositorWarehouseRepository stepOneRepo;
	private final PledgingDischargeDepositorLoanAppRepository stepTwoRepo;
	private final PledgingDischargeResidentialRepository stepThreeRepo;
	private final BankRepository bankRepo;
	private final LoanPurposeRepository loanPurposeRepo;
	private final SchemeRepository schemeRepo;
	private final EmploymentDetailsRepository empDetailsRepo;
	private final PledgingDischargeBankDetailsRepository stepFourRepo;
	private final PledgingDischargeUploadDocsRepository stepFiveRepo;
	private final PledgingDischargeMainRepository mainRepo;
	private final JdbcTemplate jdbcTemplate;
	private final ObjectMapper om;

	private static final String STATUS = "status";
	private static final String AN_UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred: ";
	private static final String ERROR = "error";

	@Override
	@Transactional
	public String publishDraft(String data) {
		log.info("Inside publishDraft method of PledgingDischargeWarehouseReceiptServiceImpl");
		try {
			PledgingDischargeWarehouseReceiptRequest request = om
					.readValue(CommonUtil.inputStreamDecoder(data), PledgingDischargeWarehouseReceiptRequest.class);

			// Build the MPledgingDischargeWarehouseReceipt object with status Published
			MPledgingDischargeWarehouseReceipt pledgingDischargeWarehouseReceipt = buildPledgingDischargeWarehouseReceipt(
					request, Status.Published);

			MPledgingDischargeWarehouseReceipt savedEntity = repository.save(pledgingDischargeWarehouseReceipt);
			return savedEntity.getPledgingDischargeReceiptId();
		} catch (CustomGeneralException exception) {
			throw exception;
		} catch (Exception e) {
			log.error("Error occurred while publishing PledgingDischargeWarehouseReceipt: {}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public String saveAsDraft(String data) {
		log.info("Inside saveAsDraft method of PledgingDischargeWarehouseReceiptServiceImpl");
		try {
			PledgingDischargeWarehouseReceiptRequest request = om
					.readValue(CommonUtil.inputStreamDecoder(data), PledgingDischargeWarehouseReceiptRequest.class);

			// Build the MPledgingDischargeWarehouseReceipt object with status Draft
			MPledgingDischargeWarehouseReceipt pledgingDischargeWarehouseReceipt = buildPledgingDischargeWarehouseReceipt(
					request, Status.Draft);

			MPledgingDischargeWarehouseReceipt savedEntity = repository.save(pledgingDischargeWarehouseReceipt);
			return savedEntity.getPledgingDischargeReceiptId();
		} catch (CustomGeneralException exception) {
			throw exception;
		} catch (Exception e) {
			log.error("Error occurred while creating PledgingDischargeWarehouseReceipt: {}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PledgingDischargeWarehouseReceiptResponse> getAll(Integer pageNumber, Integer pageSize, String sortCol,
			String sortDir, String search) {
		log.info("Inside getAll paginated method of PledgingDischargeWarehouseReceiptServiceImpl");
		try {
			Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "DESC"),
					sortCol != null ? sortCol : "createdOn");
			Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

			Page<PledgingDischargeMain> page;
			if (StringUtils.hasText(search)) {
				page = mainRepo.findByFilters(search, pageable);
			} else {
				page = mainRepo.getAll(pageable);
			}
			
			//List<PledgingDischargeMain> data = page.getContent();

			List<PledgingDischargeWarehouseReceiptResponse> data = page.getContent().stream()
					.map(Mapper::mapToPledgingDischargeWarehouseReceiptResponse).toList();

			return new PageImpl<>(data, pageable, page.getTotalElements());

		} catch (Exception e) {
			log.error("Error occurred in getAll method of PledgingDischargeWarehouseReceiptServiceImpl: {}",
					e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public PledgingDischargeWarehouseReceiptResponse getDraft() {
		MPledgingDischargeWarehouseReceipt pledgingDischargeWarehouseReceipt = repository
				.findByCreatedByAndStatus(Status.Draft, 7);
		//return Mapper.mapToPledgingDischargeWarehouseReceiptResponse(pledgingDischargeWarehouseReceipt);
		return null;
	}

	private String handleFileUpload(String base64File, String fileType) {
		String filePath = null;
		if (StringUtils.hasText(base64File)) {
			byte[] decodedFile = Base64.getDecoder().decode(base64File.getBytes());
			String uniqueFileName = fileType + "_" + UUID.randomUUID();
			String fileUrl = DocumentUploadutil.uploadFileByte(uniqueFileName, decodedFile,
					FolderAndDirectoryConstant.PLEDGING_DISCHARGE_WAREHOUSE_RECEIPT);

			if (fileUrl.startsWith("Document")) {
				throw new CustomGeneralException(fileUrl);
			}
			filePath = fileUrl
					.substring(fileUrl.indexOf(FolderAndDirectoryConstant.PLEDGING_DISCHARGE_WAREHOUSE_RECEIPT));
		}
		return filePath;
	}

	private BigDecimal safeBigDecimal(String value) {
		if (value == null || value.trim().isEmpty()) {
			return BigDecimal.ZERO; // or another default value
		}
		try {
			return new BigDecimal(value);
		} catch (NumberFormatException e) {
			log.error("Invalid number format for value: {}", value);
			return BigDecimal.ZERO; // or handle the error as needed
		}
	}

	private boolean hasEmploymentDetailsData(EmploymentDetails employmentDetails) {
		return StringUtils.hasText(employmentDetails.getPayrollNo())
				|| StringUtils.hasText(employmentDetails.getContract())
				|| StringUtils.hasText(employmentDetails.getNameOfCurrentEmployer())
				|| !ObjectUtils.isEmpty(employmentDetails.getNoOfYearsWithEmployer())
				|| !ObjectUtils.isEmpty(employmentDetails.getCurrentSalaryPM())
				|| !ObjectUtils.isEmpty(employmentDetails.getMonthlyExpenditure())
				|| StringUtils.hasText(employmentDetails.getTerms())
				|| StringUtils.hasText(employmentDetails.getWorkPhysicalAddress())
				|| StringUtils.hasText(employmentDetails.getTelephoneOffice())
				|| StringUtils.hasText(employmentDetails.getPosition())
				|| StringUtils.hasText(employmentDetails.getSelfEmployed())
				|| StringUtils.hasText(employmentDetails.getDepartment())
				|| StringUtils.hasText(employmentDetails.getEmploymentTerms())
				|| StringUtils.hasText(employmentDetails.getEmployerEmail());
	}

	private MPledgingDischargeWarehouseReceipt buildPledgingDischargeWarehouseReceipt(
			PledgingDischargeWarehouseReceiptRequest request, Status status) {
		BuyerDepositor buyer = null;
		TwarehouseReceipt twarehouseReceipt = null;
		Bank bank = null;
		MLoanPurpose loanPurpose = null;
		MScheme scheme = null;

		if (request.getDepositorId() != null) {
			buyer = entityManager.find(BuyerDepositor.class, request.getDepositorId());
		}
		if (request.getWareHouseReceiptNo() != null) {
			twarehouseReceipt = entityManager.find(TwarehouseReceipt.class, request.getWareHouseReceiptNo());
		}
		if (request.getBankId() != null) {
			bank = entityManager.find(Bank.class, request.getBankId());
		}
		if (request.getLoanPurposeId() != null) {
			loanPurpose = entityManager.find(MLoanPurpose.class, request.getLoanPurposeId());
		}
		if (request.getSchemeId() != null) {
			scheme = entityManager.find(MScheme.class, request.getSchemeId());
		}

		EmploymentDetails employmentDetails = null;
		if (request.getEmploymentDetails() != null && hasEmploymentDetailsData(request.getEmploymentDetails())) {
			employmentDetails = new EmploymentDetails();
			employmentDetails.setPayrollNo(request.getEmploymentDetails().getPayrollNo());
			employmentDetails.setContract(request.getEmploymentDetails().getContract());
			employmentDetails.setNameOfCurrentEmployer(request.getEmploymentDetails().getNameOfCurrentEmployer());
			employmentDetails.setNoOfYearsWithEmployer(request.getEmploymentDetails().getNoOfYearsWithEmployer());
			employmentDetails.setCurrentSalaryPM(request.getEmploymentDetails().getCurrentSalaryPM());
			employmentDetails.setMonthlyExpenditure(request.getEmploymentDetails().getMonthlyExpenditure());
			employmentDetails.setTerms(request.getEmploymentDetails().getTerms());
			employmentDetails.setWorkPhysicalAddress(request.getEmploymentDetails().getWorkPhysicalAddress());
			employmentDetails.setTelephoneOffice(request.getEmploymentDetails().getTelephoneOffice());
			employmentDetails.setPosition(request.getEmploymentDetails().getPosition());
			employmentDetails.setSelfEmployed(request.getEmploymentDetails().getSelfEmployed());
			employmentDetails.setDepartment(request.getEmploymentDetails().getDepartment());
			employmentDetails.setEmploymentTerms(request.getEmploymentDetails().getEmploymentTerms());
			employmentDetails.setEmployerEmail(request.getEmploymentDetails().getEmployerEmail());
			employmentDetails = employmentDetailsRepository.save(request.getEmploymentDetails());
		}

		return MPledgingDischargeWarehouseReceipt.builder()
				.pledgingDischargeReceiptId(request.getPledgingDischargeReceiptId()).buyer(buyer)
				.warehouseReceipt(twarehouseReceipt).bank(bank).loanPurpose(loanPurpose).scheme(scheme)
				.proposedCreditAmount(safeBigDecimal(request.getProposedCreditAmount()))
				.tenureOfLoanRepayment(request.getTenureOfLoanRepayment()).area(request.getArea())
				.plotNo(request.getPlotNo()).streetName(request.getStreetName())
				.lengthOfStayAtPresentAddress(request.getLengthOfStayAtPresentAddress())
				.nearestMarket(request.getNearestMarket()).currentPoliceStation(request.getCurrentPoliceStation())
				.yearsOfStay(request.getYearsOfStay()).gender(request.getGender())
				.maritalStatus(request.getMaritalStatus()).religion(request.getReligion())
				.isEmployed(request.getIsEmployed()).employmentDetails(employmentDetails)
				.nameOfAccountHolder(request.getNameOfAccountHolder()).accountNo(request.getAccountNo())
				.bankBranchName(request.getBankBranchName()).swiftCodes(request.getSwiftCodes())
				.bankStatementUpload(handleFileUpload(request.getBankStatement(), "Bank_Statement"))
				.passportPhotoUpload(handleFileUpload(request.getPassportSizePhoto(), "Passport_Photo"))
				.latestIdOrPassportUpload(handleFileUpload(request.getLatestIDOrPassport(), "ID_Passport"))
				.addressProofUpload(handleFileUpload(request.getAddressProof(), "Address_Proof"))
				.warehouseReceiptUpload(handleFileUpload(request.getWarehouseReceipt(), "Warehouse_Receipt"))
				.pinCertificateUpload(handleFileUpload(request.getPinCertificate(), "Pin_Certificate")).status(status)
				.createdBy(7) // TODO: should be set based on id from extracted token
				.build();
	}

	@Override
	public JSONObject saveStepOne(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			PledgingDischargeDepositorWarehouseDTO dto = om.readValue(decodedData,
					PledgingDischargeDepositorWarehouseDTO.class);
			PledgingDischargeDepositorWarehouse entity = new PledgingDischargeDepositorWarehouse();
			if (dto.getPledgingDischargeReceiptId() != null) {
				entity.setPledgingDischargeReceiptId(dto.getPledgingDischargeReceiptId());
			}
			BuyerDepositor bd = bdRepo.findById(dto.getDepositorId()).get();
			entity.setBuyer(bd);
			TwarehouseReceipt wr = wrRepo.findById(dto.getWareHouseReceiptNo()).get();
			entity.setWarehouseReceipt(wr);
			entity.setIntCreatedBy(dto.getUserId());
			entity.setBitDraftStatus(true);
			PledgingDischargeDepositorWarehouse resultData = stepOneRepo.save(entity);
			json.put("result", resultData.getPledgingDischargeReceiptId());
			json.put(STATUS, 200);
		} catch (Exception e) {
			log.error("PledgingDischargeWarehouseReceipt:saveStepOne()",e);
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override
	public String getCountStepOneByCreatedByAndDraftStatus(Integer intId) {
		return stepOneRepo.getCountStepOneByCreatedByAndDraftStatus(intId);
	}

	@Override
	public PledgingDischargeDepositorWarehouse getStepOneDataById(String id) {
		return stepOneRepo.findById(id).orElse(null);
	}

	@Override
	public JSONObject saveStepTwo(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			PledgingDischargeDepositorLoanAppDTO dto = om.readValue(decodedData,
					PledgingDischargeDepositorLoanAppDTO.class);
			PledgingDischargeDepositorLoanApp entity = new PledgingDischargeDepositorLoanApp();
			if (dto.getPdLoanAppId() != null) {
				entity.setPdLoanAppId(dto.getPdLoanAppId());
			}
			PledgingDischargeDepositorWarehouse wrdata = stepOneRepo.findById(dto.getPledgingDischargeReceiptId())
					.get();
			entity.setPledgingDischargeDepositorWarehouse(wrdata);
			Bank bank = bankRepo.findById(dto.getBankId()).get();
			entity.setBank(bank);
			MLoanPurpose loanPurpose = loanPurposeRepo.findById(dto.getLoanPurposeId()).get();
			entity.setLoanPurpose(loanPurpose);
			MScheme sch = schemeRepo.findById(dto.getSchemeId()).get();
			entity.setScheme(sch);
			entity.setProposedCreditAmount(dto.getProposedCreditAmount());
			entity.setTenureOfLoanRepayment(dto.getTenureOfLoanRepayment());
			entity.setCreatedBy(dto.getUserId());
			entity.setDraftStatus(true);
			stepTwoRepo.save(entity);
			json.put(STATUS, 200);
		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override
	public String getDraftStatusOfStepTwo(Integer intId) {
		return stepTwoRepo.getDraftStatusOfStepTwo(intId);
	}

	@Override
	public PledgingDischargeDepositorLoanApp getStepTwoDataById(String id) {
		return stepTwoRepo.findById(id).orElse(null);
	}

	@Override
	public JSONObject saveStepThree(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			PledgeDischargeResidentialDTO dto = om.readValue(decodedData, PledgeDischargeResidentialDTO.class);
			PledgingDischargeResidential entity = new PledgingDischargeResidential();
			if (dto.getVchPdResidentialId() != null) {
				entity.setPdResidentialId(dto.getVchPdResidentialId());
			}
			PledgingDischargeDepositorWarehouse wrdata = stepOneRepo.findById(dto.getPledgingDischargeReceiptId())
					.get();
			entity.setPledgingDischargeDepositorWarehouse(wrdata);
			entity.setArea(dto.getArea());
			entity.setPlotNo(dto.getPlotNo());
			entity.setStreetName(dto.getStreetName());
			entity.setLengthOfStayAtPresentAddress(dto.getLengthOfStayAtPresentAddress());
			entity.setNearestMarket(dto.getNearestMarket());
			entity.setCurrentPoliceStation(dto.getCurrentPoliceStation());
			entity.setYearsOfStay(Integer.parseInt(dto.getYearsOfStay()));
			entity.setGender(dto.getGender());
			entity.setMaritalStatus(dto.getMaritalStatus());
			entity.setReligion(dto.getReligion());
			entity.setIsEmployed(dto.getIsEmployed());
			entity.setDraftStatus(true);
			entity.setCreatedBy(dto.getUserId());
			PledgingDischargeResidential savedData = stepThreeRepo.save(entity);
			if (savedData.getIsEmployed().equals("yes") && dto.getEmploymentDetails() != null) {
				PledgeDischargeEmploymentDetailsDTO empDto = dto.getEmploymentDetails();
				EmploymentDetails emp = new EmploymentDetails();
				if (empDto.getId() != null) {
					emp.setId(empDto.getId());
				}
				emp.setPayrollNo(empDto.getPayrollNo());
				emp.setNameOfCurrentEmployer(empDto.getNameOfCurrentEmployer());
				emp.setNoOfYearsWithEmployer(empDto.getNoOfYearsWithEmployer());
				emp.setCurrentSalaryPM(empDto.getCurrentSalaryPM());
				emp.setMonthlyExpenditure(empDto.getMonthlyExpenditure());
				emp.setTerms(empDto.getTerms());
				emp.setContract(empDto.getContract());
				emp.setSelfEmployed(empDto.getSelfEmployed());
				emp.setWorkPhysicalAddress(empDto.getWorkPhysicalAddress());
				emp.setTelephoneOffice(empDto.getTelephoneOffice());
				emp.setPosition(empDto.getPosition());
				emp.setDepartment(empDto.getDepartment());
				emp.setEmploymentTerms(empDto.getEmploymentTerms());
				emp.setEmployerEmail(empDto.getEmployerEmail());
				emp.setPledgingDischargeResidential(savedData);
				empDetailsRepo.save(emp);
			}else if(savedData.getIsEmployed().equals("no")) {
				EmploymentDetails emp = empDetailsRepo.findByPledgingDischargeResidential(savedData);
				if(emp != null) {
					empDetailsRepo.delete(emp);
				}
			}
			json.put(STATUS, 200);
		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override
	public String getDraftStatusOfStepThree(Integer intId) {
		return stepThreeRepo.getDraftStatusOfStepThree(intId);
	}

	@Override
	public PledgingDischargeResidential getStepThreeDataById(String id) {
		return stepThreeRepo.findById(id).orElse(null);
	}

	@Override
	public JSONObject saveStepFour(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			
			PledgingDischargeBankDetailsDTO dto = om.readValue(decodedData, PledgingDischargeBankDetailsDTO.class);
			PledgingDischargeBankDetails entity = new PledgingDischargeBankDetails();
			if(dto.getPdBankDetailsId() != null) {
				entity.setPdBankDetailsId(dto.getPdBankDetailsId());
			}
			PledgingDischargeDepositorWarehouse wrdata = stepOneRepo.findById(dto.getPledgingDischargeReceiptId())
					.get();
			entity.setPledgingDischargeDepositorWarehouse(wrdata);
			entity.setNameOfAccountHolder(dto.getNameOfAccountHolder());
			entity.setAccountNo(dto.getAccountNo());
			entity.setBankBranchName(dto.getBankBranchName());
			entity.setSwiftCodes(dto.getSwiftCodes());
			entity.setCreatedBy(dto.getUserId());
			entity.setDraftStatus(true);
			stepFourRepo.save(entity);
			json.put(STATUS, 200);
		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override
	public String getDraftStatusOfStepFour(Integer intId) {
		return stepFourRepo.getDraftStatusOfStepFour(intId);
	}

	@Override
	public PledgingDischargeBankDetails getStepFourDataById(String id) {
		return stepFourRepo.findById(id).orElse(null);
	}

	@Override
	public JSONObject saveStepFive(String data) {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			PledgeDischargeStepFiveDTO dto = om.readValue(decodedData, PledgeDischargeStepFiveDTO.class);
			PledgingDischargeUploadDocs entity = new PledgingDischargeUploadDocs();
			PledgingDischargeDepositorWarehouse wrdata = stepOneRepo.findById(dto.getPledgingDischargeReceiptId())
					.get();
			entity.setPledgingDischargeDepositorWarehouse(wrdata);
			entity.setBankStatementUpload(handleFileUpload(dto.getBankStatement(), "Bank_Statement"));
			entity.setPassportPhotoUpload(handleFileUpload(dto.getPassportSizePhoto(), "Passport_Photo"));
			entity.setLatestIdOrPassportUpload(handleFileUpload(dto.getLatestIDOrPassport(), "ID_Passport"));
			entity.setAddressProofUpload(handleFileUpload(dto.getAddressProof(), "Address_Proof"));
			entity.setWarehouseReceiptUpload(handleFileUpload(dto.getWarehouseReceipt(), "Warehouse_Receipt"));
			entity.setPinCertificateUpload(handleFileUpload(dto.getPinCertificate(), "Pin_Certificate"));
			entity.setIntCreatedBy(dto.getUserId());
			entity.setBitDraftStatus(true);
			PledgingDischargeUploadDocs savedEntity = stepFiveRepo.save(entity);
			String result = (String) saveDataInPledgingMainTable(savedEntity.getIntCreatedBy());
			json.put("output" , result);
			json.put(STATUS, 200);
		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}
	
	private Object saveDataInPledgingMainTable(Integer intCreatedBy) {

		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("pledging_submit");
		Map<String, Object> inParams = new HashMap<>();
		inParams.put("USER_ID", intCreatedBy);
		Map<String, Object> outParams = simpleJdbcCall.execute(inParams);
		return outParams.get("RESULT_MSG");
	}

	@Override
	public String getDraftStatusOfStepFive(Integer intId) {
		return stepFiveRepo.getDraftStatusOfStepFive(intId);
	}

	@Override
	public PledgingDischargeUploadDocs getStepFiveDataById(String id) {
		return stepFiveRepo.findById(id).orElse(null);
	}
	
	@Override
	public Page<PledgingDischargeWarehouseReceiptResponse> getLoanApplicants(Integer pageNumber, Integer pageSize,
			String sortCol, String sortDir, String search) {
		log.info("Inside getAll paginated method of PledgingDischargeWarehouseReceiptServiceImpl");
		try {
			Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "DESC"),
					sortCol != null ? sortCol : "createdOn");
			Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

			Page<PledgingDischargeMain> page;
//			if (StringUtils.hasText(search)) {
//				page = mainRepo.findByFilters(search, pageable);
//			} else {
//				page = mainRepo.getAll(pageable);
//			}
			page = mainRepo.getAll(pageable);
			
			List<PledgingDischargeWarehouseReceiptResponse> data = page.getContent().stream()
					.map(Mapper::mapToPledgingDischargeWarehouseReceiptResponse).toList();

			return new PageImpl<>(data, pageable, page.getTotalElements());

		} catch (Exception e) {
			log.error("Error occurred in getAll method of PledgingDischargeWarehouseReceiptServiceImpl: {}",
					e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public PledgingDischargeWarehouseReceiptResponse getByDepositorId(Integer pageNumber, Integer pageSize,
			String sortCol, String sortDir, String search, String depositorId) {
		log.info("Inside getAll paginated method of PledgingDischargeWarehouseReceiptServiceImpl");
		try {

			return Mapper.mapToPledgingDischargeWarehouseReceiptResponse(mainRepo.getByDepositorId(depositorId));

		} catch (Exception e) {
			log.error("Error occurred in getByDepositorId method of PledgingDischargeWarehouseReceiptServiceImpl: {}",
					e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

}
