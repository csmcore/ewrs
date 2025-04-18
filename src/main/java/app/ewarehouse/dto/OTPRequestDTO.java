package app.ewarehouse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OTPRequestDTO {
	private String email;
	private String mobile;
	private String ip;
	private String userid;
	private String type;
	
	
}
