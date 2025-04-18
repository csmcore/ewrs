package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.CommodityTradeData;

@Repository
public interface TradeDataRepository extends JpaRepository<CommodityTradeData, Long>{

	@Query(value = """
	        SELECT 
	            timestamp, 
	            open, high, low, close 
	        FROM t_trade_data 
	        WHERE commodity_name = :commodityName
	        """, nativeQuery = true)
	    List<Object[]> findByCommodity(String commodityName);
}
