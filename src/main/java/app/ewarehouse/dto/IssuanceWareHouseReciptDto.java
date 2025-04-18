package app.ewarehouse.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssuanceWareHouseReciptDto {
 
 
	//private Integer issuanceWhId;
    
	private String intDepositorWhOperator;
    
	//private String commodityName;
	
	private Integer commodityType; 
	
	private String commodityOrigin;
 
	private String commodityCode;
 
	private Double originalQuantity;
 
	private Double originalGrossWeight;
 
	private Double originalNetWeight;
 
	private Double currentQuantity;
 
	private Double currentNetWeight;
	
	
	private Integer cropYear;
 
	private String grade;
 
	private String lotNumber;
 
	private String qualityInspectionPath;
 
	private String weighingTicketPath;
 
	private String grnPath;
	
	private List<IssuaneWareHouseOperatorChargesDto> wareHouseChargesOperation;
	private Integer userId;
	
	private String datePicker;
	
	private String wareHouseId;
	
	private Double grandTotal;
 
}
