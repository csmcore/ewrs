package app.ewarehouse.master.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCommoditySellingPrice {
	private String commodityName;
    private String depositorId;  // Keep it String if it's not an Integer
    private String marketPrice;
    private String marketValue;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String closingPrice;
    private String date;
    private String sellingPriceLowerLimit;  
    private String sellingPriceUpperLimit;
    private Integer issueId;
}