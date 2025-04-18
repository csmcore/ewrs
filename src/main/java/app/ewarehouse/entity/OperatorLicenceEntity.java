package app.ewarehouse.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "t_operator_licence_application") // Change this to your actual table name
@Data
public class OperatorLicenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    @Column(name = "intLicenceSno")
    private Integer licenceSno;

    @Column(name = "vchWareHouseId", length = 50, nullable = false)
    private String warehouseId;

    @Column(name = "vchCompanyId", length = 50, nullable = false)
    private String companyId;

    @Column(name = "intConfirmityId")
    private Integer confirmityId;

    @Column(name = "vchFormOneCId", length = 50)
    private String formOneCId;

    @Column(name = "intPaymentId")
    private Integer paymentId;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "dtmCreatedOn")
    private LocalDateTime createdOn;

    @Column(name = "stmUpdatedOn")
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag;

    @Column(name = "intCreatedRoleId")  
    private Integer createdRoleId;

    @Column(name = "vchCertId")
    private String certId;

    @Column(name = "bitCertGen")
    private Boolean certGen;

    @Column(name = "dtCertIssue")
    private LocalDate certIssueDate;

    @Column(name = "dtCnfApproval")
    private LocalDate cnfApprovalDate;

    @Column(name = "intApprovedBy")
    private Integer approvedBy;

    @Column(name = "intApprovedByRole")
    private Integer approvedByRole;

    @Column(name = "vchApproverRemarks", length = 255)
    private String approverRemarks;
    @Enumerated(EnumType.STRING)
    @Column(name = "vchApprovalStatus")
    private OperatorLicenceStatus approvalStatus;

    @Column(name = "bitLicenseGen")
    private Boolean licenseGen;

    @Column(name = "bitLicenceCertGen")
    private Boolean licenceCertGen;

    @Column(name = "vchOPlId", length = 50)
    private String oplId;

    @Column(name = "vchPaymentStatus", length = 50)
    private String paymentStatus;

    @Column(name = "vchApplicationStatus", length = 50)
    private String applicationStatus;
    @Column(name = "vchFormStatus", length = 50)
    private String formStatus;
}






