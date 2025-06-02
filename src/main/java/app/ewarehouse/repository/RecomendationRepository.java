package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Recommendation;
import jakarta.transaction.Transactional;

@Repository
public interface RecomendationRepository extends JpaRepository<Recommendation, Long> {


	List<Recommendation> findByIntLocationIdAndBitDeletedFlagFalse(Long intLocationId);
	
	@Modifying
    @Transactional
    @Query(value = "UPDATE t_routine_com_inpector_recommendation SET bit_deleted_flag = 1 WHERE recommendation_id = :id", nativeQuery = true)
	void markAsDeleted(Long id);

}
