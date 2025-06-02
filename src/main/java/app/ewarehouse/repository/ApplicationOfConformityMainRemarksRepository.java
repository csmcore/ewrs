package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ApplicationOfConformityMainRemarks;

@Repository
public interface ApplicationOfConformityMainRemarksRepository
		extends JpaRepository<ApplicationOfConformityMainRemarks, Integer> {

	@Query("SELECT r FROM ApplicationOfConformityMainRemarks r WHERE r.applicationId.applicationId = :applicationId")
	List<ApplicationOfConformityMainRemarks> findByApplicationId(@Param("applicationId") String applicationId);

}
