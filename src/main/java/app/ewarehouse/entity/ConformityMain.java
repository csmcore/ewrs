package app.ewarehouse.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
@Data
@Entity
@Table(name = "t_confirmity_main")
public class ConformityMain {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intcnfId")
	private Integer id;

	@Column(name = "vchWareHouseId", length = 20)
	private String warehouseId;

	@ManyToOne
	@JoinColumn(name = "vchCompanyId", nullable = false)
	private AocCompProfileDetails company;

//    @Column(name = "intlocId")
//    private Integer locationId;

	@ManyToOne
	@JoinColumn(name = "intlocId", nullable = false)
	private ApplicationOfConformityLocationDetails warehouseLocation;

//    @Column(name = "intStorageId")
//    private Integer storageId;

	@ManyToOne
	@JoinColumn(name = "intStorageId", nullable = false)
	private ApplicationOfConformityCommodityStorage storage;

//    @Column(name = "intFormAId")
//    private Integer formAId;

	@ManyToOne
	@JoinColumn(name = "intFormAId", nullable = false)
	private ApplicationOfConformityFormOneA formOneA;

//    @Column(name = "intFormBId")
//    private Integer formBId;

	@ManyToOne
	@JoinColumn(name = "intFormBId", nullable = false)
	private ApplicationOfConformityFormOneB formOneB;

//    @Column(name = "intPaymentId")
//    private Integer paymentId;

	@ManyToOne
	@JoinColumn(name = "intPaymentId", nullable = false)
	private PaymentData payment;

	@Column(name = "intCreatedBy")
	private Integer createdBy;

	@CreationTimestamp
	@Column(name = "dtmCreatedOn")
	private LocalDateTime createdOn;

	@UpdateTimestamp
	@Column(name = "stmUpdatedOn", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private LocalDateTime updatedOn;

	@Column(name = "bitDeletedFlag")
	private Boolean deletedFlag = false;

	@Column(name = "intCreatedRoleId")
	private Integer createdRoleId;

	@Column(name = "intCertId")
	private Integer certId;

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

	@Column(name = "vchApproverrRemarks", length = 150)
	private String approverRemarks;

	@Enumerated(EnumType.STRING)
	@Column(name = "vchApproverStatus")
	private ApproverStatus approverStatus;

	@Column(name = "bitLicenseGen")
	private Boolean licenseGen;

	@Column(name = "bitLicenceCerti")
	private Boolean licenceCerti;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "enmApplicationStatus")
	private ApproverStatus applicationStatus;
	
	@Column(name = "bitIsEditable")
	private Boolean isEditable = true;
	
	@Column(name = "intApplicationStatus")
    private Integer intApplicationStatus;
	
	@Column(name = "bitOicOneCC")
    private Boolean oicOneCC = false;

    @Column(name = "bitOicOneInsp")
    private Boolean oicOneInsp = false;

    @Column(name = "bitOicTwoCC")
    private Boolean oicTwoCC = false;

    @Column(name = "bitOicTwoInsp")
    private Boolean oicTwoInsp = false;
    

    @Column(name = "bitOPLApplied")
    private Boolean bitOPLApplied = false;
    @Column(name = "intOPLAppId")
    private Integer intOPLAppId = 0;
    
    @Column(name = "vchRejectionRemarks")
    private String rejectionRemarks;
    
    @Column(name = "vchDeferCeoRemarks")
    private String deferCeoRemarks;
    
    @Transient
    private String certificateNo;
    
    @Transient
    private String applicantStatus;


}
