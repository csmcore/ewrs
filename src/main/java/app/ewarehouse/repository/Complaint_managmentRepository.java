package app.ewarehouse.repository;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintApplicationStatus;
import app.ewarehouse.entity.Complaint_managment;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional; 

@Repository
public interface Complaint_managmentRepository extends JpaRepository<Complaint_managment, Integer> {

	@Query(value="""
			SELECT MT.vchActionOnApplication,MT.ActionCondition,MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, MT.complaint_no as complaintNo ,TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo,TSA.tinVerifiedBy,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName,
			TSA.intLabelId,TD.intId as userRoleId
			FROM complaint_managment AS MT
			JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
			LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, TSA.intPendingAt) > 0 AND TSA.tinStatus != 4) OR (TSA.intForwardedUserId=:userId AND TSA.tinStatus = 4 AND TSA.intForwardedUserId != 0)
			WHERE MT.bitDeletedFlag = 0
			AND TSA.bitDeletedFlag = 0
			AND TSA.intProcessId =:intId
			AND TSA.tinStatus NOT IN (3, 7, 8)
			AND TSA.intPendingAt != 0
			AND TD.intId =:roleId  and (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or MT.contact_no like CONCAT('%',:txtContactNumber,'%'))
			ORDER BY TSA.dtmStatusDate DESC""" ,nativeQuery=true,
			countQuery = """
				SELECT count(1)
				FROM complaint_managment AS MT
				JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
				LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, TSA.intPendingAt) > 0 AND TSA.tinStatus != 4) OR (TSA.intForwardedUserId=:userId AND TSA.tinStatus = 4 AND TSA.intForwardedUserId != 0)
				WHERE MT.bitDeletedFlag = 0
				AND TSA.bitDeletedFlag = 0
				AND TSA.intProcessId =:intId
				AND TSA.tinStatus NOT IN (3, 7, 8)
				AND TSA.intPendingAt != 0
				AND TD.intId =:roleId  and (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or MT.contact_no like CONCAT('%',:txtContactNumber,'%'))
				ORDER BY TSA.dtmStatusDate DESC""")
	Page<Map<String,Object>> getAllPendingData(@Param("intId")Integer intId,@Param("roleId") Integer roleId,@Param("userId") Integer userId,@Param("txtFullName") String txtFullName,@Param("txtContactNumber") String txtContactNumber, Pageable pageRequest);

	
	@Query(value="""
			SELECT MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, MT.complaint_no as complaintNo , TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo ,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName,
			TSA.intLabelId,TD.intId as userRoleId
			FROM complaint_managment AS MT
			JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
			LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
			WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND tinStatus = 8
			AND (:complaintNo='0' or MT.complaint_no like CONCAT('%',:complaintNo,'%'))  and (:contactNo='0' or MT.contact_no like CONCAT('%',:contactNo,'%'))
			ORDER BY TSA.dtmStatusDate DESC""",nativeQuery=true,
			countQuery = """
				SELECT count(1)
				FROM complaint_managment AS MT
				JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
				LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
				WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND tinStatus = 8
				AND (:complaintNo='0' or MT.complaint_no like CONCAT('%',:complaintNo,'%'))  and (:contactNo='0' or MT.contact_no like CONCAT('%',:contactNo,'%'))
				ORDER BY TSA.dtmStatusDate DESC""")
	Page<Map<String,Object>> getAllApprovedData(@Param("intId")Integer intId, @Param("complaintNo") String complaintNo, @Param("contactNo") String contactNo, Pageable pageRequest);

	
	@Query(value="""
			SELECT MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, MT.complaint_no as complaintNo , TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo ,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName ,
			TSA.intLabelId,TD.intId as userRoleId
			FROM complaint_managment AS MT
			JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
			LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
			WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND tinStatus = 7
			AND (:complaintNo='0' or MT.complaint_no like CONCAT('%',:complaintNo,'%'))  and (:contactNo='0' or MT.contact_no like CONCAT('%',:contactNo,'%'))
			ORDER BY TSA.dtmStatusDate DESC""",nativeQuery=true,
			countQuery = """
				SELECT count(1)
				FROM complaint_managment AS MT
				JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
				LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
				WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND tinStatus = 7
				AND (:complaintNo='0' or MT.complaint_no like CONCAT('%',:complaintNo,'%'))  and (:contactNo='0' or MT.contact_no like CONCAT('%',:contactNo,'%'))
				ORDER BY TSA.dtmStatusDate DESC""")
	Page<Map<String,Object>> getAllRejectedData(@Param("intId")Integer intId, @Param("complaintNo") String complaintNo, @Param("contactNo") String contactNo, Pageable pageRequest);
	
	
	@Query(value="""
			SELECT MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo ,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName,
			TSA.intLabelId,TD.intId as userRoleId
			FROM complaint_managment AS MT
			JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
			LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
			WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND MT.applicationStatus = :applicationStatus
			AND (:complaintNo='0' or MT.complaint_no like CONCAT('%',:complaintNo,'%'))  and (:contactNo='0' or MT.contact_no like CONCAT('%',:contactNo,'%'))
			ORDER BY TSA.dtmStatusDate DESC""", nativeQuery=true,
			countQuery = """
				SELECT count(1)
				FROM complaint_managment AS MT
				JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
				LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
				WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND MT.applicationStatus = :applicationStatus
				AND (:complaintNo='0' or MT.complaint_no like CONCAT('%',:complaintNo,'%'))  and (:contactNo='0' or MT.contact_no like CONCAT('%',:contactNo,'%'))
				ORDER BY TSA.dtmStatusDate DESC""")
	Page<Map<String,Object>> getAllApprovedDataNew(Integer intId, String applicationStatus, @Param("complaintNo") String complaintNo, @Param("contactNo") String contactNo, Pageable pageRequest);

	
	@Query(value="""
			SELECT MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo ,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName ,
			TSA.intLabelId,TD.intId as userRoleId
			FROM complaint_managment AS MT
			JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
			LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
			WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND MT.applicationStatus = :applicationStatus
			AND (:complaintNo='0' or MT.complaint_no like CONCAT('%',:complaintNo,'%'))  and (:contactNo='0' or MT.contact_no like CONCAT('%',:contactNo,'%'))
			ORDER BY TSA.dtmStatusDate DESC""", nativeQuery=true,
			countQuery = """
				SELECT count(1)
				FROM complaint_managment AS MT
				JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
				LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
				WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND MT.applicationStatus = :applicationStatus
				AND (:complaintNo='0' or MT.complaint_no like CONCAT('%',:complaintNo,'%'))  and (:contactNo='0' or MT.contact_no like CONCAT('%',:contactNo,'%'))
				ORDER BY TSA.dtmStatusDate DESC""")
	Page<Map<String,Object>> getAllRejectedDataNew(Integer intId, String applicationStatus, @Param("complaintNo") String complaintNo, @Param("contactNo") String contactNo, Pageable pageRequest);
	
	
	
	@Query(value = """
		       WITH ComplaintRecords AS (
	           SELECT
	           MT.vchActionOnApplication, MT.ActionCondition, MT.intCMPId AS intId, MT.dtmCreatedOn,
	           MT.complaint_no AS complaintNo,TSA.dtmStatusDate,TSA.tinStatus, TSA.intPendingAt,
	           (SELECT vchRoleName FROM m_admin_role WHERE intId=:roleId) AS vchRolename,
	           TSA.intStageNo, TSA.tinVerifiedBy, TSA.vchApprovalDoc, TSA.tinVerifyLetterGenStatus,
	           (SELECT mu.vchFullName FROM m_admin_user AS mu WHERE TSA.tinStatus = 4 AND mu.intId = TSA.intForwardedUserId AND mu.bitDeletedFlag = 0) AS userName,
	           TSA.intLabelId,  complaint_icm.icmRoleId AS userRoleId, user.vchFullName AS user_name, user.vchMobileNumber AS contact_no,
	           TSA.vchForwardAllAction, complaint_icm.icmStatus, complaint_icm.icmUserRoleId AS rolePendingAt, complaint_icm.icmActionTaken,
	           'complaint' AS sourceType
	           FROM complaint_managment_new AS MT
	           JOIN m_admin_user AS user ON user.intId = MT.userId
	           JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
	           JOIN t_complaint_icm AS complaint_icm ON complaint_icm.icmComplaintAppID = MT.intCMPId
	           WHERE
	           TSA.bitDeletedFlag = 0
	           AND TSA.intProcessId=:intId
	           AND TSA.intPendingAt != 0
	           AND complaint_icm.icmUserId=:userId
	           AND complaint_icm.icmUserRoleId=:roleId
	           AND complaint_icm.icmRoleId=:roleIcmId
	           AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
	           ORDER BY TSA.dtmStatusDate DESC
	          ),
	 
	         RevocateRecords AS (
	         SELECT
	         MT.vchActionOnApplication, MT.ActionCondition, MT.intCMPId AS intId,MT.dtmCreatedOn,MT.complaint_no AS complaintNo,
	         TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, 
	         (SELECT vchRoleName FROM m_admin_role WHERE intId=:roleId) AS vchRolename,
	         TSA.intStageNo, TSA.tinVerifiedBy, TSA.vchApprovalDoc, TSA.tinVerifyLetterGenStatus,
	        (SELECT mu.vchFullName FROM m_admin_user AS mu WHERE TSA.tinStatus = 4 AND mu.intId = TSA.intForwardedUserId AND mu.bitDeletedFlag = 0) AS userName,
	        TSA.intLabelId, revocate_icm.icmRoleId AS userRoleId,user.vchFullName AS user_name,user.vchMobileNumber AS contact_no,
	        TSA.vchForwardAllAction,  revocate_icm.icmStatus,revocate_icm.icmUserRoleId AS rolePendingAt, revocate_icm.icmActionTaken,
	        'revocate' AS sourceType
	        FROM complaint_managment_new AS MT
	        JOIN m_admin_user AS user ON user.intId = MT.userId
	        JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
	        JOIN t_revocate_icm AS revocate_icm ON revocate_icm.icmComplaintAppID = MT.intCMPId
	        WHERE
	        TSA.bitDeletedFlag = 0
	        AND TSA.intProcessId=:intId
	        AND TSA.intPendingAt != 0
	        AND revocate_icm.icmUserId=:userId
	        AND revocate_icm.icmUserRoleId=:roleId
	        AND revocate_icm.icmRoleId=:roleIcmId
	        AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
	        ORDER BY TSA.dtmStatusDate DESC
	        )
	       SELECT * FROM (
	       SELECT *,
	        ROW_NUMBER() OVER (PARTITION BY intId ORDER BY dtmStatusDate DESC, sourceType DESC) AS rowNum
	        FROM (
	          SELECT * FROM ComplaintRecords
	          UNION ALL
	          SELECT * FROM RevocateRecords
	        ) AS CombinedRecords
	      ) AS FinalRecords
	    WHERE rowNum = 1
	    OR sourceType = 'complaint'
	    ORDER BY dtmStatusDate DESC
		        """, nativeQuery = true,countQuery = """
		        		
		       WITH ComplaintRecords AS (
 SELECT 
     COUNT(1) AS complaint_count
 FROM complaint_managment_new AS MT
 JOIN m_admin_user AS user ON user.intId = MT.userId
 JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
 JOIN t_complaint_icm AS complaint_icm ON complaint_icm.icmComplaintAppID = MT.intCMPId
 WHERE 
     TSA.bitDeletedFlag = 0
     AND TSA.intProcessId = 160
     AND TSA.intPendingAt != 0
     AND complaint_icm.icmUserId=:userId
	    AND complaint_icm.icmUserRoleId=:roleId
	    AND complaint_icm.icmRoleId=:roleIcmId
),
RevocateRecords AS (
 SELECT 
     COUNT(1) AS revocate_count
 FROM complaint_managment_new AS MT
 JOIN m_admin_user AS user ON user.intId = MT.userId
 JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
 JOIN t_revocate_icm AS revocate_icm ON revocate_icm.icmComplaintAppID = MT.intCMPId
 WHERE 
     TSA.bitDeletedFlag = 0
    AND TSA.intProcessId = 160
    AND TSA.intPendingAt != 0
    AND revocate_icm.icmUserId=:userId
	   AND revocate_icm.icmUserRoleId=:roleId
	   AND revocate_icm.icmRoleId=:roleIcmId
)
SELECT (SELECT complaint_count FROM ComplaintRecords) + (SELECT revocate_count FROM RevocateRecords) 
 AS totcount;
		    """)
		
Page<Map<String,Object>> getRevokeAndICMData(@Param("intId")Integer intId,@Param("roleIcmId") Integer roleIcmId,@Param("roleId") Integer roleId,@Param("userId") Integer userId,@Param("txtFullName") String txtFullName,@Param("txtContactNumber") String txtContactNumber, Pageable pageRequest);
	
	
	
	
	
	
	
	@Query(value="""
			SELECT MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, MT.complaint_no as complaintNo , TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo ,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName ,
			TSA.intLabelId,TD.intId as userRoleId
			FROM complaint_managment AS MT
			JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
			LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
			WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND tinStatus >=0
			AND (:complaintNo='0' or MT.complaint_no like CONCAT('%',:complaintNo,'%'))  and (:contactNo='0' or MT.contact_no like CONCAT('%',:contactNo,'%'))
			ORDER BY TSA.dtmStatusDate DESC""",nativeQuery=true,
			countQuery = """
					SELECT count(1)
					FROM complaint_managment AS MT
					JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
					LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
					WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND tinStatus >=0
					AND (:complaintNo='0' or MT.complaint_no like CONCAT('%',:complaintNo,'%'))  and (:contactNo='0' or MT.contact_no like CONCAT('%',:contactNo,'%'))
					ORDER BY TSA.dtmStatusDate DESC""")
	Page<Map<String,Object>> getAllSummaryData(@Param("intId")Integer intId, @Param("complaintNo") String complaintNo, @Param("contactNo") String contactNo, Pageable pageRequest);
	
//	@Query(value="""
//			SELECT MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, MT.complaint_no as complaintNo , TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo ,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName ,TSA.intLabelId
//			FROM complaint_managment AS MT
//			JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
//			LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
//			WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND tinStatus >=0
//			AND MT.complaint_no like CONCAT('%',:complaintNo,'%') AND MT.contact_no like CONCAT('%',:contactNo,'%')
//			AND TSA.intPendingAt = :pendingAt
//			ORDER BY TSA.dtmStatusDate DESC""",nativeQuery=true,
//			countQuery = """
//					SELECT count(1)
//					FROM complaint_managment AS MT
//					JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
//					LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId
//					WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND tinStatus >=0
//					AND MT.complaint_no like CONCAT('%',:complaintNo,'%') AND MT.contact_no like CONCAT('%',:contactNo,'%')
//					AND TSA.intPendingAt = :pendingAt
//					ORDER BY TSA.dtmStatusDate DESC""")
//	Page<Map<String,Object>> getAllSummaryData(@Param("intId")Integer intId, @Param("complaintNo") String complaintNo, @Param("contactNo") String contactNo, @Param("pendingAt") Integer pendingAt,Pageable pageRequest);
	
	
	Complaint_managment findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	
	@Query("From Complaint_managment where bitDeletedFlag=:bitDeletedFlag  and (:txtFullName='0' or complaintNo = :txtFullName) and (:txtContactNumber='0' or txtContactNumber = :txtContactNumber) and (:intCreatedBy=0 or intCreatedBy = :intCreatedBy)")
	List<Complaint_managment> findAllByBitDeletedFlagAndIntInsertStatus(Boolean bitDeletedFlag,Pageable pageRequest,String txtFullName,String txtContactNumber, Integer intCreatedBy);
	
	Integer countByBitDeletedFlag(Boolean bitDeletedFlag);
	Integer countByBitDeletedFlagAndIntCreatedBy(boolean b, int i);
	
	
	@Query("Select count(c) From Complaint_managment c where bitDeletedFlag=:bitDeletedFlag  and (:txtFullName='0' or complaintNo like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or txtContactNumber like CONCAT('%',:txtContactNumber,'%')) ")
	Integer countByBitDeletedFlagAndIntCreatedBy(@Param("bitDeletedFlag")boolean bitDeletedFlag, @Param("txtFullName")String txtFullName,@Param("txtContactNumber")String txtContactNumber );
	@Transactional
	@Query(value="UPDATE complaint_managment SET vchActionOnApplication=:actiontype , ActionCondition=:actionCondition WHERE intId=:applicationId",nativeQuery = true)
	@Modifying
	int UpdateActionOnApplication(@Param("actiontype")String actiontype,@Param("actionCondition") String actionCondition,@Param("applicationId")Integer applicationId);
	
	
	@Query(value="Select count(*) as count from t_complaint_mgmt_insp_schedule \n"
			+ "inner join complaint_managment on t_complaint_mgmt_insp_schedule.complaintID=complaint_managment.intId\n"
			+ " where inspectorId=:userId AND intRoleId=:roleId AND t_complaint_mgmt_insp_schedule.bitTakeActionStatus = 0",nativeQuery = true)
	Integer checkInspectorAssign(@Param("userId") Integer userId,@Param("roleId") Integer RoleId);
	
	
	@Query(value="""
			SELECT MT.vchActionOnApplication,MT.ActionCondition,MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn,MT.complaint_no as complaintNo, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo,TSA.tinVerifiedBy,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName,
			TSA.intLabelId,TD.intId as userRoleId
			FROM complaint_managment AS MT
			JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
			JOIN t_complaint_mgmt_insp_schedule AS insp_schd on insp_schd.complaintID=MT.intId
			LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, TSA.intPendingAt) > 0 AND TSA.tinStatus != 4) OR (TSA.intForwardedUserId=:userId AND TSA.tinStatus = 4 AND TSA.intForwardedUserId != 0)
			WHERE MT.bitDeletedFlag = 0
			AND TSA.bitDeletedFlag = 0
			AND TSA.intProcessId =:intId
			AND TSA.tinStatus NOT IN (3, 7, 8)
			AND TSA.intPendingAt != 0
			AND TSA.intStageNo NOT IN(2)
			AND insp_schd.inspectorId=:userId
			AND TD.intId =:roleId  and (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or MT.contact_no like CONCAT('%',:txtContactNumber,'%'))
			ORDER BY TSA.dtmStatusDate DESC""" ,nativeQuery=true,
			countQuery = """
				SELECT count(1)
				FROM complaint_managment AS MT
				JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
				JOIN t_complaint_mgmt_insp_schedule AS insp_schd on insp_schd.complaintID=MT.intId
				LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, TSA.intPendingAt) > 0 AND TSA.tinStatus != 4) OR (TSA.intForwardedUserId=:userId AND TSA.tinStatus = 4 AND TSA.intForwardedUserId != 0)
				WHERE MT.bitDeletedFlag = 0
				AND TSA.bitDeletedFlag = 0
				AND TSA.intProcessId =:intId
				AND TSA.tinStatus NOT IN (3, 7, 8)
				AND TSA.intPendingAt != 0
				AND TSA.intStageNo NOT IN(2)
				AND insp_schd.inspectorId=:userId
				AND TD.intId =:roleId  and (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or MT.contact_no like CONCAT('%',:txtContactNumber,'%'))
				ORDER BY TSA.dtmStatusDate DESC""")
	Page<Map<String,Object>> getAllInspectorPendingData(@Param("intId")Integer intId,@Param("roleId") Integer roleId,@Param("userId") Integer userId,@Param("txtFullName") String txtFullName,@Param("txtContactNumber") String txtContactNumber, Pageable pageRequest);
	
	@Query(value="""
			SELECT MT.vchActionOnApplication,MT.ActionCondition,MT.intCMPId as intId, MT.dtmCreatedOn as dtmCreatedOn,MT.complaint_no as complaintNo, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt,(SELECT vchRoleName FROM m_admin_role where intId=:roleId)as vchRolename, TSA.intStageNo,TSA.tinVerifiedBy,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName,
			TSA.intLabelId,complaint_icm.icmRoleId as userRoleId,user.vchFullName as user_name,user.vchMobileNumber as contact_no,TSA.vchForwardAllAction,complaint_icm.icmStatus,complaint_icm.icmUserRoleId as rolePendingAt,complaint_icm.icmActionTaken
			FROM complaint_managment_new AS MT
			JOIN m_admin_user AS user ON user.intId=MT.userId
			JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
			JOIN t_complaint_icm AS complaint_icm on complaint_icm.icmComplaintAppID=MT.intCMPId
			AND TSA.bitDeletedFlag = 0
			AND TSA.intProcessId =:intId
			AND TSA.intPendingAt != 0
			AND complaint_icm.icmUserId=:userId
            AND complaint_icm.icmUserRoleId=:roleId
			AND complaint_icm.icmRoleId =:roleIcmId  
			AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
			ORDER BY TSA.dtmStatusDate DESC""" ,nativeQuery=true,
			countQuery = """
				SELECT count(1)
				FROM complaint_managment_new AS MT
			    JOIN m_admin_user AS user ON user.intId=MT.userId
			    JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
				JOIN t_complaint_icm AS complaint_icm on complaint_icm.icmComplaintAppID=MT.intCMPId
				WHERE MT.bitDeletedFlag = 0
				AND TSA.bitDeletedFlag = 0
				AND TSA.intProcessId =:intId
				AND TSA.intPendingAt != 0
			    AND complaint_icm.icmUserId=:userId
                AND complaint_icm.icmUserRoleId=:roleId
				AND complaint_icm.icmRoleId=:roleIcmId 
				AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
				ORDER BY TSA.dtmStatusDate DESC""")
	
	Page<Map<String,Object>> getICMData(@Param("intId")Integer intId,@Param("roleIcmId") Integer roleIcmId,@Param("roleId") Integer roleId,@Param("userId") Integer userId,@Param("txtFullName") String txtFullName,@Param("txtContactNumber") String txtContactNumber, Pageable pageRequest);
	
	
	
	/*
	 * @Query(value="""
	 * 
	 * WITH RankedData AS ( SELECT *, ROW_NUMBER() OVER (PARTITION BY complaintNo
	 * ORDER BY dtmCreatedOn DESC) AS row_num FROM ( SELECT
	 * MT.vchActionOnApplication, MT.ActionCondition, MT.intCMPId AS intId,
	 * complaint_icm.dtmCreatedOn AS dtmCreatedOn, MT.complaint_no AS complaintNo,
	 * TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, (SELECT vchRoleName FROM
	 * m_admin_role WHERE intId=:roleId) AS vchRolename, TSA.intStageNo,
	 * TSA.tinVerifiedBy, TSA.vchApprovalDoc, TSA.tinVerifyLetterGenStatus, (SELECT
	 * mu.vchFullName FROM m_admin_user AS mu WHERE TSA.tinStatus = 4 AND mu.intId =
	 * TSA.intForwardedUserId AND mu.bitDeletedFlag = 0) AS userName,
	 * TSA.intLabelId, user.vchFullName AS user_name, user.vchMobileNumber AS
	 * contact_no, TSA.vchForwardAllAction, complaint_icm.icmRoleId AS userRoleId,
	 * complaint_icm.icmStatus, complaint_icm.icmUserRoleId AS rolePendingAt,
	 * complaint_icm.icmActionTaken FROM complaint_managment_new AS MT JOIN
	 * m_admin_user AS user ON user.intId = MT.userId JOIN t_online_service_approval
	 * AS TSA ON MT.intCMPId = TSA.intOnlineServiceId LEFT JOIN t_complaint_icm AS
	 * complaint_icm ON complaint_icm.icmComplaintAppID = MT.intCMPId WHERE
	 * TSA.bitDeletedFlag = 0 AND TSA.intProcessId=:intId AND TSA.intPendingAt != 0
	 * AND complaint_icm.icmUserId=:userId AND complaint_icm.icmUserRoleId=:roleId
	 * AND complaint_icm.icmRoleId=:roleIcmId AND (:txtFullName='0' or
	 * MT.complaint_no like CONCAT('%',:txtFullName,'%')) and (:txtContactNumber='0'
	 * or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%')) UNION SELECT
	 * MT.vchActionOnApplication, MT.ActionCondition, MT.intCMPId AS intId,
	 * revocate_icm.dtmCreatedOn AS dtmCreatedOn, MT.complaint_no AS complaintNo,
	 * TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, (SELECT vchRoleName FROM
	 * m_admin_role WHERE intId=:roleId) AS vchRolename, TSA.intStageNo,
	 * TSA.tinVerifiedBy, TSA.vchApprovalDoc, TSA.tinVerifyLetterGenStatus, (SELECT
	 * mu.vchFullName FROM m_admin_user AS mu WHERE TSA.tinStatus = 4 AND mu.intId =
	 * TSA.intForwardedUserId AND mu.bitDeletedFlag = 0) AS userName,
	 * TSA.intLabelId, user.vchFullName AS user_name, user.vchMobileNumber AS
	 * contact_no, TSA.vchForwardAllAction, revocate_icm.icmRoleId AS userRoleId,
	 * revocate_icm.icmStatus, revocate_icm.icmUserRoleId AS rolePendingAt,
	 * revocate_icm.icmActionTaken FROM complaint_managment_new AS MT JOIN
	 * m_admin_user AS user ON user.intId = MT.userId JOIN t_online_service_approval
	 * AS TSA ON MT.intCMPId = TSA.intOnlineServiceId LEFT JOIN t_revocate_icm AS
	 * revocate_icm ON revocate_icm.icmComplaintAppID = MT.intCMPId WHERE
	 * TSA.bitDeletedFlag = 0 AND TSA.intProcessId=:intId AND TSA.intPendingAt != 0
	 * AND revocate_icm.icmUserId=:userId AND revocate_icm.icmUserRoleId=:roleId AND
	 * revocate_icm.icmRoleId=:roleIcmId AND (:txtFullName='0' or MT.complaint_no
	 * like CONCAT('%',:txtFullName,'%')) and (:txtContactNumber='0' or
	 * user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%')) ) AS
	 * CombinedData ) SELECT * FROM RankedData WHERE row_num = 1 ORDER BY
	 * dtmStatusDate DESC""" ,nativeQuery=true)
	 * 
	 * Page<Map<String,Object>> getRevokeAndICMData(@Param("intId")Integer
	 * intId,@Param("roleIcmId") Integer roleIcmId,@Param("roleId") Integer
	 * roleId,@Param("userId") Integer userId,@Param("txtFullName") String
	 * txtFullName,@Param("txtContactNumber") String txtContactNumber, Pageable
	 * pageRequest);
	 * 
	 */

@Query(value="""
			
			WITH RankedData AS (
    SELECT *,
           ROW_NUMBER() OVER (PARTITION BY complaintNo ORDER BY dtmCreatedOn DESC) AS row_num
    FROM (
        SELECT
            MT.vchActionOnApplication, MT.ActionCondition, MT.intCMPId AS intId,
            complaint_icm.dtmCreatedOn AS dtmCreatedOn, MT.complaint_no AS complaintNo,
            TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt,
            (SELECT vchRoleName FROM m_admin_role WHERE intId=:roleId) AS vchRolename,
            TSA.intStageNo, TSA.tinVerifiedBy, TSA.vchApprovalDoc, TSA.tinVerifyLetterGenStatus,
            (SELECT mu.vchFullName FROM m_admin_user AS mu WHERE TSA.tinStatus = 4
             AND mu.intId = TSA.intForwardedUserId AND mu.bitDeletedFlag = 0) AS userName,
            TSA.intLabelId, user.vchFullName AS user_name, user.vchMobileNumber AS contact_no,
            TSA.vchForwardAllAction,
            complaint_icm.icmRoleId AS userRoleId, complaint_icm.icmStatus,
            complaint_icm.icmUserRoleId AS rolePendingAt, complaint_icm.icmActionTaken
        FROM
            complaint_managment_new AS MT
        JOIN
            m_admin_user AS user ON user.intId = MT.userId
        JOIN
            t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
        LEFT JOIN
            t_complaint_icm AS complaint_icm ON complaint_icm.icmComplaintAppID = MT.intCMPId
        WHERE
            TSA.bitDeletedFlag = 0
            AND TSA.intProcessId=:intId
            AND TSA.intPendingAt != 0
            AND complaint_icm.icmUserId=:userId
            AND complaint_icm.icmUserRoleId=:roleId
			AND complaint_icm.icmRoleId IN (12 , 52)
            AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
        UNION
        SELECT
            MT.vchActionOnApplication, MT.ActionCondition, MT.intCMPId AS intId,
            revocate_icm.dtmCreatedOn AS dtmCreatedOn, MT.complaint_no AS complaintNo,
            TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt,
            (SELECT vchRoleName FROM m_admin_role WHERE intId=:roleId) AS vchRolename,
            TSA.intStageNo, TSA.tinVerifiedBy, TSA.vchApprovalDoc, TSA.tinVerifyLetterGenStatus,
            (SELECT mu.vchFullName FROM m_admin_user AS mu WHERE TSA.tinStatus = 4
             AND mu.intId = TSA.intForwardedUserId AND mu.bitDeletedFlag = 0) AS userName,
            TSA.intLabelId, user.vchFullName AS user_name, user.vchMobileNumber AS contact_no,
            TSA.vchForwardAllAction,
            revocate_icm.icmRoleId AS userRoleId, revocate_icm.icmStatus,
            revocate_icm.icmUserRoleId AS rolePendingAt, revocate_icm.icmActionTaken
        FROM
            complaint_managment_new AS MT
        JOIN
            m_admin_user AS user ON user.intId = MT.userId
        JOIN
            t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
        LEFT JOIN
            t_revocate_icm AS revocate_icm ON revocate_icm.icmComplaintAppID = MT.intCMPId
        WHERE
            TSA.bitDeletedFlag = 0
            AND TSA.intProcessId=:intId
            AND TSA.intPendingAt != 0
            AND revocate_icm.icmUserId=:userId
            AND revocate_icm.icmUserRoleId=:roleId
            AND revocate_icm.icmRoleId IN (12 , 52)
            AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
    ) AS CombinedData
)
SELECT *
FROM RankedData
WHERE row_num = 1
ORDER BY dtmStatusDate DESC""" ,nativeQuery=true , countQuery = """
		WITH RankedData AS (
    SELECT *,
           ROW_NUMBER() OVER (PARTITION BY complaintNo ORDER BY dtmCreatedOn DESC) AS row_num
    FROM (
        SELECT
            MT.vchActionOnApplication, MT.ActionCondition, MT.intCMPId AS intId,
            complaint_icm.dtmCreatedOn AS dtmCreatedOn, MT.complaint_no AS complaintNo,
            TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt,
            (SELECT vchRoleName FROM m_admin_role WHERE intId=:roleId) AS vchRolename,
            TSA.intStageNo, TSA.tinVerifiedBy, TSA.vchApprovalDoc, TSA.tinVerifyLetterGenStatus,
            (SELECT mu.vchFullName FROM m_admin_user AS mu WHERE TSA.tinStatus = 4
             AND mu.intId = TSA.intForwardedUserId AND mu.bitDeletedFlag = 0) AS userName,
            TSA.intLabelId, user.vchFullName AS user_name, user.vchMobileNumber AS contact_no,
            TSA.vchForwardAllAction,
            complaint_icm.icmRoleId AS userRoleId, complaint_icm.icmStatus,
            complaint_icm.icmUserRoleId AS rolePendingAt, complaint_icm.icmActionTaken
        FROM
            complaint_managment_new AS MT
        JOIN
            m_admin_user AS user ON user.intId = MT.userId
        JOIN
            t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
        LEFT JOIN
            t_complaint_icm AS complaint_icm ON complaint_icm.icmComplaintAppID = MT.intCMPId
        WHERE
            TSA.bitDeletedFlag = 0
            AND TSA.intProcessId=:intId
            AND TSA.intPendingAt != 0
            AND complaint_icm.icmUserId=:userId
            AND complaint_icm.icmUserRoleId=:roleId
			AND complaint_icm.icmRoleId IN (12 , 52)
            AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
        UNION
        SELECT
            MT.vchActionOnApplication, MT.ActionCondition, MT.intCMPId AS intId,
            revocate_icm.dtmCreatedOn AS dtmCreatedOn, MT.complaint_no AS complaintNo,
            TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt,
            (SELECT vchRoleName FROM m_admin_role WHERE intId=:roleId) AS vchRolename,
            TSA.intStageNo, TSA.tinVerifiedBy, TSA.vchApprovalDoc, TSA.tinVerifyLetterGenStatus,
            (SELECT mu.vchFullName FROM m_admin_user AS mu WHERE TSA.tinStatus = 4
             AND mu.intId = TSA.intForwardedUserId AND mu.bitDeletedFlag = 0) AS userName,
            TSA.intLabelId, user.vchFullName AS user_name, user.vchMobileNumber AS contact_no,
            TSA.vchForwardAllAction,
            revocate_icm.icmRoleId AS userRoleId, revocate_icm.icmStatus,
            revocate_icm.icmUserRoleId AS rolePendingAt, revocate_icm.icmActionTaken
        FROM
            complaint_managment_new AS MT
        JOIN
            m_admin_user AS user ON user.intId = MT.userId
        JOIN
            t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
        LEFT JOIN
            t_revocate_icm AS revocate_icm ON revocate_icm.icmComplaintAppID = MT.intCMPId
        WHERE
            TSA.bitDeletedFlag = 0
            AND TSA.intProcessId=:intId
            AND TSA.intPendingAt != 0
            AND revocate_icm.icmUserId=:userId
            AND revocate_icm.icmUserRoleId=:roleId
            AND revocate_icm.icmRoleId IN (12 , 52)
            AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
    ) AS CombinedData
)
SELECT count(1)
FROM RankedData
WHERE row_num = 1
ORDER BY dtmStatusDate DESC
		
		""")
	
	Page<Map<String,Object>> getRevokeAndICMData(@Param("intId")Integer intId,@Param("roleId") Integer roleId,@Param("userId") Integer userId,@Param("txtFullName") String txtFullName,@Param("txtContactNumber") String txtContactNumber, Pageable pageRequest);
	
		
	
	@Query( value="""
			SELECT count(*) as count
				FROM complaint_managment_new AS MT
			    JOIN m_admin_user AS user ON user.intId=MT.userId
			    JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
				JOIN t_revocate_icm AS reoke_complaint_icm on reoke_complaint_icm.icmComplaintAppID=MT.intCMPId
				WHERE MT.bitDeletedFlag = 0
				AND TSA.bitDeletedFlag = 0
				AND TSA.intPendingAt != 0
                AND TSA.intProcessId=160
                AND reoke_complaint_icm.icmComplaintAppID=:appId 
			""", nativeQuery=true)
	Integer checkIcmApplicationRevokeAssign(@Param("appId") Integer appId);

	
	@Query(value="""
			SELECT intCMPId FROM ewrsdevdb.complaint_managment_new where applicationStatus='Suspended'
			""",nativeQuery=true)
	List<Integer> getApplicationList();
	
	
	  @Query(value = """
				SELECT MT.vchActionOnApplication,MT.ActionCondition,MT.intCMPId as intId, MT.dtmCreatedOn as dtmCreatedOn,MT.complaint_no as complaintNo, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt,
				(SELECT vchRoleName FROM m_admin_role where intId=:roleId)as vchRolename, TSA.intStageNo,TSA.tinVerifiedBy,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,
				(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName,
				TSA.intLabelId,reoke_complaint_icm.icmRoleId as userRoleId,user.vchFullName as user_name,
				user.vchMobileNumber as contact_no,TSA.vchForwardAllAction,reoke_complaint_icm.icmStatus,
				reoke_complaint_icm.icmUserRoleId as rolePendingAt,reoke_complaint_icm.icmActionTaken
				FROM complaint_managment_new AS MT
				JOIN m_admin_user AS user ON user.intId=MT.userId
				JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
				JOIN t_revocate_icm AS reoke_complaint_icm on reoke_complaint_icm.icmComplaintAppID=MT.intCMPId
				AND TSA.bitDeletedFlag = 0
				AND TSA.intProcessId=:intId
				AND TSA.intPendingAt != 0
				AND reoke_complaint_icm.icmUserId=:userId 
                AND reoke_complaint_icm.icmUserRoleId=:roleId 
			    AND reoke_complaint_icm.icmRoleId=:roleIcmId 
                AND reoke_complaint_icm.icmComplaintAppID=:appId
                AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
			    ORDER BY TSA.dtmStatusDate DESC 
		        """, nativeQuery = true,countQuery = """
				SELECT count(1)
				FROM complaint_managment_new AS MT
				JOIN m_admin_user AS user ON user.intId=MT.userId
				JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
				JOIN t_revocate_icm AS reoke_complaint_icm on reoke_complaint_icm.icmComplaintAppID=MT.intCMPId
				AND TSA.bitDeletedFlag = 0
				AND TSA.intProcessId=:intId
				AND TSA.intPendingAt != 0
				AND reoke_complaint_icm.icmUserId=:userId 
                AND reoke_complaint_icm.icmUserRoleId=:roleId 
			    AND reoke_complaint_icm.icmRoleId=:roleIcmId 
                AND reoke_complaint_icm.icmComplaintAppID=:appId
                AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
			    ORDER BY TSA.dtmStatusDate DESC""")
		    Page<Map<String, Object>> getRevokeData(
		        @Param("intId") Integer intId,
		        @Param("roleIcmId") Integer roleIcmId,
		        @Param("roleId") Integer roleId,
		        @Param("userId") Integer userId,
		        @Param("appId") Integer appId,
		        @Param("txtFullName") String txtFullName,
		        @Param("txtContactNumber") String txtContactNumber,
		        Pageable pageRequest
		    );
	
	
	  
	  @Query(value = """
				SELECT MT.vchActionOnApplication,MT.ActionCondition,MT.intCMPId as intId, MT.dtmCreatedOn as dtmCreatedOn,MT.complaint_no as complaintNo, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt,
				(SELECT vchRoleName FROM m_admin_role where intId=TSA.intPendingAt)as vchRolename, TSA.intStageNo,TSA.tinVerifiedBy,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,
				(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName,
				TSA.intLabelId,reoke_complaint_icm.icmRoleId as userRoleId,user.vchFullName as user_name,
				user.vchMobileNumber as contact_no,TSA.vchForwardAllAction,reoke_complaint_icm.icmStatus,
				reoke_complaint_icm.icmUserRoleId as rolePendingAt,reoke_complaint_icm.icmActionTaken
				FROM complaint_managment_new AS MT
				JOIN m_admin_user AS user ON user.intId=MT.userId
				JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
				JOIN t_revocate_icm AS reoke_complaint_icm on reoke_complaint_icm.icmComplaintAppID=MT.intCMPId
				AND TSA.bitDeletedFlag = 0
				AND TSA.intProcessId=:intId
				AND TSA.intPendingAt != 0
              AND reoke_complaint_icm.icmComplaintAppID=:appId
              AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
			    ORDER BY TSA.dtmStatusDate DESC 
		        """, nativeQuery = true,countQuery = """
				SELECT count(1)
				FROM complaint_managment_new AS MT
				JOIN m_admin_user AS user ON user.intId=MT.userId
				JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
				JOIN t_revocate_icm AS reoke_complaint_icm on reoke_complaint_icm.icmComplaintAppID=MT.intCMPId
				AND TSA.bitDeletedFlag = 0
				AND TSA.intProcessId=:intId
				AND TSA.intPendingAt != 0
			    AND reoke_complaint_icm.icmComplaintAppID=:appId
              AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
			    ORDER BY TSA.dtmStatusDate DESC""")
		    Page<Map<String, Object>> getAssignRevokeData(
		        @Param("intId") Integer intId,
		        @Param("appId") Integer appId,
		        @Param("txtFullName") String txtFullName,
		        @Param("txtContactNumber") String txtContactNumber,
		        Pageable pageRequest
		    );
	  
	  
	  
	  
	
	@Query(value="""
			SELECT MT.vchActionOnApplication,MT.ActionCondition,MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo,TSA.tinVerifiedBy,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName,
			TSA.intLabelId,TD.intId as userRoleId
			FROM complaint_managment AS MT
			JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
			LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, TSA.intPendingAt) > 0 AND TSA.tinStatus != 4) OR (TSA.intForwardedUserId=:userId AND TSA.tinStatus = 4 AND TSA.intForwardedUserId != 0)
			WHERE MT.bitDeletedFlag = 0
			AND TSA.bitDeletedFlag = 0
			AND TSA.intProcessId =:intId
			AND TSA.intPendingAt != 0
			AND TSA.intPendingAt != 0
			AND TD.intId =:roleId  and (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or MT.contact_no like CONCAT('%',:txtContactNumber,'%'))
			ORDER BY TSA.dtmStatusDate DESC""",nativeQuery=true,
			countQuery = """
				SELECT count(1)
				FROM complaint_managment AS MT
				JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId
				LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, TSA.intPendingAt) > 0 AND TSA.tinStatus != 4) OR (TSA.intForwardedUserId=:userId AND TSA.tinStatus = 4 AND TSA.intForwardedUserId != 0)
				WHERE MT.bitDeletedFlag = 0
				AND TSA.bitDeletedFlag = 0
				AND TSA.intProcessId =:intId
				AND TSA.intPendingAt != 0
				AND TSA.intPendingAt != 0
				AND TD.intId =:roleId  and (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or MT.contact_no like CONCAT('%',:txtContactNumber,'%'))
				ORDER BY TSA.dtmStatusDate DESC""")
	Page<Map<String,Object>> getAllAssignData(@Param("intId")Integer intId,@Param("roleId") Integer roleId,@Param("userId") Integer userId,@Param("txtFullName") String txtFullName,@Param("txtContactNumber") String txtContactNumber, Pageable pageRequest);
	
	
	@Query(value=""" 
			SELECT MT.vchActionOnApplication,MT.ActionCondition,MT.intCMPId as intId,MT.dtmCreatedOn as dtmCreatedOn, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo,TSA.tinVerifiedBy,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName,
			TSA.intLabelId,TD.intId as userRoleId,user.vchFullName as user_name,user.vchMobileNumber as contact_no,TSA.vchForwardAllAction,TSA.intPendingAt as rolePendingAt
			FROM complaint_managment_new AS MT
			JOIN m_admin_user AS user ON user.intId=MT.userId
			JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
			LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, TSA.intPendingAt) > 0 AND TSA.tinStatus != 4) OR (TSA.intForwardedUserId=:userId AND TSA.tinStatus = 4 AND TSA.intForwardedUserId != 0)
			WHERE MT.bitDeletedFlag = 0
			AND TSA.bitDeletedFlag = 0
			AND TSA.intProcessId =:intId
			AND TSA.intPendingAt != 0
			AND TSA.intPendingAt != 0 
			AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
			ORDER BY TSA.dtmStatusDate DESC""",nativeQuery=true,
			countQuery = """
				SELECT count(1)
				FROM complaint_managment_new AS MT
				JOIN m_admin_user AS user ON user.intId=MT.userId
				JOIN t_online_service_approval AS TSA ON MT.intCMPId = TSA.intOnlineServiceId
				LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, TSA.intPendingAt) > 0 AND TSA.tinStatus != 4) OR (TSA.intForwardedUserId=:userId AND TSA.tinStatus = 4 AND TSA.intForwardedUserId != 0)
				WHERE MT.bitDeletedFlag = 0
				AND TSA.bitDeletedFlag = 0
				AND TSA.intProcessId =:intId
				AND TSA.intPendingAt != 0
				AND TSA.intPendingAt != 0
				AND (:txtFullName='0' or MT.complaint_no like CONCAT('%',:txtFullName,'%'))  and (:txtContactNumber='0' or user.vchMobileNumber like CONCAT('%',:txtContactNumber,'%'))
				ORDER BY TSA.dtmStatusDate DESC""")
	Page<Map<String,Object>> getAllReviewData(@Param("intId")Integer intId,@Param("userId") Integer userId,@Param("txtFullName") String txtFullName,@Param("txtContactNumber") String txtContactNumber, Pageable pageRequest);
	
	
	
	
	@Query("Select complaintNo from Complaint_managment order by complaintNo desc limit 1")
	String getId();
	@Query("SELECT c FROM Complaint_managment c " +
			"WHERE c.bitDeletedFlag = false AND c.intInsertStatus = 1 " +
			"AND c.applicationStatus IN :applicationStatuses " +
			"AND c.rdoComplaintAgainst = 101 " +
			"AND (:search IS NULL OR " +
			"LOWER(c.selNameofCollateralManager) LIKE LOWER(CONCAT('%', :search, '%')) " +
			"OR LOWER(c.selWarehouseShopName) LIKE LOWER(CONCAT('%', :search, '%')) " +
			"OR LOWER(c.txtWarehouseOperator) LIKE LOWER(CONCAT('%', :search, '%')) " +
			"OR LOWER(c.vchActionOnApplication) LIKE LOWER(CONCAT('%', :search, '%')) " +
			"OR LOWER(c.applicationStatus) LIKE LOWER(CONCAT('%', :search, '%')) " +
			"OR LOWER(c.txtFullName) LIKE LOWER(CONCAT('%', :search, '%')) " +
			"OR CAST(c.txtContactNumber AS string) LIKE CONCAT('%', :search, '%')) ")
	Page<Complaint_managment> findByFilters(String search, Pageable pageable, List<ComplaintApplicationStatus> applicationStatuses);

	Optional<Complaint_managment> findBySelNameofCollateralManagerAndBitDeletedFlag(String selNameofCollateralManager, boolean bitDeletedFlag );
	
	@Query( value="""
			SELECT count(*) as countValue  FROM t_complaint_icm where icmUserId =:userId AND icmRoleId=:roleIcmId
			AND icmUserRoleId =:roleId  AND icmActionTaken =false AND icmStatus = 'APPLICATION_ASSIGNED'
			""", nativeQuery=true)
	Integer checkIcmApplicationAssign(@Param("roleIcmId") Integer roleIcmId,@Param("roleId") Integer roleId,@Param("userId") Integer userId);
	
	
	@Query(value="""
			SELECT icmRoleId,intCLCRoleId FROM m_admin_user where intId=:userId AND intRoleId=:roleId AND bitDeletedFlag=0 
			""",nativeQuery = true)
	List<Object[]> getCommitteeRoleId(Integer userId,Integer roleId);
	
	
	
	 @Query(value = """
		        SELECT icmRoleId, icmUserRoleId, icmUserId, icmUserName, dtmCreatedOn, dtmUpdatedOn, icmComplaintAppId 
		        FROM t_complaint_icm 
		        WHERE icmComplaintAppId = :appId 
		        AND intLableId = :lableId
		    """, nativeQuery = true)
		    List<Object[]> findByICMemberList(@Param("appId") Integer appId, @Param("lableId") Integer labelId);
	

		    @Query(value = """
			        SELECT icmRoleId, icmUserRoleId, icmUserId, icmUserName, dtmCreatedOn, dtmUpdatedOn, icmComplaintAppId 
			        FROM t_revocate_icm 
			        WHERE icmComplaintAppId = :appId 
			        AND intLableId = :lableId
			    """, nativeQuery = true)
			    List<Object[]> findByRevokeICMemberList(@Param("appId") Integer appId, @Param("lableId") Integer labelId);
}