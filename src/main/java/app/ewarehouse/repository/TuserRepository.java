package app.ewarehouse.repository;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.ewarehouse.entity.LoginTrail;
import app.ewarehouse.entity.Tuser;
import jakarta.persistence.Tuple;
@Repository
public interface TuserRepository extends JpaRepository<Tuser, Integer> {
Tuser findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

@Query("From Tuser where  bitDeletedFlag=:bitDeletedFlag AND selRole!=0 ")
List<Tuser> findAllByBitDeletedFlag(Boolean bitDeletedFlag);

@Query("From Tuser where  bitDeletedFlag=:bitDeletedFlag AND selRole!=0 AND warehouseId IS NOT NULL")
List<Tuser> findAllWhUserByBitDeletedFlag(Boolean bitDeletedFlag);

@Query("SELECT t FROM Tuser t WHERE t.warehouseId = :warehouseId AND t.bitDeletedFlag = :bitDeletedFlag")
List<Tuser> findAllByWareHouseIdAndBitDeletedFlag(@Param("warehouseId") String warehouseId, @Param("bitDeletedFlag") Boolean bitDeletedFlag);



@Query("From Tuser where selEmployeeType=:id and bitDeletedFlag=:bitDeletedFlag")
List<Tuser> findByUserDataBySelEmpId(@Param("id") Integer id,Boolean bitDeletedFlag);
Tuser findByTxtUserIdAndTxtPasswordAndBitDeletedFlag(String txtUserId, String txtPassword, boolean bitDeletedFlag);
@Query("From Tuser where txtUserId=:txtUserId and bitDeletedFlag=:bitDeletedFlag ")
Tuser getByUserId(String txtUserId,boolean bitDeletedFlag);
@Query("From Tuser where selDepartment=:id and bitDeletedFlag=:bitDeletedFlag")
List<Tuser> getBydeptIdandbitDeletFlag(@Param("id")Integer id, boolean bitDeletedFlag);
@Query("From Tuser where selDesignation=:id and bitDeletedFlag=:bitDeletedFlag")
List<Tuser> getByDesignationIdandBitDeletedFlag(@Param("id")Integer id, boolean bitDeletedFlag);
@Query("From Tuser where selRole=:id and bitDeletedFlag=:bitDeletedFlag")
List<Tuser> getByRoleIdandBitDeletedFlag(@Param("id")Integer id, boolean bitDeletedFlag);
@Query("From Tuser where selGroup=:id and bitDeletedFlag=:bitDeletedFlag")
List<Tuser> getByGroupsIdAndBitDeletedFlag(@Param("id")Integer id, boolean bitDeletedFlag);

@Query("Select txtFullName from Tuser Where intId=:intUserId and bitDeletedFlag=false")
String getUserNameById(@Param("intUserId")Integer intUserId);

@Query("Select chkPrevilege From Tuser where intId=:userId AND bitDeletedFlag=:bitDeletedFlag")
Integer getcheckPrevilegeByUserId(@Param("userId")Integer userId, boolean bitDeletedFlag );
@Query("From Tuser where bitDeletedFlag=:bitDeletedFlag and (:selDepartment=0 or selDepartment=:selDepartment)and (:selRole=0 or selRole=:selRole) and (:selDesignation=0 or selDesignation=:selDesignation) ")
List<Tuser> findBySelDepartmentAndSelRoleAndSelDesignationAndBitDeletedFlag(Integer selDepartment, Integer selRole,
		Integer selDesignation, boolean bitDeletedFlag);

Tuser getByTxtUserIdAndTxtEmailIdAndBitDeletedFlag(String loginId, String emailId, Boolean bitDeletedFlag);
Tuser getByTxtUserIdAndBitDeletedFlag(String loginId,Boolean bitDeletedFlag);
@Query(value=" select mu.intId,mu.vchFullName,mr.vchRoleName from m_admin_user as mu \r\n"
		+ "  left join m_admin_role as mr on mu.intRoleId=mr.intId and mr.bitDeletedFlag=0\r\n"
		+ "  where mu.intRoleId!=:intRoleId and mu.intRoleId!=0 and mu.bitDeletedFlag=0 order by mu.vchFullName asc",nativeQuery = true)
List<Map<String ,Object>> findUserListByRoleId(@Param("intRoleId")Integer intRoleId);

@Query("Select selRole from Tuser Where intId=:userId and bitDeletedFlag=false ")
Integer findByRoleIdByUserId(@Param("userId") Integer userId);

	List<Tuser> getByTxtEmailId(String emailId);

//	@Query(value = "SELECT intId , vchFullName,intRoleId FROM m_admin_user where bitDeletedFlag = 0 and intRoleId = (select intId from m_admin_role where vchRoleName = 'Inspector') AND (vchUserStatus IS NULL OR vchUserStatus NOT LIKE 'Suspend')" , nativeQuery = true)
//	List<Tuple> getInspectors();
	
	@Query(value = "SELECT intId , vchFullName,intRoleId FROM m_admin_user where bitDeletedFlag = 0 and intRoleId = (select intId from m_admin_role where vchRoleName = 'Inspector') AND (vchUserStatus IS NULL OR vchUserStatus NOT LIKE 'Suspend') AND (intCountyId IS NOT NULL AND intSubCountyId IS NOT NULL AND intWardId IS NOT NULL)" , nativeQuery = true)
	List<Tuple> getInspectors();
	
	
	
	
	@Query(value = """
			   SELECT intId , vchFullName FROM m_admin_user where bitDeletedFlag = 0 and intCountyId=:countyId and intSubCountyId=:subCountyId and intRoleId = (select intId from m_admin_role where intId =:roleId) AND (vchUserStatus IS NULL OR vchUserStatus NOT LIKE 'Suspend')
			""" , nativeQuery = true)
	List<Tuple> getNotSuspendedUser(Integer countyId, Integer subCountyId, Integer roleId);
	
	
	@Query(value = "SELECT intId , vchFullName FROM m_admin_user where bitDeletedFlag = 0 and intRoleId = (select intId from m_admin_role where vchRoleName = 'CECEMInspector') AND (vchUserStatus IS NULL OR vchUserStatus NOT LIKE 'Suspend')" , nativeQuery = true)
	List<Tuple> getCECMInspectors();

	@Query(value = "SELECT intId , vchFullName FROM m_admin_user where bitDeletedFlag = 0 and intRoleId = (select intId from m_admin_role where vchRoleName = 'Collateral Manager') AND (vchUserStatus IS NULL OR vchUserStatus NOT IN ('Suspend'))" , nativeQuery = true)
	List<Tuple> getCollateral();
	
	@Query(value ="""
			SELECT intId, vchFullName FROM m_admin_user WHERE bitDeletedFlag = 0 AND intRoleId =:roleId
			  And vchWareHouse=:vchWareHouse
			  AND (vchUserStatus IS NULL OR vchUserStatus != 'Suspend')
			"""  , nativeQuery = true)
	List<Tuple> getWareHouseWorkerByWarehouseAndRole(String vchWareHouse,Integer roleId);
	
	
	@Query(value ="""
			SELECT intId, vchFullName FROM m_admin_user WHERE bitDeletedFlag = 0 AND intRoleId =:roleId
			  AND (vchUserStatus IS NULL OR vchUserStatus != 'Suspend')
			"""  , nativeQuery = true)
	List<Tuple> getWareHouseWorkerByRole(Integer roleId);
	
	
	
//	@Query(value ="""
//			SELECT intId, vchFullName,vchWarehouse FROM m_admin_user WHERE bitDeletedFlag = 0 AND intRoleId =:roleId
//			  AND (vchUserStatus IS NULL OR vchUserStatus != 'Suspend')
//			"""  , nativeQuery = true)
//	List<Tuple> getWareHouseEmpByRole(Integer roleId);
	
	
	@Query(value ="""
			  SELECT intId, vchFullName,vchWarehouse FROM m_admin_user WHERE bitDeletedFlag = 0 AND intRoleId =:roleId
			  AND (vchUserStatus IS NULL OR vchUserStatus != 'Suspend')
			  AND (intCountyId IS NOT NULL AND intSubCountyId IS NOT NULL AND intWardId IS NOT NULL)
			"""  , nativeQuery = true)
	List<Tuple> getWareHouseEmpByRole(Integer roleId);  
	
	
	@Query(value="SELECT vchRoleName FROM m_admin_role where intId=:roleId",nativeQuery = true)
	String getPendignAtUser(@Param("roleId") Integer roleId);
    
	@Transactional
	@Modifying
	@Query("UPDATE Tuser c SET c.bitIsTwoFactorAuthEnabled = NOT c.bitIsTwoFactorAuthEnabled WHERE c.intId = :id")
	void changeUserStatus(Integer id);
	
	@Modifying
	@Query("UPDATE Tuser u SET u.bitIsSaveActivityLogEnabled = :data WHERE u.intId!=1")
	int updateActivityLog(Boolean data);
	
	@Modifying
	@Query("UPDATE Tuser u SET u.bitIsTwoFactorAuthEnabled = :data WHERE u.intId!=1")
	int updateTwoFactorAuthEnabled(Boolean data);

	@Query("SELECT s.actionStatus FROM SecuritySetting s WHERE s.bitDeletedFlag = false AND s.ssType = 'Save my Activity Logs'")
	Boolean getActionStatus();

	@Modifying
	@Query("UPDATE SecuritySetting ss SET ss.actionStatus= :ssactionStatus WHERE ss.ssType='Save my Activity Logs'")
	int updateActionStatus(Boolean ssactionStatus);

	@Query("SELECT t.stmUpdatedOn FROM Tuser t WHERE t.intId = 1")
	Date getLastChangedPswd();
	
	@Transactional
	@Modifying
	@Query("UPDATE Tuser c SET c.bitIsSaveActivityLogEnabled = NOT c.bitIsSaveActivityLogEnabled WHERE c.intId = :id")
	void updateActivityLogStatusByUser(Integer id);

	@Query("From Tuser Where txtUserId=:userId and bitDeletedFlag=false ")
	Optional<Tuser> findByUserId(String userId);
    
	@Query(value = "select intId from m_admin_user where vchLoginId = :str and bitDeletedFlag=false", nativeQuery = true)
	Integer findRelatedIdById(String str);
	
	@Query("SELECT l.dateTime FROM LoginTrail l WHERE l.user.intId = :userId AND l.action = 'Login' order by l.dateTime desc LIMIT 1")
	LocalDateTime getLastLoginTime(Integer userId);
   
	/*
	 * @Query(value = "SELECT vchIpAddress, intUserId, vchOs , dtmDateTime\r\n" +
	 * "FROM t_login_trail\r\n" + "WHERE intUserId = :userId\r\n" +
	 * "  AND DATE(dtmDateTime) = CURDATE()order by dtmDateTime desc limit 1",
	 * nativeQuery = true)
	 */
	
	@Query(value = "SELECT vchIpAddress, vchOs, dtmDateTime "
            + "FROM t_login_trail "
            + "WHERE intUserId = :userId "
            + "AND enmStatus = 'SUCCESS' "
            + "AND vchAction = 'Login' "
            + "AND DATE(dtmDateTime) = CURDATE() "
            + "ORDER BY dtmDateTime DESC LIMIT 2", nativeQuery = true)
	List<Tuple> findDistinctCountsForLogin(@Param("userId") Integer userId);
	
	@Query(value = "SELECT vchIpAddress, vchOs, dtmDateTime "
            + "FROM t_login_trail "
            + "WHERE intUserId = :userId "
            + "AND enmStatus = 'SUCCESS' "
            + "AND vchAction = 'Login' "
            + "AND DATE(dtmDateTime) = CURDATE() "
            + "ORDER BY dtmDateTime DESC LIMIT 1", nativeQuery = true)
	List<Tuple> findLastLoginIPFDetails(@Param("userId") Integer userId);

	
	@Query(value = """
	        SELECT COUNT(*) 
	        FROM t_login_trail 
	        WHERE enmStatus = 'SUCCESS' 
	        AND vchAction = 'Logout' 
	        AND dtmDateTime > :dateTime 
	        AND vchIpAddress = :lastLoginIp
	    """, nativeQuery = true)
	    Integer userIsLogout(@Param("dateTime") Timestamp dateTime, @Param("lastLoginIp") String lastLoginIp);
	
	@Query(value = "select bitIsTwoFactorAuthEnabled from m_admin_user where intId = :userId", nativeQuery = true)
	Boolean findBitIsTwoFactorAuthEnabledByIntId(@Param("userId") Integer userId);

	@Query(value = "SELECT intId as userId ,vchFullName as fullName,intRoleId as roleId,icmRoleId as icMemberRoleId FROM m_admin_user where icmRoleId=:roleId and bitDeletedFlag=0;" , nativeQuery = true)
	List<Tuple> getIcMembers(Integer roleId);
	
	@Query(value = "SELECT intId as userId ,vchFullName as fullName,intRoleId as roleId,intCLCRoleId as icMemberRoleId FROM m_admin_user where intCLCRoleId=:roleId and bitDeletedFlag=0" , nativeQuery = true)
	List<Tuple> getClCMembers(Integer roleId);

	@Query(value = "SELECT intid,committeemember,roleId FROM t_committee_members where bitDeletedFlag=0" , nativeQuery = true)
	List<Tuple> getCommitteeMembersList();
	
	@Query("SELECT t.txtFullName FROM Tuser t WHERE t.intId = :intId")
    String findFullNameById(@Param("intId") Integer intId);
	
	Tuser findByTxtEmailId(String email);

	@Query(value = "select intId , vchFullName from m_admin_user where intRoleId = 59;" , nativeQuery = true)
	List<Tuple> getCCMembers();

	@Query(value="select vchFullName from m_admin_user  where intId=:createdBy",nativeQuery = true)
	String getUserName(Integer createdBy);

	Tuser findByIntId(Integer createdBy);

	boolean existsByTxtUserId(String emailId);

	Optional<Tuser> findBySelRole(Integer selRole);

	@Query("SELECT t.warehouseId FROM Tuser t WHERE t.intId = :intId")
    String findWarehouseIdByIntId(@Param("intId") Integer intId);
	
	@Query(value="select * from m_admin_user where vchEmailId=:emailId and bitDeletedFlag=:bitDeletedFlag", nativeQuery = true)
	Tuser getByUseremailId(String emailId,boolean bitDeletedFlag);
	
}

