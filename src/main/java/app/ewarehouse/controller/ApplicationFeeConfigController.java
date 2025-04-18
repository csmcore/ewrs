package app.ewarehouse.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.exception.DuplicateComplaintStatusException;
import app.ewarehouse.service.ApplicationFeeConfigService;
import app.ewarehouse.util.CommonUtil;

@RestController
@RequestMapping("/api/master/application-fee")
@CrossOrigin("*")
public class ApplicationFeeConfigController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationFeeConfigController.class);
	
	private final ApplicationFeeConfigService service;
	private final ObjectMapper objectMapper;
	
	public ApplicationFeeConfigController(ApplicationFeeConfigService service , ObjectMapper objectMapper) {
		this.service = service;
		this.objectMapper = objectMapper;
	}

	@PostMapping("/save")
	public ResponseEntity<String> saveApplicationFeeConfig(@RequestBody String data) {
	    logger.info("Inside saveApplicationFeeConfig method of ApplicationFeeConfigController class");
	    try {
	        JSONObject response = service.saveApplicationFeeConfig(data);
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	    } catch (DuplicateComplaintStatusException e) {
	        logger.error("Duplicate entry error: {}", e.getMessage(), e);
	        JSONObject errorResponse = new JSONObject();
	        errorResponse.put("status", "Error");
	        errorResponse.put("message", e.getMessage());
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body(CommonUtil.inputStreamEncoder(errorResponse.toString()).toString());
	    }catch (Exception e) {
	        logger.error("Unexpected error: {}", e.getMessage(), e);
	        JSONObject errorResponse = new JSONObject();
	        errorResponse.put("status", "Error");
	        errorResponse.put("message", "An unexpected error occurred. Please try again.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(CommonUtil.inputStreamEncoder(errorResponse.toString()).toString());
	    }
	}
	
	//view paginated
	@GetMapping("/view")
    public ResponseEntity<String> getAll(@RequestParam(value = "page", required = false) Integer pageNumber, @RequestParam(value = "size", required = false) Integer pageSize, @RequestParam(required = false) String search) throws JsonProcessingException {
    	logger.info("Inside getAll method of CountryController");
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(service.getAll(pageNumber, pageSize,search));
        return buildResponse(responseDTO);
    }
	
	//get fee by alias name
	@PostMapping("/get-fee")
	public ResponseEntity<String> getFeeByAliasName(@RequestBody String data){
		JSONObject response = service.getFeeByAliasName(data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}
	
	private ResponseEntity<String> buildResponse(ResponseDTO<?> responseDTO) throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }


}
