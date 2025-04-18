package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.TconformityAction;
@Repository
public interface TconformityActionRepository extends JpaRepository<TconformityAction, Integer> {

	 @Query("SELECT t FROM TconformityAction t WHERE t.applicationConformity.applicationId = :applicationId")
	    List<TconformityAction> findByApplicationId(@Param("applicationId") String applicationId);

}
