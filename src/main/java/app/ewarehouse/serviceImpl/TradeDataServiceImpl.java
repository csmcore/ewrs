package app.ewarehouse.serviceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.ewarehouse.repository.TradeDataRepository;
import app.ewarehouse.service.TradeDataService;

@Service
public class TradeDataServiceImpl implements TradeDataService{
	
	@Autowired
	private TradeDataRepository tradeDataRepository;

	@Override
	public List<Object[]> getTradeDataByCommodity(String commodityName) {
	    // Fetch raw database results (id, timestamp, open, high, low, close)
	    List<Object[]> rawDataList = tradeDataRepository.findByCommodity(commodityName);
	    
	    List<Object[]> result = new ArrayList<>();
	    
	    // Loop through the fetched data and convert it to Object[]
	    for (Object[] row : rawDataList) {
	        Object[] formattedRow = new Object[7]; // 7 fields: timestamp, openPrice, highPrice, lowPrice, closePrice, marketPrice, marketValue

//	        String timestampStr = (String) row[0];  // Assuming timestamp is the first column
//	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//	        LocalDate timestamp = LocalDate.parse(timestampStr, formatter);
	        
	     // ✅ Handle timestamp as java.sql.Date, converting to LocalDate
	        java.sql.Date sqlDate = (java.sql.Date) row[0];  // Assuming timestamp is the first column
	        LocalDate timestamp = sqlDate.toLocalDate();  // Convert to LocalDate
	        double openPrice = parseDouble(row[1]);
	        double highPrice = parseDouble(row[2]);
	        double lowPrice = parseDouble(row[3]);
	        double closePrice = parseDouble(row[4]);

	        // Calculate Market Price (average of open, high, low, close)
	        double marketPrice = (openPrice + highPrice + lowPrice + closePrice) / 4.0;
	        
	        // Market Value (assuming default quantity of 1)
	        double marketValue = marketPrice * 1;
	        
	        marketPrice = new BigDecimal(marketPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
	        marketValue = new BigDecimal(marketValue).setScale(2, RoundingMode.HALF_UP).doubleValue();

	        // Fill the Object[] with the values
	        formattedRow[0] = timestamp;      // Timestamp (long)
	        formattedRow[1] = openPrice;      // Open Price (double)
	        formattedRow[2] = highPrice;      // High Price (double)
	        formattedRow[3] = lowPrice;       // Low Price (double)
	        formattedRow[4] = closePrice;     // Close Price (double)
	        formattedRow[5] = marketPrice;    // Market Price (double)
	        formattedRow[6] = marketValue;    // Market Value (double)

	        // Add the row to the result
	        result.add(formattedRow);
	    }
	    
	    return result;  // Return the formatted list
	}


	private double parseDouble(Object value) {
	    if (value instanceof BigDecimal) {
	        return ((BigDecimal) value).doubleValue();
	    } else if (value instanceof Double) {
	        return (double) value;
	    } else if (value instanceof String) {
	        return Double.parseDouble((String) value);
	    }
	    return 0.0;  // Default fallback
	}

	
	

}
