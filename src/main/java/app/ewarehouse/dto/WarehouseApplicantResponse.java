package app.ewarehouse.dto;

import app.ewarehouse.entity.Status;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class WarehouseApplicantResponse {
    private String vchApplicantId;
    private String vchFirstName;
    private String vchLastName;
    private String vchMiddleName;
    private String vchPassportNo;
    private String vchPhoneNo;
    private CompanyResponse company;
    private WarehouseParticularsResponse warehouseParticulars;
    private String vchEmail;
    private String vchPaymentMethod;
    private Boolean bitDeclaration;
    private Status enmStatus;
    private Integer intVerifiedById;
    private String vchVerificationRemark;
    private Date dtmCreatedOn;
    private Date stmUpdatedOn;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Integer intApprovedBy;
    private String vchFinanceRemarks;
    private String vchVerificationRemarks;
    private InspectorDTO inspector;
    private String vchManagerRcRemarks;
    private String vchUploadInspectionPlan;
    private Date dtmDateTimeInspection;
    private String vchInspectorRemarks;
    private String vchManagerRccRemarks;
    private String vchFoodCropsRemarks;
}
