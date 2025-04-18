/**
 * 
 */
package app.ewarehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintType;
import jakarta.transaction.Transactional;

/**
 * Priyanka Singh
 */
@Repository
public interface ComplaintTypeRepository extends JpaRepository<ComplaintType, Integer> {

	ComplaintType findByComplaintTypeIgnoreCase(String complaintType);

	ComplaintType findByComplaintId(Integer complaintId);


    @Modifying
    @Transactional
    @Query("UPDATE ComplaintType c SET c.isActive = true WHERE c.complaintId = :complaintId")
    int resetComplaintId(@Param("complaintId") Integer complaintId);

//    @Query("FROM ComplaintType where isActive = false")
//    List<ComplaintType> getComplaintData();

    @Query("SELECT c FROM ComplaintType c ORDER BY c.createdOn DESC")
    Page<ComplaintType> findAllData(Pageable pageable);

    List<ComplaintType> findByIsActive(boolean isActive);

    @Query("SELECT c FROM ComplaintType c JOIN c.roles r WHERE r.intId = :roleId")
	List<ComplaintType> getComplaintTypesByRoleId(@Param("roleId") Integer roleId);
    
//    @Query("SELECT c FROM ComplaintType c")
//   	List<ComplaintType> getComplaintTypesByRoleId();

    @Query("SELECT c FROM ComplaintType c " +
            "WHERE (:search IS NULL OR " +
            "LOWER(c.complaintType) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.complaintStatus) LIKE LOWER(CONCAT('%', :search, '%')))"+
            "order by c.complaintId desc")
	Page<ComplaintType> findByFilters(String search, Pageable pageable);

	@Query("SELECT c FROM ComplaintType c order by c.complaintId desc")
	Page<ComplaintType> findAllComplaintList(Pageable pageable);
}
