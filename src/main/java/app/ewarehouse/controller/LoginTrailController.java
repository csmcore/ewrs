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
import app.ewarehouse.service.LoginTrailService;
import app.ewarehouse.util.CommonUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class LoginTrailController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginTrailController.class);

	private LoginTrailService loginTrailService;
	private final ObjectMapper objectMapper;
	

	public LoginTrailController(LoginTrailService loginTrailService,ObjectMapper objectMapper) {
		this.objectMapper=objectMapper;
		this.loginTrailService = loginTrailService;
	}
	
	@PostMapping("/login-trail/save")
	public ResponseEntity<String> saveLoginTrailData(@RequestBody String data) throws JsonProcessingException {
		JSONObject response = new JSONObject();
		logger.info("Request to save Login Trail details: {}", data);
		response = loginTrailService.saveLoginTrailData(data);
		logger.info("Login Trail details saved successfully");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	@GetMapping("/viewLoginTrail")
	public ResponseEntity<String> getAll(@RequestParam(value = "page", required = false) Integer pageNumber, @RequestParam(value = "size", required = false) Integer pageSize,  @RequestParam(value = "sortDirection", required = false) String sortDir,  @RequestParam(value = "sortColumn", required = false) String sortCol, @RequestParam(required = false) String search) throws JsonProcessingException {
	    	logger.info("Inside getAll method of LoginTrailController");
	        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
	        responseDTO.setStatus(HttpStatus.OK.value());
	        responseDTO.setResult(loginTrailService.getAll(pageNumber, pageSize, sortCol, sortDir, search));
	        return buildResponse(responseDTO);
    } 
	
   private ResponseEntity<String> buildResponse(ResponseDTO<?> responseDTO) throws JsonProcessingException {
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
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
