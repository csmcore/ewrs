package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingCategoryDTO {
	
	private Integer notificationCategoryId;

	private String notificationCayegoryType;
    
	private String notificationDetails;
	

}
