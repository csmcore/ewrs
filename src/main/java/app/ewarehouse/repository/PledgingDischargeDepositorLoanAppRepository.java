package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.PledgingDischargeDepositorLoanApp;

@Repository
public interface PledgingDischargeDepositorLoanAppRepository extends JpaRepository<PledgingDischargeDepositorLoanApp, String> {

	@Query("SELECT p.pdLoanAppId FROM PledgingDischargeDepositorLoanApp p WHERE p.createdBy = :intId AND p.draftStatus = true")
	String getDraftStatusOfStepTwo(@Param("intId") Integer intId);
}
