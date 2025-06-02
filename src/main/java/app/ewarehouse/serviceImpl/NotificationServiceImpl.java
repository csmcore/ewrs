package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.SendNotificationDto;
import app.ewarehouse.entity.NotificationEntity;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.NotificationRepository;
import app.ewarehouse.service.NotificationService;
import app.ewarehouse.util.Mapper;

@Service
public class NotificationServiceImpl implements NotificationService{

	
	@Autowired
	NotificationRepository  notifyRepository;
	@Override
	public SendNotificationDto setNotifications(NotificationEntity notificationDto) {

		SendNotificationDto sendNotificationDto=null;
		
		try {
			notifyRepository.save(notificationDto);
			
			sendNotificationDto=Mapper.mapSendNotificationDtoResponse(notificationDto);
		}
		catch(Exception e)
		{
			 throw new CustomGeneralException("Notification is not saved" + e);
		}
		
		return sendNotificationDto;
	}
	@Override
	public List<SendNotificationDto> getAllNotificationsById(Integer uid) {
		
		List<NotificationEntity> notifyEntity=notifyRepository.findByToAuthIdAndReadStatusFalseOrderByCreatedOnDesc(uid);
		  List<SendNotificationDto>  notificationList=new ArrayList<SendNotificationDto>();
		   if(notifyEntity!=null&&notifyEntity.size()>0) {
			   for (NotificationEntity notificationDto : notifyEntity) {
				   SendNotificationDto sendNotificationDto=Mapper.mapSendNotificationDtoResponse(notificationDto);
					
					notificationList.add(sendNotificationDto);
			}   
			   
		   }
		 
		return notificationList;
	}
	@Override
	public SendNotificationDto setMarkAllNotificationsById(Integer uid) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SendNotificationDto setMarkAllNotifications(List<SendNotificationDto> notificationlist) {
		
		SendNotificationDto notification_Dto=null;
		
		if(notificationlist!=null&&notificationlist.size()>0) {
			List<NotificationEntity>  notifyList = new ArrayList<NotificationEntity>();
		
			   for (SendNotificationDto notificationDto : notificationlist) {
				   NotificationEntity notifyEntity  = Mapper.mapSendNotificationDtoToNotificationEntity(notificationDto);
					
				   notifyList.add(notifyEntity);
			}   
		
			   notifyRepository.saveAll(notifyList);
			   
			   notification_Dto= SendNotificationDto.builder().notification("All Notification Marked").notificationType("message").bitReadStatus(true).build();
			   
		}
		else {
			 notification_Dto= SendNotificationDto.builder().notification("All Notification not Marked").notificationType("Fail").bitReadStatus(false).build();
		}
		
		return notification_Dto;
	}
	@Override  
	public List<Integer> getCMUserList(Integer lableId, Integer processId, Integer ComplaintAppId,Integer roleid) {
		
		List<Object[]> CMUserList=notifyRepository.getCMUserList(lableId, processId, ComplaintAppId,roleid);
		List<Integer> userlist=new ArrayList<Integer>();
		for(Object[]cmuserobj:CMUserList) {
			
			userlist.add((Integer)cmuserobj[0]);
			
		}
		
		
		
		return userlist;
	}
	
	@Override
	public List<Integer> getOPLCMUserList(Integer ComplaintAppId,Integer roleid) {
		
		List<Object[]> CMUserList=notifyRepository.getOPLCMUserList(ComplaintAppId,roleid);
		List<Integer> userlist=new ArrayList<Integer>();
		for(Object[]cmuserobj:CMUserList) {
			
			userlist.add((Integer)cmuserobj[0]);
			
		}
		
		
		
		return userlist;
	}

	
	
	
	
}
