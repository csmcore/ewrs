package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_application_of_certificate_of_compliance")
public class TapplicationOfCertificateOfCompliance {

    @Id
    @GeneratedValue(generator = "application-compliance-custom-id")
    @GenericGenerator(
            name = "application-compliance-custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_application_of_certificate_of_compliance"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "vchapplicationId")
            })
    @Column(name = "vchapplicationId")
    private String txtApplicationId;

    @Column(name = "vchFullName")
    private String txtFullName;

    @Column(name = "vchAddress")
    private String txtAddress;

    @Column(name = "vchPostalCode")
    private String txtPostalCode;

    @Column(name = "vchTown")
    private String txtTown;

    @Column(name = "vchTelephoneNumber")
    private String txtTelephoneNumber;

    @Column(name = "vchMobilePhoneNumber")
    private String txtMobilePhoneNumber;

    @Column(name = "vchEmail")
    private String txtEmail;

    @Column(name = "vchWebsite")
    private String txtWebsite;

    @Column(name = "vchEntityType")
    private String txtEntityType;

    @Column(name = "vchOtherEntityType")
    private String txtOtherEntityType;

    @Column(name = "vchCompanyRegistrationNumber")
    private String txtCompanyRegistrationNumber;

    @Column(name = "vchKraPin")
    private String txtKraPin;

    @Column(name = "vchDirectorIDs")
    private String txtDirectorIDs;

    @Column(name = "vchDirectorIDsName")
    private String txtDirectorIDsName;

    @Column(name = "vchDirectorsPassportsName")
    private String txtDirectorsPassportsName;

    @Column(name = "vchDirectorsPassports")
    private String txtDirectorsPassports;

    @Column(name = "vchPhysicalAddress")
    private String txtPhysicalAddress;

    @ManyToOne
    @JoinColumn(name = "intSubCountyId")
    private SubCounty subCounty;

    @Column(name = "vchPhysicalTown")
    private String txtPhysicalTown;

    @Column(name = "vchStreetName")
    private String txtStreetName;

    @Column(name = "vchpaymentMode")
    private String txtPaymentMode;

    @Column(name = "is_declared")
    private Boolean isDeclared;

    @Column(name="intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private int intUpdatedBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private Date dtmCreatedOn;

    @Column(name = "stmUpdatedOn")
    @UpdateTimestamp
    private Date stmUpdatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private draftStatus status;

    @Column(name = "intCurrentRole")
    private Integer intCurrentRole;

    @Column(name = "vchOicLegalRemark")
    private String vchOicLegalRemark;

    @Column(name = "vchOicFinRemark")
    private String vchOicFinRemark;

    @Column(name = "vchOicLegalTwoRemark")
    private String vchOicLegalTwoRemark;

    @ManyToOne
    @JoinColumn(name = "intInspectorId")
    private Tuser inspector;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intInspectionDetailsId")
    private TApplicationComplianceInspection inspectionDetails;

    @Column(name = "vchOicLegalThreeRemark")
    private String vchOicLegalThreeRemark;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intApprovalDetails")
    private TApplicationComplianceApprover aproverDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intCeoApprovalDetails")
    private TApplicationComplianceCeo ceoApprovalDetails;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmApprovalStatus")
    private Status enmApprovalStatus = Status.Pending;

    @Column(name = "intApprovalStage")
    private Integer intApprovalStage;

    @Column(name = "vchTransactionNo")
    private String vchTransactionNo;

    @Column(name = "vchChallanNo")
    private String vchChallanNo;    

    @Column(name = "bitPaymentSuccess")
    private Boolean bitPaymentSuccess;

    @Enumerated(EnumType.STRING)
    @Column(name = "collateralManagerStatus")
    private CollateralManagerStatus collateralManagerStatus = CollateralManagerStatus.NoObjection ;

    @Column(name = "timesSuspended")
    private Integer timesSuspended = 0;
    
    @Column(name = "intAdminUserId")
    private Integer intAdminUserId;
    

}

