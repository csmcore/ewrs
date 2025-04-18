package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.OicCollateralTakeAction;

@Repository
public interface OicCollateralTakeActionRepository extends JpaRepository<OicCollateralTakeAction, Integer> {

	@Query("SELECT o.remarkByOic FROM OicCollateralTakeAction o WHERE o.colletral.applicationId = :applicationId")
	String findRemarkByOicByApplicationId(@Param("applicationId") String applicationId);

}
