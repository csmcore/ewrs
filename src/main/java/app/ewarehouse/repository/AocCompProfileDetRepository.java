package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.AocCompProfileDetails;

@Repository
public interface AocCompProfileDetRepository extends JpaRepository<AocCompProfileDetails, String> {

	
	@Query("Select profDetId from AocCompProfileDetails order by profDetId desc limit 1")
	String getId();

	List<AocCompProfileDetails> findByCreatedBy(Integer createdBy);
	
	Page<AocCompProfileDetails> findByCreatedBy(Integer createdBy, Pageable pageable);

	@Query(value = "SELECT COUNT(vchProfDetId) FROM aoc_comp_profile_det WHERE intCreatedBy = :userId", nativeQuery = true)
	Integer getCountProfileDetails(@Param("userId") Integer userId);
	
	AocCompProfileDetails findByProfDetIdAndDeletedFlagFalse(String compId);


	
}
