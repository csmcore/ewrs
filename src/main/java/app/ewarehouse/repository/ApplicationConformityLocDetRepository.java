package app.ewarehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.ewarehouse.entity.ApplicationOfConformityLocationDetails;
import app.ewarehouse.entity.AocCompProfileDetails;


@Repository
public interface ApplicationConformityLocDetRepository extends JpaRepository<ApplicationOfConformityLocationDetails, Long> {

	Optional<ApplicationOfConformityLocationDetails> findTopByWarehouseIdStartingWithOrderByWarehouseIdDesc(String string);

	boolean existsByWarehouseId(String warehouseId);

	ApplicationOfConformityLocationDetails findByWarehouseId(String warehouseId);
	
	@Query(value = "select intWhLocationId , vchWarehouseName from t_application_of_conformity_location_det taocld where taocld.vchCompanyId = :companyId and taocld.bitDeletedFlag = 0 and taocld.bitDraftStatus = 1",nativeQuery = true)
	List<Object[]> getDraftedWarehouseDetails(String companyId);
	
	List<ApplicationOfConformityLocationDetails> findByCompanyAndDraftStatusTrueAndDeletedFlagFalse(AocCompProfileDetails company);

	@Modifying
    @Transactional
    @Query("UPDATE ApplicationOfConformityLocationDetails d SET d.deletedFlag = true WHERE d.whLocationId = :locationId")
	int markAsDeleted(Long locationId);
	
	@Query(value = "SELECT \r\n"
			+ "    taocld.intWhLocationId, \r\n"
			+ "    taocld.vchWarehouseId, \r\n"
			+ "    taocld.vchWarehouseName\r\n"
			+ "FROM t_application_of_conformity_location_det taocld\r\n"
			+ "INNER JOIN t_application_of_conformity_commodity_storage taoccs \r\n"
			+ "    ON taocld.intWhLocationId = taoccs.intWhLocationId\r\n"
			+ "INNER JOIN t_application_of_conformity_commodity_details taoccd\r\n"
			+ "    ON taocld.intWhLocationId = taoccd.intWhLocationId\r\n"
			+ "INNER JOIN t_application_of_conformity_form_one_a taocfoa\r\n"
			+ "	ON taocld.intWhLocationId = taocfoa.intWhLocationId\r\n"
			+ "INNER JOIN t_application_of_conformity_form_one_b taocfob\r\n"
			+ "	ON taocld.intWhLocationId = taocfob.intWhLocationId\r\n"
			+ "WHERE taocld.vchCompanyId = :companyId \r\n"
			+ "    AND taocld.bitDraftStatus = 1 \r\n"
			+ "    AND taocld.bitDeletedFlag = 0\r\n"
			+ "    AND taoccs.bitDraftStatus = 1\r\n"
			+ "    AND taoccs.bitDeletedFlag = 0\r\n"
			+ "    AND taoccd.bitDraftStatus = 1\r\n"
			+ "    AND taoccd.bitDeletedFlag = 0\r\n"
			+ "    AND taocfoa.bitDraftStatus = 1\r\n"
			+ "    AND taocfoa.bitDeletedFlag = 0\r\n"
			+ "    AND taocfob.bitDraftStatus = 1\r\n"
			+ "    AND taocfob.bitDeletedFlag = 0\r\n"
			+ "GROUP BY taocld.intWhLocationId, taocld.vchWarehouseId, taocld.vchWarehouseName;" , nativeQuery = true)
	List<Object[]> getDraftedWarehousesForPayment(String companyId);

}
