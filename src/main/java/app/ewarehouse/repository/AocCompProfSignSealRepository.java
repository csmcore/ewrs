package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.AocCompProfSignSeal;
import app.ewarehouse.entity.AocCompProfileDetails;

@Repository
public interface AocCompProfSignSealRepository extends JpaRepository<AocCompProfSignSeal, String> {
	AocCompProfSignSeal findByProfDet(AocCompProfileDetails profDet);
	
	
	@Query("Select signSealId from AocCompProfSignSeal order by signSealId desc limit 1")
	String getId();
}
