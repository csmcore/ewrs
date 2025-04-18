package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.ewarehouse.entity.GraderWeigherExperience;
import app.ewarehouse.entity.GraderWeigherForm;

@Repository
public interface GraderWeigherExperienceRepository extends JpaRepository<GraderWeigherExperience, String> {

	List<GraderWeigherExperience> findByGraderWeigherAndDeletedFlagFalse(GraderWeigherForm graderWeigherData);

	@Modifying
    @Transactional
    @Query("UPDATE GraderWeigherExperience exp SET exp.deletedFlag = true WHERE exp.experienceId = :experienceId")
	void markAsDeleted(String experienceId);

	@Query("Select experienceId from GraderWeigherExperience order by experienceId desc limit 1")
	String getId();

}
