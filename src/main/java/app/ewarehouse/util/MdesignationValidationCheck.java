package app.ewarehouse.util;

import org.json.JSONObject;

public class MdesignationValidationCheck {
	public static String BackendValidation(JSONObject obj) {
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtDesignationName").toString())) {
			errorStatus = 1;
			errMsg = "Designation Name Should Not Be Empty!";
		}
		if (errorStatus == 0 && CommonValidator.isCharecterKey(obj.get("txtDesignationName").toString())) {
			errorStatus = 1;
			errMsg = "Designation Name Should Be A Character!";
		}
		if (errorStatus == 0 && !(obj.get("txtAliasName").toString() == "")) {

			if (CommonValidator.isCharecterKey(obj.get("txtAliasName").toString())) {

				errorStatus = 1;
				errMsg = "Alias Name Name Should Be A Character!";
			}
		}
		return errMsg;
	}
}
