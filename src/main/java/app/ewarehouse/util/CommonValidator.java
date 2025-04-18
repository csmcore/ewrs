package app.ewarehouse.util;

import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonValidator {
	public static boolean isEmpty(String data) {
		return (data == null || data.isEmpty() || data.trim().isEmpty());
	}

	public static boolean minLengthCheck(String data, int minLength) {
		return (data.length() > 0 && data.length() < minLength);
	}

	public static boolean maxLengthCheck(String data, int maxLength) {
		return (data.length() > 0 && data.length() > maxLength);
	}

	public static boolean isCharecterKey(String charKey, String specialCharecter) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(charKey)) {

			String charKeyRegex = "^[A-Za-z" + specialCharecter + " ]+$";
			Pattern pattern = Pattern.compile(charKeyRegex);
			Matcher matcher = pattern.matcher(charKey);
// return matcher.matches();

			if (matcher.matches()) {
			} else {
				status = true;
			}

		}
		return status;

	}

	public static boolean isCharecterKey(String charKey) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(charKey)) {

			String charKeyRegex = "^[A-Za-z ]+$";
			Pattern pattern = Pattern.compile(charKeyRegex);
			Matcher matcher = pattern.matcher(charKey);
// return matcher.matches();

			if (matcher.matches()) {
			} else {
				status = true;
			}

		}
		return status;

	}

	public static boolean isNumericKey(String numbeKey) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(numbeKey)) {
			String numbeKeyRegex = "^[0-9]+$";
			Pattern pattern = Pattern.compile(numbeKeyRegex);
			Matcher matcher = pattern.matcher(numbeKey);
// return matcher.matches();

			if (matcher.matches()) {
			} else {
				status = true;
			}

		}
		return status;
	}

	public static boolean isAlphaNumericKey(String alphaNumKey) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(alphaNumKey)) {
			String alphaNumKeyRegex = "^[0-9a-zA-Z @.-/,]*$";
			Pattern pattern = Pattern.compile(alphaNumKeyRegex);
			Matcher matcher = pattern.matcher(alphaNumKey);

			if (matcher.matches()) {
			} else {
				status = true;
			}

		}
		return status;
	}

	public static boolean isDecimal(String decimalKey) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(decimalKey)) {
			String decimalKeyRegex = "^\\d+?\\.\\d+?$";
			Pattern pattern = Pattern.compile(decimalKeyRegex);
			Matcher matcher = pattern.matcher(decimalKey);

			if (matcher.matches()) {
			} else {
				status = true;
			}

		}
		return status;
	}

	public static boolean emailCheck(String emailId) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(emailId)) {
			String emailRegex = "^([a-zA-Z0-9_.+-])+\\@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";
			Pattern pattern = Pattern.compile(emailRegex);
			Matcher matcher = pattern.matcher(emailId);
// return matcher.matches();

			if (matcher.matches()) {
			} else {
				status = true;
			}

		}
		return status;
	}

	public static boolean isSpecialCharKey(String text, String specialCharecter) {
		Boolean status = true;
		if (!Strings.isNullOrEmpty(text)) {
			String textRegx = "[" + specialCharecter + "]*";
			Pattern pattern = Pattern.compile(textRegx);
			Matcher matcher = pattern.matcher(text);
// return matcher.matches();

			if (matcher.matches()) {
			} else {
				status = false;
			}

		}
		return status;
	}

	public static boolean isSpecialCharKey(String text) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(text)) {
			String textRegx = "[^&%$#@!~]*";
			Pattern pattern = Pattern.compile(textRegx);
			Matcher matcher = pattern.matcher(text);
// return matcher.matches();

			if (matcher.matches()) {
			} else {
				status = true;
			}

		}
		return status;
	}

	public static boolean validNumberCheck(String numbeKey) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(numbeKey)) {
			String numbeKeyRegex = "^[0-9]+$";
			Pattern pattern = Pattern.compile(numbeKeyRegex);
			Matcher matcher = pattern.matcher(numbeKey);
// return matcher.matches();

			if (matcher.matches()) {
			} else {
				status = true;
			}

		}
		return status;
	}

	public static boolean validTelCheck(String mobile) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(mobile)) {
			String mobileRegex = "^0[0-9]{9}$";
			Pattern pattern = Pattern.compile(mobileRegex);
			Matcher matcher = pattern.matcher(mobile);
// return matcher.matches();
			if (matcher.matches()) {
			} else {
				status = true;
			}
		}
		return status;

	}

	public static boolean validPwdCheck(String password) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(password)) {
			String passwordRegex = "^.*(?=.{8,15})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&!%()*?]).*$";

			Pattern pattern = Pattern.compile(passwordRegex);
			Matcher matcher = pattern.matcher(password);
			// return matcher.matches();
			if (matcher.matches()) {
			} else {
				status = true;

			}
		}
		return status;

	}

	public static boolean isUrl(String url) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(url)) {
			String urlRegex = "^(?:(?:https?|ftp):\\/\\/)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})))(?::\\d{2,5})?(?:\\/\\S*)?$";
			Pattern pattern = Pattern.compile(urlRegex);
			Matcher matcher = pattern.matcher(url);
			if (matcher.matches()) {
			} else {
				status = true;
			}
		}
		return status;
	}

	public static boolean isPassword(String password) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(password)) {
			String passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\\s).{8,15}$";
			Pattern pattern = Pattern.compile(passwordRegex);
			Matcher matcher = pattern.matcher(password);
			if (matcher.matches()) {
			} else {
				status = true;

			}

		}
		return status;
	}

	public static boolean BlankCheckRdoDropChk(String data) {
		return (data == null || data.isEmpty() || data.trim().isEmpty()) || data.equals("0");
	}

	public static boolean validateFile(String str) {
		Boolean status = false;
		if (!Strings.isNullOrEmpty(str)) {
			String regex = "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp|pdf|doc|docx|xls|jpg|csv|dbf|htm|html|mht|mhtml|ods|prn|txt|xla|xlam|xlsb|xlsx|xlt|xltm|xlsm|xlw|xps|docm|dot|dotm|dotx|odt|rtf|wps|xml|msword|mp4|ogx|oga|ogv|ogg|webm|mp3|mpeg))$)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(str);
			if (matcher.matches()) {

			} else {
				status = true;
			}
		}
		return status;

	}
}