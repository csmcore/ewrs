package app.ewarehouse.service;

import java.util.List;

import app.ewarehouse.dto.SendNotificationDto;
import app.ewarehouse.entity.NotificationEntity;

public interface NotificationService {
    
	SendNotificationDto setNotifications(NotificationEntity notificationDto);
	List<SendNotificationDto> getAllNotificationsById(Integer uid);
	SendNotificationDto setMarkAllNotificationsById(Integer uid);
	SendNotificationDto setMarkAllNotifications(List<SendNotificationDto> notificationlist);
	
	List<Integer> getCMUserList(Integer lableId,Integer processId,Integer ComplaintAppId,Integer roleid);
	List<Integer> getOPLCMUserList(Integer ComplaintAppId,Integer roleid);
}



