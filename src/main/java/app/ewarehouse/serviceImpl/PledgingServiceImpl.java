package app.ewarehouse.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.DepositorPaymentDetailsDto;
import app.ewarehouse.dto.LoanSanctionDTO;
import app.ewarehouse.entity.DepositorPaymentDetails;
import app.ewarehouse.entity.IssuanceWareHouseRecipt;
import app.ewarehouse.entity.IssuanceWarehouseReceiptActionHistory;
import app.ewarehouse.entity.PaymentStatus;
import app.ewarehouse.entity.Pledge;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.master.dto.PledgeRequest;
import app.ewarehouse.repository.IssuanceWareHouseReciptRepository;
import app.ewarehouse.repository.IssuanceWarehouseReceiptActionHistoryRepository;
import app.ewarehouse.repository.PaymentRepository;
import app.ewarehouse.repository.PledgeRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.PledgeService;
import app.ewarehouse.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PledgingServiceImpl implements PledgeService {
	
	@Autowired
	TuserRepository tuserRepository ;
	
	@Autowired
	IssuanceWarehouseReceiptActionHistoryRepository issuanceWarehouseReceiptActionHistoryRepository;

	private static final String STATUS = "status";
	private static final String AN_UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred: ";
	private static final String ERROR = "error";
	private final ObjectMapper om;
	private final PaymentRepository paymentRepository;
	private final PledgeRepository repository;
	private final IssuanceWareHouseReciptRepository issuanceWareHouseReciptRepository;

	public PledgingServiceImpl(PledgeRepository repository, ObjectMapper om, PaymentRepository paymentRepository,
			IssuanceWareHouseReciptRepository issuanceWareHouseReciptRepository) {
		this.repository = repository;
		this.paymentRepository = paymentRepository;
		this.om = om;
		this.issuanceWareHouseReciptRepository = issuanceWareHouseReciptRepository;
	}

	public Pledge saveLoan(String requestData) {
		Pledge loan = new Pledge();

		String decodedData = CommonUtil.inputStreamDecoder(requestData);

		PledgeRequest request;
		try {
			request = new ObjectMapper().readValue(decodedData, PledgeRequest.class);
			loan.setIntCreatedBy(request.getUserId());
			loan.setFinancialInstitution(request.getFinancialInstitution());
			loan.setDepositorId(request.getDepositorId());
			loan.setDurationOfLoan(request.getDurationOfLoan());
			loan.setProposedLoanAmount(request.getProposedLoanAmount());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.ENGLISH);
			LocalDate date = LocalDate.parse(request.getDateOfPledging(), formatter);
			LocalDate date1 = LocalDate.parse(request.getProposedDateOfLoanClearance(), formatter);
			loan.setDateOfPledging(date);
			loan.setProposedDateOfLoanClearance(date1);
			loan.setBitDeletedFlag(false);
			loan.setIssueId(request.getIssueId());
			// **Convert String to BigDecimal for comparison**
			BigDecimal proposedAmount;
			try {
				proposedAmount = new BigDecimal(request.getProposedLoanAmount());
			} catch (NumberFormatException e) {
				log.error("Invalid proposed loan amount format: " + request.getProposedLoanAmount(), e);
				proposedAmount = BigDecimal.ZERO; // Default to 0 in case of error
			}

			// **Loan Status Logic**
			if (proposedAmount.compareTo(BigDecimal.valueOf(100000)) <= 0) {
				loan.setLoanStatus("Sanctioned");
			} else {
				loan.setLoanStatus("Rejected");
			}

			repository.updatePledgeStatus(request.getDepositorId(),request.getIssueId());
			
			 // Step 2: Update existing ActionHistory record
            IssuanceWarehouseReceiptActionHistory actionHistory = new IssuanceWarehouseReceiptActionHistory();
            actionHistory.setIntIssueanceWhId(request.getIssueId()); // or whatever field holds the PK
            actionHistory.setDepositorId(request.getDepositorId());
            actionHistory.setVchActionTakenBy(request.getUserName());
            actionHistory.setRoleId(request.getRoleId());
            actionHistory.setRoleName(request.getRoleName());
            actionHistory.setVchStatus("Pledging Applied");
            actionHistory.setUserId(request.getUserId());
            issuanceWarehouseReceiptActionHistoryRepository.save(actionHistory);
            
		} catch (JsonProcessingException e) {
			log.error("PledgingServiceImpl:saveLoan() ", e);
		}

		return repository.save(loan);
	}

	@Transactional
	@Override
	public JSONObject savePaymentData(String data) throws JsonProcessingException {
		JSONObject json = new JSONObject();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			DepositorPaymentDetailsDto paymentDto = om.readValue(data, DepositorPaymentDetailsDto.class);
			DepositorPaymentDetails paymentData = new DepositorPaymentDetails();

			paymentData.setPaymentType(paymentDto.getPaymentType());
			paymentData.setIntCreatedBy(paymentDto.getUserId());
			paymentData.setDepositorId(paymentDto.getDepositorId());
			// Payment Integration will start from here
			// for this time i am setting payment status as SUCCESS
			paymentData.setEnmPaymentStatus(PaymentStatus.UNPAID);
			DepositorPaymentDetails savedEntity = paymentRepository.save(paymentData);

			if (savedEntity.getEnmPaymentStatus() == PaymentStatus.UNPAID) {
				// call the procedure
				// String result = (String)
				// saveDataInAocMainTable(savedEntity.getIntCreatedBy());
				json.put(STATUS, 200);
				json.put("payid", savedEntity.getId());
				json.put("custId", savedEntity.getDepositorId());
				json.put("paymentStatus", PaymentStatus.UNPAID);
			} else {
				// payment failed.
				json.put(STATUS, 403);
				json.put("id", "Payment Failed");
			}

		} catch (Exception e) {
			json.put(ERROR, AN_UNEXPECTED_ERROR_OCCURRED + e.getMessage());
			json.put(STATUS, 500);
		}
		return json;
	}

	@Override
	public List<IssuanceWareHouseRecipt> getDepositorDetails(String emailId) {
		return issuanceWareHouseReciptRepository.findByEmail(emailId);
	}

	@Override
	public List<IssuanceWareHouseRecipt> getDepositorDetailsById(String depositorId,Integer id1) {
		return issuanceWareHouseReciptRepository.findByIdAndBitDeletedFlag(depositorId,id1, false);
	}

	@Override
	public LoanSanctionDTO getLoanSanctionDetails(Integer issueId, String loanStatus) {

		// Fetch depositor details based on depositorId
		Pledge loanDetails = repository.getDepositorDetailsByIssueId(issueId);

		if (loanDetails == null) {
			throw new IllegalArgumentException("No depositor details found for ID: " + issueId);
		}

		if ("SANCTIONED".equalsIgnoreCase(loanStatus)) {
			LocalDate proposedClearanceDate =loanDetails.getProposedDateOfLoanClearance();

		    // Ensure approved date of loan clearance is greater than proposed date (Example: +3 months)
		    LocalDate approvedClearanceDate = proposedClearanceDate.plusMonths(3);

		    // Ensure sanctioned amount is less than proposed amount (Example: 90% of proposed amount)
		    double proposedLoanAmount = loanDetails.getProposedLoanAmount();
		    double sanctionedLoanAmount = proposedLoanAmount * 0.9; // Adjust logic as needed

		    // Format approved date as "dd/MM/yyyy"
		    String formattedApprovedClearanceDate = approvedClearanceDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		    LocalDate dateOfPledging = loanDetails.getDateOfPledging();
		    long approvedDurationOfLoan = ChronoUnit.MONTHS.between(dateOfPledging, approvedClearanceDate);

			return new LoanSanctionDTO(sanctionedLoanAmount, // Dynamic: 90% of Proposed Loan Amount
					8.0, // Static: Interest Rate
					formattedApprovedClearanceDate, // Ensuring it's later than
					(int)approvedDurationOfLoan, // Static: Approved Duration of Loan (Months)
					loanDetails.getDateOfPledging(),
					proposedLoanAmount,
					loanDetails.getProposedDateOfLoanClearance(),
					"SANCTIONED",
					loanDetails.getDurationOfLoan());	
		} else if ("REJECTED".equalsIgnoreCase(loanStatus)) {
			return new LoanSanctionDTO("REJECTED", "Loan rejected due to insufficient credit score.",
					loanDetails.getDateOfPledging(), loanDetails.getProposedLoanAmount(),
					loanDetails.getProposedDateOfLoanClearance(), loanDetails.getDurationOfLoan());
		} else {
			throw new IllegalArgumentException("Invalid loan status provided.");
		}
	}

}
