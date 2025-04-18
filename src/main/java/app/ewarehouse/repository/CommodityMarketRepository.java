package app.ewarehouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import app.ewarehouse.entity.CommodityMarket;
import jakarta.transaction.Transactional;

public interface CommodityMarketRepository extends JpaRepository<CommodityMarket, Long>{

	Optional<CommodityMarket> findByDepositorIdAndCommodityNameAndDate(
	        String depositorId, 
	        String commodityName, 
	        String date
	    );

	    Optional<CommodityMarket> findTopByDepositorIdAndCommodityNameOrderByDateDesc(
	        String depositorId, 
	        String commodityName
	    );
	    @Transactional
	    @Modifying
		@Query(value = """
				UPDATE t_issuance_ware_house_recipt_application SET tradeStatus=true 
				WHERE intDepositorWhOperator = :depositorId AND intIssueanceWhId=:issueId
				""", nativeQuery = true)
		void updateTradeStatus (String depositorId,Integer issueId);
}
