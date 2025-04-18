package app.ewarehouse.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name = "t_trade_data")
@Data
public class CommodityTradeData {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private LocalDate timestamp;
	    private String open;
	    private String high;
	    private String low;
	    private String close;
	    private String commodityName;
	    
	    private Double marketPrice;  // Calculated
	    private Double marketValue;  // Calculated
}
