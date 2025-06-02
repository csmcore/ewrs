package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.ewarehouse.entity.AocCompProfDirectorDetails;
import app.ewarehouse.entity.AocCompProfileDetails;

@Repository
public interface AocCompProfDirectorDetRepository extends JpaRepository<AocCompProfDirectorDetails, String> {
	//List<AocCompProfDirectorDetails> findByProfDet(AocCompProfileDetails profDet);
	
	List<AocCompProfDirectorDetails> findByProfDetAndDeletedFlagFalse(AocCompProfileDetails profDet);
	
	@Query("Select directorId from AocCompProfDirectorDetails order by directorId desc limit 1")
	String getId();
	
	@Modifying
    @Transactional
    @Query("UPDATE AocCompProfDirectorDetails d SET d.deletedFlag = true WHERE d.directorId = :directorId")
    void markAsDeleted(String directorId);

	@Modifying
    @Transactional
    @Query("UPDATE AocCompProfDirectorDetails d SET d.deletedFlag = true WHERE d.profDet.profDetId = :profileId")
	void deleteAllByProfileId(String profileId);
	
	
}
