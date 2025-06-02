package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ApplicationOfConformityActionHistory;

@Repository
public interface ApplicationOfConformityActionHistoryRepository extends JpaRepository<ApplicationOfConformityActionHistory, Integer> {
	
	@Query(value = "SELECT * FROM t_application_of_conformity_action_history WHERE vchWareHouseId = :wareHouseId", nativeQuery = true)
	List<ApplicationOfConformityActionHistory> findConformityActionHistoryByWareHouseId(@Param("wareHouseId") String wareHouseId);


}
