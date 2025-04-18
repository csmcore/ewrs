package app.ewarehouse.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import app.ewarehouse.entity.BuyerDepositorAndWareHouseOperator;
import app.ewarehouse.entity.Commodity;
import app.ewarehouse.entity.CommodityMarket;

public interface CommodityService {
	
	Integer save(String commodityRequest);

    List<Commodity> getAll();

    Commodity getById(Integer id);

    Integer update(Integer id, String updatedCommodity);

//    boolean delete(Integer id) throws Exception;
    
    void delete(String data);

    Page<Commodity> getAllCommoditiesList(Pageable pageable, String search);

    Page<Commodity> allCommoditiesList(Integer pageNumber, Integer pageSize, String sortCol, String sortDir, String search);

	CommodityMarket getMarketData(String depositorId, String commodityName, String date);

	CommodityMarket updateSellingPrice(String request);

	List<BuyerDepositorAndWareHouseOperator> getDepositorDetails();

	
}
