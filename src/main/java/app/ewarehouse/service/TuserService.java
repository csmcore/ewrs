package app.ewarehouse.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import app.ewarehouse.dto.InspectorListDto;
import app.ewarehouse.dto.UserDataResponseDto;
import app.ewarehouse.dto.UserDetailsResponseDto;
import app.ewarehouse.entity.Tuser;

public interface TuserService {
	JSONObject save(String tuser);

	JSONObject getById(Integer Id);

	JSONArray getAll(String formParams);

	JSONObject deleteById(Integer id);

	JSONArray findUserList(String data);

	List<Tuser> findByMobileOrEmail(String mobile, String email);

	UserDataResponseDto getUserDataByEmailId(String email);

	List<InspectorListDto> getInspectors();

	List<InspectorListDto> getCollateral();

	List<InspectorListDto> getWareHouseWorker(String wareHouseId,Integer roleId);

	UserDetailsResponseDto getUserDetails(Integer userId);

	List<InspectorListDto> getInspectorsByComplaintType(Integer complaintType);

	JSONObject changePassword(String s);

	JSONObject updateUserStatus(Integer id);
	
	void updateBlockStatus(Integer userId, boolean isBlocked);
	
	JSONObject saveActLogEnableDisable(String data);

	JSONObject saveIsTwoFactorAuthEnabled(String data);

	JSONObject getActivityLogStatus();

	String getLastChangePswd();

	JSONObject updateActivityLogStatusByUser(Integer id);

	Integer findRelatedIdById(String str);
	
	String getLastLoginTime(Integer userId);
	
	JSONObject changeUserPassword(String data);
	
	String sendEmailForIpAndOsBasis(String data);

	Integer getUserIdByName(String userName);

	Map<String, Object>  getIpCheck(String ipAdress, Integer userId);

	List<Map<String, Object>> getIcMembers(Integer roleId);
	
	public List<InspectorListDto> getNotSuspendedUser(Integer countyId, Integer subCountyId, Integer roleId);

	List<Map<String,Object>> getCommitteeMembers();

	List<InspectorListDto> getCCMembers();

	boolean checkIfUserExists(String string);

	JSONObject uploadUserDetailList(MultipartFile file) throws IOException;
	
}
