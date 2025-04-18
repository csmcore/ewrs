package app.ewarehouse.service;
import org.json.JSONArray;
import org.json.JSONObject;

public interface MdesignationService {
JSONObject save(String tdesignation);
JSONObject getById(Integer Id);
JSONArray getAll(String formParams);
JSONObject deleteById(Integer id);
}
