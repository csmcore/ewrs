package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintObservationResponse;

import java.util.List;

@Repository
public interface ComplaintObservationResponseRepository extends JpaRepository<ComplaintObservationResponse, Integer> {
    List<ComplaintObservationResponse> findByObResAPIdAndObResStageAndDeleteFlag(int obResAPId,int ObResStage, boolean deleteFlag);

}
