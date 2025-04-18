package app.ewarehouse.dto;

import lombok.Data;

@Data
public class CommodityRequest {
	private String name;
	private Double charges;
	private String typeId;
	private String seasonId;
}
