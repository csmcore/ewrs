package app.ewarehouse.service;

import org.json.JSONArray;
import org.json.JSONObject;


public interface TOnlineServiceApprovalService {

	JSONObject getTakeActionDetails(String data);


	JSONObject getAllNotingDetails(String data);

	JSONObject getQueryDetailsByUsingData(String data);

	JSONObject saveIntoQueryDetails(JSONObject jsObj);

	JSONObject noResubmitStatus(String data);
	JSONObject getAllForwradAuthority(String data);
	JSONObject checkUserSusStatus(Integer lableId,Integer userId)throws Exception;

}
