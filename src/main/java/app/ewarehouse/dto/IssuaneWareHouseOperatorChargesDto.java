package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssuaneWareHouseOperatorChargesDto {
 
 
	//private Integer intIssueOperatorId;
    
	
	private Integer intIssueanceWhId;
    
	private Integer chargeHeaders;
    
	
	private Integer unitType;
 
	private Double unitCharge;
 
	private Double quantity;
 
	private Double totalCharge;
	
	private Integer commodityName;
	
	private Double grandTotal;
	
	//private Integer userId;
 
	
 
 
}
