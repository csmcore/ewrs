package app.ewarehouse.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.ewarehouse.entity.Action;
import app.ewarehouse.entity.OperatorLicence;
import app.ewarehouse.entity.Stakeholder;
import app.ewarehouse.entity.Status;
import jakarta.persistence.Tuple;


public interface OperatorLicenceRepository extends JpaRepository<OperatorLicence, Integer> {
	
	@Query("""
			FROM OperatorLicence o WHERE o.status=:status AND o.forwardedTo=:stakeholder AND o.action=:action
			AND (:search IS NULL
			OR LOWER(o.vchApplicationNo) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(o.businessName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(o.businessEntityType) LIKE LOWER(CONCAT('%', :search, '%')))""")
	Page<OperatorLicence> findByStatusAndForwardedToAndAction(Status status, Stakeholder stakeholder,
			Action action, Pageable pageable, String search);

	@Query("""
			FROM OperatorLicence o WHERE o.forwardedTo=:forwardedTo
			AND (:search IS NULL
			OR LOWER(o.vchApplicationNo) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(o.businessName) LIKE LOWER(CONCAT('%', :search, '%'))
			OR LOWER(o.businessEntityType) LIKE LOWER(CONCAT('%', :search, '%')))""")
	Page<OperatorLicence> findByForwardedTo(Pageable pageable, Stakeholder forwardedTo, String search);

	@Query("SELECT o FROM OperatorLicence o WHERE o.bitDeleteFlag = false AND " +
			"(:status IS NULL OR o.status = :status) AND " +
			"(:userId IS NULL OR o.intCreatedBy = :userId)")
	List<OperatorLicence> findByFilters(Status status, Integer userId);

	@Query("SELECT o FROM OperatorLicence o WHERE o.bitDeleteFlag = false AND " +
			"(:status IS NULL OR o.status = :status) AND " +
			"(:userId IS NULL OR o.intCreatedBy = :userId)")
	Page<OperatorLicence> findByFilters(Status status, Integer userId, Pageable pageable);
	
	
	@Query(value="""
			SELECT loc.vchWarehouseId,loc.vchWarehouseName,tcm.intcnfId  from t_confirmity_main tcm inner join t_application_of_conformity_location_det loc
			on tcm.intlocId=loc.intWhLocationId where
			tcm.vchWareHouseId =  loc.vchWarehouseId AND
			tcm.bitDeletedFlag=false AND
			tcm.vchApproverStatus='APPROVED' AND
			tcm.bitLicenseGen=false AND
			tcm.bitLicenceCerti=false AND
			tcm.vchCompanyId=:compId
			""",nativeQuery=true)
	List<Tuple> getOperatorWarehouseByCompanyId(@Param("compId") String compId);
	
	
//	@Query(value="""
//			SELECT loc.vchWarehouseId,loc.vchWarehouseName,tcm.intcnfId,com.vchCompany,tcm.vchCompanyId
//from t_confirmity_main tcm inner join t_application_of_conformity_location_det loc
//			on tcm.intlocId=loc.intWhLocationId 
//		inner join aoc_comp_profile_det com on com.vchProfDetId=tcm.vchCompanyId
//
//            where tcm.vchWareHouseId =  loc.vchWarehouseId 
//            AND tcm.bitDeletedFlag=false 
//            AND tcm.vchApproverStatus='APPROVED' 
//			AND  tcm.vchCompanyId=:compId  
//            AND tcm.bitLicenseGen=true
//            AND tcm.bitLicenceCerti=true   
//			""",nativeQuery=true)
//	List<Tuple> getOperatorWarehouseNameByWareHouse(String compId);
	
	
}
