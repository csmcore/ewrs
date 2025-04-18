package app.ewarehouse.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.ComplaintMgmtInspSchedule;
import app.ewarehouse.service.ComplaintMgmtInspScheduleService;
import app.ewarehouse.util.CommonUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/complaint-mgmt-insp-schedule")
public class ComplaintMgmtInspScheduleController {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ComplaintMgmtInspScheduleService service;
	
	private static final Logger logger = LoggerFactory.getLogger(ComplaintMgmtInspScheduleController.class);

	@PostMapping("/save")
	public ResponseEntity<String> saveCompanyDetails(@RequestBody String data) throws JsonProcessingException {
		JSONObject response = new JSONObject();
		logger.info("Request to save company details: {}", data);
		response = service.saveSchedule(data);
		logger.info("Company details saved successfully");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	
	@GetMapping("/getData/{id}")
	public ResponseEntity<?> getAllCompanyDetailsData(@PathVariable Integer id) throws JsonProcessingException{
		ComplaintMgmtInspSchedule complaintMgmtInspSchedule = service.getData(id);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(complaintMgmtInspSchedule)).toString());
	}
	
	
	
	
	private <T> String buildJsonResponse(T response) throws JsonProcessingException {
		Object result;
		if (response instanceof JSONObject) {
			result = response.toString();
		} else {
			result = response;
		}

		return objectMapper.writeValueAsString(ResponseDTO.builder().status(200).result(result).build());
	}
	
}
