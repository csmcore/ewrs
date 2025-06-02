package app.ewarehouse.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ReinstatementActionHistory;

@Repository
public interface ReinstatementActionHistoryRepository extends JpaRepository<ReinstatementActionHistory, Integer> {

	List<ReinstatementActionHistory> findByReinstatementId(String reinsatementId);

}
