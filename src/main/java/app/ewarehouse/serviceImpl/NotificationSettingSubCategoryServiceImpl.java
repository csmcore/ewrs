package app.ewarehouse.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.NotificationCatSubCatDto;
import app.ewarehouse.dto.NotificationSettingSubCategoryDTO;
import app.ewarehouse.entity.NotificationSettingSubCategory;
import app.ewarehouse.repository.NotificationSettingSubCategoryRepository;
import app.ewarehouse.repository.UserWiseNotificationRepository;
import app.ewarehouse.service.NotificationSettingSubCategoryService;

@Service
public class NotificationSettingSubCategoryServiceImpl implements NotificationSettingSubCategoryService {
	private static final Logger logger = LoggerFactory.getLogger(NotificationSettingSubCategoryServiceImpl.class);
	@Autowired
	private NotificationSettingSubCategoryRepository notificationSettingSubCategoryRepository;
	
	@Autowired
	private UserWiseNotificationRepository userWiseNotificationRepository;


	@Override
	public List<NotificationCatSubCatDto> getAllNotificationCategoriesWithSubcategories() {
		 logger.info("Featching all the information category with Subcategory");
		List<NotificationSettingSubCategory> categories = notificationSettingSubCategoryRepository
				.findByBitDeletedFlagFalse();

		Map<Integer, NotificationCatSubCatDto> categoryMap = new HashMap<>();

		for (NotificationSettingSubCategory category : categories) {
			Integer catId = category.getNotificationCategory().getNotificationCategoryId();

			if (!categoryMap.containsKey(catId)) {
				NotificationCatSubCatDto categoryDto = new NotificationCatSubCatDto();
				categoryDto.setNotificationCatId(catId);
				categoryDto.setNotificationCatType(category.getNotificationCategory().getNotificationCayegoryType());
				categoryDto.setNotificationDetails(category.getNotificationCategory().getNotificationDetails());
				categoryDto.setSubcategory(new ArrayList<>()); 

				categoryMap.put(catId, categoryDto);
			}

			NotificationCatSubCatDto categoryDto = categoryMap.get(catId);

			NotificationSettingSubCategoryDTO subCategoryDto = new NotificationSettingSubCategoryDTO();
			subCategoryDto.setNotificationSubCategoryId(category.getNotificationSubCategoryId());
			subCategoryDto.setNotificationCategory(category.getNotificationCategory().getNotificationCategoryId());
			subCategoryDto.setNotificationSubCategoryName(category.getNotificationSubCategoryName());
			subCategoryDto.setNotificSubcatStatus(category.getNotificSubcatStatus());

			categoryDto.getSubcategory().add(subCategoryDto);
		}

		return new ArrayList<>(categoryMap.values());
	}

	@Override
	public JSONObject updateStatusSubcategory(Integer id) {
		JSONObject json = new JSONObject();
		try {
			 logger.info("Changing status for SubCategory with ID {}", id);
			notificationSettingSubCategoryRepository.changeStatusSubCategory(id);
			NotificationSettingSubCategory data = notificationSettingSubCategoryRepository.findById(id).orElse(null);
			if (data != null) {
				userWiseNotificationRepository.changeStatusSubCategoryWithUserWiseNotifiaction(id , data.getNotificSubcatStatus());
				json.put("status", 200);
				json.put("notificaionStatus", data.getNotificSubcatStatus());
			} else {
				json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			json.put("message", e);
			 logger.info("Changing status for SubCategory with ID {}", e);		}
		return json;
	}

}
