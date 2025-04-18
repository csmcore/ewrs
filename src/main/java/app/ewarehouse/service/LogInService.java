package app.ewarehouse.service;

import app.ewarehouse.dto.LoginDto;
import app.ewarehouse.entity.CompanyWarehouseDetails;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
public interface LogInService {
	
	public JSONObject loinCheck(LoginDto LoginDto);
	JSONObject validateUserIdEmail(String decode);

	JSONObject validateTime(String decode);

	JSONObject saveforgetpasswordchange(String decode);
	List<Map<String, Object>> getUserWiseNotificationStatus(Integer id);
	public List<CompanyWarehouseDetails> getAllCompanyDetails();
	public JSONObject keyCloakLogin(LoginDto loginDto);
	public JSONObject logoutKeycloak(String decode);

}
