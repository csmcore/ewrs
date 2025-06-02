

package app.ewarehouse.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class SendNotificationDto {

	private Integer authNotificationNo;
	private Integer fromAuthId;
	private Integer toAuthId;
	private String notification;
	private Boolean bitReadStatus;
	private String notificationModule;
	private String notificationType;
	private String vchPath;
}
