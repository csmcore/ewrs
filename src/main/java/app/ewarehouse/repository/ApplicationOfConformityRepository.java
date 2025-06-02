package app.ewarehouse.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ApplicationOfConformity;
import app.ewarehouse.entity.Status;
import jakarta.persistence.Tuple;

@Repository
public interface ApplicationOfConformityRepository extends JpaRepository<ApplicationOfConformity, String> {

//	@Query("SELECT a FROM ApplicationOfConformity a WHERE a.bitDeletedFlag = false AND "
//			+ "(:fromDate IS NULL OR a.dtmCreatedOn >= :fromDate) AND "
//			+ "(:toDate IS NULL OR a.dtmCreatedOn <= :toDate) AND " + "(:status IS NULL OR a.enmStatus = :status)")
//	Page<ApplicationOfConformity> findByFilters(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
//			@Param("status") Status status, @Param("pendingAt") Integer pendingAt, Pageable pageable);
	
	@Query("SELECT a FROM ApplicationOfConformity a WHERE a.bitDeletedFlag = false AND "
		       + "(:fromDate IS NULL OR a.dtmCreatedOn >= :fromDate) AND "
		       + "(:toDate IS NULL OR a.dtmCreatedOn <= :toDate) AND "
		       + "(:status IS NULL OR a.enmStatus = :status) AND "
		       + "(:pendingAt IS NULL OR a.pendingAt = :pendingAt)")
		Page<ApplicationOfConformity> findByFilters(@Param("fromDate") Date fromDate, 
		                                            @Param("toDate") Date toDate,
		                                            @Param("status") Status status, 
		                                            @Param("pendingAt") Integer pendingAt, 
		                                            Pageable pageable);


	@Query("SELECT DISTINCT aoc FROM ApplicationOfConformity aoc JOIN FETCH aoc.particularOfApplicantsId poa "
			+ "LEFT JOIN FETCH poa.directors dir " + "WHERE aoc.applicationId = :applicationId")
	Optional<ApplicationOfConformity> findByApplicationIdWithDirectors(@Param("applicationId") String applicationId);

	@Query(value = "SELECT COUNT(*) FROM t_application_of_conformity_main WHERE intCreatedBy = :createdBy AND enmStatus = :status", nativeQuery = true)
	Integer countByIntCreatedByAndEnmStatus(@Param("createdBy") Integer createdBy, @Param("status") String status);

	List<ApplicationOfConformity> findByIntCreatedByAndEnmStatus(Integer createdBy, Status status);
	
	List<ApplicationOfConformity> findByIntCreatedByAndApplicationId(Integer createdBy, String applicationId);

	@Query("SELECT a FROM ApplicationOfConformity a WHERE a.bitDeletedFlag = false AND "
			+ "(:fromDate IS NULL OR a.dtmCreatedOn >= :fromDate) AND "
			+ "(:toDate IS NULL OR a.dtmCreatedOn <= :toDate) AND " + "(:userId IS NULL OR a.intCreatedBy = :userId)")
	Page<ApplicationOfConformity> findByUserIdFilters(Date fromDate, Date toDate, Integer userId, Pageable pageable);

	@Query(value = """
					SELECT DISTINCT aoc.vchApplicationId AS applicationId,
			       ta.vchShop AS shop
			FROM t_application_of_conformity_main aoc
			INNER JOIN m_final_operator_license_master ol
			    ON ol.vchConformityId = aoc.vchApplicationId COLLATE utf8mb4_unicode_ci
			INNER JOIN aoc_comp_profile_det cp
			    ON cp.vchProfDetId = aoc.vchProfDetId
			INNER JOIN t_application_of_conformity_particular_of_applicants ta
			    ON ta.intParticularOfApplicantsId = aoc.intParticularOfApplicantsId
			INNER JOIN m_admin_user mau
			    ON mau.vchWarehouse = ol.vchConformityId COLLATE utf8mb4_unicode_ci
			    AND mau.intRoleId =:roleId
			    AND mau.bitDeletedFlag = FALSE
			WHERE aoc.enmStatus = 'Accepted'
			    AND ol.enmStatus = 'Created'
			    AND ta.intCountyId = :countyId
			    AND ta.intSubCountyId = :subCountyId
			""", nativeQuery = true)
	List<Tuple> getApprovedApplicationIdAndShop(Integer countyId,  Integer subCountyId,Integer roleId);

	@Query(value = "SELECT p.vchApplicantFullName " + "FROM t_application_of_conformity_particular_of_applicants p "
			+ "INNER JOIN t_application_of_conformity_main tm "
			+ "ON p.intParticularOfApplicantsId = tm.intParticularOfApplicantsId "
			+ "INNER JOIN m_final_operator_license_master opm " + "ON tm.vchApplicationId = opm.vchConformityId "
			+ "INNER JOIN t_operator_licence opl " + "ON opl.vchApplicationNo = opm.vchApplicationId "
			+ "WHERE tm.enmStatus = 'Accepted' " + "AND opm.vchConformityId = :conformityId "
			+ "ORDER BY tm.intParticularOfApplicantsId DESC " + "LIMIT 1", nativeQuery = true)
	String getOperatorFullName(@Param("conformityId") String conformityId);

	@Query("SELECT poa.typeOfCommodities FROM ApplicationOfConformity am JOIN am.particularOfApplicantsId poa WHERE am.applicationId = :id AND am.enmStatus = :status AND am.bitDeletedFlag = :bitDeletedFlag")
	String getCommodityTypes(@Param("id") String id, @Param("status") Status status,
			@Param("bitDeletedFlag") boolean bitDeletedFlag);


	@Query(value = "SELECT \r\n"
			+ "    tm.vchApplicationId AS applicationId, \r\n"
			+ "    CONCAT(ta.vchShop, ' (', tm.vchApplicationId, ')') AS shop \r\n"
			+ "FROM \r\n"
			+ "    t_application_of_conformity_main tm \r\n"
			+ "INNER JOIN \r\n"
			+ "    t_application_of_conformity_particular_of_applicants ta \r\n"
			+ "ON \r\n"
			+ "    tm.intParticularOfApplicantsId = ta.intParticularOfApplicantsId \r\n"
			+ "WHERE \r\n"
			+ "    tm.enmStatus = 'Accepted'\r\n"
			+ "    AND (:loggedApplicationId IS NULL OR :loggedApplicationId = '0' OR tm.vchApplicationId = :loggedApplicationId);", 
    nativeQuery = true)	
	List<Tuple> getAllApprovedApplicationIdAndShop(@Param("loggedApplicationId") String loggedApplicationId);
	
	
	
	@Query(value = """
			    SELECT  distinct aoc.vchApplicationId AS applicationId,wd.vchShop AS shop
			 FROM t_application_of_conformity_main aoc inner join     
			 m_final_operator_license_master ol on ol.vchConformityId=aoc.vchApplicationId 
			 inner join aoc_comp_profile_det cp on cp.vchProfDetId =aoc.vchProfDetId 
			 inner join t_application_of_conformity_particular_of_applicants wd on wd.intParticularOfApplicantsId=aoc.intParticularOfApplicantsId
			 And aoc.enmStatus='Accepted'
			 AND ol.enmStatus='Created'
             AND wd.intCountyId=:countyId
             AND wd.intSubCountyId=:subCountyId
             AND aoc.bitDeletedFlag=false
			""", nativeQuery = true)
			List<Tuple> getWareHouseIdAndName(Integer countyId,  Integer subCountyId);	
	
}




