package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationOfConformitySupportingDocumentsDTO {
	private Integer userId;
	private Integer supportDocId;
	private Integer intParticularOfApplicantsId;
	private String businessPlanPath;
	private Boolean businessPlanPathFetchFromDb;
	private String taxComplianceCertificatePath;
	private Boolean taxComplianceCertificatePathFetchFromDb;
	private String goodConductCertificatePath;
	private Boolean goodConductCertificatePathFetchFromDb;
	private String identificationProofPath;
	private Boolean identificationProofPathFetchFromDb;
	private String insurancePolicyPath;
	private Boolean insurancePolicyPathFetchFromDb;
	private String licenseForTheWarehousePath;
	private Boolean licenseForTheWarehousePathFetchFromDb;
	private String certificateOfRegistrationPath;
	private Boolean certificateOfRegistrationPathFetchFromDb;
	private Boolean draftStatus;
}
