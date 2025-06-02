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
import app.ewarehouse.exception.DuplicateDataFoundException;
import app.ewarehouse.service.LotMasterService;
import app.ewarehouse.util.CommonUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/master/lot")
public class LotMasterController {
	private static final Logger logger = LoggerFactory.getLogger(LotMasterController.class);
	private static final String MESSAGE = "message";
	private static final String STATUS = "status";
	private final LotMasterService service;
	private final ObjectMapper objectMapper;
	

	public LotMasterController(LotMasterService service, ObjectMapper objectMapper) {
		this.service = service;
		this.objectMapper = objectMapper;
	}


	@PostMapping("/save")
	public ResponseEntity<String> saveLot(@RequestBody String data) {
	    logger.info("Inside saveLot method of LotMasterController class");

	    try {
	        JSONObject response = service.saveLot(data);
	        if("Error".equals(response.get(STATUS))) {
	        	throw new Exception();
	        }
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	    } catch (DuplicateDataFoundException e) {
	        logger.warn("Duplicate data error: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	            .body(CommonUtil.inputStreamEncoder(
	                new JSONObject().put(STATUS, "Conflict")
	                                .put(MESSAGE, e.getMessage())
	                                .toString()).toString());
	    } catch (IllegalArgumentException e) {
	        logger.warn("Validation error: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(CommonUtil.inputStreamEncoder(
	                new JSONObject().put(STATUS, "Validation Error")
	                                .put(MESSAGE, e.getMessage())
	                                .toString()).toString());
	    } catch (Exception e) {
	        logger.error("An unexpected error occurred", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(CommonUtil.inputStreamEncoder(
	                new JSONObject().put(STATUS, "Error")
	                                .put(MESSAGE, "An unexpected error occurred while saving data")
	                                .toString()).toString());
	    }
	}
	
	
	
	@GetMapping("/view")
    public ResponseEntity<String> getAll(@RequestParam(value = "page", required = false) Integer pageNumber, @RequestParam(value = "size", required = false) Integer pageSize, @RequestParam(required = false) String search) throws JsonProcessingException {
    	logger.info("Inside getAll method of CountryController");
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(service.getAll(pageNumber, pageSize, search));
        return buildResponse(responseDTO);
    }
	
	
	private ResponseEntity<String> buildResponse(ResponseDTO<?> responseDTO) throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }
}
