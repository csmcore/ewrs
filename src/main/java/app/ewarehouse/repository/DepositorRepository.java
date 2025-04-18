/**
 * 
 */
package app.ewarehouse.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Depositor;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TempUser;
import app.ewarehouse.entity.Ward;

/**
 * Priyanka Singh
 */
@Repository
public interface DepositorRepository extends JpaRepository<Depositor, String> {
	
	List<Depositor> findAllByBitDeletedFlag(boolean bitDeletedFlag);

	Depositor findByIntIdAndBitDeletedFlag(String intId, boolean bitDeletedFlag);


	 @Query("SELECT d FROM Depositor d WHERE d.bitDeletedFlag = false AND (:fromDate IS NULL OR d.dtmCreatedOn >= :fromDate) AND (:toDate IS NULL OR d.dtmCreatedOn <= :toDate) AND (:status IS NULL OR d.enmStatus = :status)ORDER BY d.dtmCreatedOn DESC")
	Page<Depositor> findByFilters(Date fromDate, Date toDate, Status status, Pageable pageable);
    
	 


}
