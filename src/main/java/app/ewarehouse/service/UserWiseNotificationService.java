package app.ewarehouse.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public interface UserWiseNotificationService {


	List<Map<String, Object>> viewUserWiseNotificationById(Integer id);

	JSONObject updateStatusUserNotification(Integer id);


	JSONObject userNameEmailDetails(Integer id);

}
