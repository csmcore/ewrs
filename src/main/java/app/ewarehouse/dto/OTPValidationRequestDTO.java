package app.ewarehouse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OTPValidationRequestDTO {
	private String email;
	private String mobile;
	private String otp;
	private String userid;
	
	
}
