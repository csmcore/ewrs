package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.ewarehouse.entity.ApplicationCertificateOfCollateral;
import app.ewarehouse.entity.CollateralCVUploadedDetails;

@Repository
public interface CollateralCvUploadRepository extends JpaRepository<CollateralCVUploadedDetails, Integer> {

	List<CollateralCVUploadedDetails> findByApplicationIdAndDeletedFlagFalse(
			ApplicationCertificateOfCollateral collateralInfo);

	@Modifying
    @Transactional
    @Query("UPDATE CollateralCVUploadedDetails c SET c.deletedFlag = true WHERE c.cvId = :cvId")
	void markAsDeleted(String cvId);

}
