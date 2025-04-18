package app.ewarehouse.service;

import org.json.JSONObject;

public interface SuspensionReasonService {

	JSONObject save(String m_suspension_reason)throws Exception;
	JSONObject getById(Integer Id)throws Exception;
	JSONObject getAll(String formParams)throws Exception;
	JSONObject deleteById(Integer id)throws Exception;
	boolean checkDuplicateData(String data);
}
