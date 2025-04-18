package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ConformityTakeActionCcDetails;

@Repository
public interface ConformityTakeActionCcDetailsRepository extends JpaRepository<ConformityTakeActionCcDetails, Integer> {

	List<ConformityTakeActionCcDetails> findByConformityId(Integer intApplicantId);
}
