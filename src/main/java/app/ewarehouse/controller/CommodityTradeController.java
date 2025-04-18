package app.ewarehouse.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.ewarehouse.service.TradeDataService;

@RestController
@RequestMapping("/api/trade")
@CrossOrigin("*")
public class CommodityTradeController {
    private final TradeDataService tradeDataService;

    public CommodityTradeController(TradeDataService tradeDataService) {
        this.tradeDataService = tradeDataService;
    }

    @GetMapping("/{commodityName}")
    public List<Object[]> getStockData(@PathVariable String commodityName) {
         List<Object[]> tradeDataByCommodity = tradeDataService.getTradeDataByCommodity(commodityName);
         return tradeDataByCommodity;
    }
    
    
    
}