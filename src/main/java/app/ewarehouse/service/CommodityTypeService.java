package app.ewarehouse.service;

import app.ewarehouse.entity.CommodityType;

import java.util.List;

public interface CommodityTypeService {
	
	boolean save(CommodityType commodityType);
	
	List<CommodityType> getAll();
	
	CommodityType getById(Integer id);


}
