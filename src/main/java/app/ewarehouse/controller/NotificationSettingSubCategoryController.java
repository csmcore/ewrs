package app.ewarehouse.controller;

import java.util.List;

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

import app.ewarehouse.dto.NotificationCatSubCatDto;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.NotificationSettingSubCategoryService;
import app.ewarehouse.util.CommonUtil;

@RestController
@RequestMapping("/notification")
public class NotificationSettingSubCategoryController {
	 private static final Logger logger = LoggerFactory.getLogger(NotificationSettingSubCategoryController.class);

	
	private NotificationSettingSubCategoryService notificationSettingSubCategoryService;
	private final ObjectMapper objectMapper;

	public NotificationSettingSubCategoryController(
			NotificationSettingSubCategoryService notificationSettingSubCategoryService, ObjectMapper objectMapper) {
		this.notificationSettingSubCategoryService = notificationSettingSubCategoryService;
		this.objectMapper = objectMapper;
	}

	

	@GetMapping("/categoriesDetails")
	public ResponseEntity<String> getAllNotificationCategoriesWithSubcategories() throws JsonProcessingException {
		logger.info("Fetching all Notation of Subcategory");
		List<NotificationCatSubCatDto> categories = notificationSettingSubCategoryService
				.getAllNotificationCategoriesWithSubcategories();
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(categories)).toString());
	}

	@GetMapping("/change/{id}")
	public ResponseEntity<String> updateStatusSubcategory(@PathVariable Integer id) throws JsonProcessingException {
		 logger.info("Updating Notification with ID: {}", id);
		JSONObject response = notificationSettingSubCategoryService.updateStatusSubcategory(id);

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());

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
