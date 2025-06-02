package app.ewarehouse.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class LoanSanctionDTO {

	private Double sanctionedLoanAmount;
	private Double interestRate;
	private String approvedDateOfLoanClearance;
	private Integer approvedDurationOfLoan;
	private String financialInstitution;
	private LocalDate dateOfPledging;
	private Double proposedLoanAmount;
	private LocalDate proposedDateOfLoanClearance;
	private Integer durationOfLoan;
	private String loanStatus;
	private String rejectionReason;

	public LoanSanctionDTO(Double sanctionedLoanAmount, Double interestRate, String approvedDateOfLoanClearance,
			Integer approvedDurationOfLoan,LocalDate dateOfPledging,
			Double proposedLoanAmount, LocalDate proposedDateOfLoanClearance, String loanStatus,Integer durationOfLoan) {
		this.sanctionedLoanAmount = sanctionedLoanAmount;
		this.interestRate = interestRate;
		this.approvedDateOfLoanClearance = approvedDateOfLoanClearance;
		this.approvedDurationOfLoan = approvedDurationOfLoan;
		this.dateOfPledging = dateOfPledging;
		this.proposedLoanAmount = proposedLoanAmount;
		this.proposedDateOfLoanClearance = proposedDateOfLoanClearance;
		this.loanStatus = loanStatus;
		this.rejectionReason = null; // No rejection reason for approved loans
		this.durationOfLoan=durationOfLoan;
	}

// Constructor for Rejected Loans
	public LoanSanctionDTO(String loanStatus, String rejectionReason,
			LocalDate dateOfPledging, Double proposedLoanAmount, LocalDate proposedDateOfLoanClearance,Integer durationOfLoan) {
		this.loanStatus = loanStatus;
		this.rejectionReason = rejectionReason;
		this.dateOfPledging = dateOfPledging;
		this.proposedLoanAmount = proposedLoanAmount;
		this.proposedDateOfLoanClearance = proposedDateOfLoanClearance;
		this.sanctionedLoanAmount = null;
		this.interestRate = null;
		this.approvedDateOfLoanClearance = null;
		this.approvedDurationOfLoan = null;
		this.durationOfLoan=durationOfLoan;
	}

	
}
