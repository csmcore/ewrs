package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.ComplaintIcm;
import app.ewarehouse.entity.OplCLC;

@Repository
public interface OPLCLCRepository extends JpaRepository<OplCLC, Long> {
	
 public OplCLC findByIcmComplaintAppIdAndIcmUserIdAndIcmUserRoleIdAndIcmActionTaken(Integer id,Integer userId,Integer roleId,boolean actionTaken);
 
 public OplCLC findByIcmComplaintAppIdAndIcmUserIdAndIcmActionTakenAndNotificationFlag(Integer appid,Integer userId,boolean actionTaken,boolean NotificationFlag);
}

