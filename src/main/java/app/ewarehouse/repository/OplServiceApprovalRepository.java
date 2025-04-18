package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.OplServiceApproval;

@Repository
public interface OplServiceApprovalRepository extends JpaRepository<OplServiceApproval, Integer> {

    // Custom query method to find records by intProcessId, intStageNo, and intLabelId
   
	OplServiceApproval findByOnlineServiceIdAndProcessIdAndStageNoAndLabelId(Integer onlineServiceId, Integer processId, Integer stageNo, Integer labelId);
	
    @Query(value = "From OplServiceApproval Where processId=:intprocessId And onlineServiceId=:onlineServiceId And deletedFlag=false And labelId=:labelId")
    OplServiceApproval findByProcessIdAndOnlineServiceIdAndDeletedFlag(@Param("intprocessId") Integer intprocessId,
			@Param("onlineServiceId") Integer onlineServiceId,@Param("labelId") Integer labelId);
    
    
    OplServiceApproval findByPendingAtAndProcessIdAndLabelIdAndDeletedFlagFalse(Integer pendingAt,Integer processId,Integer labelId);
    
  
    @Modifying
    @Query("DELETE FROM OplServiceApproval o WHERE o.onlineServiceId = :onlineServiceId")
    void deleteByOnlineServiceId(@Param("onlineServiceId") Integer onlineServiceId);
}


