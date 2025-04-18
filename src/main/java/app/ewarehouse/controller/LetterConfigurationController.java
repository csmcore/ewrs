package app.ewarehouse.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.ewarehouse.service.LetterConfigurationService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.LetterConfigValidationCheck;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/letterConfig")
public class LetterConfigurationController {

	
	private LetterConfigurationService letterConfigurationService;

	public LetterConfigurationController(LetterConfigurationService letterConfigurationService) {
		super();
		this.letterConfigurationService = letterConfigurationService;
	}

	String data = "";
	JSONObject response = new JSONObject();

	private static final Logger logger = LoggerFactory.getLogger(LetterConfigurationController.class);

	@PostMapping("/addletterConfigController/getAllForms")
	public ResponseEntity<?> getAllForms(@RequestBody String formData) {
		logger.info("Inside getAllForms method of LetterConfigurationController");
		try {
			JSONObject requestObj = new JSONObject(formData);
			if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
					requestObj.getString("REQUEST_TOKEN"))) {
				response = letterConfigurationService.getAllFormData(CommonUtil.inputStreamDecoder(formData));
			} else {
				response.put("msg", "error");
				response.put("status", 417);
			}
		} catch (Exception e) {
			logger.error("LetterConfigurationController:getAllForms()",e);	
			response.put("status", 400);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@GetMapping("/addletterConfigController/getwayType")
	public ResponseEntity<?> getGateWay() {

		logger.info("Inside getGateWay method of LetterConfigurationController");
		response = letterConfigurationService.getwayType();

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@GetMapping("/addletterConfigController/getLetterTypeData")
	public ResponseEntity<?> getLetterTypeData() {
		logger.info("Inside getLetterTypeData method of LetterConfigurationController");

		response = letterConfigurationService.getLetterTypeData();

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@PostMapping("/addletterConfigController/addEdit")
	public ResponseEntity<?> create(@RequestBody String formData) throws Exception {
		logger.info("Inside create method of LetterConfigurationController");
		try {
			JSONObject requestObj = new JSONObject(formData);
			if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
					requestObj.getString("REQUEST_TOKEN"))) {
				data = CommonUtil.inputStreamDecoder(formData);
				String validationMsg = LetterConfigValidationCheck.BackendValidation(new JSONObject(data));

				if (validationMsg != null) {
					response.put("status", 502);
					response.put("errMsg", validationMsg);
					logger.warn("Inside create method of LetterConfigurationController Validation Error");
				} else {
					response = letterConfigurationService.save(data);
				}
			} else {
				response.put("msg", "error");
				response.put("status", 417);
			}
		} catch (Exception e) {
			logger.error("Inside create method of LetterConfigurationController error occur:" + e);
			response.put("status", 400);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@PostMapping("/addletterConfigController/getConfigurationKeys")
	public ResponseEntity<?> getConfigurationKeys(@RequestBody String formData) {
		logger.info("Inside getConfigurationKeys method of LetterConfigurationController");
		JSONArray jsArr = letterConfigurationService.getConfigurationKeys();
		if (jsArr != null) {
			response.put("result", jsArr);
			response.put("status", 200);
		} else {

			response.put("status", 417);
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@PostMapping("/addletterConfigController/viewLetterConfig")
	public ResponseEntity<?> viewLetterConfig(@RequestBody String formParams) {
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
					requestObj.getString("REQUEST_TOKEN"))) {
				response = letterConfigurationService.getAll(CommonUtil.inputStreamDecoder(formParams));
			} else {
				response.put("msg", "error");
				response.put("status", 417);
			}
		} catch (Exception e) {
			logger.error("Inside getAll method of LetterConfigurationController error occur:" + e);
			response.put("status", 400);
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@PostMapping("/addletterConfigController/DeleteLetterConfig")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		logger.info("Inside delete method of LetterConfigurationController");
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
					requestObj.getString("REQUEST_TOKEN"))) {
				data = CommonUtil.inputStreamDecoder(formParams);
				JSONObject json = new JSONObject(data);
				response = letterConfigurationService.deleteById(json.getInt("itemId"));
			} else {
				response.put("msg", "error");
				response.put("status", 417);
			}
		} catch (Exception e) {
			logger.error("Inside delete method of LetterConfigurationController error occur:" + e);
			response.put("status", 400);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@PostMapping("/addletterConfigController/viewAllPublishedLetters")
	public ResponseEntity<?> viewAllPublishedLetters(@RequestBody String requestParam) {

		logger.info("Inside viewAllPublishedLetters method of LetterConfigurationController");
		try {
			JSONObject requestObj = new JSONObject(requestParam);
			if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
					requestObj.getString("REQUEST_TOKEN"))) {
				data = CommonUtil.inputStreamDecoder(requestParam);
				JSONObject json = new JSONObject(data);
				response = letterConfigurationService.viewAllPublishLetters(json.getInt("formId"));
			} else {
				response.put("msg", "error");
				response.put("status", 417);
			}
		} catch (Exception e) {
			logger.error("Inside viewAllPublishedLetters method of LetterConfigurationController error occur:" + e);
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

	@PostMapping("/addletterConfigController/getLetterlist")
	public ResponseEntity<?> getLetterlist(@RequestBody String requestParam) {

		logger.info("Inside getLetterlist method of LetterConfigurationController");
		try {
			JSONObject requestObj = new JSONObject(requestParam);
			if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
					requestObj.getString("REQUEST_TOKEN"))) {
				data = CommonUtil.inputStreamDecoder(requestParam);

				response = letterConfigurationService.viewLetterList(data);
			} else {
				response.put("msg", "error");
				response.put("status", 417);
			}
		} catch (Exception e) {
			logger.error("Inside getLetterlist method of LetterConfigurationController error occur:" + e);
		}

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}

}
