package app.ewarehouse.dto;

import app.ewarehouse.entity.EmploymentDetails;
import lombok.Data;

@Data
public class PledgingDischargeWarehouseReceiptRequest {
    private String pledgingDischargeReceiptId;
    private String depositorId;
    private String wareHouseReceiptNo;
    private Integer loanPurposeId;
    private Integer bankId;
    private Integer schemeId;
    private String proposedCreditAmount;
    private String tenureOfLoanRepayment;
    private String area;
    private String plotNo;
    private String streetName;
    private String lengthOfStayAtPresentAddress;
    private String nearestMarket;
    private String currentPoliceStation;
    private Integer yearsOfStay;
    private String gender;
    private String contract;
    private String selfEmployed;
    private String maritalStatus;
    private String religion;
    private String isEmployed;
    private EmploymentDetails employmentDetails;
    private String nameOfAccountHolder;
    private String accountNo;
    private String bankBranchName;
    private String swiftCodes;
    private String bankStatement;
    private String passportSizePhoto;
    private String latestIDOrPassport;
    private String addressProof;
    private String warehouseReceipt;
    private String pinCertificate;
}
