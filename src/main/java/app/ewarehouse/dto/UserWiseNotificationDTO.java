package app.ewarehouse.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserWiseNotificationDTO {
	
	 private Integer userwiseNotificationId;
	 private int notificationCatId;
	 private String notificationCatType;
	 private String notificationDetails;
	 private List<NotificationSettingSubCategoryDTO> subcategory;

}
