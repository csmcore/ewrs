package app.ewarehouse.serviceImpl;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.JwtAuthenticationResponse;
import app.ewarehouse.dto.RefreshTokenRequest;
import app.ewarehouse.dto.SignUpRequest;
import app.ewarehouse.dto.SigninRequest;
import app.ewarehouse.entity.Role;
import app.ewarehouse.entity.TempUser;
import app.ewarehouse.entity.Users;
import app.ewarehouse.repository.TempUserRepository;
import app.ewarehouse.repository.UsersRepository;
import app.ewarehouse.service.AuthenticationService;
import app.ewarehouse.service.JwtService;




@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private  UsersRepository usersRepository;
	
	@Autowired
	private  PasswordEncoder passwordEncoder;
	
	@Autowired
	private  AuthenticationManager authenticationManager;
	
	@Autowired
	private  JwtService jwtService;
	
	@Autowired
	private TempUserRepository tempUserRepo;
	
	public Users signup(SignUpRequest signUpRequest) {
		Users user = new Users();
		user.setEmail(signUpRequest.getEmail());
		user.setFirstname(signUpRequest.getFirstname());
		user.setSecondname(signUpRequest.getSecondname());
		user.setRole(Role.USER);
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		
		return usersRepository.save(user);
	}
	
	
	public JwtAuthenticationResponse signin(SigninRequest signinRequest, Map<String, String> claims) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
				(signinRequest.getEmail(),signinRequest.getPassword()));
		
		var user = usersRepository.findByEmail(signinRequest.getEmail()).orElseThrow(()-> new IllegalArgumentException("Invalid email or password"));
		var jwt = jwtService.generateToken(user,claims);
		var refreshToken = jwtService.generateRefreshToken(claims, user);
		JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
		jwtAuthenticationResponse.setToken(jwt);
		jwtAuthenticationResponse.setRefreshToken(refreshToken);
		jwtAuthenticationResponse.setFirstName(user.getFirstname());
		jwtAuthenticationResponse.setRole(user.getRole());
		return jwtAuthenticationResponse;
		
	}
	
	public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest,Map<String, String> claims) {
		String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
		Users user = usersRepository.findByEmail(userEmail).orElseThrow();
		if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
			var jwt = jwtService.generateToken(user,claims);
			JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
			jwtAuthenticationResponse.setToken(jwt);
			jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
			
			return jwtAuthenticationResponse;
		}
		return null;
	}


	@Override
	public void tempUserSignUp(TempUser user) {
		tempUserRepo.save(user);
	}


	
}
