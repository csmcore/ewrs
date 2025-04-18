package app.ewarehouse.service;
import org.json.JSONArray;
import org.json.JSONObject;
 

public interface MemployeetypeService {
JSONObject save(String temployeetype);
JSONObject getById(Integer Id);
JSONArray getAll(String formParams);
JSONObject deleteById(Integer id);
}
