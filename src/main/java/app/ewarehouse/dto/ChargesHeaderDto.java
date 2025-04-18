package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargesHeaderDto {
	
    private Integer intChargesHeader;
    
	private String vchChargesHeader;
	
	
	private Integer intUnitType;


}
