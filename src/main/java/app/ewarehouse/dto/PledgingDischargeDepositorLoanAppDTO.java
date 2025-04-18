package app.ewarehouse.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PledgingDischargeDepositorLoanAppDTO {
    
	private String pdLoanAppId;
    private Integer bankId;
    private Integer loanPurposeId;
    private String pledgingDischargeReceiptId;
    private BigDecimal proposedCreditAmount;
    private Integer schemeId;
    private String tenureOfLoanRepayment;
    private Integer userId;
}
