package app.ewarehouse.master.repository;

import app.ewarehouse.entity.Country;
import app.ewarehouse.master.entity.ComplaintStatusMaster;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chinmaya.jena
 * @since 03-07-2024
 */

@Repository
public interface ComplaintStatusMasterRepository extends JpaRepository<ComplaintStatusMaster, Integer> {
	List<ComplaintStatusMaster> findByBitDeletedFlagFalse();
	
	@Modifying
    @Query("UPDATE ComplaintStatusMaster c SET c.bitDeletedFlag = NOT c.bitDeletedFlag WHERE c.id = :id")
    void changeBitDeletedFlag(@Param("id") Integer id);

	@Query("SELECT c FROM ComplaintStatusMaster c order by c.dtmCreatedOn desc")
	Page<ComplaintStatusMaster> findAllComplaintStatus(Pageable pageable);

	@Query("SELECT c FROM ComplaintStatusMaster c " +
            "WHERE (:search IS NULL OR " +
            "LOWER(c.vchComplaintStatusName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            ") order by c.dtmCreatedOn desc")
	Page<ComplaintStatusMaster> findByFilters(String search, Pageable pageable);
}
