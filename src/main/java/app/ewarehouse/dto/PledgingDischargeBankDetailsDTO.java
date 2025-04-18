package app.ewarehouse.dto;

import lombok.Data;

@Data
public class PledgingDischargeBankDetailsDTO {

	private String pdBankDetailsId;
    private String pledgingDischargeReceiptId;
    private Integer userId;
    private String nameOfAccountHolder;
    private String accountNo;
    private String bankBranchName;
    private String swiftCodes;
}
