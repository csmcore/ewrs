package app.ewarehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PledgeDischargeResidentialDTO {
	
	private String vchPdResidentialId;
	
	@NotNull
	private String pledgingDischargeReceiptId;
	
	private Integer userId;
	
	@NotNull
    private String area;
    
    @NotNull
    private String plotNo;
    
    @NotNull
    private String streetName;
    
    @NotNull
    private String lengthOfStayAtPresentAddress;
    
    @NotNull
    private String nearestMarket;
    
    @NotNull
    private String currentPoliceStation;
    
    @NotNull
    private String yearsOfStay;
    
    @NotNull
    private String gender;
    
    @NotNull
    private String maritalStatus;
    
    @NotNull
    private String religion;
    
    @NotNull
    private String isEmployed;
    
    private PledgeDischargeEmploymentDetailsDTO employmentDetails;
}
