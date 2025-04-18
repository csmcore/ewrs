package app.ewarehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.ewarehouse.entity.NotificationSettingSubCategory;

@Repository
public interface NotificationSettingSubCategoryRepository extends JpaRepository<NotificationSettingSubCategory,Integer> {
    
	List<NotificationSettingSubCategory> findByBitDeletedFlagFalse();

	@Transactional
	@Modifying
	@Query("UPDATE NotificationSettingSubCategory c SET c.notificSubcatStatus = NOT c.notificSubcatStatus WHERE c.notificationSubCategoryId = :id")
	void changeStatusSubCategory(@Param("id") Integer id);

}
