package app.ewarehouse.service;
import org.json.JSONArray;
import org.json.JSONObject;
 

public interface MdepartmentService {
JSONObject save(String tdepartment);
JSONObject getById(Integer Id);
JSONArray getAll(String formParams);
JSONObject deleteById(Integer id);
}
