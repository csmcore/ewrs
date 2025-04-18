package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.Trolepermission;
import jakarta.transaction.Transactional;
@Repository
public interface TrolepermissionRepository extends JpaRepository<Trolepermission, Integer> {
	Trolepermission findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	@Query("From Trolepermission where bitDeletedFlag=:bitDeletedFlag")
	List<Trolepermission> findAllByBitDeletedFlag(Boolean bitDeletedFlag);

	@Query("From Trolepermission where selSelectRole=:intRole and bitDeletedFlag=:bitDeletedFlag")
	List<Trolepermission> findByRoleId(@Param("intRole") Integer intRole, Boolean bitDeletedFlag);

	@Query("From Trolepermission where selSelectUser=:intUser and bitDeletedFlag=:bitDeletedFlag")
	List<Trolepermission> findByUserId(@Param("intUser") Integer intUser, Boolean bitDeletedFlag);

	@Query("From Trolepermission where selPermissionFor=:selPermissionFor and selSelectRole=:selSelectRole and bitDeletedFlag=:bitDeletedFlag")
	List<Trolepermission> getByForPermissionAndRoleIdAndBitDeletedFlag(Integer selPermissionFor, Integer selSelectRole,
			Boolean bitDeletedFlag);

	@Transactional
	@Query("DELETE FROM Trolepermission e " + "WHERE e.selPermissionFor = :selPermissionFor "
			+ "AND e.selSelectRole = :selSelectRole " + "AND e.bitDeletedFlag = :bitDeletedFlag")
	void deleteAllBySelPermissionForAndSelSelectRoleAndBitDeletedFlag(Integer selPermissionFor, Integer selSelectRole,
			Boolean bitDeletedFlag);

	@Query(value = "select l.intId,l.intParentLinkId,l.intLinkType,l.vchLinkName,rp.intRole,rp.intViewManageRight,\n"
			+ "rp.intEditRight,rp.intDelete,rp.publish,rp.intadd,rp.intall From m_admin_menulinks l\n"
			+ "left join m_admin_rolepermission rp On  rp.intLinkId=l.intId WHERE l.bitDeletedFlag=0;", nativeQuery = true)
	List<Object[]> getAllDataBindindlinkAndRoleTbl();

	@Query("From Trolepermission where selSelectRole=:roleId")
	List<Trolepermission> getByRoleId(@Param("roleId") Integer roleId);

	@Query("FROM Trolepermission e " + "WHERE e.selPermissionFor = :selPermissionFor "
			+ "AND e.selSelectRole = :selSelectRole " + "AND e.bitDeletedFlag = :bitDeletedFlag")
	List<Trolepermission> getDataByUsingthisId(Integer selPermissionFor, Integer selSelectRole, boolean bitDeletedFlag);

	@Query("From Trolepermission where selPermissionFor=:selPermissionFor and selSelectUser=:selSelectUser and bitDeletedFlag=:bitDeletedFlag")
	List<Trolepermission> findByintForPermissionAndSelUser(Integer selPermissionFor, Integer selSelectUser,
			boolean bitDeletedFlag);

	@Query(value = "  SELECT DATE(TR.dtmCreatedOn), TR.tinForPermission, TR.intRole, TR.intUserId, TM.vchRoleName,\n"
			+ "(select TU.vchFullName from m_admin_user as TU where TU.intId=TR.intUserId AND TU.bitDeletedFlag=0) \n"
			+ "FROM m_admin_rolepermission AS TR Left JOIN m_admin_role AS TM ON TR.intRole = TM.intId WHERE TR.bitDeletedFlag=0\n"
			+ "GROUP BY DATE(TR.dtmCreatedOn), TR.tinForPermission, TR.intRole, TR.intUserId, TM.vchRoleName \n"
			+ "ORDER BY DATE(TR.dtmCreatedOn) DESC", nativeQuery = true)
	List<Object[]> getDataFromRoleTableAndUserAndRolePermission();

	@Query("From Trolepermission where selSelectRole=:roleId AND selSelectUser=:userId AND bitDeletedFlag=:bitDeletedFlag")
	List<Trolepermission> getByRoleIDAndUserId(Integer roleId, Integer userId, boolean bitDeletedFlag);

	@Query("From Trolepermission where selSelectRole=:roleId AND vchLinkId=:linkId AND bitDeletedFlag=:bitDeletedFlag")
	Trolepermission findByIntRoleAndIntLinkIdAndBitDeletedFlag(@Param("roleId") Integer roleId,
			@Param("linkId") String linkId, boolean bitDeletedFlag);

	@Query("From Trolepermission where selSelectUser=:userId AND bitDeletedFlag=:bitDeletedFlag")
	List<Trolepermission> getRolePermissionListByUserId(@Param("userId") Integer userId, boolean bitDeletedFlag);

	@Query("From Trolepermission where selSelectUser=:userId AND vchLinkId=:linkId AND bitDeletedFlag=:bitDeletedFlag")
	Trolepermission findBySelUserAndIntLinkIdAndBitDeletedFlag(@Param("userId") Integer userId,
			@Param("linkId") String linkId, boolean bitDeletedFlag);

	@Query("Select count(*) as count From Trolepermission where selSelectUser=:userId AND bitDeletedFlag=:bitDeletedFlag")
	Integer checkRolePermissionListByUserId(@Param("userId") Integer userId, boolean bitDeletedFlag);

	@Query("Select count(*) as count From Trolepermission where selSelectUser=:userId AND vchLinkId=:linkId AND bitDeletedFlag=:bitDeletedFlag")
	Integer checkBySelUserAndIntLinkIdAndBitDeletedFlag(@Param("userId") Integer userId,@Param("linkId") String linkId, boolean bitDeletedFlag);
	
	@Query("Select count(*) as count from Trolepermission where selSelectRole=:roleId AND vchLinkId=:linkId AND bitDeletedFlag=:bitDeletedFlag")
	Integer checkByIntRoleAndIntLinkIdAndBitDeletedFlag(@Param("roleId") Integer roleId,@Param("linkId") String linkId, boolean bitDeletedFlag);
	

}
