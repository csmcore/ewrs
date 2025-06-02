package app.ewarehouse.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Data
@Table(name = "t_warehouse_structure_applicant")
@DynamicUpdate
public class WarehouseApplicant implements Serializable {

    @Id
    @GeneratedValue(generator = "warehouse-applicant-custom-id")
    @GenericGenerator(
            name = "warehouse-applicant-custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_warehouse_structure_applicant"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "vchApplicantId")
            })
    @Column(name = "vchApplicantId")
    private String vchApplicantId;

    @NotBlank(message = "First name is mandatory")
    @Column(name = "vchFirstName")
    private String vchFirstName;

    @NotBlank(message = "Last name is mandatory")
    @Column(name = "vchLastName")
    private String vchLastName;

    @NotBlank(message = "Full name is mandatory")
    @Column(name = "vchMiddleName")
    private String vchMiddleName;

    @NotBlank(message = "Passport number is mandatory")
    @Column(name = "vchPassportNo")
    private String vchPassportNo;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^0\\d{9}$", message = "Telephone number is invalid")
    @Column(name = "vchPhoneNo")
    private String vchPhoneNo;

    @ManyToOne(cascade = CascadeType.ALL)  // Add cascade here
    @JoinColumn(name = "intCompanyId")
    private Company company;

    @ManyToOne(cascade = CascadeType.ALL)  // Add cascade here
    @JoinColumn(name = "intWareHouseParticularsId")
    private WarehouseParticulars warehouseParticulars;

    @Email(message = "Email address is invalid")
    @Column(name = "vchEmail")
    private String vchEmail;

    @NotBlank(message = "Payment method is mandatory")
    @Column(name = "vchPaymentMethod")
    private String vchPaymentMethod;

    @Column(name = "bitDeclaration")
    private Boolean bitDeclaration;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmStatus")
    private Status enmStatus = Status.Pending;

    @Column(name = "intVerifiedById")
    private Integer intVerifiedById;

    @Column(name = "vchVerificationRemark")
    private String vchVerificationRemark;

    @Column(name = "dtmCreatedOn")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date dtmCreatedOn;

    @Column(name = "stmUpdatedOn")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date stmUpdatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @Column(name = "intApprovedBy")
    private Integer intApprovedBy;
    
    @Column(name = "vchFinanceRemarks", length = 200)
    private String financeRemarks;

    @Column(name = "vchVerificationRemarks", length = 200)
    private String verificationRemarks;

    @ManyToOne
    @JoinColumn(name = "intInspectorId", referencedColumnName = "id")
    private Inspector inspector;

    @Column(name = "vchManagerRcRemarks", length = 200)
    private String managerRcRemarks;

    @Column(name = "vchUploadInspectionPlan", length = 255)
    private String uploadInspectionPlan;

    @Column(name = "dtmDateTimeInspection")
    private LocalDateTime dateTimeInspection;

    @Column(name = "vchInspectorRemarks", length = 200)
    private String inspectorRemarks;

    @Column(name = "vchManagerRccRemarks", length = 200)
    private String managerRccRemarks;

    @Column(name = "vchFoodCropsRemarks", length = 200)
    private String foodCropsRemarks;
}
