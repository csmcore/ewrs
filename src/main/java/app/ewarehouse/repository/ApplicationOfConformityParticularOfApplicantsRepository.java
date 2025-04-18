package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.ewarehouse.entity.ApplicationOfConformityParticularOfApplicants;

public interface ApplicationOfConformityParticularOfApplicantsRepository extends JpaRepository<ApplicationOfConformityParticularOfApplicants, Integer>{

	@Query("SELECT a.id FROM ApplicationOfConformityParticularOfApplicants a WHERE a.intCreatedBy = :intId AND a.bitDraftStatusFlag = true")
	Long countByCreatedByAndDraftStatus(@Param("intId") Integer intId);

}
