package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.NotificationEntity;
import jakarta.transaction.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {

    // Custom query methods can be added if needed

    // Example: Find notifications by recipient (ToAuthId)
    List<NotificationEntity> findByToAuthId(Long toAuthId);

    // Example: Find unread notifications for a recipient
    List<NotificationEntity> findByToAuthIdAndReadStatusFalseOrderByCreatedOnDesc(Integer toAuthId);
    
    @Query(value="""
    		select icmUserId from t_complaint_icm where icmActionTaken =false and bitDeletedFlag=false
and bitNotificationFlag=false and intSentNotificationId=0 and intLableId=:lableId and 
intProcessId=:processId and icmComplaintAppId=:ComplaintAppId and icmRoleId=:roleid
    		""",nativeQuery=true)
    
    List<Object[]> getCMUserList(Integer lableId,Integer processId,Integer ComplaintAppId,Integer roleid);
    
    
    
    @Query(value="""
    		select icmUserId from t_opl_clc where icmActionTaken =false and bitDeletedFlag=false
 and intLableId=301 and 
intProcessId=200 and icmComplaintAppId=:ComplaintAppId and icmRoleId=:roleId
    		""",nativeQuery=true)
    
    List<Object[]> getOPLCMUserList(Integer ComplaintAppId, Integer roleId);
    
    @Transactional
    @Modifying
    @Query(value = """
        UPDATE t_online_service_approval 
        SET bitNotificationFlag = true, intSentNotificationId = :notifyId 
        WHERE intLabelId = :lableId 
          AND intOnlineServiceId = :ComplaintAppId 
          AND intStageNo = :stageNo 
          AND intPendingAt = :roleid
        """, nativeQuery = true)
    int setNotificationStatus(
        @Param("lableId") Integer lableId, 
        @Param("stageNo") Integer stageNo, 
        @Param("ComplaintAppId") Integer ComplaintAppId, 
        @Param("roleid") Integer roleid, 
        @Param("notifyId") Integer notifyId
    );
    
    
    @Transactional
    @Modifying
    @Query(value = """
        UPDATE t_opl_service_approval 
        SET bitNotificationFlag = true, intSentNotificationId = :notifyId 
        WHERE intLabelId = :lableId 
          AND intOnlineServiceId = :ComplaintAppId 
          AND intStageNo = :stageNo 
          AND intPendingAt = :roleid
        """, nativeQuery = true)
    int setOPLNotificationStatus(
        @Param("lableId") Integer lableId, 
        @Param("stageNo") Integer stageNo, 
        @Param("ComplaintAppId") Integer ComplaintAppId, 
        @Param("roleid") Integer roleid, 
        @Param("notifyId") Integer notifyId
    );
    
    
    @Query(value = """
        SELECT name_grader FROM complaint_managment where intId = :ComplaintAppId  and complaint_against = :lableId and bitDeletedFlag=false and intSentNotifyId=0;
        """, nativeQuery = true)
    int getGraderUserId(@Param("lableId") Integer lableId, @Param("ComplaintAppId") Integer ComplaintAppId);
    
    
    @Query(value = """
            SELECT inspector_name FROM complaint_managment where intId = :ComplaintAppId  and complaint_against = :lableId and bitDeletedFlag=false and intSentNotifyId=0;
            """, nativeQuery = true)
        int getInspectorId(@Param("lableId") Integer lableId, @Param("ComplaintAppId") Integer ComplaintAppId);
   

    @Query(value = """
            SELECT coll_manager_name FROM complaint_managment where intId = :ComplaintAppId  and complaint_against = :lableId and bitDeletedFlag=false and intSentNotifyId=0;
            """, nativeQuery = true)
        int getColleatrolId(@Param("lableId") Integer lableId, @Param("ComplaintAppId") Integer ComplaintAppId);
    
    
    @Transactional
    @Modifying
    @Query(value = """
        Update complaint_managment SET intSentNotifyId =:notifyId  where intId = :ComplaintAppId  and complaint_against = :lableId and bitDeletedFlag=false and intSentNotifyId=0;
        """, nativeQuery = true)
    int setComplaintNotificationStatus(@Param("notifyId") Integer notifyId,@Param("lableId") Integer lableId,@Param("ComplaintAppId") Integer ComplaintAppId);
    
    
}
