package app.ewarehouse.util;

import app.ewarehouse.dto.ResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import jakarta.persistence.EntityManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class CommonUtil {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	public static String inputStreamDecoder(String data) {
		JSONObject requestObj = new JSONObject(data);
		byte[] decoded = Base64.getDecoder().decode(requestObj.getString("REQUEST_DATA"));
		return new String(decoded, StandardCharsets.UTF_8);
	}
	public static JSONObject inputStreamEncoder(String data) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("RESPONSE_DATA", Base64.getEncoder().encodeToString(data.getBytes()));
		jsonObject.put("RESPONSE_TOKEN", getHmacMessage(Base64.getEncoder().encodeToString(data.getBytes())));
		return jsonObject;
	}

	public static String getHmacMessage(String message) {
		String hash = Hashing.hmacSha256("P2@G2#N201684728499".getBytes(StandardCharsets.UTF_8)).hashString(message, StandardCharsets.UTF_8).toString();
		return hash;
	}

	public static boolean hashRequestMatch(String requestData, String requestToken) {
		boolean flag = false;
		String HmacMessage = getHmacMessage(requestData);
		if (HmacMessage.equals(requestToken)) {
			flag = true;
		}
		return flag;
	}
	public static List<Object[]> getDynResultList(EntityManager em, String query) {
		logger.info("Inside getDynResultList method of CommonUtil");
		return em.createNativeQuery(query).getResultList();
	}
	public static Object getDynSingleData(EntityManager em, String query) {
		logger.info("Inside getDynSingleData method of CommonUtil");
		return em.createNativeQuery(query).getSingleResult();
	}
	public static <T> String encodedJsonResponse(T response, ObjectMapper objectMapper) throws JsonProcessingException {
		return inputStreamEncoder(
				objectMapper.writeValueAsString(
						ResponseDTO.<T>builder()
							.status(200)
							.result(response)
							.build()
				)
		).toString();
	}
	
	public static String validateSmsString(String sms) {
        if (sms.contains(" ")) {
            sms = sms.replace(" ", "%20");
            return sms;
        } else {
            return sms;
        }
    }
}
