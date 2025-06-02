package app.ewarehouse.service;
import org.json.JSONArray;
import org.json.JSONObject;
 

public interface MgroupsService {
JSONObject save(String tgroups);
JSONObject getById(Integer Id);
JSONArray getAll(String formParams);
JSONObject deleteById(Integer id);
}
