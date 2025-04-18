package app.ewarehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Country;
import app.ewarehouse.entity.County;
import app.ewarehouse.entity.SubCounty;
import jakarta.persistence.Tuple;


@Repository
public interface CountyRepository extends JpaRepository<County, Integer> {
	List<County> findByCountryCountryId(Integer countryId);
	Page<County> findAll(Pageable pageable);
	Page<County> findById(Pageable pageable,Integer id);
	Page<County> findByCountryCountryId(Pageable pageable,Integer countryId);
	County findByIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);
	
	  @Query("SELECT c FROM County c order by  c.id desc")
	    Page<County> findAllCounties(Pageable pageable);

	    @Query("SELECT c FROM County c " +
	            "WHERE (:search IS NULL OR " +
	            "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
	            "OR LOWER(c.country.countryName) LIKE LOWER(CONCAT('%', :search, '%'))) order by c.id desc")
	    Page<County> findByCountyFilters(@Param("search") String search, Pageable pageable);
	
	
	
	
	//Get Approved County list
	@Query(value = "SELECT  distinct(cpa.intCountyId) as countyId, county.county_name as countyName FROM t_application_of_conformity_particular_of_applicants cpa \r\n"
			+ "inner join t_county_master county on cpa.intCountyId=county.county_id;" , nativeQuery = true)
	List<Tuple> getApprovedCountyList();
	
	
	
	@Query(value = """ 
			
		     SELECT county_id,county_name FROM t_county_master  where county_id IN(
			 SELECT  distinct wd.intCountyId
			  FROM t_application_of_conformity_main aoc inner join     
			 m_final_operator_license_master ol on ol.vchConformityId=aoc.vchApplicationId 
			 inner join aoc_comp_profile_det cp on cp.vchProfDetId =aoc.vchProfDetId 
			 inner join t_application_of_conformity_particular_of_applicants wd on wd.intParticularOfApplicantsId=aoc.intParticularOfApplicantsId
			 where  ol.vchConformityId IN(select warehouse_id from t_grader_weigher_registration )
			 And aoc.enmStatus='Accepted'
			 AND ol.enmStatus='Created'
			 ) and bitDeletedFlag=false order by county_name;
			""" , nativeQuery = true)
	List<Object[]> findGraderCounties();
	
	

	@Query(value = """ 
		select distinct cm.county_id,cm.county_name from t_county_master cm inner join m_admin_user am 
        on am.intCountyId=cm.county_id where cm.bitDeletedFlag=false and am.bitDeletedFlag=false  and am.intRoleId=:RoleId
        order by county_name
			""" , nativeQuery = true)
	List<Object[]> findCountyByRoleId(Integer RoleId);
	
	
	@Query(value = """ 
				  SELECT  distinct cm.county_id, cm.county_name
			 FROM t_application_of_conformity_main aoc inner join
			m_final_operator_license_master ol on ol.vchConformityId=aoc.vchApplicationId
			inner join aoc_comp_profile_det cp on cp.vchProfDetId =aoc.vchProfDetId
			inner join t_application_of_conformity_particular_of_applicants wd on wd.intParticularOfApplicantsId=aoc.intParticularOfApplicantsId
			   inner join t_county_master cm on wd.intCountyId=cm.county_id
			And aoc.enmStatus='Accepted'
			AND ol.enmStatus='Created'
			   And cm.bitDeletedFlag=false order by cm.county_name
		    
			""" , nativeQuery = true)
	List<Object[]> findWareHouseCounties();
	
	List<County> findByBitDeletedFlagFalse();
	 
	
}
