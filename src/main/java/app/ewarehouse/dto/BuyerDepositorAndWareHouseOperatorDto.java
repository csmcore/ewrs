package app.ewarehouse.dto;

import app.ewarehouse.entity.Country;
import app.ewarehouse.entity.County;
import app.ewarehouse.entity.SubCounty;
import app.ewarehouse.entity.Ward;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyerDepositorAndWareHouseOperatorDto {
	
	private String intDepositorWhOperator;

	private String typeOfUser;

	private String companyName;

	private String wareHouseName;

	private String entityType;

	private String depositorType;

	private String name;

	private String mobileNumber;

	private String email;

	private String postalCode;

	private String postalAddress;
	
	private Integer county;
	
	private Integer subCounty;
	
	private Integer ward;

	private Integer nationality;

	private String nationalId;

	private String entityRegistrationNumber;

	private String passportNumber;

	private String alienId;

	private Integer userId;

	private WarehouseDetailsDTO WarehouseDetailsDTO;
	
	//private String vchWareHouseId;
	
	private Integer registerBy;
	
	private String govtIssuedId;
	
	private String vchIdNo;
	
	//private String wareHouseId;
	
	//private String applicationId;
	//private String companyId;
	
	//private String wareHouseId;
	
	
	    
	   
}
