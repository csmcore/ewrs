package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.AocCompProfileDetails;
import app.ewarehouse.entity.ApplicationOfConformityCommodityDetails;
import app.ewarehouse.entity.ApplicationOfConformityLocationDetails;


@Repository
public interface ApplicationOfConformityCommodityDetailsRepository extends JpaRepository<ApplicationOfConformityCommodityDetails, Long> {

	List<ApplicationOfConformityCommodityDetails> findByWarehouseLocation(
			ApplicationOfConformityLocationDetails locationData);
	
	
	List<ApplicationOfConformityCommodityDetails> findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(AocCompProfileDetails company);

	@Query("SELECT COUNT(cd) FROM ApplicationOfConformityCommodityDetails cd WHERE cd.warehouseLocation.whLocationId = :locationId AND cd.deletedFlag = false")
	int countReferencesInCommodityDetails(Long locationId);

	void deleteByCompanyAndDraftStatusTrueAndDeletedFlagFalse(AocCompProfileDetails company);
	
	@Modifying
	@Query(value = "DELETE FROM t_application_of_conformity_commodity_details WHERE intWhLocationId = :warehouseId AND bitDeletedFlag = false", nativeQuery = true)
	void deleteByWarehouseId(Long warehouseId);
}
