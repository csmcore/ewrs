package app.ewarehouse.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.OplServiceApprovalNotings;

@Repository
public interface OplServiceApprovalNotingsRepository extends JpaRepository<OplServiceApprovalNotings, Integer> {

    // Custom query method to find records by intOnlineServiceId and intProcessId
    List<OplServiceApprovalNotings> findByOnlineServiceIdAndProcessId(Integer onlineServiceId, Integer processId);
    OplServiceApprovalNotings findByOnlineServiceIdAndProcessIdAndStageNo(Integer onlineServiceId, Integer processId,Integer stageNO);
    
    @Query(value = """
    	    SELECT 
    	        TN.intOnlineServiceId AS intOnlineServiceId, 
    	        TN.intFromAuthority AS intFromAuthority, 
    	        TN.intToAuthority AS intToAuthority, 
    	        TN.txtNoting AS txtNoting, 
    	        TN.dtActionTaken AS dtActionTaken,
    	        TN.intProcessId AS intProcessId, 
    	        RR.vchRolename AS vchRolename, 
    	        TN.intStatus AS intStatus, 
    	        UR.vchFullName AS vchFullName,
    	        TN.intStageNo AS intStageNo, 
    	        TN.intNotingsId AS intNotingsId,
    	        TN.vchActionTaken AS vchActionTaken, 
    	        TN.intActionTakenId AS intActionTakenId
    	    FROM t_opl_service_approval_notings TN
    	    JOIN t_operator_licence_application opla 
    	        ON TN.intOnlineServiceId = opla.intLicenceSno
    	    LEFT JOIN m_admin_role RR 
    	        ON TN.intFromAuthority = RR.intId AND RR.bitDeletedFlag = 0
    	    LEFT JOIN m_admin_user UR 
    	        ON TN.intCreatedBy = UR.intId AND UR.bitDeletedFlag = 0
    	    WHERE TN.bitDeletedFlag = 0
    	      AND TN.intStageNo NOT IN (901)
    	      AND TN.intOnlineServiceId = :intLicenceSno
    	      AND TN.intProcessId = 200
    	""", nativeQuery = true)
    	List<Map<String, Object>> getOperatorLicenceActionHistory(@Param("intLicenceSno") Integer intLicenceSno);


	
}
