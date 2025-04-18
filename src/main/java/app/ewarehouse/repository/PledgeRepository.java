package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Pledge;
import jakarta.transaction.Transactional;

@Repository
public interface PledgeRepository extends JpaRepository<Pledge, Long>{

	@Transactional
    @Modifying
	@Query(value = """
			UPDATE t_issuance_ware_house_recipt_application SET pledgeStatus=true 
			WHERE intDepositorWhOperator = :depositorId AND intIssueanceWhId=:issueId
			""", nativeQuery = true)
	void updatePledgeStatus(String depositorId,Integer issueId);
	
	@Query(value = """
			SELECT * FROM  t_financial_loans WHERE depositorId=:depositorId 
			""", nativeQuery = true)
	Pledge getDepositorDetailsById(String depositorId);
	
	@Query(value = """
			SELECT tf.*
			FROM  t_financial_loans tf 
				inner join t_issuance_ware_house_recipt_application iss on tf.issueId=iss.intIssueanceWhId
			WHERE tf.issueId=:issueId AND tf.bitDeletedFlag=false
			""", nativeQuery = true)
	Pledge getDepositorDetailsByIssueId(Integer issueId);

}
