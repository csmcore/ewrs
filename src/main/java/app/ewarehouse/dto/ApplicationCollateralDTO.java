package app.ewarehouse.dto;

import app.ewarehouse.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationCollateralDTO {
    private String txtApplicationId;
    private String txtFullName;
    private String txtAddress;
    private String txtPostalCode;
    private String txtTown;
    private String txtTelephoneNumber;
    private String txtMobilePhoneNumber;
    private String txtEmail;
    private String txtWebsite;
    private String txtEntityType;
    private String txtOtherEntityType;
    private String txtCompanyRegistrationNumber;
    private String txtKraPin;
    private String txtDirectorIDs;
    private String txtDirectorIDsName;
    private String txtDirectorsPassportsName;
    private String txtDirectorsPassports;
    private String txtPhysicalAddress;
    private SubCountyResponse subCounty;
    private String txtPhysicalTown;
    private String txtStreetName;
    private String txtPaymentMode;
    private Boolean isDeclared;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Date dtmCreatedOn;
    private Date stmUpdatedOn;
    private Boolean bitDeletedFlag;
    private draftStatus status;
    private Integer intCurrentRole;
    private String vchOicLegalRemark;
    private String vchOicFinRemark;
    private String vchOicLegalTwoRemark;
    private UserRoleIdResponse inspector;
    private TApplicationComplianceInspection inspectionDetails;
    private String vchOicLegalThreeRemark;
    private TApplicationComplianceApprover aproverDetails;
    private TApplicationComplianceCeo ceoApprovalDetails;
    private Status enmApprovalStatus;
    private Integer intApprovalStage;
    private String vchTransactionNo;
    private String vchChallanNo;
    private Boolean bitPaymentSuccess;
    private CollateralManagerStatus collateralManagerStatus;
    private Integer timesSuspended;
    private Integer experience;
    private String Remark;
}
