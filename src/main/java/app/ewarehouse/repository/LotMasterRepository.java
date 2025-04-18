package app.ewarehouse.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.LotMaster;
@Repository
public interface LotMasterRepository extends JpaRepository<LotMaster, Integer> {

	@Query("SELECT l.noOfBags FROM LotMaster l WHERE l.typeOfLot = 'Maximum' ORDER BY l.noOfBags DESC LIMIT 1")
    Optional<Integer> findNoOfBagsForMaximumType();

    @Query("SELECT l.noOfBags FROM LotMaster l WHERE l.typeOfLot = 'Minimum' ORDER BY l.noOfBags ASC LIMIT 1")
    Optional<Integer> findNoOfBagsForMinimumType();
    
    Optional<LotMaster> findByTypeOfLot(String typeOfLot);

    @Query("SELECT l FROM LotMaster l " +
    	       "WHERE (:search IS NULL OR " +
    	       "LOWER(l.typeOfLot) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
    	       "STR(l.noOfBags) LIKE CONCAT('%', :search, '%') OR " +
    	       "STR(l.kgPerBag) LIKE CONCAT('%', :search, '%')) " +
    	       "ORDER BY l.lotId DESC")
	Page<LotMaster> findByFilters(String search, Pageable pageable);

	@Query("SELECT l FROM LotMaster l order by l.lotId desc")
	Page<LotMaster> findAllLotData(Pageable pageable);
	
}
