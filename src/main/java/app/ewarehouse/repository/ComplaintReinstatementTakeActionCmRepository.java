package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintReinstatement;
import app.ewarehouse.entity.ComplaintReinstatementTakeActionCm;

@Repository
public interface ComplaintReinstatementTakeActionCmRepository extends JpaRepository<ComplaintReinstatementTakeActionCm, Integer> {

	ComplaintReinstatementTakeActionCm findByCommitteeMemberIdAndReinstatement(int userId,
			ComplaintReinstatement reinStatementData);

	@Query(value = """
		    SELECT 
		        CASE 
		            WHEN COUNT(*) = SUM(CASE WHEN c.bitForwardStatus = TRUE THEN 1 ELSE 0 END) 
		            THEN 1 ELSE 0 
		        END 
		    FROM t_complaint_reinstatement_take_action_cm c 
		    WHERE c.reinstatementId = :reinsatementId
		    """, nativeQuery = true)
		Integer areAllRecordsTrue(@Param("reinsatementId") String reinsatementId);

	List<ComplaintReinstatementTakeActionCm> findByReinstatement(ComplaintReinstatement data);



}
