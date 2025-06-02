package app.ewarehouse.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import app.ewarehouse.dto.SendNotificationDto;
import app.ewarehouse.entity.ComplaintIcm;
import app.ewarehouse.entity.NotificationEntity;
import app.ewarehouse.entity.OplSetAuthority;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.repository.ComplaintIcmRepository;
import app.ewarehouse.repository.NotificationRepository;
import app.ewarehouse.repository.OplSetAuthorityRepository;
import app.ewarehouse.repository.TrolepermissionRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.serviceImpl.NotificationServiceImpl;
import app.ewarehouse.util.FnAuthority;

@Controller
public class NotificationController {

    @Autowired
	NotificationServiceImpl notificationServiceImpl;
    
    @Autowired
    FnAuthority fnAuthority;
    
    @Autowired
    TuserRepository userRepo;
    
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
	private TrolepermissionRepository trolepermissionRepository;
    @Autowired
    private ComplaintIcmRepository  complaintRepo;
    @Autowired
	NotificationRepository  notifyRepository;
    @Autowired
	OplSetAuthorityRepository setAuthorityRepository;
    @MessageMapping("/sendNotification")  
    public void notifyUser(NotificationEntity message) {
    	SendNotificationDto setNotificationDto = notificationServiceImpl.setNotifications(message);   // This sends the message to the specific user
        messagingTemplate.convertAndSendToUser(message.getToAuthId().toString(), "/topic/getNotifications", setNotificationDto);
    }
    
    
    
    
    
    // Receive messages from clients
//    @SendTo("/topic/getNotifications")  // Broadcast to all clients subscribed to this topic
//    public SendNotificationDto sendNotification(NotificationEntity notificationDto) {
//    	
//    	
//    	 SendNotificationDto setNotificationDto = notificationServiceImpl.setNotifications(notificationDto);
//    	
//        return setNotificationDto;  // Send the message back
//    }
    

    
    @MessageMapping("/markAllNotification")  // Receive messages from clients
    @SendTo("/topic/getNotifications")  // Broadcast to all clients subscribed to this topic
    public SendNotificationDto setMarkAllNotifications(List<SendNotificationDto> notificationlist) {
    	
    	 SendNotificationDto setNotificationDto = notificationServiceImpl.setMarkAllNotifications(notificationlist);
    	
        return setNotificationDto;  // Send the message back
    }    
    
    
    
	/*
	 * // For sending a notification from the server-side code public void
	 * sendNotificationToUser(String message) {
	 * messagingTemplate.convertAndSend("/topic/getNotifications", message); }
	 */
    
    
    @MessageMapping("/notifyOPLUsers")
    public void notificationForOPL(NotificationEntity message) {
		List<Integer> user_id_List = null;
		if (message != null && message.getNotificationModule().equals("Operator License")) {
			
			if (message.getStageNo() == 0 || message.getStageNo() == 2||message.getStageNo() == 3) {
				OplSetAuthority setAuthority = setAuthorityRepository.findByProcessIdAndStageNoAndLabelId(200,
						message.getStageNo() + 1, 301);
				Integer setauthId = setAuthority.getSetAuthId();
				Integer roleId = setAuthority.getPrimaryAuth();
				message.setToAuthId(setauthId);
				List<Integer> userIdList = userRepo.getByRoleIdandBitDeletedFlag(roleId, false).stream()
						.map(Tuser::getIntId).toList();
				user_id_List = new ArrayList<Integer>();
				for (Integer userid : userIdList) {
					Integer checkedPermission = trolepermissionRepository
							.checkBySelUserAndIntLinkIdAndBitDeletedFlag(userid, message.getLinkId().toString(), false);
					if (checkedPermission == 1) {
						user_id_List.add(userid);
					}

					else {
						Integer checkedPermissionRole = trolepermissionRepository
								.checkByIntRoleAndIntLinkIdAndBitDeletedFlag(roleId, message.getLinkId().toString(),
										false);
						if (checkedPermissionRole == 1) {
							user_id_List.add(userid);
						}

					}

				}
				if (user_id_List != null) {
					user_id_List.stream().forEach(id -> {
						message.setToAuthId(id);
						SendNotificationDto setNotificationDto = notificationServiceImpl.setNotifications(message);
//						notifyRepository.setOPLNotificationStatus(message.getLabelId(), message.getStageNo() + 1,
//								message.getCompAppId(), roleId, setNotificationDto.getAuthNotificationNo());
						messagingTemplate.convertAndSendToUser(id.toString(), "/topic/getNotifications",
								setNotificationDto);

					});
				}

			}

			else if (message.getStageNo() == 1) {

				message.setNotification("Provide Report for Operator License");
				OplSetAuthority setAuthority = setAuthorityRepository.findByProcessIdAndStageNoAndLabelId(200,
						message.getStageNo() + 1, 301);
				Integer roleId = setAuthority.getPrimaryAuth();
				Integer setauthId = setAuthority.getSetAuthId();
				message.setToAuthId(setauthId);
				List<Integer> userIdList = notificationServiceImpl.getOPLCMUserList(message.getCompAppId(), roleId);
				user_id_List = new ArrayList<Integer>();
				for (Integer userid : userIdList) {
					Integer checkedPermission = trolepermissionRepository
							.checkBySelUserAndIntLinkIdAndBitDeletedFlag(userid, message.getLinkId().toString(), false);
					if (checkedPermission == 1) {
						user_id_List.add(userid);
					}

					else {
						Integer checkedPermissionRole = trolepermissionRepository
								.checkByIntRoleAndIntLinkIdAndBitDeletedFlag(roleId, message.getLinkId().toString(),
										false);

						if (checkedPermissionRole == 1) {
							user_id_List.add(userid);
						}

					}

				}
				if (user_id_List != null) {
					user_id_List.stream().forEach(id -> {
						message.setToAuthId(id);
						SendNotificationDto setNotificationDto = notificationServiceImpl.setNotifications(message);
						// ComplaintIcm cmuserObj=
						// complaintRepo.findByIcmComplaintAppIdAndIcmUserIdAndIcmActionTakenAndNotificationFlag(message.getCompAppId(),id,false,false);
						// cmuserObj.setNotificationFlag(true);
						// cmuserObj.setSentNotificationId(setNotificationDto.getAuthNotificationNo());
						// complaintRepo.save(cmuserObj);
						messagingTemplate.convertAndSendToUser(id.toString(), "/topic/getNotifications",
								setNotificationDto);

					});
				}

			}
			
			
			
		}
    	
    }
    
    @MessageMapping("/notifyUsers")
    public void notifyUsers(NotificationEntity message) {
    	message.setVchPath("");
		JSONObject authority = null;
		if (message != null && message.getNotificationModule().equals("Complaint")) {
			List<Integer> user_id_List=null;
			if (message.getStageNo() == 0) {
				authority = fnAuthority.getAuthority(message.getIntProcessId(), message.getStageNo() + 1,
						message.getLabelId());
				Integer roleId = authority.getInt("pendingAuthorities");
				List<Integer> userIdList = userRepo.getByRoleIdandBitDeletedFlag(roleId, false).stream()
						.map(Tuser::getIntId).toList();
				 user_id_List = new ArrayList<Integer>();
				for (Integer userid : userIdList) {
					Integer checkedPermission = trolepermissionRepository.checkBySelUserAndIntLinkIdAndBitDeletedFlag(userid, message.getLinkId().toString(), false);
					if (checkedPermission == 1) {
						user_id_List.add(userid);
					}
					
					else {
						Integer checkedPermissionRole = trolepermissionRepository
								.checkByIntRoleAndIntLinkIdAndBitDeletedFlag(roleId, message.getLinkId().toString(),
										false);
						if (checkedPermissionRole == 1) {
							user_id_List.add(userid);
						}

					}
					 

				}
				if(user_id_List!=null) {
					user_id_List.stream().forEach(id->{
		 				message.setToAuthId(id);
		 				SendNotificationDto setNotificationDto = notificationServiceImpl.setNotifications(message);
		 				//notifyRepository.setNotificationStatus(message.getLabelId(), message.getStageNo() + 1,message.getCompAppId(),roleId,setNotificationDto.getAuthNotificationNo());
		 			    	 messagingTemplate.convertAndSendToUser(id.toString(), "/topic/getNotifications", setNotificationDto); 
		 				 
		 			 });
				}

			}
			else if(message.getStageNo() == 1) {
				
				message.setNotification("Provide Report for Complaint");
				authority = fnAuthority.getAuthority(message.getIntProcessId(), message.getStageNo() + 1,
						message.getLabelId());
				Integer roleId = authority.getInt("pendingAuthorities");
				List<Integer> userIdList = notificationServiceImpl.getCMUserList(message.getLabelId(),message.getIntProcessId(),message.getCompAppId(),roleId);
				 user_id_List = new ArrayList<Integer>();
				for (Integer userid : userIdList) {
					Integer checkedPermission = trolepermissionRepository.checkBySelUserAndIntLinkIdAndBitDeletedFlag(userid, message.getLinkId().toString(), false);
					if (checkedPermission == 1) {
						user_id_List.add(userid);
					}
					
					else {
						Integer checkedPermissionRole = trolepermissionRepository
								.checkByIntRoleAndIntLinkIdAndBitDeletedFlag(roleId, message.getLinkId().toString(),
										false);

						if (checkedPermissionRole == 1) {
							user_id_List.add(userid);
						}

					}
					 

				}
				if(user_id_List!=null) {
					user_id_List.stream().forEach(id->{
		 				message.setToAuthId(id);
		 				SendNotificationDto setNotificationDto = notificationServiceImpl.setNotifications(message);
		 				ComplaintIcm  cmuserObj=	complaintRepo.findByIcmComplaintAppIdAndIcmUserIdAndIcmActionTakenAndNotificationFlag(message.getCompAppId(),id,false,false);
		 				cmuserObj.setNotificationFlag(true);
		 				cmuserObj.setSentNotificationId(setNotificationDto.getAuthNotificationNo());
		 				complaintRepo.save(cmuserObj);
		 			    	 messagingTemplate.convertAndSendToUser(id.toString(), "/topic/getNotifications", setNotificationDto); 
		 				 
		 			 });
				}
				
				
			}
			
			else if(message.getStageNo() == 2||message.getStageNo() == 3&&message.getAppRoleId()==52||message.getAppRoleId()==12) {
				message.setNotification("Report Submitted for Complaint");
				authority = fnAuthority.getAuthority(message.getIntProcessId(), 3,
						message.getLabelId());
				Integer roleId = authority.getInt("pendingAuthorities");
				List<Integer> userIdList = userRepo.getByRoleIdandBitDeletedFlag(roleId, false).stream()
						.map(Tuser::getIntId).toList();
				 user_id_List = new ArrayList<Integer>();
				for (Integer userid : userIdList) {
					Integer checkedPermission = trolepermissionRepository.checkBySelUserAndIntLinkIdAndBitDeletedFlag(userid, message.getLinkId().toString(), false);
					if (checkedPermission == 1) {
						user_id_List.add(userid);
					}
					
					else {
						Integer checkedPermissionRole = trolepermissionRepository
								.checkByIntRoleAndIntLinkIdAndBitDeletedFlag(roleId, message.getLinkId().toString(),
										false);

						if (checkedPermissionRole == 1) {
							user_id_List.add(userid);
						}

					}
					 

				}
				if(user_id_List!=null) {
					user_id_List.stream().forEach(id->{
		 				message.setToAuthId(id);
		 				SendNotificationDto setNotificationDto = notificationServiceImpl.setNotifications(message);
		 				notifyRepository.setNotificationStatus(message.getLabelId(), message.getStageNo() + 1,message.getCompAppId(),roleId,setNotificationDto.getAuthNotificationNo());
		 			    	 messagingTemplate.convertAndSendToUser(id.toString(), "/topic/getNotifications", setNotificationDto); 
		 				 
		 			 });
				}
								
			}
			
			else if(message.getStageNo() == 3&&message.getAppRoleId()==5||message.getAppRoleId()==13) {
				
				if(message.getIntAction()==8 && message.getLabelId()==104) {
					message.setNotification("Approve the complaint The decision to suspend has been made");
					message.setVchPath("");
					Integer graderUserId=notifyRepository.getGraderUserId(message.getLabelId(),message.getCompAppId());
					SendNotificationDto setNotificationDto = notificationServiceImpl.setNotifications(message);
					notifyRepository.setComplaintNotificationStatus(message.getLabelId(),message.getCompAppId(),setNotificationDto.getAuthNotificationNo());
	 			    messagingTemplate.convertAndSendToUser(graderUserId.toString(), "/topic/getNotifications", setNotificationDto); 
				}
				else if(message.getIntAction()==8 && message.getLabelId()==103) {
					message.setNotification("Approve the complaint The decision to suspend has been made");
					message.setVchPath("");
					Integer graderUserId=notifyRepository.getInspectorId(message.getLabelId(),message.getCompAppId());
					SendNotificationDto setNotificationDto = notificationServiceImpl.setNotifications(message);
					notifyRepository.setComplaintNotificationStatus(message.getLabelId(),message.getCompAppId(),setNotificationDto.getAuthNotificationNo());
	 			    messagingTemplate.convertAndSendToUser(graderUserId.toString(), "/topic/getNotifications", setNotificationDto); 
				}
				else if(message.getIntAction()==8 && message.getLabelId()==102) {
					message.setNotification("Approve the complaint The decision to suspend has been made");
					message.setVchPath("");
					Integer graderUserId=notifyRepository.getColleatrolId(message.getLabelId(),message.getCompAppId());
					SendNotificationDto setNotificationDto = notificationServiceImpl.setNotifications(message);
					notifyRepository.setComplaintNotificationStatus(message.getLabelId(),message.getCompAppId(),setNotificationDto.getAuthNotificationNo());
	 			    messagingTemplate.convertAndSendToUser(graderUserId.toString(), "/topic/getNotifications", setNotificationDto); 
				}

			}
			
		}

	}
}
