package app.ewarehouse.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.NotificationSettingSubCategoryService;
import app.ewarehouse.service.UserWiseNotificationService;
import app.ewarehouse.util.CommonUtil;

@RestController
@RequestMapping("/user")
public class UserWiseNotificationController {
	 private static final Logger logger = LoggerFactory.getLogger(UserWiseNotificationController.class);

	private NotificationSettingSubCategoryService notificationSettingSubCategoryService;
	private final ObjectMapper objectMapper;
	
	private UserWiseNotificationService userWiseNotificationService;

	public UserWiseNotificationController(NotificationSettingSubCategoryService notificationSettingSubCategoryService,
			ObjectMapper objectMapper, UserWiseNotificationService userWiseNotificationService) {
		super();
		this.notificationSettingSubCategoryService = notificationSettingSubCategoryService;
		this.objectMapper = objectMapper;
		this.userWiseNotificationService = userWiseNotificationService;
	}
	
	
	@GetMapping("/userwiseDetails/{id}")
	public ResponseEntity<String> viewUserWiseNotification(@PathVariable Integer id) throws JsonProcessingException {
		logger.info("Fetching all UserNotification");
		List<Map<String, Object>> userDetails = userWiseNotificationService
				.viewUserWiseNotificationById(id);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(userDetails)).toString());
	}
	
	
	@GetMapping("/userNameEmailDetails/{id}")
	 public ResponseEntity<?> userNameEmailDetails(@PathVariable Integer id) {
		JSONObject response= userWiseNotificationService.userNameEmailDetails(id);
		return  ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
    
	}
	
	
	@GetMapping("/updateUserNotification/{id}")
	public ResponseEntity<String> updateStatusUserNotification(@PathVariable Integer id) throws JsonProcessingException {
		 logger.info("Updating UserNotification with ID: {}", id);
		JSONObject userResponse = userWiseNotificationService.updateStatusUserNotification(id);

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(userResponse)).toString());

	}
	
	private <T> String buildJsonResponse(T response) throws JsonProcessingException {
		Object result;
		if (response instanceof JSONObject) {
			result = response.toString();
		} else {
			result = response;
		}

		return objectMapper.writeValueAsString(ResponseDTO.builder().status(200).result(result).build());
	}
	

}
