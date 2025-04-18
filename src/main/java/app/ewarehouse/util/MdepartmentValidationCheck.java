package app.ewarehouse.util;

import org.json.JSONObject;

public class MdepartmentValidationCheck {
	public static String BackendValidation(JSONObject obj) {
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtDepartmentName").toString())) {
			errorStatus = 1;
			errMsg = "Department Name Should Not Be Empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtDepartmentName").toString(), 2)) {
			errorStatus = 1;
			errMsg = "Department Name  Minimum Length Should Be 2";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.get("txtDepartmentName").toString(), 45)) {
			errorStatus = 1;
			errMsg = "Department Name Maxmimum Length Should Be 32";
		}
		if (errorStatus == 0 && CommonValidator.isCharecterKey(obj.get("txtDepartmentName").toString())) {
			errorStatus = 1;
			errMsg = "Department Name Should Be A Character!";
		}
		if (errorStatus == 0 && CommonValidator.isSpecialCharKey(obj.get("txtDepartmentName").toString())) {
			errorStatus = 1;
			errMsg = "Department Name Should Be SpecialCharKey !";
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
