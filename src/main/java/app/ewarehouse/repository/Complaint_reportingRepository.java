package app.ewarehouse.repository;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Complaint_reporting;

@Repository
public interface Complaint_reportingRepository extends JpaRepository<Complaint_reporting, Integer> {
Complaint_reporting findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

@Query("From Complaint_reporting where bitDeletedFlag=:bitDeletedFlag  and (:txtFullName='0' or txtFullName like CONCAT('%',:txtFullName,'%'))  and (:txtEmailAddress='0' or txtEmailAddress like CONCAT('%',:txtEmailAddress,'%')) ")
List<Complaint_reporting> findAllByBitDeletedFlagAndIntInsertStatus(Boolean bitDeletedFlag,Pageable pageRequest,String txtFullName,String txtEmailAddress);
Integer countByBitDeletedFlag(Boolean bitDeletedFlag);

@Query(value="SELECT MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo,TSA.tinVerifiedBy,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName \r\n"
		+ "FROM complaint_reporting AS MT\r\n"
		+ "JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId\r\n"
		+ "LEFT JOIN m_admin_role AS TD ON(FIND_IN_SET(TD.intId, TSA.intPendingAt) > 0 AND TSA.tinStatus != 4) OR (TSA.intForwardedUserId=:userId AND TSA.tinStatus = 4 AND TSA.intForwardedUserId != 0)\r\n"
		+ "WHERE MT.bitDeletedFlag = 0\r\n"
		+ "  AND TSA.bitDeletedFlag = 0\r\n"
		+ "  AND TSA.intProcessId =:intId \r\n"
		+ "  AND TSA.tinStatus NOT IN (3, 7, 8)\r\n"
		+ "  AND TSA.intPendingAt != 0\r\n"
		+ "  AND TD.intId =:roleId \r\n"
		+ "ORDER BY TSA.dtmStatusDate DESC;" ,nativeQuery=true)
List<Map<String,Object>> getAllPendingData(@Param("intId")Integer intId,@Param("roleId") Integer roleId,@Param("userId") Integer userId);

@Query(value="SELECT MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo ,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName \r\n"
		+ "            FROM complaint_reporting AS MT \r\n"
		+ "            JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId \r\n"
		+ "            LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId \r\n"
		+ "            WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND tinStatus = 8\r\n"
		+ "            ORDER BY TSA.dtmStatusDate DESC;\r\n"
		+ "            ",nativeQuery=true)
List<Map<String,Object>> getAllApprovedData(@Param("intId")Integer intId);

@Query(value="SELECT MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo ,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName\r\n"
		+ "            FROM complaint_reporting AS MT \r\n"
		+ "            JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId \r\n"
		+ "            LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId \r\n"
		+ "            WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND tinStatus = 7\r\n"
		+ "            ORDER BY TSA.dtmStatusDate DESC;",nativeQuery=true)
List<Map<String,Object>> getAllRejectedData(@Param("intId")Integer intId);

@Query(value="SELECT MT.intId as intId, MT.dtmCreatedOn as dtmCreatedOn, TSA.dtmStatusDate, TSA.tinStatus, TSA.intPendingAt, TD.vchRolename, TSA.intStageNo ,TSA.vchApprovalDoc,TSA.tinVerifyLetterGenStatus,(select mu.vchFullName from m_admin_user as mu Where TSA.tinStatus=4 AND mu.intId=TSA.intForwardedUserId and mu.bitDeletedFlag=0) as userName \r\n"
		+ "            FROM complaint_reporting AS MT \r\n"
		+ "            JOIN t_online_service_approval AS TSA ON MT.intId = TSA.intOnlineServiceId \r\n"
		+ "            LEFT JOIN m_admin_role AS TD ON TSA.intPendingAt = TD.intId \r\n"
		+ "            WHERE MT.bitDeletedFlag = 0 AND TSA.bitDeletedFlag = 0 AND TSA.intProcessId =:intId AND tinStatus >=0 \r\n"
		+ "            ORDER BY TSA.dtmStatusDate DESC;",nativeQuery=true)
List<Map<String,Object>> getAllSummaryData(@Param("intId")Integer intId);


}