package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.ewarehouse.entity.CollateralTakeActionCC;
import app.ewarehouse.entity.CollateralTakeActionCCDetails;


public interface CollateralTakeActionCCDetailsRepository extends JpaRepository<CollateralTakeActionCCDetails, Integer> {

	List<CollateralTakeActionCCDetails> findByAllocateCCId(CollateralTakeActionCC collateralTakeActionCC);

}
