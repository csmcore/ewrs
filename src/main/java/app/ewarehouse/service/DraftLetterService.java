package app.ewarehouse.service;

import org.json.JSONObject;

public interface DraftLetterService {

	JSONObject saveDraftLetter(String data);

	JSONObject getLetterById(Integer  intConfigId);

	JSONObject generateStatusByintConfigId(Integer intConfigId,Integer processId,Integer roleId);

	String getLetterContentByIntConfigId(Integer intLetterConfigId, Integer intServiceId);

	String getIntLetterConfigIdByIntConfigId(Integer parseInt);

    String saveDocumentByIds(String data, Integer procesId, Integer serviceId,Integer roleId);

	JSONObject generateStatuByRoleId(Integer serviceId);

	JSONObject saveInApprovalForWorkFolw(String data);

	

}
