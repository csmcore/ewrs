package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "t_application_of_conformity_warehouse_operator_viability")
public class ApplicationOfConformityWarehouseOperatorViability {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intViabilityId")
    private Integer id;

    @Column(name = "vchCompanyRegistrationCertificatePath")
    private String companyRegistrationCertificatePath;

    @Column(name = "vchBusinessPermitPath")
    private String businessPermitPath;

    @Column(name = "vchBusinessStrategicPlanPath")
    private String businessStrategicPlanPath;

    @Column(name = "vchLeaseAgreementPath")
    private String leaseAgreementPath;

    @Column(name = "vchTitleDeedPath")
    private String titleDeedPath;

    @Column(name = "vchAssetRegister")
    private String assetRegister;

    @Column(name = "vchRecommendationLettersPath")
    private String recommendationLettersPath;

    @Column(name = "vchCertificationPath")
    private String certificationPath;

    @Column(name = "vchBusinessOpportunity")
    private String businessOpportunity;

    @Column(name = "vchCustomerBasePath")
    private String customerBasePath;

    @Column(name = "vchProductionOfTargetAreasPath")
    private String productionOfTargetAreasPath;

    @Column(name = "vchSupplyVolumesPath")
    private String supplyVolumesPath;

    @Column(name = "vchFinancialProjectionsPath")
    private String financialProjectionsPath;

    @Column(name = "vchAuditedFinancialReportPath")
    private String auditedFinancialReportPath;

    @Column(name = "vchFireInsurancePolicyPath")
    private String fireInsurancePolicyPath;

    @Column(name = "vchTheftInsurancePolicyPath")
    private String theftInsurancePolicyPath;

    @Column(name = "vchNaturalCalamityInsurancePolicyPath")
    private String naturalCalamityInsurancePolicyPath;

    @Column(name = "vchPerformanceBondPath")
    private String performanceBondPath;

    @Column(name = "vchBusinessContinuityPath")
    private String businessContinuityPath;

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
	
	@Column(name = "bitDraftStatusFlag")
	private Boolean bitDraftStatusFlag = false;
	
	@ManyToOne
    @JoinColumn(name = "intParticularOfApplicantsId" , referencedColumnName = "intParticularOfApplicantsId")
    private ApplicationOfConformityParticularOfApplicants particularOfApplicants;
	
}
