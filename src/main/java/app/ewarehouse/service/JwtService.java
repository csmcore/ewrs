package app.ewarehouse.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

	String extractUserName(String token);
	
	String generateToken(UserDetails userDetails, Map<String, String> claims);
	
	boolean isTokenValid(String token , UserDetails userDetails);

	String generateRefreshToken(Map<String,String> extraClaims, UserDetails userDetails);
}
