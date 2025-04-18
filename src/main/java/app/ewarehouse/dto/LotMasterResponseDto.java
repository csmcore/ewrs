package app.ewarehouse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LotMasterResponseDto {
	private Integer id;
	private String typeOfLot;
	private Integer noOfBags;
	private Integer kgPerBag;
}
