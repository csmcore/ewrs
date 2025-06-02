package app.ewarehouse.service;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import app.ewarehouse.dto.UserRoleIdResponse;
 

public interface MroleService {
JSONObject save(String trole);
JSONObject getById(Integer Id);
JSONArray getAll(String formParams);
JSONObject deleteById(Integer id);
 List<UserRoleIdResponse> getUserByRoleId(int roleid);
}
