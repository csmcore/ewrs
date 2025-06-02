package app.ewarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.ewarehouse.entity.OplCLC;
import app.ewarehouse.entity.OplInspectorIcm;

@Repository
public interface OPLInspectorIcmRepository extends JpaRepository<OplInspectorIcm, Long> {
	
 public OplInspectorIcm findByIcmComplaintAppIdAndIcmUserIdAndIcmUserRoleIdAndIcmActionTaken(Integer id,Integer userId,Integer roleId,boolean actionTaken);
 
 public OplInspectorIcm findByIcmComplaintAppIdAndIcmUserIdAndIcmActionTakenAndNotificationFlag(Integer appid,Integer userId,boolean actionTaken,boolean NotificationFlag);
}

