package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ConformityMain;
import app.ewarehouse.entity.ConformityTakeActionCc;

@Repository
public interface ConformityTakeActionCcRepository extends JpaRepository<ConformityTakeActionCc, Integer> {
	ConformityTakeActionCc findByCertificateCommitteeIdAndConformity(Integer certificateCommitteeId, ConformityMain conformity);
	
	
	@Query("SELECT CASE WHEN COUNT(c) = SUM(CASE WHEN c.forwardToOicStatus = TRUE THEN 1 ELSE 0 END) " +
		       "THEN TRUE ELSE FALSE END " +
		       "FROM ConformityTakeActionCc c WHERE c.conformity.id = :intcnfId")
	Boolean areAllRecordsTrue(@Param("intcnfId") Integer intcnfId);
//	@Query(value = "SELECT CASE WHEN COUNT(*) = SUM(bitForwardToOicStatus) THEN TRUE ELSE FALSE END " +
//            "FROM t_cnf_take_action_cc WHERE intcnfId = :intcnfId", 
//    nativeQuery = true)
//	boolean areAllRecordsTrue(@Param("intcnfId") Integer intcnfId);
}
