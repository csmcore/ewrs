package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.EmploymentDetails;
import app.ewarehouse.entity.PledgingDischargeResidential;


@Repository
public interface EmploymentDetailsRepository extends JpaRepository<EmploymentDetails, Integer> {
	
	EmploymentDetails findByPledgingDischargeResidential(PledgingDischargeResidential pledgingDischargeResidential);
}
