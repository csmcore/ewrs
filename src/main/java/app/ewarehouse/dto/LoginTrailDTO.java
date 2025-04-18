package app.ewarehouse.dto;

import java.time.LocalDateTime;

import app.ewarehouse.entity.LoginAttemptStatus;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LoginTrailDTO {
	
	private String userName;
	
	private String roleName;
	
	private String action;
	
	private LocalDateTime datetime;
	
	private String ipAddress;
	
	private String latitude;
	
	private String longitude;
	
	private String os;
	
	private String deviceName;
	
	private LoginAttemptStatus enmStatus;
	

}
