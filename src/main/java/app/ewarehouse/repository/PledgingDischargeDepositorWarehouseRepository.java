package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.PledgingDischargeDepositorWarehouse;

@Repository
public interface PledgingDischargeDepositorWarehouseRepository extends JpaRepository<PledgingDischargeDepositorWarehouse, String> {

	@Query("SELECT p.pledgingDischargeReceiptId FROM PledgingDischargeDepositorWarehouse p WHERE p.intCreatedBy = :intId AND p.bitDraftStatus = true")
	String getCountStepOneByCreatedByAndDraftStatus(@Param("intId") Integer intId);

}
