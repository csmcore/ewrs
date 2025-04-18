package app.ewarehouse.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
//@AllArgsConstructor
@NoArgsConstructor
public class PledgeDTO {
    private Long id;
    private String depositorId;
    private String financialInstitution;
    private LocalDate dateOfPledging;
    private Double proposedLoanAmount;
    private LocalDate proposedDateOfLoanClearance;
    private String loanStatus;
    private Integer durationOfLoan;
    private Integer intCreatedBy;
    private Boolean tfBitDeletedFlag;
    private Boolean issBitDeletedFlag;

    public PledgeDTO(Long id, String depositorId, String financialInstitution, LocalDate dateOfPledging, 
                     Double proposedLoanAmount, LocalDate proposedDateOfLoanClearance, String loanStatus,
                     Integer durationOfLoan, Integer intCreatedBy, Boolean tfBitDeletedFlag, Boolean issBitDeletedFlag) {
        this.id = id;
        this.depositorId = depositorId;
        this.financialInstitution = financialInstitution;
        this.dateOfPledging = dateOfPledging;
        this.proposedLoanAmount = proposedLoanAmount;
        this.proposedDateOfLoanClearance = proposedDateOfLoanClearance;
        this.loanStatus = loanStatus;
        this.durationOfLoan = durationOfLoan;
        this.intCreatedBy = intCreatedBy;
        this.tfBitDeletedFlag = tfBitDeletedFlag;
        this.issBitDeletedFlag = issBitDeletedFlag;
    }
}

