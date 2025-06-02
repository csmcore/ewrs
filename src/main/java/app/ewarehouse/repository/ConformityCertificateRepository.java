package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ConformityCertificate;

@Repository
public interface ConformityCertificateRepository extends JpaRepository<ConformityCertificate, Integer>{

	@Query("Select certificateNo from ConformityCertificate order by certificateId desc limit 1")
	String getId();
	
	
	ConformityCertificate findByConformityId(Integer conformityId);
	
}
