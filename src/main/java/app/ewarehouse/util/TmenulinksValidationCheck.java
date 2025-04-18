package app.ewarehouse.util;

import org.json.JSONObject;

public class TmenulinksValidationCheck {
	public static String BackendValidation(JSONObject obj) {
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.BlankCheckRdoDropChk(obj.get("selLinkType").toString())) {
			errorStatus = 1;
			errMsg = "Link Type  Should Not Be Empty !";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtLinkName").toString(), 2)) {
			errorStatus = 1;
			errMsg = "Link Name  Minimum Length Should Be 2";
		}
		if (errorStatus == 0 && !(obj.get("txtLinkName").toString() == "")) {

			if (CommonValidator.isCharecterKey(obj.get("txtLinkName").toString()))

			{
				errorStatus = 1;
				errMsg = "Link Name Should Be A Character!";
			}
		}

		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtURL").toString(), 2)) {
			errorStatus = 1;
			errMsg = "URL  Minimum Length Should Be 2";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtCSSClass").toString(), 2)) {
			errorStatus = 1;
			errMsg = "CSS Class  Minimum Length Should Be 2";
		}
		if (errorStatus == 0 && !(obj.get("txtCSSClass").toString() == "")) {

			if (CommonValidator.isCharecterKey(obj.get("txtCSSClass").toString())) {

				errorStatus = 1;
				errMsg = "CSS Class Should Be A Character!";
			}
		}
		if (errorStatus == 0 && CommonValidator.isNumericKey(obj.get("txtSerialNo").toString())) {
			errorStatus = 1;
			errMsg = "Serial No Should Be Numeric!";
		}
		if (errorStatus == 0 && CommonValidator.validNumberCheck(obj.get("txtSerialNo").toString())) {
			errorStatus = 1;
			errMsg = "Serial No  Should Be Valid !";
		}
		return errMsg;
	}
}
