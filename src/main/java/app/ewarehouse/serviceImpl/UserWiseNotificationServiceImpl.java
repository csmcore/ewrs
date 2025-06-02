package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import app.ewarehouse.entity.NotificationSettingCategory;
import app.ewarehouse.entity.NotificationSettingSubCategory;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.entity.UserWiseNotification;
import app.ewarehouse.repository.NotificationSettingSubCategoryRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.repository.UserWiseNotificationRepository;
import app.ewarehouse.service.UserWiseNotificationService;
import jakarta.persistence.Tuple;

@Service
public class UserWiseNotificationServiceImpl implements UserWiseNotificationService {

	private static final Logger logger = LoggerFactory.getLogger(UserWiseNotificationServiceImpl.class);

	@Autowired
	private UserWiseNotificationRepository userWiseNotificationRepository;

	@Autowired
	private NotificationSettingSubCategoryRepository notificationSettingSubCategoryRepository;

	@Autowired
	private TuserRepository tUserRepository;

	public List<Map<String, Object>> viewUserWiseNotificationById(Integer id) {
		logger.info("Changing viewDetails for usernotification with ID");

		List<UserWiseNotification> userList = userWiseNotificationRepository.findByUserId(id);

		if (userList == null || userList.isEmpty()) {
			List<NotificationSettingSubCategory> subList = notificationSettingSubCategoryRepository
					.findByBitDeletedFlagFalse();

			if (subList != null && !subList.isEmpty()) {
				subList.stream().forEach(subCategories -> {
					UserWiseNotification userWiseNotification = new UserWiseNotification();

					userWiseNotification.setNotificationCategory(subCategories.getNotificationCategory());
					userWiseNotification.setNotificationSubCategory(subCategories);
					Tuser tUser = tUserRepository.findByIntIdAndBitDeletedFlag(id, false);
					userWiseNotification.setUserId(tUser);
					userWiseNotification.setUserwiseNotificStatus(subCategories.getNotificSubcatStatus());
					userWiseNotification.setCreatedBy(id);
					userWiseNotification.setBitDeletedFlag(false);
					userWiseNotification.setCreatedOn(new Date());

					userWiseNotificationRepository.save(userWiseNotification);
				});
			}
		}

		// Fetch the updated user notification list and convert to DTOs
		List<UserWiseNotification> updatedUserList = userWiseNotificationRepository.findByUserId(id);
		Map<Integer, Map<String, Object>> categoryMap = new HashMap<>();

		updatedUserList.forEach(userWiseNotification -> {
			NotificationSettingCategory category = userWiseNotification.getNotificationCategory();
			NotificationSettingSubCategory subCategory = userWiseNotification.getNotificationSubCategory();

			categoryMap.putIfAbsent(category.getNotificationCategoryId(), new HashMap<>());
			Map<String, Object> categoryDetails = categoryMap.get(category.getNotificationCategoryId());

			List<Map<String, Object>> subCategories = (List<Map<String, Object>>) categoryDetails
					.getOrDefault("subCategories", new ArrayList<>());

			Map<String, Object> subCategoryDetails = new HashMap<>();
			subCategoryDetails.put("id", subCategory.getNotificationSubCategoryId());
			subCategoryDetails.put("name", subCategory.getNotificationSubCategoryName());
			subCategoryDetails.put("status", userWiseNotification.getUserwiseNotificStatus());
			subCategoryDetails.put("userwiseNotificationId", userWiseNotification.getUserwiseNotificationId());
			subCategoryDetails.put("userwiseNotificStatus", userWiseNotification.getUserwiseNotificStatus());

			subCategories.add(subCategoryDetails);

			categoryDetails.put("notificationCategory", Map.of("id", category.getNotificationCategoryId(), "type",
					category.getNotificationCayegoryType(), "details", category.getNotificationDetails()));
			categoryDetails.put("subCategories", subCategories);

			categoryMap.put(category.getNotificationCategoryId(), categoryDetails);
		});

		return new ArrayList<>(categoryMap.values());
	}

	@Override
	public JSONObject updateStatusUserNotification(Integer id) {
		JSONObject json = new JSONObject();
		try {
			logger.info("Changing status for usernotification with ID {}", id);
			userWiseNotificationRepository.changeStatusSubCategory(id);
			UserWiseNotification data = userWiseNotificationRepository.findById(id).orElse(null);
			if (data != null) {
				json.put("status", 200);
				json.put("userNotificaionStatus", data.getUserwiseNotificStatus());
			} else {
				json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			json.put("message", e);
			logger.info("Changing status for usernotification with ID {}", e);
		}
		return json;
	}

	@Override
	public JSONObject userNameEmailDetails(Integer id) {
		Tuple fetchingData = userWiseNotificationRepository.fetchingUserNameANdEmail(id);
		JSONObject obj = new JSONObject();
		obj.put("fullName",(String) fetchingData.get(0));
		obj.put("emailId", (String) fetchingData.get(1));
		return obj;

	}

}
