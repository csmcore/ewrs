package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.ewarehouse.entity.ApplicationOfConformitySupportingDocuments;

public interface ApplicationOfConformitySupportingDocumentsRepository extends JpaRepository<ApplicationOfConformitySupportingDocuments, Integer> {

	@Query("SELECT a.id FROM ApplicationOfConformitySupportingDocuments a WHERE a.intCreatedBy = :intId AND a.bitDraftStatusFlag = true")
	Long getDraftStatusOfSupportingDocs(@Param("intId") Integer intId);

}
