package app.ewarehouse.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.CommonUserWiseDto;
import app.ewarehouse.service.CommonUserWiseService;
import app.ewarehouse.service.LogInService;
import app.ewarehouse.service.TuserService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.EmailUtil;
@Service
public class CommonUserWiseServiceImpl implements CommonUserWiseService {
	
	
	/*
	 * @Autowired private EmailUtil emailUtil;
	 * 
	 * 
	 * @Autowired private SMSUtil smsUtil;
	 */
	
	@Autowired
	LogInService service;
	
	@Autowired
	TuserService userService;
	
	@Autowired
    private ObjectMapper om;
	 

	    @Override
	    public String processActivityByUser(String data) {
	    	String decodedData = CommonUtil.inputStreamDecoder(data);
			om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    	JSONObject json = new JSONObject();
	    	
	    	try {
	    		CommonUserWiseDto dto = om.readValue(decodedData,
	    				CommonUserWiseDto.class);
	    		List<Map<String, Object>> activityType = dto.getActivityType();
		    	boolean emailSent = false;

		    	if (activityType == null || activityType.isEmpty()) {
		    		
		    		Integer uId =userService.findRelatedIdById(dto.getUserName());
					List<Map<String, Object>> totalRes = service.getUserWiseNotificationStatus(uId);
					Optional<Boolean> status = totalRes.stream()
				            .filter(entry -> dto.getActivity().equals(entry.get("notificationSubCategoryName")))
				            .map(entry -> (Boolean) entry.get("notificSubcatStatus"))
				            .findFirst();
					 if (status.isPresent()) {
				           if(Boolean.TRUE.equals(status.get())) {
				        	   List<String> messageModes = dto.getMessageMode(); 
			    	            if (messageModes != null && !messageModes.isEmpty()) {
			    	               
			    	                if (messageModes.contains("email")) {
			    	                    if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
			    	                     Boolean sendStatus = EmailUtil.sendMail("Notification", dto.getMessage(), dto.getEmail());
			    	                        if(Boolean.TRUE.equals(sendStatus)) {
				    	                        emailSent = true;
				    	                     	}
				    	                     	else {
				    	                     		emailSent = false;
				    	                     	}
			    	                        
			    	                    }
			    	                }
			    	                // Check if "sms" is present in the messageMode list
				    	               /* if (messageModes.contains("sms")) {
				    	                    if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty()) {
				    	                    	try {
													smsUtil.sms(dto.getPhoneNumber(), dto.getMessage());
												} catch (KeyManagementException | NoSuchAlgorithmException e) {
													e.printStackTrace();
												} 
				    	                        smsSent = true;
				    	                    }
				    	                }*/
			    	            }
				           }
				           else {
				        	   json.put("status", "Matching activity found, but status did not match.");
				           }
				        } else {
				        	json.put("status", "No matching activity found for: " + dto.getActivity());
				            
				        }
		    	}
		    	else {
		    		boolean activityMatched = false;
			    	boolean statusMatched = false;

		    	for (Map<String, Object> activity : activityType) {
		    	    String notificationSubCategoryName = (String) activity.get("notificationSubCategoryName");

		    	    // Check if activity matches
		    	    if (notificationSubCategoryName.equals(dto.getActivity())) {
		    	        activityMatched = true; 

		    	        // Check if status is true
		    	        if (Boolean.TRUE.equals(activity.get("notificSubcatStatus"))) {
		    	            statusMatched = true; 

		    	            List<String> messageModes = dto.getMessageMode(); 
		    	            if (messageModes != null && !messageModes.isEmpty()) {
		    	               
		    	                if (messageModes.contains("email")) {
		    	                    if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
		    	                     Boolean sendStatus =  EmailUtil.sendMail("Notification", dto.getMessage(), dto.getEmail());
		    	                     	if(Boolean.TRUE.equals(sendStatus)) {
		    	                        emailSent = true;
		    	                     	}
		    	                     	else {
		    	                     		emailSent = false;
		    	                     	}
		    	                    }
		    	                }
		    	                // Check if "sms" is present in the messageMode list
			    	               /* if (messageModes.contains("sms")) {
			    	                    if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty()) {
			    	                    	try {
												smsUtil.sms(dto.getPhoneNumber(), dto.getMessage());
											} catch (KeyManagementException | NoSuchAlgorithmException e) {
												e.printStackTrace();
											} 
			    	                        smsSent = true;
			    	                    }
			    	                }*/
		    	            }
		    	        }

		    	        break;
		    	    }
		    	}
		    	if (!activityMatched) {
		    		json.put("status", "No matching activity found for: " + dto.getActivity());
		    	   // return "No matching activity found for: " + dto.getActivity();
		    	}
		    	if (activityMatched && !statusMatched) {
		    		json.put("status", "Matching activity found, but status did not match.");
		    	   // return "Matching activity found, but status did not match.";
		    	}

		    	
		    	}
		    	
		    	

		    	
		    	if(emailSent) {
		    		json.put("status", "Email Sent");
		    	}else {
		    		json.put("status", "There might be some error");
		    	}
			} catch (Exception e) {
				json.put("status", "There is some internal server error");
			}
	    	
//	    	
//	    	StringBuilder result = new StringBuilder("Processing completed: ");
//	    	if (emailSent) result.append("Email sent. ");
	    	//if (smsSent) result.append("SMS sent. ");
	    	return json.toString();

	    	
	    	
	    }  	
	    	
	    	
	    	
	    	
	    	
	    	

}
