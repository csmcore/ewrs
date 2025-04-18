package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.ewarehouse.entity.AocCompProfDirectorDetails;
import app.ewarehouse.entity.AocComplianceCollarterDirector;
import app.ewarehouse.entity.ApplicationCertificateOfCollateral;

@Repository
public interface AOCCollateralDirectorRepository extends JpaRepository<AocComplianceCollarterDirector, String>{


	@Modifying
    @Transactional
    @Query("UPDATE AocComplianceCollarterDirector d SET d.deletedFlag = true WHERE d.applicationId.applicationId = :applicationId")
	void deleteAllByApplicationId(String applicationId);

	@Query("Select directorId from AocComplianceCollarterDirector order by directorId desc limit 1")
	String getId();

	@Query("SELECT d FROM AocComplianceCollarterDirector d WHERE d.applicationId.applicationId = :applicationId")
	List<AocComplianceCollarterDirector> findAllByApplicationId(String applicationId);

	List<AocComplianceCollarterDirector> findByApplicationIdAndDeletedFlagFalse(ApplicationCertificateOfCollateral collateralInfo);

	@Modifying
    @Transactional
    @Query("UPDATE AocComplianceCollarterDirector d SET d.deletedFlag = true WHERE d.directorId = :directorId")
    void markAsDeleted(String directorId);
	

}
