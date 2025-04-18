package app.ewarehouse.repository;

import java.util.List;
import java.util.Optional;

import app.ewarehouse.dto.SubCountyResponse;
import app.ewarehouse.entity.Commodity;
import app.ewarehouse.entity.County;
import app.ewarehouse.entity.SubCounty;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCountyRepository extends JpaRepository<SubCounty, Integer> {

	@Modifying
	@Transactional
	@Query(value = "UPDATE t_sub_county SET bitDeletedFlag = 1 WHERE intId = :id", nativeQuery = true)
	void deleteSubCounty(@Param("id") Integer id);

	List<SubCounty> findByCountyAndBitDeletedFlag(County county, boolean bitDeletedFlag);

	Optional<SubCounty> findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	List<SubCounty> findAllByBitDeletedFlag(Boolean bitDeletedFlag);

	/*
	 * @Query(value =
	 * "SELECT DISTINCT(cpa.intSubCountyId) AS subCountyId, subcounty.vchSubCountyName AS subCountyName "
	 * + "FROM t_application_of_conformity_particular_of_applicants cpa "
	 * +
	 * "INNER JOIN t_sub_county subcounty ON cpa.intSubCountyId = subcounty.intId "
	 * + "AND subcounty.intCountyId = :countyId", nativeQuery = true) List<Tuple>
	 * getApprovedSubCountyList(@Param("countyId") Integer countyId);
	 */
	
	
	
	@Query(value = """
		    SELECT distinct sc.intId AS subCountyId ,sc.vchSubCountyName AS subCountyName FROM t_sub_county sc inner join m_admin_user am
			on sc.intId= am.intSubCountyId
			And sc.intCountyId=am.intCountyId
			And am.bitDeletedFlag = 0
			And am.intCountyId=:countyId
			And am.intRoleId = (select intId from m_admin_role where intId =:roleId) 
			     
			""", nativeQuery = true)
	List<Tuple> getApprovedSubCountyList(Integer countyId,Integer roleId);
	
	
	

	@Query(value = """
			SELECT *
			FROM
			    t_sub_county sc
			INNER JOIN
			    t_county_master c ON sc.intCountyId = c.county_id
			WHERE
			    (sc.vchSubCountyName IS NOT NULL AND LOWER(sc.vchSubCountyName) LIKE LOWER(CONCAT('%', :search, '%')))
			    OR
			    (c.county_name IS NOT NULL AND LOWER(c.county_name) LIKE LOWER(CONCAT('%', :search, '%')))
			    order by sc.dtmCreatedOn desc
			""", nativeQuery = true)
	Page<Object> findAllSubCountyFilter(Pageable pageable, String search);

	@Query(value = "select * from t_sub_county sc\r\n"
			+ "  inner join t_county_master cm on sc.intCountyId=cm.county_id order by sc.dtmCreatedOn desc", nativeQuery = true)
	Page<Object> findAllSubCountyDetails(Pageable pageable);

//    @Query("SELECT c FROM Commodity c WHERE c.Id = :id")
//	 Optional<Commodity> findCommodityById(@Param("id") Integer id);
	
	@Query("SELECT s FROM SubCounty s WHERE s.intId = :id")
	Optional<SubCounty> findSubcountyById(Integer id);
}
