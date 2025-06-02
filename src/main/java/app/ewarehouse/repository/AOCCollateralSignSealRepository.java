package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.AocComplianceCollarterSignSeal;
import app.ewarehouse.entity.ApplicationCertificateOfCollateral;

@Repository
public interface AOCCollateralSignSealRepository extends JpaRepository<AocComplianceCollarterSignSeal, String> {

	@Query("Select signSealId from AocComplianceCollarterSignSeal order by signSealId desc limit 1")
	String getId();

	@Query("SELECT s FROM AocComplianceCollarterSignSeal s WHERE s.applicationId.applicationId = :applicationId")
	AocComplianceCollarterSignSeal findByApplicationId(String applicationId);

	AocComplianceCollarterSignSeal findByApplicationId(ApplicationCertificateOfCollateral collateralInfo);

}
