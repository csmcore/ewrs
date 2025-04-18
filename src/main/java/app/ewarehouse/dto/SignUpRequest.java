package app.ewarehouse.dto;

import lombok.Data;

@Data
public class SignUpRequest {
	private String firstname;
	private String secondname;
	private String email;
	private String password;
}
