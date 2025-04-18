package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.PledgingDischargeUploadDocs;

@Repository
public interface PledgingDischargeUploadDocsRepository extends JpaRepository<PledgingDischargeUploadDocs, String> {
	@Query("SELECT p.vchPdUploadDocsId FROM PledgingDischargeUploadDocs p WHERE p.intCreatedBy = :intId AND p.bitDraftStatus = true")
	String getDraftStatusOfStepFive(@Param("intId") Integer intId);
}
