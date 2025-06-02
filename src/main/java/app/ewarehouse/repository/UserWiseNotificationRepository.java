package app.ewarehouse.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.ewarehouse.entity.Tuser;
import app.ewarehouse.entity.UserWiseNotification;
import jakarta.persistence.Tuple;

@Repository
public interface UserWiseNotificationRepository extends JpaRepository<UserWiseNotification, Integer> {

	
	@Query(value = "SELECT * FROM userwise_notification WHERE intId = :userId AND bitDeletedFlag = false", nativeQuery = true)
    List<UserWiseNotification> findByUserId(@Param("userId") Integer userId);
    
	@Transactional
	@Modifying
	@Query("UPDATE UserWiseNotification c SET c.userwiseNotificStatus = NOT c.userwiseNotificStatus WHERE c.userwiseNotificationId = :id")
	void changeStatusSubCategory(@Param("id") Integer id);
    
	
	@Transactional
	@Modifying
	@Query("UPDATE UserWiseNotification c SET c.userwiseNotificStatus = :value WHERE c.notificationSubCategory.notificationSubCategoryId = :id")
	void changeStatusSubCategoryWithUserWiseNotifiaction(Integer id, Boolean value);
    
	@Query("select txtFullName, txtEmailId from Tuser where intId= :id")
	Tuple fetchingUserNameANdEmail(Integer id);

}
