package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.ewarehouse.entity.ApplicationOfConformityWarehouseOperatorViability;

public interface ApplicationOfConformityWarehouseOperatorViabilityRepository extends JpaRepository<ApplicationOfConformityWarehouseOperatorViability, Integer> {

	@Query("SELECT a.id FROM ApplicationOfConformityWarehouseOperatorViability a WHERE a.intCreatedBy = :intId AND a.bitDraftStatusFlag = true")
	Long getDraftStatusOfViability(@Param("intId") Integer intId);

}
