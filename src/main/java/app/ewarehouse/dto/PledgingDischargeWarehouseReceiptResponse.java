package app.ewarehouse.dto;

import app.ewarehouse.entity.Bank;
import app.ewarehouse.entity.EmploymentDetails;
import app.ewarehouse.entity.MLoanPurpose;
import app.ewarehouse.entity.MScheme;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PledgingDischargeWarehouseReceiptResponse {
    private String pledgingDischargeId;
    private BuyerDepositorResponse buyer;
    private WarehouseReceiptResponse warehouseReceiptResponse;
    private MLoanPurpose loanPurpose;
    private Bank bank;
    private MScheme scheme;
    private BigDecimal proposedCreditAmount;
    private String tenureOfLoanRepayment;
    private String area;
    private String plotNo;
    private String streetName;
    private String lengthOfStayAtPresentAddress;
    private String nearestMarket;
    private String currentPoliceStation;
    private Integer yearsOfStay;
    private String gender;
    private String maritalStatus;
    private String religion;
    private String isEmployed;
//    private String payrollNo;
//    private String nameOfCurrentEmployer;
//    private Integer noOfYearsWithEmployer;
//    private BigDecimal currentSalaryPM;
//    private BigDecimal monthlyExpenditure;
//    private String terms;
//    private String workPhysicalAddress;
//    private String telephoneOffice;
//    private String position;
//    private String contract;
//    private String selfEmployed;
//    private String department;
//    private String employmentTerms;
//    private String employerEmail;
    private EmploymentDetails employmentDetails;
    private String nameOfAccountHolder;
    private String accountNo;
    private String bankBranchName;
    private String swiftCodes;
    private String bankStatementUpload;
    private String passportPhotoUpload;
    private String latestIdOrPassportUpload;
    private String addressProof;
    private String warehouseReceiptUpload;
    private String pinCertificate;
}
