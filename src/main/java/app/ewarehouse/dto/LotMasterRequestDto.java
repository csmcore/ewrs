package app.ewarehouse.dto;

import lombok.Data;

@Data
public class LotMasterRequestDto {

	private Integer id;
	private String typeOfLot;
	private Integer noOfBags;
	private Integer kgPerBag;
}
