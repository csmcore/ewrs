package app.ewarehouse.dto;

import java.util.List;

import lombok.Data;

@Data
public class AppliationConformityCommodityDto {

	private List<WarehouseStorageDTO> commodityStorageDetails;
    private Integer userId;
    private String companyId;
	
}

