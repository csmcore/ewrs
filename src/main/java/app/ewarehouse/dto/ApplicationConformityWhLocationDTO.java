package app.ewarehouse.dto;

import lombok.Data;

@Data
public class ApplicationConformityWhLocationDTO {

	private Long whLocationId;
    private String warehouseName;
    private String warehouseOperatorName;
    private String email;
    private String mobileNumber;
    private String lrNumber;
    private Integer county;
    private Integer subCounty;
    private Integer ward;
    private String longitude;
    private String latitude;
    private String streetName;
    private String building;
    private String policyNumber;
    private Integer userId;
    private String companyId;
	
}
