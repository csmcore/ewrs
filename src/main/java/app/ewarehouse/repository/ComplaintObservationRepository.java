package app.ewarehouse.repository;

import java.util.List;

import app.ewarehouse.entity.ComplaintObservationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintObservation;

@Repository
public interface ComplaintObservationRepository extends JpaRepository<ComplaintObservation, Integer> {
    // Add custom query methods if needed
	
	   List<ComplaintObservation> findByIntLableIdAndIntRoleId(Integer lableId,Integer roleId);

}