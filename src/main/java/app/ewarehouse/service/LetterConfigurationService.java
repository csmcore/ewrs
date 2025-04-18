package app.ewarehouse.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface LetterConfigurationService {

	JSONObject getAllFormData(String formData);

	JSONObject getwayType();

	JSONObject save(String data) throws Exception;

	JSONObject getLetterTypeData();

	JSONArray getConfigurationKeys();

	JSONObject getAll(String formdata);

	JSONObject deleteById(Integer formdata);

	JSONObject viewAllPublishLetters(Integer formId);

	JSONObject viewLetterList(String data);

	JSONObject getletterData(String getData);

}
