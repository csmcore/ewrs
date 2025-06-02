package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ApplicationCertificateOfCollateral;
import app.ewarehouse.entity.CollateralTakeActionCC;

@Repository
public interface CollateralTakeActionCCRepository extends JpaRepository<CollateralTakeActionCC, Integer> {


	CollateralTakeActionCC findByCertificateCommitteeIdAndColletral(int userId,
			ApplicationCertificateOfCollateral applicantData);

	@Query(value = """
		    SELECT 
		        CASE 
		            WHEN COUNT(*) = SUM(CASE WHEN c.bitForwardToOicStatus = TRUE THEN 1 ELSE 0 END) 
		            THEN 1 ELSE 0 
		        END 
		    FROM t_oic_collateral_take_action_cc c 
		    WHERE c.vchapplicationId = :vchApplicationId
		    """, nativeQuery = true)
		Integer areAllRecordsTrue(@Param("vchApplicationId") String vchApplicationId);

	List<CollateralTakeActionCC> findByColletral(ApplicationCertificateOfCollateral applicantData);




}
