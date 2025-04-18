package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationOfConformityWarehouseOperatorViabilityDTO {
	private Integer userId;
	private Integer viabilityId;
	private Integer intParticularOfApplicantsId;
    private String companyRegistrationCertificatePath;
    private Boolean companyRegistrationCertificatePathFetchFromDb;
    private String businessPermitPath;
    private Boolean businessPermitPathFetchFromDb;
    private String businessStrategicPlanPath;
    private Boolean businessStrategicPlanPathFetchFromDb;
    private String leaseAgreementPath;
    private Boolean leaseAgreementPathFetchFromDb;
    private String titleDeedPath;
    private Boolean titleDeedPathFetchFromDb;
    private String assetRegister;
    private Boolean assetRegisterFetchFromDb;
    private String recommendationLettersPath;
    private Boolean recommendationLettersPathFetchFromDb;
    private String certificationPath;
    private Boolean certificationPathFetchFromDb;
    private String businessOpportunity;
    private Boolean businessOpportunityFetchFromDb;
    private String customerBasePath;
    private Boolean customerBasePathFetchFromDb;
    private String productionOfTargetAreasPath;
    private Boolean productionOfTargetAreasPathFetchFromDb;
    private String supplyVolumesPath;
    private Boolean supplyVolumesPathFetchFromDb;
    private String financialProjectionsPath;
    private Boolean financialProjectionsPathFetchFromDb;
    private String auditedFinancialReportPath;
    private Boolean auditedFinancialReportPathFetchFromDb;
    private String fireInsurancePolicyPath;
    private Boolean fireInsurancePolicyPathFetchFromDb;
    private String theftInsurancePolicyPath;
    private Boolean theftInsurancePolicyPathFetchFromDb;
    private String naturalCalamityInsurancePolicyPath;
    private Boolean naturalCalamityInsurancePolicyPathFetchFromDb;
    private String performanceBondPath;
    private Boolean performanceBondPathFetchFromDb;
    private String businessContinuityPath;
    private Boolean businessContinuityPathFetchFromDb;
    private Boolean draftStatus;
	
}
