package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_commodity_market_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommodityMarket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String commodityName;
    private String depositorId;
    private String marketPrice;
    private String marketValue;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String closingPrice;
    private String date;
    
    private String sellingPriceLowerLimit;  
    private String sellingPriceUpperLimit;
    private Integer intCreatedBy;
    private Integer issueId;
    
    @ManyToOne
    @JoinColumn(name = "issueId", referencedColumnName = "intIssueanceWhId", insertable = false, updatable = false)
    @JsonBackReference
    private IssuanceWareHouseRecipt issuanceWareHouseRecipt;

    
}
