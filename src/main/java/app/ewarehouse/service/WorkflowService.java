package app.ewarehouse.service;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface WorkflowService {

	

	List<Map<String, Object>> getallApprovalAction();
	List<Map<String, Object>> getallOfficersApi();
	String setWorkflow(String setWorkflow);
	List<Map<String, Object>> getFormName();
	JSONObject fillWorkflow(String request,Integer workFlowControlId, String dynFilterDetails);
	

}
