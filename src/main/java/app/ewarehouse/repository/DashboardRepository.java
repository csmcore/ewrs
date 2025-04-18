package app.ewarehouse.repository;

import java.util.List;

public interface DashboardRepository {

	List<Object[]> getQuantityAgainstCommodity();

	List<Object[]> getQuantityAgainstSeasonwise();

	List<Object[]> getQuantityAgainstMonthwise(int currentYear);

	List<Object[]> getQuantityAgainstCountywise();

	List<Object[]> getReceiptStatus();

	List<Object[]> getPledgeReceiptStatus();

	List<Object[]> getReceiptAgainstMonthwise(int currentYear);

	List<Object[]> getReceiptAgainstCommodity();

	List<Object[]> getReceiptAgainstCountywise();

	List<Object[]> getSoldReceiptAgainstMonthwise();

	List<Object[]> getSoldReceiptAgainstCountywise();

	List<Object[]> getTotalPriceAgainstCountywise();

	List<Object[]> getTotalPriceAgainstCommoditywise();

}
