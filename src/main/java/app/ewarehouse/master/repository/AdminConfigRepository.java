package app.ewarehouse.master.repository;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.AdminConfiguration;

@Repository
public interface AdminConfigRepository extends JpaRepository<AdminConfiguration, Integer> {

	AdminConfiguration findByUserName(String userName);

	@Query(value = "SELECT * FROM m_admin_config where bitDeletedFlag=0 ORDER BY dtmCreatedOn DESC", nativeQuery = true)
	Page<AdminConfiguration> findAllByOrderByDtmCreatedAtDesc(Pageable pageable);

	@Query(value = "SELECT count(bitLoginAttempted) FROM t_login_trail where bitDeletedFlag=0 and vchAction=:action and enmStatus=:status and vchIpAddress=:ip ", nativeQuery = true)
	long count(String ip, String action, String status);
	
	@Query(value = "SELECT noOfAttempt FROM m_admin_config where bitDeletedFlag=0 ORDER BY dtmCreatedOn DESC", nativeQuery = true)
	Integer getMaxAttempt();

	@Query(value = "SELECT dtmDateTime FROM t_login_trail where bitLoginAttempted=1 and bitDeletedFlag=0 and vchAction=:action and enmStatus=:status and vchEnteredUserName=:userId ORDER BY dtmDateTime LIMIT 1", nativeQuery = true)
	String getLastFailedTime(String userId, String action, String status);
	
	
	@Query(value = "SELECT noOfAttempt,idleTime FROM m_admin_config where bitDeletedFlag=0 ", nativeQuery = true)
	Map<String,Object> getNoOfAttemptAndIdleTimeValue();

	@Query(value = "SELECT COUNT(bitLoginAttempted) FROM t_login_trail " +
            "WHERE bitLoginAttempted=1 AND enmStatus='FAILURE' AND dtmDateTime >= :failedDateTime AND dtmDateTime <= :now",nativeQuery = true)	
	long countBetweenLastAndCurrentTime(LocalDateTime failedDateTime, LocalDateTime now);

	@Modifying
	@Query(value = "UPDATE t_login_trail SET bitLoginAttempted=0 WHERE vchAction=:action and enmStatus=:status and vchEnteredUserName=:userId", nativeQuery = true)
	void updateLoginStatus(String userId, String action, String status);

	@Query(value = "SELECT count(*) FROM m_admin_user where tinStatus=0 and vchLoginId=:userId", nativeQuery = true)
	Integer checkUserExist(String userId);

	@Modifying
	@Query(value = "UPDATE m_admin_user SET isLocked=1 WHERE vchLoginId=:userId ", nativeQuery = true)
	void blockUser(String userId);

	@Modifying
	@Query(value = "UPDATE m_admin_user SET isLocked=0 WHERE vchLoginId=:userId ", nativeQuery = true)
	void unBlockUser(String userId);

	@Query(value = "SELECT isLocked FROM m_admin_user WHERE vchLoginId=:userId ", nativeQuery = true)
	boolean isUserLocked(String userId);
	
	@Query(value = "SELECT vchEmailId FROM m_admin_user WHERE vchLoginId=:userId ", nativeQuery = true)
	String getEmailId(String userId);

	@Query(value = "SELECT vchLoginId FROM m_admin_user WHERE intId=:id ", nativeQuery = true)
	String findUserNameById(Integer id);
	
}
