package app.ewarehouse.dto;

import lombok.Data;

@Data
public class ApplicationConformityFormOneBDto {
	private Long formOneBDocId;
	private Long warehouseId;
	private String strategicPlan;
	private String leaseAgreement;
	private String titleDeed;
	private String assetRegister;
	private String recommendationLetters;
	private String auditedFinancialReports;
	private String riskAnalysis;
	private String companyId;
	private Integer userId;
}
