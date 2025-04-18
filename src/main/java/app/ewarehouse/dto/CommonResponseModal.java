package app.ewarehouse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponseModal {
	private String message;
	private boolean status;
	private String otp;
}
