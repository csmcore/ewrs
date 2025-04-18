package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.CollateralCertificate;

@Repository
public interface CollateralCertificateRepository extends JpaRepository<CollateralCertificate, Integer> {

	@Query("Select certificateNo from CollateralCertificate order by certificateId desc limit 1")
	String getId();

	CollateralCertificate findByApplicationId(String applicantId);


}
