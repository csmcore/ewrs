package app.ewarehouse.dto;

import app.ewarehouse.entity.Country;
import app.ewarehouse.entity.County;
import app.ewarehouse.entity.Status;
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
public class DepositoryDetailsDto {
	

	private String typeOfUser;

	private String companyName;
	private String companyId;

	private String wareHouseName;
	private String wareHouseId;

	private String typeOfEntity;

	private String typeOfDepositor;

	private String nameOfDepositor;

	private String mobileNumber;

	private String emailAddress;

	private String postalCode;

	private String postalAddress;
	
	private String depositorId;
	
	private String countyName;
	
	private String subCountyName;
	
	private String ward;
	
	private Status status;
	
	private String nationality;
	
	private String nationalId;
	
	private String alienId;
	
	private String passportNumber;
	
	private String entityRegistrationNumber;
	


}
