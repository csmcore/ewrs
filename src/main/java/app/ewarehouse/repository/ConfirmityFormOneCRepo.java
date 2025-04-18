package app.ewarehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ConfirmityFormOneCEntity;
import jakarta.transaction.Transactional;
@Repository
public interface ConfirmityFormOneCRepo extends JpaRepository<ConfirmityFormOneCEntity, Long> {
	
	  List<ConfirmityFormOneCEntity> findByWareHouseIdAndDeletedFlagFalse(String warehouseId);
	  
	  @Query(value="Select count(*) as count from ConfirmityFormOneCEntity where wareHouseId=:wareHouseId and deletedFlag=false  ")
	  Integer checkFormStatus(@Param("wareHouseId")String wareHouseId);
	  
	  
	  Optional<ConfirmityFormOneCEntity> findTopByFormOneCIdStartingWithOrderByFormOneCIdDesc(String IdFormat);

	  
	  @Modifying
	    @Transactional
	    @Query("DELETE FROM ConfirmityFormOneCEntity onec WHERE onec.wareHouseId = :wareHouseId")
	    void deleteByWareHouseId(@Param("wareHouseId") String wareHouseId);
	  
	  @Modifying
	    @Transactional
	    @Query("DELETE FROM ConfirmityFormOneCEntity onec WHERE onec.id=:docId")
	    void deleteByDocId(@Param("docId") Long docId);
}


