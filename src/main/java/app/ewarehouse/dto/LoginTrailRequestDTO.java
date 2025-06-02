package app.ewarehouse.dto;

import lombok.Data;

@Data
public class LoginTrailRequestDTO {

	private Integer userId;
    private Integer roleId;
    private String action;
    private String ipAddress;
    private String latitude;
    private String longitude;
    private BrowserDetailsDTO browserDetails;
    private String enteredUserName;
    private String enmStatus;
	
}
