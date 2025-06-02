package app.ewarehouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintIcm;

@Repository
public interface ComplaintIcmRepository extends JpaRepository<ComplaintIcm, Long> {
	
 public ComplaintIcm findByIcmComplaintAppIdAndIcmUserIdAndIcmUserRoleIdAndIcmActionTaken(Integer id,Integer userId,Integer roleId,boolean actionTaken);
 
 public ComplaintIcm findByIcmComplaintAppIdAndIcmUserIdAndIcmActionTakenAndNotificationFlag(Integer appid,Integer userId,boolean actionTaken,boolean NotificationFlag);
}

