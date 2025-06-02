package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.IssuanceWarehouseReceiptActionHistory;

@Repository
public interface IssuanceWarehouseReceiptActionHistoryRepository extends JpaRepository<IssuanceWarehouseReceiptActionHistory, Integer> {

	@Query(value="select * from t_issuance_wh_receipt_action_history where intIssueanceWhId=:issuanceWhId and depositorId=:depositorId", nativeQuery = true)
	List<IssuanceWarehouseReceiptActionHistory> findByIntIssueanceWhId(Integer issuanceWhId, String depositorId);

}
