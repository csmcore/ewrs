package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Date;

@Data
@Entity
@Table(name = "t_application_of_conformity_supporting_documents")
public class ApplicationOfConformitySupportingDocuments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intSupportingDocumentId")
	private Integer id;

	@Column(name = "vchBusinessPlanPath")
	private String businessPlanPath;

	@Column(name = "vchTaxComplianceCertificatePath")
	private String taxComplianceCertificatePath;

	@Column(name = "vchGoodConductCertificatePath")
	private String goodConductCertificatePath;

	@Column(name = "vchIdentificationProofPath")
	private String identificationProofPath;

	@Column(name = "vchInsurancePolicyPath")
	private String insurancePolicyPath;

	@Column(name = "vchLicenseForTheWarehousePath")
	private String licenseForTheWarehousePath;

	@Column(name = "vchCertificateofRegistrationPath")
	private String certificateOfRegistrationPath;

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
