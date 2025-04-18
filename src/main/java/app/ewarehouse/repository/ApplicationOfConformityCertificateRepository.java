package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ApplicationOfConformityCertificate;

@Repository
public interface ApplicationOfConformityCertificateRepository extends JpaRepository<ApplicationOfConformityCertificate, Integer>{

	@Query("Select certificateNo from ApplicationOfConformityCertificate order by certificateId desc limit 1")
	String getId();
	
	
	@Query("SELECT a FROM ApplicationOfConformityCertificate a WHERE a.application.applicationId = :applicationId")
	ApplicationOfConformityCertificate findByApplicationId(@Param("applicationId") String applicationId);
	
	
	ApplicationOfConformityCertificate findByCertificateNo(String certificateNo);

}
