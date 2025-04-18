package app.ewarehouse.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.ewarehouse.entity.Action;
import app.ewarehouse.entity.Inspector;
import app.ewarehouse.entity.Stakeholder;
import app.ewarehouse.entity.Status;

public interface InspectorRepository extends JpaRepository<Inspector, Integer> {
	Page<Inspector> findByStatusAndForwardedTo(Status status, Stakeholder forwardedTo, Pageable pageable);

	Page<Inspector> findByStatusInAndForwardedToAndCurrentActionIn(List<Status> statuses, Stakeholder ceo,
			List<Action> actions, Pageable pageable);

	Page<Inspector> findByStatusInAndForwardedTo(List<Status> statuses, Stakeholder forwardedTo, Pageable pageable);

	Page<Inspector> findByCurrentActionAndForwardedTo(Action status, Stakeholder forwardedTo, Pageable pageable);
	
	Page<Inspector> findByStatusInAndForwardedToIn(List<Status> statuses, List<Stakeholder> stakeholders, Pageable pageable);


	@Query("SELECT i FROM Inspector i WHERE " +
		       "(:fromDate IS NULL OR i.dtmCreatedOn >= :fromDate) " +
		       "AND (:toDate IS NULL OR i.dtmCreatedOn <= :toDate) " +
		       "AND (:status IS NULL OR i.status = :status) " +
		       "AND (:action IS NULL OR i.currentAction = :action) " +
		       "AND (:stakeholder IS NULL OR i.forwardedTo = :stakeholder) " +
		       "AND (:forwardedTo IS NULL OR i.forwardedTo = :forwardedTo)" +
		       "AND (:search IS NULL OR " +
	            "LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
	            "OR LOWER(i.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
	            "OR LOWER(i.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%')) " +
	            "OR CAST(i.intId AS string) LIKE CONCAT('%', :search, '%') " +
	            "OR LOWER(CAST(i.status AS string)) LIKE LOWER(CONCAT('%', :search, '%')))")
		Page<Inspector> findByFilters(@Param("fromDate") Date fromDate,
		                              @Param("toDate") Date toDate,
		                              @Param("status") Status status,
		                              @Param("action") Action action,
		                              @Param("stakeholder") Stakeholder stakeholder,
		                              @Param("search") String search,
		                              @Param("forwardedTo") Stakeholder forwardedTo,
		                              Pageable pageable);
	
	    
	    @Query("SELECT i FROM Inspector i WHERE " +
	    	       "(:fromDate IS NULL OR i.dtmCreatedOn >= :fromDate) " +
	    	       "AND (:toDate IS NULL OR i.dtmCreatedOn <= :toDate) " +
	    	       "AND (:status IS NULL OR i.status = :status) " +
	    	       "AND (:action IS NULL OR i.currentAction = :action) " +
	    	       "AND (:stakeholder IS NULL OR i.forwardedTo = :stakeholder) " +
	    	       "AND (:forwardedTo IS NULL OR i.forwardedTo = :forwardedTo) " +
	    	       "AND (:search IS NULL OR " +
	    	       "LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
	    	       "OR LOWER(i.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
	    	       "OR LOWER(i.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%')) " +
	    	       "OR CAST(i.intId AS string) LIKE CONCAT('%', :search, '%') " +
	    	       "OR LOWER(CAST(i.status AS string)) LIKE LOWER(CONCAT('%', :search, '%'))) " +
	    	       "AND (:statuses IS NULL OR i.status IN :statuses) " +
	    	       "AND (:stakeholders IS NULL OR i.forwardedTo IN :stakeholders)")
	    	Page<Inspector> findFilteredApplications(@Param("fromDate") Date fromDate,
	    	                                         @Param("toDate") Date toDate,
	    	                                         @Param("status") Status status,
	    	                                         @Param("action") Action action,
	    	                                         @Param("stakeholder") Stakeholder stakeholder,
	    	                                         @Param("search") String search,
	    	                                         @Param("forwardedTo") Stakeholder forwardedTo,
	    	                                         @Param("statuses") List<Status> statuses,
	    	                                         @Param("stakeholders") List<Stakeholder> stakeholders,
	    	                                         Pageable pageable);
}
