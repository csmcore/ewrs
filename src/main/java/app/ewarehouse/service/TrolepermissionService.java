package app.ewarehouse.service;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

import app.ewarehouse.entity.Trolepermission;
public interface TrolepermissionService {
	JSONObject save(String trolepermission);

	JSONArray getById(Integer roleId, Integer userId);

	JSONArray getAll(String formParams);

	JSONObject deleteById(Integer id);

	JSONArray bindAllMenuLinks(String data);

	JSONObject givePermissionDetails(String data);

	List<Trolepermission> getRolePermissionListByUserId(Integer userId);
}
