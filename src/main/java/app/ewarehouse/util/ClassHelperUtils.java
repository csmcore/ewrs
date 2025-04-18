package app.ewarehouse.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;

public class ClassHelperUtils {

	private ClassHelperUtils() {
	}

	public static Map<String, Object> createSuccessResponse(Object data, String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "success");
		response.put("statusCode", HttpStatus.OK.value());
		response.put("message", message);
		response.put("data", data);
		return response;
	}

	public static Map<String, Object> createDuplicateResponse(Object data, String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "duplicate");
		response.put("statusCode", HttpStatus.OK.value());
		response.put("message", message);
		response.put("data", data);
		return response;
	}

	public static Map<String, Object> createNoContentResponse(String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "failure");
		response.put("statusCode", HttpStatus.NO_CONTENT.value());
		response.put("message", message);
		return response;
	}

	public static Map<String, Object> exceptionContentResponse(Object data, String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "failure");
		response.put("statusCode", HttpStatus.EXPECTATION_FAILED.value());
		response.put("message", message);
		return response;
	}

	public static Map<String, Object> createErrorResponse(String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "failure");
		response.put("statusCode", HttpStatus.NOT_IMPLEMENTED.value());
		response.put("message", message);
		return response;
	}

	public static Map<String, Object> createSuccessEncryptedMap(Object data, String message) {
		Map<String, Object> response = new LinkedHashMap<>();

		response.put("encData", EncryptionUtils
				.encrypt(new JSONObject(ClassHelperUtils.createSuccessResponse(data, message)).toString()));
		return response;
	}

	public static Map<String, Object> createDuplicateEncryptedMap(Object data, String message) {
		Map<String, Object> response = new LinkedHashMap<>();

		response.put("encData", EncryptionUtils
				.encrypt(new JSONObject(ClassHelperUtils.createDuplicateResponse(data, message)).toString()));
		return response;
	}

	public static Map<String, Object> createExceptionEncryptedMap(Object data, String message) {
		Map<String, Object> response = new LinkedHashMap<>();

		response.put("encData", EncryptionUtils
				.encrypt(new JSONObject(ClassHelperUtils.exceptionContentResponse(data, message)).toString()));
		return response;
	}

	public static Map<String, Object> createNoContentEncryptedMap(String message) {
		Map<String, Object> response = new LinkedHashMap<>();

		response.put("encData",
				EncryptionUtils.encrypt(new JSONObject(ClassHelperUtils.createNoContentResponse(message)).toString()));
		return response;
	}

	public static Map<String, Object> createErrorEncryptedMap(String message) {
		Map<String, Object> response = new LinkedHashMap<>();

		response.put("encData",
				EncryptionUtils.encrypt(new JSONObject(ClassHelperUtils.createErrorResponse(message)).toString()));
		return response;
	}
}
