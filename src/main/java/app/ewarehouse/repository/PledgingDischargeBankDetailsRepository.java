package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.PledgingDischargeBankDetails;

@Repository
public interface PledgingDischargeBankDetailsRepository extends JpaRepository<PledgingDischargeBankDetails, String> {
	@Query("SELECT p.pdBankDetailsId FROM PledgingDischargeBankDetails p WHERE p.createdBy = :intId AND p.draftStatus = true")
	String getDraftStatusOfStepFour(@Param("intId") Integer intId);
}
