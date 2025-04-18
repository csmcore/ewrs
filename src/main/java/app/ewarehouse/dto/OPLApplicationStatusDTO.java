package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor  
@Builder
public class OPLApplicationStatusDTO {
       
	    private Integer intLicenceSno;
	    private String vchOPlId;
	    private String vchWarehouseName;
	    private String vchWhOperatorName;
	    private String vchEmail;
	    private String vchMobileNumber;
	    private String vchLrNumber;
	    private String countyName;
	    private String vchSubCountyName;
	    private String wardName;
	    private String userName;
	    private String vchFormOneCId;
	    private Integer paymentId;
	    private String enmPaymentStatus;
	    private String vchApplicationStatus;
	    private String vchApprovalStatus;
	    private String vchWareHouseId;
	    private String vchCompanyId;
	    private Boolean bitLicenseGen;
	    private Boolean bitLicenceCertGen;
	    private String vchApproverRemarks;
	    private String vchOplicenceId;
	    private String amountExpected;
	    private String currency;
	    private String vchStreetName;
	    private String vchBuilding;
	    private String vchCertId;
	    private Boolean bitCertGen;
	
}
