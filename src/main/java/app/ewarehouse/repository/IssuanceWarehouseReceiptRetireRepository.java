package app.ewarehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.ewarehouse.entity.IssuanceWarehouseReceiptRetire;

public interface IssuanceWarehouseReceiptRetireRepository extends JpaRepository<IssuanceWarehouseReceiptRetire, Integer>{

	@Query(
		    value = """
		        SELECT * FROM t_warehouse_receipt_retire_history 
		        WHERE isRetired = 1  
		        ORDER BY dtmCreatedOn DESC
		        """,
		    countQuery = """
		        SELECT COUNT(*) FROM t_warehouse_receipt_retire_history 
		        WHERE isRetired = 1
		        """,
		    nativeQuery = true
		)
	Page<IssuanceWarehouseReceiptRetire> findAllRetiredData(Pageable pageable);

	@Query(
		    value = """
		        SELECT * FROM t_warehouse_receipt_retire_history 
		        WHERE isRetired = 1 AND intDepositorWhOperator = :depositorId
		        ORDER BY dtmCreatedOn DESC
		        """,
		    countQuery = """
		        SELECT COUNT(*) FROM t_warehouse_receipt_retire_history 
		        WHERE isRetired = 1 AND intDepositorWhOperator = :depositorId
		        """,
		    nativeQuery = true
		)
	Page<IssuanceWarehouseReceiptRetire> findAllRetiredDataForDepositor(Pageable pageable, String depositorId);

}
