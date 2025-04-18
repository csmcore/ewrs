package app.ewarehouse.master.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.master.entity.WardMaster;

@Repository
public interface WardMasterRepo extends JpaRepository<WardMaster, Integer> {
    
	
	
	WardMaster findByIntWardMasterIdAndBitDeletedFlag(int id,boolean b);
	
	
	@Modifying
    @Query("UPDATE WardMaster c SET c.bitDeletedFlag = NOT c.bitDeletedFlag WHERE c.id = :id")
	void changeBitDeletedFlagWard(Integer id);
	
	@Query(value = """
			SELECT mas.intWardMasterId, mas.wardName, sc.intId AS subCountyId, sc.vchSubCountyName, c.county_id AS countyId, c.county_name,mas.bitDeletedFlag
			FROM
			    m_master_ward mas
			INNER JOIN
			    t_county_master c ON mas.intCountyId = c.county_id
			INNER JOIN
			    t_sub_county sc ON mas.intSubCountyId = sc.intId
			WHERE
			    (mas.wardName IS NOT NULL AND LOWER(mas.wardName) LIKE LOWER(CONCAT('%', :search, '%')))
			    OR
			    (c.county_name IS NOT NULL AND LOWER(c.county_name) LIKE LOWER(CONCAT('%', :search, '%')))
			    OR
			    (sc.vchSubCountyName IS NOT NULL AND LOWER(sc.vchSubCountyName) LIKE LOWER(CONCAT('%', :search, '%')))
			    order by mas.intWardMasterId desc
			""", nativeQuery = true)
	Page<Object> findByWardFilters(String search, Pageable pageable);
	

	@Query(
		    value = "SELECT mas.intWardMasterId, mas.wardName, sc.intId AS subCountyId, sc.vchSubCountyName, c.county_id AS countyId, c.county_name,mas.bitDeletedFlag " +
		            "FROM m_master_ward mas " +
		            "INNER JOIN t_county_master c ON mas.intCountyId = c.county_id " +
		            "INNER JOIN t_sub_county sc ON mas.intSubCountyId = sc.intId "+
		            " order by mas.intWardMasterId desc",
		    nativeQuery = true)
	Page<Object> findAllWard(Pageable pageable);
	
	@Query(value = "select intWardMasterId,wardName from m_master_ward where intCountyId=:countyId and intSubCountyId=:subcountyId  and bitDeletedFlag=0;", nativeQuery = true)
	List<Object[]> getWardsByCountyAndSubCounty(Integer countyId, Integer subcountyId);


	boolean existsByWardNameAndBitDeletedFlagFalse(String wardName);

}
