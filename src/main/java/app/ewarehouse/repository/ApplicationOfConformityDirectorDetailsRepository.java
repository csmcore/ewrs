package app.ewarehouse.repository;


import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.dto.ApplicationOfConformityDirectorResponseDTO;
import app.ewarehouse.entity.ApplicationOfConformityDirectorDetails;
@Repository
public interface ApplicationOfConformityDirectorDetailsRepository extends JpaRepository<ApplicationOfConformityDirectorDetails, Integer> {
	
	@Query("SELECT new app.ewarehouse.dto.ApplicationOfConformityDirectorResponseDTO(" +
		       "d.id, " +
		       "d.directorName, " +
//		       "d.particularOfApplicants.id, " +
		       "d.nationality.id, " +
		       "d.passportNumber, " +
		       "d.uploadWorkPermitPath) " +
		       "FROM ApplicationOfConformityDirectorDetails d " +
		       "WHERE d.particularOfApplicants.id = :particularOfApplicantsId")
		Set<ApplicationOfConformityDirectorResponseDTO> findDirectorDetailsByParticularOfApplicantsId(@Param("particularOfApplicantsId") Integer particularOfApplicantsId);
}
