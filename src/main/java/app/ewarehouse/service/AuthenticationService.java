package app.ewarehouse.service;

import java.util.Map;

import org.json.JSONObject;

import app.ewarehouse.dto.JwtAuthenticationResponse;
import app.ewarehouse.dto.RefreshTokenRequest;
import app.ewarehouse.dto.SignUpRequest;
import app.ewarehouse.dto.SigninRequest;
import app.ewarehouse.entity.TempUser;
import app.ewarehouse.entity.Users;

public interface AuthenticationService {
	Users signup(SignUpRequest signUpRequest);
	
	JwtAuthenticationResponse signin(SigninRequest signinRequest,Map<String, String> claims);
	
	JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest,Map<String, String> claims);
	
	void tempUserSignUp(TempUser user);
}
