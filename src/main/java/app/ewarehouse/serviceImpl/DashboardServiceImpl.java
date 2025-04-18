package app.ewarehouse.serviceImpl;

import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import app.ewarehouse.repository.DashboardRepository;
import app.ewarehouse.service.DashboardService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

	
	private final DashboardRepository repository;
	
	@Override
	public JSONObject getQuantityAgainstCommodity() {
	    JSONObject response = new JSONObject();
	    List<Object[]> data = repository.getQuantityAgainstCommodity();
	    List<String> commodityNames = new ArrayList<>();
	    List<Object> quantities = new ArrayList<>();
	    for (Object[] row : data) {
	        commodityNames.add((String) row[0]);
	        quantities.add(row[1]);
	    }
	    response.put("commodityNames", commodityNames);
	    response.put("quantities", quantities);
	    return response;
	}

	@Override
	public JSONObject getQuantityAgainstSeasonwise() {
		JSONObject response = new JSONObject();
	    List<Object[]> data = repository.getQuantityAgainstSeasonwise();
	    List<String> seasons = new ArrayList<>();
	    List<Object> quantities = new ArrayList<>();
	    for (Object[] row : data) {
	    	seasons.add((String) row[0]);
	        quantities.add(row[1]);
	    }
	    response.put("seasons", seasons);
	    response.put("quantities", quantities);
	    return response;
	}

	@Override
	public JSONObject getQuantityAgainstMonthwise() {
		JSONObject response = new JSONObject();
		int currentYear = Year.now().getValue();
	    List<Object[]> data = repository.getQuantityAgainstMonthwise(currentYear);
	    List<String> seasons = new ArrayList<>();
	    List<Object> quantitypercents = new ArrayList<>();
	    for (Object[] row : data) {
	    	seasons.add((String) row[0]);
	    	quantitypercents.add(row[1]);
	    }
	    response.put("months", seasons);
	    response.put("quantitypercents", quantitypercents);
	    return response;
	}

	@Override
	public JSONObject getQuantityAgainstCountywise() {
		JSONObject response = new JSONObject();
	    List<Object[]> data = repository.getQuantityAgainstCountywise();
	    List<Object> totalQuantities = new ArrayList<>();
	    List<Object> countyNames = new ArrayList<>();
	    for (Object[] row : data) {
	    	totalQuantities.add(row[0]);
	    	countyNames.add(row[1]);
	    }
	    response.put("totalQuantities", totalQuantities);
	    response.put("countyNames", countyNames);
	    return response;
	}

	@Override
	public JSONObject getReceiptStatus() {
		JSONObject response = new JSONObject();
	    List<Object[]> data = repository.getReceiptStatus();
	    List<Object> receiptStatus = new ArrayList<>();
	    List<Object> percentage = new ArrayList<>();
	    for (Object[] row : data) {
	    	receiptStatus.add(row[0]);
	    	percentage.add(row[1]);
	    }
	    response.put("receiptStatus", receiptStatus);
	    response.put("percentage", percentage);
	    return response;
	}

	@Override
	public JSONObject getPledgeReceiptStatus() {
		JSONObject response = new JSONObject();
	    List<Object[]> data = repository.getPledgeReceiptStatus();
	    List<Object> receiptStatus = new ArrayList<>();
	    List<Object> percentage = new ArrayList<>();
	    for (Object[] row : data) {
	    	receiptStatus.add(row[0]);
	    	percentage.add(row[1]);
	    }
	    response.put("receiptStatus", receiptStatus);
	    response.put("percentage", percentage);
	    return response;
	}

	@Override
	public JSONObject getReceiptAgainstMonthwise() {
		JSONObject response = new JSONObject();
		int currentYear = Year.now().getValue();
	    List<Object[]> data = repository.getReceiptAgainstMonthwise(currentYear);
	    List<String> months = new ArrayList<>();
	    List<Object> receipts = new ArrayList<>();
	    for (Object[] row : data) {
	    	months.add((String) row[0]);
	    	receipts.add(row[1]);
	    }
	    response.put("months", months);
	    response.put("receipts", receipts);
	    return response;
	}

	@Override
	public JSONObject getReceiptAgainstCommodity() {
		JSONObject response = new JSONObject();
	    List<Object[]> data = repository.getReceiptAgainstCommodity();
	    List<String> commodityNames = new ArrayList<>();
	    List<Object> receipts = new ArrayList<>();
	    for (Object[] row : data) {
	        commodityNames.add((String) row[0]);
	        receipts.add(row[1]);
	    }
	    response.put("commodityNames", commodityNames);
	    response.put("receipts", receipts);
	    return response;
	}

	@Override
	public JSONObject getReceiptAgainstCountywise() {
		JSONObject response = new JSONObject();
	    List<Object[]> data = repository.getReceiptAgainstCountywise();
	    List<Object> receipts = new ArrayList<>();
	    List<Object> countyNames = new ArrayList<>();
	    for (Object[] row : data) {
	    	receipts.add(row[0]);
	    	countyNames.add(row[1]);
	    }
	    response.put("receipts", receipts);
	    response.put("countyNames", countyNames);
	    return response;
	    
	}

	@Override
	public JSONObject getSoldReceiptAgainstMonthwise() {
		JSONObject response = new JSONObject();
	    Map<String, Long> monthData = new LinkedHashMap<>();
	    monthData.put("Jan", 0L);
	    monthData.put("Feb", 0L);
	    monthData.put("Mar", 0L);
	    monthData.put("Apr", 0L);
	    monthData.put("May", 0L);
	    monthData.put("Jun", 0L);
	    monthData.put("Jul", 0L);
	    monthData.put("Aug", 0L);
	    monthData.put("Sep", 0L);
	    monthData.put("Oct", 0L);
	    monthData.put("Nov", 0L);
	    monthData.put("Dec", 0L);
	    try {
	        List<Object[]> data = repository.getSoldReceiptAgainstMonthwise();
	        for (Object[] row : data) {
	            String month = (String) row[0];
	            Long soldReceipts = (Long) row[1];
	            monthData.put(month, soldReceipts);
	        }
	        response.put("months", new ArrayList<>(monthData.keySet()));
	        response.put("soldReceipts", new ArrayList<>(monthData.values()));
	    } catch (Exception e) {
	        response.put("error", "Data retrieval failed: " + e.getMessage());
	    }
	    return response;
	}

	@Override
	public JSONObject getSoldReceiptAgainstCountywise() {
		JSONObject response = new JSONObject();
	    List<Object[]> data = repository.getSoldReceiptAgainstCountywise();
	    List<Object> soldReceipts = new ArrayList<>();
	    List<Object> countyNames = new ArrayList<>();
	    for (Object[] row : data) {
	    	soldReceipts.add(row[0]);
	    	countyNames.add(row[1]);
	    }
	    response.put("soldReceipts", soldReceipts);
	    response.put("countyNames", countyNames);
	    return response;
	}

	@Override
	public JSONObject getTotalPriceAgainstCountywise() {
		JSONObject response = new JSONObject();
	    List<Object[]> data = repository.getTotalPriceAgainstCountywise();
	    List<Object> counties = new ArrayList<>();
	    List<Object> totalprice = new ArrayList<>();
	    for (Object[] row : data) {
	    	counties.add(row[0]);
	    	totalprice.add(row[1]);
	    }
	    response.put("counties", counties);
	    response.put("totalprice", totalprice);
	    return response;
	}

	@Override
	public JSONObject getTotalPriceAgainstCommoditywise() {
		JSONObject response = new JSONObject();
	    List<Object[]> data = repository.getTotalPriceAgainstCommoditywise();
	    List<Object> commodity = new ArrayList<>();
	    List<Object> totalsalesamount = new ArrayList<>();
	    for (Object[] row : data) {
	    	commodity.add(row[0]);
	    	totalsalesamount.add(row[1]);
	    }
	    response.put("commodity", commodity);
	    response.put("totalsalesamount", totalsalesamount);
	    return response;
	}


	

}
