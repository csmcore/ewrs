package app.ewarehouse.util;

import app.ewarehouse.entity.Tuser;
import app.ewarehouse.repository.TuserRepository;
import com.google.common.hash.Hashing;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
@Component
public class TokenCreaterAndMatcher {
	
	@Autowired
	private TuserRepository mAdminUserMasterRepository;
	
    public static String getHmacMessage(String message) {

        String hash = Hashing.hmacSha256("P2@G2#N201684728499".getBytes(StandardCharsets.UTF_8))
                .hashString(message, StandardCharsets.UTF_8).toString();

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

	public boolean matchAuthorization(String requestData) {
		JSONObject jsonObject = new JSONObject(requestData);
		boolean flag = false;
		
		if (hashRequestMatch(jsonObject.getString("REQUEST_DATA"),
				jsonObject.getString("REQUEST_TOKEN"))) {
			byte[] requestData1 = Base64.getDecoder().decode(jsonObject.getString("REQUEST_DATA"));
			String decodedStr = new String(requestData1, StandardCharsets.UTF_8);
			JSONObject jsonObj = new JSONObject(decodedStr);
			if(jsonObj.getString("USER_TYPE").equals("1")) {
				Tuser mAdminUserMaster = mAdminUserMasterRepository.getById(jsonObj.getInt("USER_ID"));

		}

		}
		return flag;
	}
	}


