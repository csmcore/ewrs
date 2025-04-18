package app.ewarehouse.service;

import java.util.List;

import org.json.JSONObject;

import app.ewarehouse.dto.NotificationCatSubCatDto;

public interface NotificationSettingSubCategoryService {

	//List<NotificationSettingSubCategory> getAllNotificSubCategory();

	List<NotificationCatSubCatDto> getAllNotificationCategoriesWithSubcategories();

	JSONObject updateStatusSubcategory(Integer id);

}
