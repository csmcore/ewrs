package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.RevocateICM;

@Repository
public interface RevocateICMRepository extends JpaRepository<RevocateICM, Long> {

	public RevocateICM findByIcmComplaintAppIdAndIcmUserIdAndIcmUserRoleIdAndIcmActionTaken(Integer id,Integer userId,Integer roleId,boolean actionTaken);
	 
	 public RevocateICM findByIcmComplaintAppIdAndIcmUserIdAndIcmActionTakenAndNotificationFlag(Integer appid,Integer userId,boolean actionTaken,boolean NotificationFlag);
}
