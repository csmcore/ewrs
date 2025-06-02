package app.ewarehouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.PledgingDischargeMain;

@Repository
public interface PledgingDischargeMainRepository extends JpaRepository<PledgingDischargeMain, String> {

	@Query("SELECT p FROM PledgingDischargeMain p where p.deletedFlag = false")
	Page<PledgingDischargeMain> getAll(Pageable pageable);
	
	//@Query("SELECT p FROM PledgingDischargeMain p where p.deletedFlag = false")
	
	
	@Query("SELECT p FROM PledgingDischargeMain p " +
		       "LEFT JOIN p.depositorWarehouse dw " +  // Join depositorWarehouse from PledgingDischargeMain
		       "LEFT JOIN dw.buyer b " +  // Join buyer from PledgingDischargeDepositorWarehouse
		       "WHERE p.deletedFlag = false " +
		       "AND (:search IS NULL OR " +
		       "LOWER(b.txtName) LIKE LOWER(CONCAT('%', :search, '%')) " +
		       "OR LOWER(b.txtPassportNo) LIKE LOWER(CONCAT('%', :search, '%')) " +
		       "OR LOWER(b.intCentralRegistryIdentifier) LIKE LOWER(CONCAT('%', :search, '%')) " +
		       "OR LOWER(b.txtEmailAddress) LIKE LOWER(CONCAT('%', :search, '%'))) " +
		       "ORDER BY LOWER(b.txtName) ASC")
	Page<PledgingDischargeMain> findByFilters(@Param("search") String search, Pageable pageable);
	
	@Query("FROM PledgingDischargeMain m WHERE m.depositorWarehouse.buyer.intId = :depositorId ORDER BY m.updatedOn DESC LIMIT 1")
	PledgingDischargeMain getByDepositorId(String depositorId);

}
