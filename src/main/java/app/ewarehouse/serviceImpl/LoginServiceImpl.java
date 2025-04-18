package app.ewarehouse.serviceImpl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.LoginDto;
import app.ewarehouse.entity.CompanyWarehouseDetails;
import app.ewarehouse.entity.NotificationSettingSubCategory;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.entity.UserWiseNotification;
import app.ewarehouse.repository.CompanyWarehouseDetailsRepository;
import app.ewarehouse.repository.MroleRepository;
import app.ewarehouse.repository.NotificationSettingSubCategoryRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.repository.UserWiseNotificationRepository;
import app.ewarehouse.service.LogInService;

@Service

public class LoginServiceImpl implements LogInService {

	@Autowired
	TuserRepository tuserRepository;
	@Autowired
	MroleRepository mroleRepository;

	@Autowired
	private UserWiseNotificationRepository userWiseNotificationRepository;

	@Autowired
	private NotificationSettingSubCategoryRepository notificationSettingSubCategoryRepository;

	@Autowired
	private CompanyWarehouseDetailsRepository companyWarehouseDetailsRepository;

	@Value("${spring.security.oauth2.client.ewrsweb.issuer-uri}")
	private String issueUrl;
	@Value("${spring.security.oauth2.client.ewrsweb.client-id}")
	private String clientId;
	@Value("${spring.security.oauth2.client.ewrsweb.client-secret}")
	private String clientSecret;
	@Value("${spring.security.oauth2.client.ewrsweb.authorization-grant-type}")
	private String grantType;

	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Override
	public JSONObject loinCheck(LoginDto loginDto) {
		logger.info("Inside loinCheck of LoginServiceImpl");
		JSONObject output = new JSONObject();
		Tuser resp = tuserRepository.findByTxtUserIdAndTxtPasswordAndBitDeletedFlag(loginDto.getUserId(),
				loginDto.getUserPassword(), false);
		if (resp != null) {
			JSONObject result = new JSONObject();
			result.put("username", resp.getTxtFullName());
			result.put("email", resp.getTxtEmailId());
			result.put("userid", resp.getIntId());
			result.put("roleId", resp.getSelRole());
			result.put("roleName", mroleRepository.getRoleNameByRoleID(resp.getSelRole(), false));
			result.put("privilage", resp.getChkPrevilege());
			result.put("warehouseId", resp.getWarehouseId());
			result.put("profileImage", resp.getFilePhoto());
			result.put("userNotificationStatus", getUserWiseNotificationStatus(resp.getIntId()));
			result.put("isActivityTrailEnabled", resp.getBitIsSaveActivityLogEnabled());
			result.put("isTwoFactorEnabled", resp.getBitIsTwoFactorAuthEnabled());
			result.put("CLCRoleId", resp.getSelCLCRoleId());
			result.put("ICMRoleId", resp.getSelIcmRoleId());
			result.put("WhInternalUserId", resp.getIntWhInternalUserId());
			logger.info("warehouseid is :" + resp.getWarehouseId());
			output.put("status", 200);
			output.put("msg", "success");
			output.put("result", result);
		} else {
			logger.warn("Inside loinCheck of LoginServiceImpl not found");
			output.put("status", 400);

		}
		return output;
	}

	@Override
	public JSONObject validateUserIdEmail(String data) {
		logger.info("Inside validateUserIdEmail method of LoginServiceImpl");
		JSONObject response = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(data);
			Tuser mAdminUserMaster = tuserRepository.getByTxtUserIdAndTxtEmailIdAndBitDeletedFlag(
					jsonObject.getString("username"), jsonObject.getString("userEmail"), false);
			if (mAdminUserMaster != null) {
				mAdminUserMaster.setDtforgetpasswordstarttime(LocalDateTime.now());
				tuserRepository.save(mAdminUserMaster);
				response.put("status", 200).put("url", jsonObject.getString("userURL"));
			} else {
				response.put("status", 400).put("msg", "Not Found");
			}
		} catch (Exception e) {
			logger.error("Inside validateTime method of LoginServiceImpl error occur:" + e);
			response.put("status", 400);
		}
		return response;
	}

	@Override
	public JSONObject validateTime(String data) {
		logger.info("Inside validateTime method of LoginServiceImpl");
		JSONObject response = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(data);
			Tuser mAdminUserMaster = tuserRepository.getByTxtUserIdAndBitDeletedFlag(jsonObject.getString("username"),
					false);
			if (mAdminUserMaster != null) {
				LocalDateTime startTime = mAdminUserMaster.getDtforgetpasswordstarttime();
				LocalDateTime presentDatetime = LocalDateTime.now();
				Duration duration = Duration.between(startTime, presentDatetime);
				if (duration.toDays() <= 0 && duration.toHours() <= 0 && duration.toMinutes() <= 30) {
					response.put("status", 200);
				} else {
					response.put("status", 300).put("msg", "Time Exceed");
				}
			} else {
				response.put("status", 400).put("msg", "Not Found");
			}
		} catch (Exception e) {
			logger.error("Inside validateTime method of LoginServiceImpl error occur:" + e);
			response.put("status", 400);
		}
		return response;
	}

	@Override
	public JSONObject saveforgetpasswordchange(String data) {
		logger.info("Inside saveforgetpasswordchange method of LoginServiceImpl");
		JSONObject response = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(data);
			Tuser mAdminUserMaster = tuserRepository.getByTxtUserIdAndBitDeletedFlag(jsonObject.getString("username"),
					false);
			if (mAdminUserMaster != null) {
				LocalDateTime startTime = mAdminUserMaster.getDtforgetpasswordstarttime();
				LocalDateTime presentDatetime = LocalDateTime.now();
				Duration duration = Duration.between(startTime, presentDatetime);
				if (duration.toDays() <= 0 && duration.toHours() <= 0 && duration.toMinutes() <= 30) {
					if (jsonObject.getString("userpassword").equals(jsonObject.getString("txtConfirmPassword"))) {
						mAdminUserMaster.setTxtPassword(jsonObject.getString("userpassword"));
						tuserRepository.save(mAdminUserMaster);
						response.put("status", 200);
					} else {
						response.put("status", 302);
					}
				} else {
					response.put("status", 300).put("msg", "Time Exceed");
				}
			}
		} catch (Exception e) {
			logger.error("Inside saveforgetpasswordchange method of LoginServiceImpl error occur:" + e);
			response.put("status", 400);
		}
		return response;
	}

	// for user specification
	@Override
	public List<Map<String, Object>> getUserWiseNotificationStatus(Integer id) {
		List<UserWiseNotification> userList = userWiseNotificationRepository.findByUserId(id);
		// System.err.println(userList);

		List<Map<String, Object>> notificationDataList = new ArrayList<>();

		if (userList == null || userList.isEmpty()) {
			List<NotificationSettingSubCategory> subList = notificationSettingSubCategoryRepository
					.findByBitDeletedFlagFalse();
			// System.err.println(subList);

			for (NotificationSettingSubCategory subCategory : subList) {
				Map<String, Object> notificationData = new HashMap<>();
				notificationData.put("notificationSubCategoryId", subCategory.getNotificationSubCategoryId());
				notificationData.put("notificationCategoryId",
						subCategory.getNotificationCategory().getNotificationCategoryId());
				notificationData.put("notificationSubCategoryName", subCategory.getNotificationSubCategoryName());
				notificationData.put("notificSubcatStatus", subCategory.getNotificSubcatStatus());

				notificationDataList.add(notificationData);
			}
		} else {

			for (UserWiseNotification userNotification : userList) {
				Map<String, Object> notificationData = new HashMap<>();
				notificationData.put("userwiseNotificationId", userNotification.getUserwiseNotificationId());
				notificationData.put("notificationCategoryId",
						userNotification.getNotificationCategory().getNotificationCategoryId());
				notificationData.put("notificationSubCategoryId",
						userNotification.getNotificationSubCategory().getNotificationSubCategoryId());
				notificationData.put("notificationSubCategoryName",
						userNotification.getNotificationSubCategory().getNotificationSubCategoryName());
				notificationData.put("notificSubcatStatus", userNotification.getUserwiseNotificStatus());

				notificationDataList.add(notificationData);
			}
		}

		return notificationDataList;
	}

	@Override
	public List<CompanyWarehouseDetails> getAllCompanyDetails() {
		return companyWarehouseDetailsRepository.findByDeletedFlagFalse();
	}

	@Override
	public JSONObject keyCloakLogin(LoginDto loginDto) {
		JSONObject loginrefresstoken = new JSONObject();
		String jsonInString = "";

		try {
			String url = issueUrl + "/protocol/openid-connect/token";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		        OAuth_Token_Request_State=606e30a0-1e27-4fb3-8a3c-43769b7c48a5");
			con.setDoOutput(true);
			con.setDoInput(true);
			// Set request parameters
			String parameters = "client_id=" + URLEncoder.encode(clientId, "UTF-8") + "&client_secret="
					+ URLEncoder.encode(clientSecret, "UTF-8") + "&grant_type=" + URLEncoder.encode(grantType, "UTF-8")
					+ "&username=" + URLEncoder.encode(loginDto.getUserId(), "UTF-8") + "&password="
					+ URLEncoder.encode(loginDto.getUserNPassword(), "UTF-8");

			// Write parameters to output stream
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();
			// Get response code
			int responseCode = con.getResponseCode();
			System.out.println("Response Code: " + responseCode);
			if (responseCode == 200) {
				loginrefresstoken.put("code", String.valueOf(responseCode));
				// Read response from input stream
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				// Print response
//				System.out.println(response.toString());
				ObjectMapper mapper = new ObjectMapper();
				jsonInString = mapper.writeValueAsString(response);
//				System.out.println(jsonInString);
				JSONObject jsonObj = new JSONObject(response.toString());
//				System.out.println(jsonObj.get("access_token"));
				loginrefresstoken.put("Access_token", (String) jsonObj.get("access_token"));
				loginrefresstoken.put("Expires_in", String.valueOf(jsonObj.get("expires_in")));
				loginrefresstoken.put("Refresh_expires_in", String.valueOf(jsonObj.get("refresh_expires_in")));
				loginrefresstoken.put("Session_state", (String) jsonObj.get("session_state"));
				loginrefresstoken.put("Refresh_token", (String) jsonObj.get("refresh_token"));
			}
			if (responseCode == 400 || responseCode == 403 || responseCode == 401) {
				loginrefresstoken.put("code", String.valueOf(responseCode));
			}

		} catch (Exception e) {
			e.printStackTrace();
			loginrefresstoken.put("code", "500");
		}
		return loginrefresstoken;
	}

	@Override
	public JSONObject logoutKeycloak(String data) {
		logger.info("Inside validateUserIdEmail method of LoginServiceImpl");
		JSONObject response = new JSONObject();
		try {
			JSONObject jsonObject = new JSONObject(data);

			String url = issueUrl + "/protocol/openid-connect/logout";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			con.setDoOutput(true);
			con.setDoInput(true);
			// Set request parameters
			String refreshToken = jsonObject.getString("refreshToken");
			String parameters = "client_id=" + URLEncoder.encode(clientId, "UTF-8") + "&client_secret="
					+ URLEncoder.encode(clientSecret, "UTF-8") + "&refresh_token="
					+ URLEncoder.encode(refreshToken, "UTF-8");
			// Write parameters to output stream
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();
			// Get response code
			int responseCode = con.getResponseCode();
			System.out.println("Response Code: " + responseCode);
			if (responseCode == 204) {
				response.put("status", 204);
				response.put("msg", "Logged out successfully");
			}
		} catch (Exception e) {
			logger.error("Inside validateTime method of LoginServiceImpl error occur:" + e);
			response.put("status", 400);
		}
		return response;
	}

}
