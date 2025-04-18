package app.ewarehouse.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.ReinstatementActionHistory;
import app.ewarehouse.service.ReinstatementService;
import app.ewarehouse.util.CommonUtil;

@RestController
@RequestMapping("/admin/reinstatement")
public class ReinStatementController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReinStatementController.class);

	@Autowired
	private ReinstatementService reinstatementService;
	
	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping("/getReinstatementData")
	public ResponseEntity<?> getReinstatementData(@RequestBody String requestData) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(requestData);
		JSONObject jsonObject = new JSONObject(decodedData);
		Map<String, Object> response = reinstatementService.getReinstatementData(jsonObject);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}

	@PostMapping("/save")
	public ResponseEntity<String> save(@RequestBody String reinStaementData) throws JsonProcessingException {
		logger.info("Inside save method of ReinStatementController");
		return ResponseEntity.ok(
				CommonUtil.encodedJsonResponse(reinstatementService.saveReinstaement(reinStaementData), objectMapper));
	}

	@PostMapping("/paginateAllData")
	public ResponseEntity<?> getReinstatementViewData(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JsonNode jsonNode = objectMapper.readTree(decodedData);
		int page = jsonNode.get("page").asInt();
		int size = jsonNode.get("size").asInt();
		int userId = jsonNode.get("userId").asInt();
//		    List<Map<String,Object>> paginatedDetails = reinstatementService.getReinstatementViewData(page, size,userId);
		Page<Map<String, Object>> paginatedDetails = reinstatementService.getReinstatementViewData(page, size, userId);

		Map<String, Object> response = new HashMap<>();
		response.put("data", paginatedDetails.getContent());
		response.put("currentPage", paginatedDetails.getNumber());
		response.put("totalItems", paginatedDetails.getTotalElements());
		response.put("totalPages", paginatedDetails.getTotalPages());

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}

	@PostMapping("/paginateAllDataId")
	public ResponseEntity<?> getReinstatementViewDataById(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JsonNode jsonNode = objectMapper.readTree(decodedData);
		String reinsatementId = jsonNode.get("reinsatementId").asText();
//		    List<Map<String,Object>> paginatedDetails = reinstatementService.getReinstatementViewData(page, size,userId);
		Map<String, Object> paginatedDetails = reinstatementService.getReinstatementViewDataById(reinsatementId);

		Map<String, Object> response = new HashMap<>();
		response.put("data", paginatedDetails);

		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}

	@PostMapping("/getReinstatementListData")
	public ResponseEntity<?> getReinstatementListData(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JsonNode jsonNode = objectMapper.readTree(decodedData);
		Integer page = jsonNode.get("page").asInt();
		Integer size = jsonNode.get("size").asInt();
		Integer roleId = jsonNode.get("roleId").asInt();
		Integer userId = jsonNode.get("userId").asInt();
		Page<Map<String, Object>> paginatedDetails = reinstatementService.getReinstatementListData(page, size, userId,
				roleId);

		Map<String, Object> response = new HashMap<>();
		response.put("applications", paginatedDetails.getContent());
		response.put("currentPage", paginatedDetails.getNumber());
		response.put("totalItems", paginatedDetails.getTotalElements());
		response.put("totalPages", paginatedDetails.getTotalPages());
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());

	}

	@PostMapping("/forward-to-committee-member")
	public ResponseEntity<String> forwardToCommitteeMember(@RequestBody String data) throws JsonProcessingException {
		JSONObject response = reinstatementService.forwardToCommitteeMember(data);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}

	@PostMapping("/submit-cm-data")
	public ResponseEntity<String> submitCCData(@RequestBody String data) throws JsonProcessingException {
		JSONObject response = reinstatementService.submitCMData(data);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}

	@PostMapping("/getCMData")
	public ResponseEntity<?> getCompanyData(@RequestBody String requestData) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(requestData);
		JSONObject jsonObject = new JSONObject(decodedData);
		String reinsatementId = jsonObject.getString("reinsatementId");
		Integer userId = jsonObject.getInt("userId");
		Map<String, Object> response = reinstatementService.getCMData(reinsatementId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}

	@PostMapping("/submit-ceosecond-data")
	public ResponseEntity<String> submitCeosecondData(@RequestBody String data) throws JsonProcessingException {
		JSONObject response = reinstatementService.submitCeosecondData(data);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	@PostMapping("/getHistoryData")
	public ResponseEntity<?>  getReinstatementHistory(@RequestBody String requestData) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(requestData);
		JSONObject jsonObject = new JSONObject(decodedData);
		String reinsatementId = jsonObject.getString("reinstatementId");
		List<ReinstatementActionHistory> reinstatementActionHistory=reinstatementService.getHistoryData(reinsatementId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(reinstatementActionHistory)).toString());
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