package app.ewarehouse.dto;

import app.ewarehouse.entity.Role;
import lombok.Data;


@Data
public class JwtAuthenticationResponse {

	private String token;
	
	private String refreshToken;
	
	private String firstName;
	
	private Role role;
}
