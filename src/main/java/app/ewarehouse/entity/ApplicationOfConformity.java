package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
@Table(name = "t_application_of_conformity_main")
public class ApplicationOfConformity {
	@Id
	@GeneratedValue(generator = "custom-id")
	@GenericGenerator(name = "custom-id", type = app.ewarehouse.util.CustomIdGenerator.class, parameters = {
			@org.hibernate.annotations.Parameter(name = "tableName", value = "t_application_of_conformity_main"),
			@org.hibernate.annotations.Parameter(name = "idName", value = "vchApplicationId") })
	@Column(name = "vchApplicationId", nullable = false, length = 10)
	private String applicationId;
	
	@ManyToOne
    @JoinColumn(name = "intParticularOfApplicantsId", nullable = false)
	private ApplicationOfConformityParticularOfApplicants particularOfApplicantsId;
	
	@ManyToOne
    @JoinColumn(name = "intSupportingDocumentId", nullable = false)
	private ApplicationOfConformitySupportingDocuments supportingDocumentId;
	
	@ManyToOne
    @JoinColumn(name = "intViabilityId", nullable = false)
	private ApplicationOfConformityWarehouseOperatorViability viabilityId;

	@ManyToOne
    @JoinColumn(name = "intPaymentId", nullable = false)
	private ApplicationOfConformityPaymentDetails paymentId;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "enmStatus")
    private Status enmStatus = Status.Pending;
	
	    @Column(name = "Role")
	    private Integer role;

    @ManyToOne
    @JoinColumn(name = "vchProfDetId")
    private AocCompProfileDetails profDet;

	@Column(name = "intCreatedBy")
	private Integer intCreatedBy;
	@Column(name = "intUpdatedBy")
	private Integer intUpdatedBy;
	@Column(name = "dtmCreatedOn")
	@CreationTimestamp
	private Date dtmCreatedOn;
	@Column(name = "stmUpdatedOn")
	@UpdateTimestamp
	private Date stmUpdatedOn;
	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag = false;
	
	@Column(name = "dtDateOfApplication")
	private LocalDate dtDateOfApplication;

	private String vchOicRemarks;
	
	@Column(name = "intOicUserId")
    private Integer oicUserId;

    @Column(name = "intOicRoleId")
    private Integer oicRoleId;
    
    private LocalDate dtDateOfInspection;
    private LocalTime tmTimeOfInspection;
    private String vchDescriptionOfInspection;
    private String vchInspectionReport;
    private String vchInspectorRemarks;
    
    @Column(name = "intInspectorUserId")
    private Integer inspectorUserId;

    @Column(name = "intInspectorRoleId")
    private Integer inspectorRoleId;
    
    
    private String vchOicFinRemarks;
    
    
    @Column(name = "intOicFinUserId")
    private Integer oicFinUserId;

    @Column(name = "intOicFinRoleId")
    private Integer oicFinRoleId;
    
    
    @Column(name = "vchMomFilePath", length = 255)
    private String momFilePath;

    @Column(name = "dtDate")
    private LocalDate date;

    @Column(name = "tmTime")
    private LocalTime time;
    
    private String vchApproverRemarks;
    
    @Column(name = "intApproverUserId")
    private Integer approverUserId;

    @Column(name = "intApproverRoleId")
    private Integer approverRoleId;
    
    private String vchCeoRemarks;
    
    @Column(name = "intCeoUserId")
    private Integer ceoUserId;

    @Column(name = "intCeoRoleId")
    private Integer ceoRoleId;
    
    @Column(name = "dtDateOfIssue")
	private LocalDate dtDateOfIssue;
    
    @Column(name = "isRequiredInspection")
    private Boolean isRequiredInspection = true;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "currentLevel")
    private AocLevels currentLevel = AocLevels.OIC;
    
    @Column(name = "pendingAt")
    private Integer pendingAt;
    
    @Column(name = "bitCertGenerate")
    private Boolean bitCertGenerate = false;
}
