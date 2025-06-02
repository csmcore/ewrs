package app.ewarehouse.controller;

import java.util.Iterator;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class SQLInjection {

	// Check SQLInjection(Ritesh Parida)
	public static Integer sqlInjection(String data, Integer type) {
		String[] dataVal = { "<button", "<form", "<iframe", "<input", "<script", "<select", "<textarea", "alert(",
				"prompt(", "confirm(", "function(", "onchange(", "onclick(", "onerror(", "onblur(", "onmouseover(",
				"onmouseout(", "onkeydown(", "onkeyup(", "onload(", "\'" };

		JSONObject json = null;
		Integer status = 0;
		if (type == 0) {
			json = new JSONObject(data);
			Iterator<String> keys = json.keys();

			while (keys.hasNext()) {
				String key = keys.next();
				Object value = json.get(key);
				for (String data1 : dataVal) {

					if (key.trim().contains(data1) || value.toString().trim().contains(data1)) {
						status = 1;
						break;
					}
				}
				if (status == 1) {
					break;
				}

			}
		} else {
			String[] splitMeth = data.split("[=]");
			for (String data5 : splitMeth) {
				for (String data6 : dataVal) {
					data5 = data5.replaceAll("\\s+", "");
					if (data5.trim().contains(data6)) {
						status = 1;
						break;
					}
				}
				if (status == 1) {
					break;
				}
			}
		}
		return status;

	}
}
