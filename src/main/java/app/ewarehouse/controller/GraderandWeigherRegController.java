package app.ewarehouse.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.AocCompProfDetailsMainDTO;
import app.ewarehouse.dto.GraderWeigherMainFormDTO;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.GraderWeigherRegService;
import app.ewarehouse.util.CommonUtil;

@RestController
@RequestMapping("/grader-weigher")
@CrossOrigin("*")
public class GraderandWeigherRegController {
	
	private static final Logger logger = LoggerFactory.getLogger(GraderandWeigherRegController.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private GraderWeigherRegService graderWeigherService;
	
	@PostMapping("/save")
	public ResponseEntity<String> saveGraderWeigher(@RequestBody String data) throws JsonProcessingException {
		JSONObject response = new JSONObject();
		logger.info("Request to save grader weigher details: {}", data);
		response = graderWeigherService.saveGraderWeigherDetails(data);
		logger.info("Grader Weigher details saved successfully");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	//for view page with pagination
	@GetMapping("/paginated")
	public ResponseEntity<?> getAllGraderWeigherDetailsData( @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
       Page<GraderWeigherMainFormDTO> paginatedGraderWeigherDetails = graderWeigherService.getGraderWeigherDetails(page, size);
		    Map<String, Object> response = new HashMap<>();
		    response.put("data", paginatedGraderWeigherDetails.getContent());
		    response.put("currentPage", paginatedGraderWeigherDetails.getNumber());
		    response.put("totalItems", paginatedGraderWeigherDetails.getTotalElements());
		    response.put("totalPages", paginatedGraderWeigherDetails.getTotalPages());

		 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
     }
	
	@PostMapping("/gerGraderWeigherData")
	public ResponseEntity<String> gerGraderWeigherDataById(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject obj = new JSONObject(decodedData);
		String graderWeigherId = (String) obj.get("graderWeigherId");
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(graderWeigherService.getGraderWeigherDetailsById(graderWeigherId)))
				.toString());
		
	}
	
	@PostMapping("/removeExperience")
	public ResponseEntity<String> removeExperience(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject obj = new JSONObject(decodedData);
		String experienceId = (String) obj.get("experienceId");
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(graderWeigherService.removeExperience(experienceId)))
				.toString());
		
	} 
	
	@PostMapping("/duplicateCheck")
	public ResponseEntity<?> duplicateCheckForFullName(@RequestBody String data) throws JsonProcessingException {
	    String decodedData = CommonUtil.inputStreamDecoder(data);
	    JSONObject obj = new JSONObject(decodedData);
	    
	    String empId = obj.getString("empId");

	    boolean isDuplicate = graderWeigherService.duplicateCheck(empId);
	    
	    JSONObject response = new JSONObject();
        response.put("status", 200);
        response.put("result", isDuplicate);

	    return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
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
