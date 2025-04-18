package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.AocCompProfileDetails;
import app.ewarehouse.entity.ApplicationOfConformityCommodityStorage;


@Repository
public interface ApplicationOfConformityCommodityStorageRepo extends JpaRepository<ApplicationOfConformityCommodityStorage, Long> {

	
	List<ApplicationOfConformityCommodityStorage> findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(AocCompProfileDetails company);
	
	
	@Query("SELECT COUNT(cs) FROM ApplicationOfConformityCommodityStorage cs WHERE cs.warehouseLocation.whLocationId = :locationId AND cs.deletedFlag = false")
	int countReferencesInCommodityStorage(@Param("locationId") Long locationId);
	
	
	void deleteByCompanyAndDraftStatusTrueAndDeletedFlagFalse(AocCompProfileDetails company);
	
	
	@Query(value="SELECT * FROM t_application_of_conformity_commodity_storage  WHERE intWhLocationId = :warehouseId AND bitDeletedFlag = false", nativeQuery = true)
	ApplicationOfConformityCommodityStorage findByWarehouseId(Long warehouseId);
	
}
