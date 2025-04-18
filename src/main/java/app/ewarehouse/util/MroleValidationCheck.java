package app.ewarehouse.util;

import org.json.JSONObject;

public class MroleValidationCheck {
	public static String BackendValidation(JSONObject obj) {
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtRoleName").toString())) {
			errorStatus = 1;
			errMsg = "Role Name Should Not  Be Empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtRoleName").toString(), 3)) {
			errorStatus = 1;
			errMsg = "Role Name  Minimum Length Should Be 3";
		}
		if (errorStatus == 0 && CommonValidator.isCharecterKey(obj.get("txtRoleName").toString())) {
			errorStatus = 1;
			errMsg = "Role Name Should Be A Character!";
		}
		if (errorStatus == 0 && CommonValidator.isSpecialCharKey(obj.get("txtRoleName").toString())) {
			errorStatus = 1;
			errMsg = "Role Name Should Be SpecialCharKey !";
		}
if (errorStatus == 0 && !(obj.get("txtAliasName").toString()=="")) {
			
			if(CommonValidator.isCharecterKey(obj.get("txtAliasName").toString())){
				 
			errorStatus = 1;
			errMsg = "Alias Name Should Be A Character!";
		}
			}
		return errMsg;
	}
}
