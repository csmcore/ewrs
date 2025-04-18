package app.ewarehouse.dto;

import lombok.Data;

@Data
public class ApplicationConformityFormOneADto {
	private Long formOneADocId;
	private Long warehouseId;
	private String businessPlan;
	private String currentTaxComplianceCertificate;
	private String authorizedSignatoryCertificateOfGoodConduct;
	private String kraPinOfTheAuthorizedSignatory;
	private String nationalIdOfTheAuthorizedSignatory;
	private String insurancePolicy;
	private String validLicenseForTheWarehouse;
	private String certificateOfIncorporationRegistration;
	private String latestCR12;
	private String latestCertificateOfCompliance;
	private String companyId;
	private Integer userId;
}
