package app.ewarehouse.service;

import org.json.JSONObject;

public interface DashboardService {

	JSONObject getQuantityAgainstCommodity();

	JSONObject getQuantityAgainstSeasonwise();

	JSONObject getQuantityAgainstMonthwise();

	JSONObject getQuantityAgainstCountywise();

	JSONObject getReceiptStatus();

	JSONObject getPledgeReceiptStatus();

	JSONObject getReceiptAgainstMonthwise();

	JSONObject getReceiptAgainstCommodity();

	JSONObject getReceiptAgainstCountywise();

	JSONObject getSoldReceiptAgainstMonthwise();

	JSONObject getSoldReceiptAgainstCountywise();

	JSONObject getTotalPriceAgainstCountywise();

	JSONObject getTotalPriceAgainstCommoditywise();

}
