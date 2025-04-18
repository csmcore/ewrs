package app.ewarehouse.service;


import org.json.JSONArray;
import org.json.JSONObject; 

public interface Complaint_reportingService {
JSONObject save(String complaint_reporting)throws Exception;
JSONObject getById(Integer Id)throws Exception;
JSONObject getAll(String formParams)throws Exception;
JSONObject deleteById(Integer id)throws Exception;
JSONObject getByActionWise(String data);
JSONObject getPreviewDetails(Integer serviceId) throws Exception;
JSONObject getEventTakeActionDetails(String data) throws Exception;
}