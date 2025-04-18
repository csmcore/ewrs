package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingSubCategoryDTO {
	
	private Integer notificationSubCategoryId;
	
	private Integer notificationCategory;
	
	private String notificationSubCategoryName;
	
	private Boolean notificSubcatStatus;
	
	
	

}
