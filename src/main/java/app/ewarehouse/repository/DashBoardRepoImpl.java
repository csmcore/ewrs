package app.ewarehouse.repository;

import java.time.Year;
import java.util.List;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class DashBoardRepoImpl implements DashboardRepository {

    private static final String P_MSG = "pMsg";
	private static final String DASHBOARD_PROC = "dashboard_proc";
	private static final String P_YEAR = "pYear";
	private static final String P_ACTION = "pAction";

	@PersistenceContext
    private EntityManager entity;

    private final int currentYear = Year.now().getValue();

    private List<Object[]> executeDashboardProcedure(String action) {
        StoredProcedureQuery query = entity.createStoredProcedureQuery(DASHBOARD_PROC);
        query.registerStoredProcedureParameter(P_ACTION, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(P_YEAR, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(P_MSG, String.class, ParameterMode.OUT);
        
        query.setParameter(P_ACTION, action);
        query.setParameter(P_YEAR, currentYear);
        query.execute();
        
        String pMsg = (String) query.getOutputParameterValue(P_MSG);
        log.info("Procedure Message for action {}: {}", action, pMsg);
        
        return query.getResultList();
    }
    
    private List<Object[]> executeDashboardProcedureWithYear(String action, int year) {
        StoredProcedureQuery query = entity.createStoredProcedureQuery(DASHBOARD_PROC);
        query.registerStoredProcedureParameter(P_ACTION, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(P_YEAR, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(P_MSG, String.class, ParameterMode.OUT);
        
        query.setParameter(P_ACTION, action);
        query.setParameter(P_YEAR, year);
        query.execute();
        
        String pMsg = (String) query.getOutputParameterValue(P_MSG);
        log.info("Procedure Message for action {}: {}", action, pMsg);
        
        return query.getResultList();
    }

    @Override
    public List<Object[]> getQuantityAgainstCommodity() {
        return executeDashboardProcedure("QT_COMM");
    }

    @Override
    public List<Object[]> getQuantityAgainstSeasonwise() {
        return executeDashboardProcedure("QT_SEASON");
    }

    @Override
    public List<Object[]> getQuantityAgainstMonthwise(int currentYear) {
        return executeDashboardProcedureWithYear("QT_MONTH_PERCENT", currentYear);
    }

    @Override
    public List<Object[]> getQuantityAgainstCountywise() {
        return executeDashboardProcedure("QT_COUNTY");
    }

    @Override
    public List<Object[]> getReceiptStatus() {
        return executeDashboardProcedure("RECEIPT_STATUS");
    }

    @Override
    public List<Object[]> getPledgeReceiptStatus() {
        return executeDashboardProcedure("PLEDGE_RECEIPT_STATUS");
    }

    @Override
    public List<Object[]> getReceiptAgainstMonthwise(int currentYear) {
        return executeDashboardProcedureWithYear("RECEIPT_MONTHWISE", currentYear);
    }

    @Override
    public List<Object[]> getReceiptAgainstCommodity() {
        return executeDashboardProcedure("RECEIPT_COMMODITYWISE");
    }

    @Override
    public List<Object[]> getReceiptAgainstCountywise() {
        return executeDashboardProcedure("RECEIPT_COUNTYWISE");
    }

    @Override
    public List<Object[]> getSoldReceiptAgainstMonthwise() {
        return executeDashboardProcedure("SOLD_RECEIPT_MONTHWISE");
    }

    @Override
    public List<Object[]> getSoldReceiptAgainstCountywise() {
        return executeDashboardProcedure("SOLD_RECEIPT_COUNTYWISE");
    }

    @Override
    public List<Object[]> getTotalPriceAgainstCountywise() {
        return executeDashboardProcedure("TOTAL_PRICE_COUNTYWISE");
    }

    @Override
    public List<Object[]> getTotalPriceAgainstCommoditywise() {
        return executeDashboardProcedure("TOTAL_PRICE_COMMODITYWISE");
    }

}
