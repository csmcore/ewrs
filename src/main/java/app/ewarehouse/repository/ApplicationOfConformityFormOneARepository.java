package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.AocCompProfileDetails;
import app.ewarehouse.entity.ApplicationOfConformityFormOneA;
import app.ewarehouse.entity.ApplicationOfConformityLocationDetails;


@Repository
public interface ApplicationOfConformityFormOneARepository extends JpaRepository<ApplicationOfConformityFormOneA, Long> {

	ApplicationOfConformityFormOneA findByWarehouseLocationAndDeletedFlagFalse(ApplicationOfConformityLocationDetails locationData);
	
	
	List<ApplicationOfConformityFormOneA> findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(AocCompProfileDetails company);
    // Add custom queries if needed


	@Query("SELECT COUNT(fa) FROM ApplicationOfConformityFormOneA fa WHERE fa.warehouseLocation.whLocationId = :locationId AND fa.deletedFlag = false")
	int countReferencesInFormOneA(Long locationId);
}

