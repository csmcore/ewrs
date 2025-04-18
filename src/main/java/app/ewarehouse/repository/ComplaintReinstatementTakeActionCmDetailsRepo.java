package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintReinstatementTakeActionCm;
import app.ewarehouse.entity.ComplaintReinstatementTakeActionCmDetails;

@Repository
public interface ComplaintReinstatementTakeActionCmDetailsRepo extends JpaRepository<ComplaintReinstatementTakeActionCmDetails, Integer> {

	List<ComplaintReinstatementTakeActionCmDetails> findByAllocateCCId(ComplaintReinstatementTakeActionCm cmTakeAction);

}
