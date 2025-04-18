package app.ewarehouse.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ChargesHeader;

@Repository
public interface ChargesHeaderRepository extends JpaRepository<ChargesHeader, Integer> {
    
	@Query(value="select intChargesHeader,vchChargesHeader from m_charges_headers where bitDeletedFlag=0;",nativeQuery=true)
	List<Object[]> getAllChargesHeader();
   
	 @Query(value = "SELECT t1.intChargesHeader, t2.vchUnitType " +
             "FROM m_charges_headers t1 " +
             "INNER JOIN m_unit_type t2 ON t1.intUnitType = t2.intUnitType " +
             "WHERE t1.intChargesHeader = :chargesId AND t1.bitDeletedFlag = 0", nativeQuery = true)
     List<Object[]> getChargesHeaderByUnitType(@Param("chargesId") Integer chargesId);


}
