package app.ewarehouse.service;

import java.util.List;

public interface TradeDataService {

	List<Object[]> getTradeDataByCommodity(String commodityName);

}
