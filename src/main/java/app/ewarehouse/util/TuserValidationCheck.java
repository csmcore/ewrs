package app.ewarehouse.util;

import org.json.JSONObject;

public class TuserValidationCheck {
	public static String BackendValidation(JSONObject obj) {
		String errMsg = null;
		Integer errorStatus = 0;
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtFullName").toString())) {
			errorStatus = 1;
			errMsg = "Full Name Should Not  Be Empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtFullName").toString(), 2)) {
			errorStatus = 1;
			errMsg = "Full Name  Minimum Length Should Be 2";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.get("txtFullName").toString(), 32)) {
			errorStatus = 1;
			errMsg = "Full Name Maxmimum Length Should Be 32";
		}
		if (errorStatus == 0 && CommonValidator.isCharecterKey(obj.get("txtFullName").toString())) {
			errorStatus = 1;
			errMsg = "Full Name Should Be A Character!";
		}
		if (errorStatus == 0 && CommonValidator.isSpecialCharKey(obj.get("txtFullName").toString())) {
			errorStatus = 1;
			errMsg = "Full Name Should Be SpecialCharKey !";
		}
		if (errorStatus == 0 && CommonValidator.validateFile(obj.optString("filePhoto"))) {
			errorStatus = 1;
			errMsg = "Invalid File Type !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtMobileNo").toString())) {
			errorStatus = 1;
			errMsg = "Mobile No Should Not  Be Empty!";
		}
//		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtMobileNo").toString(), 10)) {
//			errorStatus = 1;
//			errMsg = "Mobile No  Minimum Length Should Be 10";
//		}
//		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.get("txtMobileNo").toString(), 10)) {
//			errorStatus = 1;
//			errMsg = "Mobile No Maxmimum Length Should Be 14";
//		}
//		if (errorStatus == 0 && CommonValidator.isNumericKey(obj.get("txtMobileNo").toString())) {
//			errorStatus = 1;
//			errMsg = "Mobile No Should Be Numeric!";
//		}
//		if (errorStatus == 0 && CommonValidator.validNumberCheck(obj.get("txtMobileNo").toString())) {
//			errorStatus = 1;
//			errMsg = "Mobile No Should Be Valid !";
//		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtEmailId").toString())) {
			errorStatus = 1;
			errMsg = "Email Id Should Not  Be Empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtEmailId").toString(), 5)) {
			errorStatus = 1;
			errMsg = "Email Id  Minimum Length Should Be 5";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.get("txtEmailId").toString(), 50)) {
			errorStatus = 1;
			errMsg = "Email Id Maxmimum Length Should Be 50";
		}
		if (errorStatus == 0 && CommonValidator.emailCheck(obj.get("txtEmailId").toString())) {
			errorStatus = 1;
			errMsg = "Email Id  Should Be Valid !";
		}
//		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.optString("txtAlternateMobileNumber"), 10)) {
//			errorStatus = 1;
//			errMsg = "Alternate Mobile Number  Minimum Length Should Be 10";
//		}
//		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.optString("txtAlternateMobileNumber"), 10)) {
//			errorStatus = 1;
//			errMsg = "Alternate Mobile Number Maxmimum Length Should Be 10";
//		}
//		if (errorStatus == 0 && !(obj.optString("txtAlternateMobileNumber")=="")){
//				
//			if(CommonValidator.isNumericKey(obj.optString("txtAlternateMobileNumber")))
//				 {
//			errorStatus = 1;
//			errMsg = "Alternate Mobile Number Should Be Numeric!";
//				 }
//		}
//		
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.optString("txtrAddress"), 2)) {
			errorStatus = 1;
			errMsg = "Address  minimum Length Should Be 2";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.optString("txtrAddress"), 125)) {
			errorStatus = 1;
			errMsg = "Address Maxmimum Length Should Be 125";
		}
		if (errorStatus == 0 && CommonValidator.isAlphaNumericKey(obj.optString("txtrAddress"))) {
			errorStatus = 1;
			errMsg = "Address Should Be AlphaNumeric !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtUserId").toString())) {
			errorStatus = 1;
			errMsg = "User Id Should Not  Be Empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtUserId").toString(), 2)) {
			errorStatus = 1;
			errMsg = "User Id  Minimum Length Should Be 2";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.get("txtUserId").toString(), 50)) {
			errorStatus = 1;
			errMsg = "User Id maxmimum length should be 50";
		}
//		if (errorStatus == 0 && CommonValidator.isAlphaNumericKey(obj.get("txtUserId").toString())) {
//			errorStatus = 1;
//			errMsg = "User Id Should Be AlphaNumeric !";
//		}
//		if (errorStatus == 0 && CommonValidator.isSpecialCharKey(obj.get("txtUserId").toString())) {
//			errorStatus = 1;
//			errMsg = "User Id Should Be SpecialCharKey !";
//		}
		
		if(obj.has("intId")&& obj.getString("intId")=="") {
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtPassword").toString())) {
			errorStatus = 1;
			errMsg = "Password Should Not  Be Empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtPassword").toString(), 3)) {
			errorStatus = 1;
			errMsg = "Password  Minimum Length Should Be 3";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.get("txtPassword").toString(), 45)) {
			errorStatus = 1;
			errMsg = "Password Maxmimum Length Should Be 45";
		}
		if (errorStatus == 0 && CommonValidator.isAlphaNumericKey(obj.get("txtPassword").toString())) {
			errorStatus = 1;
			errMsg = "Password Should Be AlphaNumeric !";
		}
		if (errorStatus == 0 && CommonValidator.isEmpty(obj.get("txtConfirmPassword").toString())) {
			errorStatus = 1;
			errMsg = "Confirm Password Should Not  Be Empty!";
		}
		if (errorStatus == 0 && CommonValidator.minLengthCheck(obj.get("txtConfirmPassword").toString(), 3)) {
			errorStatus = 1;
			errMsg = "Confirm Password  Minimum Length Should Be 3";
		}
		if (errorStatus == 0 && CommonValidator.maxLengthCheck(obj.get("txtConfirmPassword").toString(), 45)) {
			errorStatus = 1;
			errMsg = "Confirm Password Maxmimum Length Should Be 45";
		}
		if (errorStatus == 0 && CommonValidator.isAlphaNumericKey(obj.get("txtConfirmPassword").toString())) {
			errorStatus = 1;
			errMsg = "Confirm Password Should Be AlphaNumeric !";
		}
		
		}
		return errMsg;
	}
}
