package app.ewarehouse.dto;

import java.util.List;

import lombok.Data;

@Data
public class WarehouseStorageDTO {

	private Long warehouseId;
    private Integer storageCapacity;
    private List<CommodityDetailDTO> commodityDetails;
}
