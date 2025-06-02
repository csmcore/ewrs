package app.ewarehouse.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Commodity;
import app.ewarehouse.entity.Country;
import app.ewarehouse.entity.Seasonality;

@Repository
public interface CommodityMasterRepository extends JpaRepository<Commodity, Integer> {

    Commodity findByNameIgnoreCase(String name);

    @Query("SELECT c FROM Commodity c where c.bitDeleteFlag = false")
    List<Commodity> findByBitDeleteFlag(boolean bitDeleteFlag);

    @Query("SELECT c FROM Commodity c ORDER BY c.dtmCreatedAt DESC")
    Page<Commodity> getAll(Pageable pageable);
    
    @Query("SELECT c FROM Commodity c order by c.id desc")
    Page<Commodity> findAllCommodities(Pageable pageable);
    
    
  /*  @Query("select c FROM Commodity c"+
    	"WHERE (:search IS NULL  "+
           "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))"+ 
           "OR LOWER(c.type.typeName) LIKE LOWER(CONCAT('%', :search, '%'))"+ 
           "OR LOWER(c.seasonality.seasonname) LIKE LOWER(CONCAT('%', :search, '%'))")
	Page<Commodity> findAllCommoditiesFilter(@Param("search") String search,Pageable pageable);*/
    
    @Query("SELECT c FROM Commodity c " +
    	       "WHERE (:search IS NULL " +
    		   "OR LOWER(CASE WHEN c.commodityType = 1 THEN 'Agricultural' WHEN c.commodityType = 2 THEN 'Energy' ELSE 'Metals' END) "+
    		   "LIKE LOWER(CONCAT('%', :search, '%')) "+
    	       "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
    	       "OR (c.type IS NOT NULL AND LOWER(c.type.typeName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
    	       "OR CAST(c.charges AS string) LIKE CONCAT('%', :search, '%') " +
    	       "OR (c.seasonality IS NOT NULL AND LOWER(c.seasonality.seasonname) LIKE LOWER(CONCAT('%', :search, '%'))))"
    	       + "order by c.id desc")
    Page<Commodity> findAllCommoditiesFilter(@Param("search") String search,Pageable pageable);

	
	 @Query("SELECT c FROM Commodity c WHERE c.Id = :id")
	 Optional<Commodity> findCommodityById(@Param("id") Integer id);
     
	 @Query(value=" select Id,name from t_commodity_master where bitDeleteFlag=0;",nativeQuery=true)
	 List<Object[]> getAllCommodityName();
    
	 @Query(value=" select commodityorigin,commoditycode from t_commodity_master where Id=:id and  bitDeleteFlag=0;",nativeQuery=true)
	List<Object[]> getCommodityNameByOriginAndCode(@Param("id") Integer id);

	//Double getUnitCharge(Integer typeId);

    
	 
    

}
