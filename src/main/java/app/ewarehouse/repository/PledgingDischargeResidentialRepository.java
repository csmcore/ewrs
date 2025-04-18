package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.PledgingDischargeResidential;

@Repository
public interface PledgingDischargeResidentialRepository extends JpaRepository<PledgingDischargeResidential, String> {

	@Query("SELECT p.pdResidentialId FROM PledgingDischargeResidential p WHERE p.createdBy = :intId AND p.draftStatus = true")
	String getDraftStatusOfStepThree(@Param("intId") Integer intId);
}
