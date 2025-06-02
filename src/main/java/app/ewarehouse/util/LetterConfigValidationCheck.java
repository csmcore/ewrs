package app.ewarehouse.util;

import org.json.JSONObject;

public class LetterConfigValidationCheck {

	public static String BackendValidation(JSONObject obj) {
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("lettername").toString())) {
			errorStatus = 1;
			errMsg = "Letter Name Should Not  Be Empty!";
		}

		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.get("lettername").toString(), 50)) {
			errorStatus = 1;
			errMsg = "Letter Name Maxmimum Length Should Be 32";
		}
		if (errorStatus == 0 && CommonValidator.isCharecterKey(obj.get("lettername").toString())) {
			errorStatus = 1;
			errMsg = "Letter Name Should Be A Character!";
		}
		if (errorStatus == 0 && CommonValidator.isSpecialCharKey(obj.get("lettername").toString())) {
			errorStatus = 1;
			errMsg = "Letter Name Should Be SpecialCharKey !";
		}

		return errMsg;
	}
}
