package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.AocCompProfileDetails;
import app.ewarehouse.entity.ApplicationOfConformityFormOneB;
import app.ewarehouse.entity.ApplicationOfConformityLocationDetails;


@Repository
public interface ApplicationOfConformityFormOneBRepository extends JpaRepository<ApplicationOfConformityFormOneB, Long> {

	ApplicationOfConformityFormOneB findByWarehouseLocationAndDeletedFlagFalse(ApplicationOfConformityLocationDetails locationData);
    // Add custom queries if needed
	List<ApplicationOfConformityFormOneB> findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(AocCompProfileDetails company);
	
	@Query("SELECT COUNT(fb) FROM ApplicationOfConformityFormOneB fb WHERE fb.warehouseLocation.whLocationId = :locationId AND fb.deletedFlag = false")
	int countReferencesInFormOneB(Long locationId);
}

