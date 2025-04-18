package app.ewarehouse.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.tika.utils.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.CommonCaptchaGenerate;
import app.ewarehouse.dto.CommonResponseModal;
import app.ewarehouse.dto.JwtAuthenticationResponse;
import app.ewarehouse.dto.LoginDto;
import app.ewarehouse.dto.Mail;
import app.ewarehouse.dto.OTPRequestDTO;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.dto.SendNotificationDto;
import app.ewarehouse.dto.SigninRequest;
import app.ewarehouse.dto.UserDataResponseDto;
import app.ewarehouse.entity.AdminConfiguration;
import app.ewarehouse.entity.CompanyWarehouseDetails;
import app.ewarehouse.entity.TempUser;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.master.service.AdminConfigService;
import app.ewarehouse.service.AuthenticationService;
import app.ewarehouse.service.LogInService;
import app.ewarehouse.service.OTPService;
import app.ewarehouse.service.TuserService;
import app.ewarehouse.serviceImpl.NotificationServiceImpl;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.EmailUtil;
import app.ewarehouse.util.TokenCreaterAndMatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {

	@Autowired
	LogInService service;

	@Autowired(required = true)
	OTPService otpservices;
	@Autowired
	public AuthenticationService authenticationService;
	@Autowired
	TuserService userService;
	@Autowired
	NotificationServiceImpl notificationServiceImpl;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	AdminConfigService adminConfigService;

	@Value("${salt}")
	private String salt;

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@PostMapping(value = "/logindetails")
	public ResponseEntity<?> logindetails(@RequestBody String democsmform, HttpServletRequest request,
			HttpServletResponse reponse) {
		logger.info("Inside logindetails method of LoginController");
		JSONObject requestObj = new JSONObject(democsmform);
		JSONObject output = new JSONObject();
		JSONArray jsonarr = new JSONArray();
		JSONObject responseOtp = new JSONObject();
		output.put("keycloakAuth", "");

		if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"), requestObj.getString("REQUEST_TOKEN"))) {
			String data = CommonUtil.inputStreamDecoder(democsmform);
			JSONObject json = new JSONObject(data);
			LoginDto loginDto = new LoginDto();
			loginDto.setUserId(json.getString("userId"));
			loginDto.setUserPassword(json.getString("userPassword"));
			loginDto.setUserNPassword(json.getString("userNPassword"));
			String userName = json.getString("userId");
			String captchaId = json.getString("captchaId");
			String captchaResult = json.getString("captchaResult");

			boolean validateCaptcha = false;
			String captchaResultValue = CommonCaptchaGenerate.get(captchaId).toString();
			if (captchaResult != null && captchaResultValue.equals(captchaResult)) {
				CommonCaptchaGenerate.remove(captchaId);
				validateCaptcha = true;
			} else {
				validateCaptcha = false;
			}

			if (!validateCaptcha) {
				output.put("msg", "Invalid Captcha");
				output.put("status", 410);
				JSONObject login_jsonobj = CommonUtil.inputStreamEncoder(output.toString());
				jsonarr.put(login_jsonobj);
				return ResponseEntity.ok(jsonarr.toString());
			}

			Integer checkUserExist = adminConfigService.checkUserExist(userName.trim());

			// Restrict user Login Based On Ip Start
//			Integer userId= userService.getUserIdByName(userName.trim());   
//			Map<String, Object> response =userService.getIpCheck(json.getString("ipAddressForLogin"),userId);

			// Map<String, Object> response =userService.getIpCheck("111.93.178.42",userId);
//			Boolean ipLoginCheck = (Boolean) response.get("isStatus");
//			String lastLoginIp = (String) response.get("lastLoginIp");
//			Boolean isTwoFactor = (Boolean) response.get("isTwoFactor");
			// Restrict user Login Based On Ip End

			if (checkUserExist > 0) {
				boolean isLocked = adminConfigService.isUserLocked(userName.trim());
				if (isLocked) {
					output.put("status", 403);
					output.put("message", "Account is locked. Please Contact to Admin.");
					JSONObject login_jsonobj = CommonUtil.inputStreamEncoder(output.toString());
					jsonarr.put(login_jsonobj);
					return ResponseEntity.ok(jsonarr.toString());
				}
//				else if(ipLoginCheck) {
//	                output.put("status", 405);
//	                output.put("message", "You are already logged in from another IP address: " + json.getString("ipAddressForLogin"));
//	                output.put("userId", userId);
//					output.put("userName", userName);
//					output.put("ipLoginCheck", lastLoginIp);
//					output.put("isTwoFactor", isTwoFactor);
//	                JSONObject login_jsonobj = CommonUtil.inputStreamEncoder(output.toString());
//					jsonarr.put(login_jsonobj);
//					return ResponseEntity.ok(jsonarr.toString());
//				}
				else {
//					JSONObject kecloakAuth = service.keyCloakLogin(loginDto);

					output = service.loinCheck(loginDto);
//					if (kecloakAuth.get("code").equals("200")) {
//						output.put("keycloakAuth", kecloakAuth);
//					} else {
//						output.put("status", 401);
//						output.put("message", "Invalid user credential");
//						JSONObject login_jsonobj = CommonUtil.inputStreamEncoder(output.toString());
//						jsonarr.put(login_jsonobj);
//						return ResponseEntity.ok(jsonarr.toString());
//					}
				}

			} else {
				output.put("status", 401);
				output.put("message", "User doesn't exist");
				JSONObject login_jsonobj = CommonUtil.inputStreamEncoder(output.toString());
				jsonarr.put(login_jsonobj);
				return ResponseEntity.ok(jsonarr.toString());
			}
			JSONObject token_jsonObject = new JSONObject();

			if (output.getInt("status") == 200) {
//	            String user_type = (String) request.getHeader("x-user-type");

				SigninRequest siginReq = SigninRequest.builder().email("ewrs@mail.com").password("ewrs").build();

				// if two factor auth enabled
				if ((boolean) output.getJSONObject("result").get("isTwoFactorEnabled")) {
					OTPRequestDTO otprequestDTO = new OTPRequestDTO();
					otprequestDTO.setEmail((String) output.getJSONObject("result").get("email"));
					otprequestDTO.setMobile("1234567890");
					otprequestDTO.setIp(null);
					otprequestDTO.setUserid(null);
					otprequestDTO.setType("twoFactor");
					CommonResponseModal otpreceive = otpservices.getOTP(otprequestDTO);
					String otp = otpreceive.getOtp();
					if (otp != null && !StringUtils.isBlank(otp)) {
						Mail mail = new Mail();
						mail.setMailSubject("Otp Mail.");
						mail.setContentType("text/html");
						// mail.setMailCc("uiidptestmail@gmail.com");
						mail.setTemplate("Your two-factor authentication OTP is: " + otp);
						mail.setMailTo((String) output.getJSONObject("result").get("email"));
						// String message =
						EmailUtil.sendMail(mail.getMailSubject(), mail.getTemplate(), mail.getMailTo());
						/*
						 * if (message.contains("Failed")) { // Adjust success condition based on your
						 * EmailUtil // implementation responseOtp.put("status", "failure");
						 * responseOtp.put("message",
						 * "OTP generated successfully, but email delivery failed."); JSONObject
						 * failureMsg = CommonUtil.inputStreamEncoder(responseOtp.toString()); return
						 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failureMsg.
						 * toString()); }
						 */

					}
					/*
					 * else { responseOtp.put("status", "failure"); responseOtp.put("message",
					 * "OTP generate failed"); JSONObject failureMsg =
					 * CommonUtil.inputStreamEncoder(responseOtp.toString()); return
					 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failureMsg.
					 * toString());
					 * 
					 * }
					 */

				}

//				  if ("ADMIN".equalsIgnoreCase(user_type)) { siginReq =
//				  SigninRequest.builder().email("admin@gmail.com").password("admin").build(); }
//				  else if ("USER".equalsIgnoreCase(user_type)) { siginReq =
//				  SigninRequest.builder().email("ewrs@mail.com").password("ewrs").build(); }

				String result = output.get("result").toString();
				Map<String, String> claims = null;
				try {
					claims = new ObjectMapper().readValue(result, HashMap.class);
				} catch (JsonMappingException e) {
					logger.error("Inside logindetails method of LoginController token not matched");
				} catch (JsonProcessingException e) {
					logger.error("Inside logindetails method of LoginController token not matched");
				}
				adminConfigService.updateLoginStatus(json.getString("userId"), "Login", "FAILURE");
				adminConfigService.unBlockUser(json.getString("userId"));
				JwtAuthenticationResponse jwtres = authenticationService.signin(siginReq, claims);

				token_jsonObject.put("token", jwtres.getToken());
				token_jsonObject.put("refreshtoken", jwtres.getRefreshToken());
			}

			if (output.getInt("status") == 400) {
				String lastFailedTime = adminConfigService.getLastFailedTime(json.getString("userId"), "Login",
						"FAILURE");
				String emailId = adminConfigService.getEmailId(userName.trim());
				output.put("email", emailId);
				Integer uId = userService.findRelatedIdById(userName.trim());
				output.put("uId", uId);
				if (lastFailedTime != null && !lastFailedTime.trim().isEmpty()) {
					try {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
						LocalDateTime failedDateTime = LocalDateTime.parse(lastFailedTime.trim(), formatter);

						LocalDateTime now = LocalDateTime.now();
						long latestCount = adminConfigService.countBetweenLastAndCurrentTime(failedDateTime, now);
						Integer maxAttempt = adminConfigService.countMaxAttempt();

						if ("supAdmin".equalsIgnoreCase(json.getString("userId"))) {
							output.put("message", "Login failed. Please try again.");
							int remainingAttempts = Math.max(maxAttempt - (int) latestCount, 0);
							output.put("remainingAttempts", remainingAttempts);
						} else if (latestCount >= maxAttempt) {
							adminConfigService.blockUser(json.getString("userId"));
							output.put("status", 403);
							output.put("message", "Account is locked.");
						} else {
							int remainingAttempts = Math.max(maxAttempt - (int) latestCount, 0);
							output.put("latestCount", latestCount);
							/*
							 * String str=json.getString("userId"); if(latestCount==3) {
							 * 
							 * Integer uId =userService.findRelatedIdById(str); List<Map<String, Object>>
							 * totalRes = service.getUserWiseNotificationStatus(uId);
							 * //System.out.println(totalRes); // Check if
							 * "Email me whenever encountering unusual activity" is enabled boolean
							 * shouldSendMail = totalRes.stream() .anyMatch(notification ->
							 * Boolean.TRUE.equals(notification.get("notificSubcatStatus")) );
							 * 
							 * if (shouldSendMail) { String email =
							 * adminConfigService.getEmailId(userName.trim()); EmailUtil.sendMail(
							 * "Unusual Login Activity Detected",
							 * "We have detected multiple failed login attempts on your account. If this wasn't you, please reset your password or contact support."
							 * , email ); logger.info("Unusual activity email sent to"); //
							 * System.out.println("Unusual activity email sent to: " + email); } else {
							 * logger.info("Unusual activity notification is disabled.");
							 * //System.out.println("Unusual activity notification is disabled."); } }
							 */
							output.put("remainingAttempts", remainingAttempts);
						}
					} catch (DateTimeParseException e) {
						logger.info("Error parsing date:{} ", e.getMessage());
					}
				} else {
					Integer maxAttempt = adminConfigService.countMaxAttempt();
					output.put("remainingAttempts", maxAttempt);
				}
			}

			JSONObject login_jsonobj = CommonUtil.inputStreamEncoder(output.toString());
			List<AdminConfiguration> configDetails = adminConfigService.getAllConfigurationDetails();
			jsonarr.put(login_jsonobj);
			jsonarr.put(token_jsonObject);
			jsonarr.put(configDetails);

		} else {
			logger.error("Inside logindetails method of LoginController token not matched");
			output.put("msg", "error");
			output.put("status", 417);
		}
		return ResponseEntity.ok(jsonarr.toString());
	}

	@PostMapping("/logoutKeycloak")
	public ResponseEntity<String> logoutKeycloak(@RequestBody String data) throws UnsupportedEncodingException {
		logger.info("Inside changePassword method of LoginController");
		JSONObject requestObj = new JSONObject(data);
		byte[] requestData = Base64.getDecoder().decode(requestObj.getString("REQUEST_DATA"));
		Integer status = SQLInjection.sqlInjection(
				URLDecoder.decode((new String(requestData, StandardCharsets.UTF_8)), StandardCharsets.UTF_8.name()), 0);
		JSONObject object = new JSONObject();
		if (status == 1) {
			logger.warn("Inside validateUserIdEmail method of LoginController got SQLInjection");
			object.put("status", 417);
			object.put("msg", "error");
		} else if (TokenCreaterAndMatcher.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
				requestObj.getString("REQUEST_TOKEN"))) {
			object = service.logoutKeycloak(URLDecoder.decode((new String(requestData, StandardCharsets.UTF_8)),
					StandardCharsets.UTF_8.name()));
		} else {
			logger.error("Inside validateUserIdEmail method of LoginController  Token mismatch");
			object.put("status", 417);
			object.put("msg", "error");
		}
		JSONObject response = new JSONObject();
		response.put("RESPONSE_DATA", Base64.getEncoder().encodeToString(object.toString().getBytes()));
		response.put("RESPONSE_TOKEN", TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(object.toString().getBytes())));
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping("/validate")
	public ResponseEntity<String> validateUserIdEmail(@RequestBody String data) throws UnsupportedEncodingException {
		logger.info("Inside changePassword method of LoginController");
		JSONObject requestObj = new JSONObject(data);
		byte[] requestData = Base64.getDecoder().decode(requestObj.getString("REQUEST_DATA"));
		Integer status = SQLInjection.sqlInjection(
				URLDecoder.decode((new String(requestData, StandardCharsets.UTF_8)), StandardCharsets.UTF_8.name()), 0);
		JSONObject object = new JSONObject();
		if (status == 1) {
			logger.warn("Inside validateUserIdEmail method of LoginController got SQLInjection");
			object.put("status", 417);
			object.put("msg", "error");
		} else if (TokenCreaterAndMatcher.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
				requestObj.getString("REQUEST_TOKEN"))) {
			object = service.validateUserIdEmail(URLDecoder.decode((new String(requestData, StandardCharsets.UTF_8)),
					StandardCharsets.UTF_8.name()));
		} else {
			logger.error("Inside validateUserIdEmail method of LoginController  Token mismatch");
			object.put("status", 417);
			object.put("msg", "error");
		}
		JSONObject response = new JSONObject();
		response.put("RESPONSE_DATA", Base64.getEncoder().encodeToString(object.toString().getBytes()));
		response.put("RESPONSE_TOKEN", TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(object.toString().getBytes())));
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping("/validateTime")
	public ResponseEntity<String> validateTime(@RequestBody String data) throws UnsupportedEncodingException {
		logger.info("Inside validateTime method of LoginController");
		JSONObject requestObj = new JSONObject(data);
		byte[] requestData = Base64.getDecoder().decode(requestObj.getString("REQUEST_DATA"));
		Integer status = SQLInjection.sqlInjection(
				URLDecoder.decode((new String(requestData, StandardCharsets.UTF_8)), StandardCharsets.UTF_8.name()), 0);
		JSONObject object = new JSONObject();
		if (status == 1) {
			logger.warn("Inside validateTime method of LoginController got SQLInjection");
			object.put("status", 417);
			object.put("msg", "error");
		} else if (TokenCreaterAndMatcher.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
				requestObj.getString("REQUEST_TOKEN"))) {
			object = service.validateTime(URLDecoder.decode((new String(requestData, StandardCharsets.UTF_8)),
					StandardCharsets.UTF_8.name()));
		} else {
			logger.error("Inside validateTime method of LoginController  Token mismatch");
			object.put("status", 417);
			object.put("msg", "error");
		}
		JSONObject response = new JSONObject();
		response.put("RESPONSE_DATA", Base64.getEncoder().encodeToString(object.toString().getBytes()));
		response.put("RESPONSE_TOKEN", TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(object.toString().getBytes())));
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping("/saveforgetpasswordchange")
	public ResponseEntity<String> saveforgetpasswordchange(@RequestBody String data)
			throws UnsupportedEncodingException {
		logger.info("Inside saveforgetpasswordchange method of LoginController");
		JSONObject requestObj = new JSONObject(data);
		byte[] requestData = Base64.getDecoder().decode(requestObj.getString("REQUEST_DATA"));
		Integer status = SQLInjection.sqlInjection(
				URLDecoder.decode((new String(requestData, StandardCharsets.UTF_8)), StandardCharsets.UTF_8.name()), 0);
		JSONObject object = new JSONObject();
		if (status == 1) {
			logger.warn("Inside saveforgetpasswordchange method of LoginController got SQLInjection");
			object.put("status", 417);
			object.put("msg", "SQLInjection");
		} else if (TokenCreaterAndMatcher.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
				requestObj.getString("REQUEST_TOKEN"))) {
			object = service.saveforgetpasswordchange(URLDecoder
					.decode((new String(requestData, StandardCharsets.UTF_8)), StandardCharsets.UTF_8.name()));
		} else {
			logger.error("Inside saveforgetpasswordchange method of LoginController  Token mismatch");
			object.put("status", 417);
			object.put("msg", "Token mismatch");
		}
		JSONObject response = new JSONObject();
		response.put("RESPONSE_DATA", Base64.getEncoder().encodeToString(object.toString().getBytes()));
		response.put("RESPONSE_TOKEN", TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(object.toString().getBytes())));
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping("createTempUser")
	public ResponseEntity<String> createTempUser(@RequestBody String data)
			throws JsonMappingException, JsonProcessingException {
		JSONObject requestObj = new JSONObject(data);
		ObjectMapper mapper = new ObjectMapper();
		byte[] requestData = Base64.getDecoder().decode(requestObj.getString("REQUEST_DATA"));
		TempUser tempUser = mapper.readValue(new String(requestData), TempUser.class);
		List<Tuser> userDb = userService.findByMobileOrEmail(tempUser.getMobile(), tempUser.getEmail());
		JSONObject object = new JSONObject();
		if (userDb.isEmpty()) {
			tempUser.setUsername(UUID.randomUUID().toString());
			authenticationService.tempUserSignUp(tempUser);

			Tuser user = new Tuser();
			user.setTxtUserId(tempUser.getEmail());
			user.setEnPassword(CommonUtil.getHmacMessage("Admin@123" + salt));
			user.setTxtMobileNo(tempUser.getMobile());
			user.setTxtEmailId(tempUser.getEmail());
			if (tempUser.getUserType() == 1) {
				user.setSelRole(43);
			} else if (tempUser.getUserType() == 2) {
				user.setSelRole(53);
			} else {
				user.setSelRole(54); // Temporary user
			}
			user.setTxtFullName(tempUser.getApplicantName());
			user.setSelGender(1);
			user.setChkPrevilege(3);
			user.setSelDesignation(1);
			user.setSelEmployeeType(0);
			user.setSelDepartment(1);
			user.setSelGroup(0);
			user.setSelHierarchy(0);
			user.setIntReportingAuth(0);

			// SEND CREDENTIAL ON MAIL
			Mail mail = new Mail();
			mail.setMailSubject("Otp Mail.");
			mail.setContentType("text/html");
			// mail.setMailCc("uiidptestmail@gmail.com");
			mail.setTemplate("Your registration credentials are:<br>" + "Username: " + tempUser.getEmail() + "<br>"
					+ "Password: " + "Admin@123");
			mail.setMailTo(tempUser.getEmail());
			EmailUtil.sendMail(mail.getMailSubject(), mail.getTemplate(), mail.getMailTo());
			userService.save(mapper.writeValueAsString(user));
			object.put("status", 200);
			object.put("msg", "success");
		} else {
			object.put("status", 409);
			object.put("msg", "conflict");
		}

		JSONObject response = new JSONObject();
		response.put("RESPONSE_DATA", Base64.getEncoder().encodeToString(object.toString().getBytes()));
		response.put("RESPONSE_TOKEN", TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(object.toString().getBytes())));
		return ResponseEntity.ok(response.toString());
	}

	@GetMapping("/getTempUserData/{email}")
	public ResponseEntity<?> getUserData(@PathVariable String email) throws JsonProcessingException {
		UserDataResponseDto data = userService.getUserDataByEmailId(email);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(data)).toString());
	}

	private <T> String buildJsonResponse(T response) throws JsonProcessingException {
		Object result;
		if (response instanceof JSONObject) {
			result = response.toString();
		} else {
			result = response;
		}

		return objectMapper.writeValueAsString(ResponseDTO.builder().status(200).result(result).build());
	}

	@GetMapping("/getNotifications/{id}") // Broadcast to all clients subscribed to this topic
	public List<SendNotificationDto> getAllNotificationsById(@PathVariable Integer id) {

		List<SendNotificationDto> setNotificationsList = notificationServiceImpl.getAllNotificationsById(id);

		return setNotificationsList; // Send the message back
	}

	@PostMapping(value = "/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody String data) {
		JSONObject response = userService.changePassword(data);

		JSONObject encodedResponse = new JSONObject();
		encodedResponse.put("RESPONSE_DATA", Base64.getEncoder().encodeToString(response.toString().getBytes()));
		encodedResponse.put("RESPONSE_TOKEN", TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(response.toString().getBytes())));
		return ResponseEntity.ok(encodedResponse.toString());
	}

	@PostMapping("/updateBlockStatus/{userId}/status")
	public ResponseEntity<?> updateBlockStatus(@PathVariable Integer userId, @RequestBody Map<String, Object> payload)
			throws JsonProcessingException {
		boolean isBlocked = (boolean) payload.get("isBlocked");
		userService.updateBlockStatus(userId, isBlocked);
		return ResponseEntity.ok(CommonUtil.encodedJsonResponse(null, objectMapper));
	}

	@PostMapping("/changePasswordUserWise")
	public ResponseEntity<?> changePasswordUserWise(@RequestBody String data) throws JsonProcessingException {
		JSONObject response = userService.changeUserPassword(data);

		JSONObject encodedResponse = new JSONObject();
		encodedResponse.put("RESPONSE_DATA", Base64.getEncoder().encodeToString(response.toString().getBytes()));
		encodedResponse.put("RESPONSE_TOKEN", TokenCreaterAndMatcher
				.getHmacMessage(Base64.getEncoder().encodeToString(response.toString().getBytes())));
		return ResponseEntity.ok(encodedResponse.toString());
	}

	@PostMapping("/sendEmailForIpAndOsBasis")
	public ResponseEntity<String> sendEmailForIpAndOsBasis(@RequestBody String data) {
		try {
			String response = userService.sendEmailForIpAndOsBasis(data);
			return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response).toString());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("There might be some error :: " + e.getMessage());
		}
	}

	@GetMapping("/getCompanies")
	public List<CompanyWarehouseDetails> getAllCompanies() {
		return service.getAllCompanyDetails();
	}

	@GetMapping("/getVersion")
	public ResponseEntity<?> getVersion() throws JsonProcessingException {
		JSONObject result = new JSONObject();
		try {
			String response="25.04.16.1071";
			result.put("version", response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("There might be some error :: " + e.getMessage());
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(result.toString()).toString());
	}

}
