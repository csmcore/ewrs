package app.ewarehouse.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.ComplaintMgmtHearingSchedule;
import app.ewarehouse.service.ComplaintMgmtHearingService;
import app.ewarehouse.util.CommonUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/complaint-mgmt-hearing-schedule")
public class ComplaintMgmtHearingScheduleController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ComplaintMgmtHearingService service;

    private static final Logger logger = LoggerFactory.getLogger(ComplaintMgmtHearingScheduleController.class);

    @PostMapping("/save")
    public ResponseEntity<String> saveHearingDetails(@RequestBody String data) {
        try {
            logger.info("Request to save hearing details: {}", data);
            JSONObject response = service.saveHearingSchedule(data);
            logger.info("Hearing details saved successfully");
            return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON", e);
            return handleJsonProcessingException(e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while saving hearing details", e);
            return handleGenericException(e);
        }
    }
    
    @GetMapping("/getData/{id}")
	public ResponseEntity<?> getHearingData(@PathVariable Integer id) throws JsonProcessingException{
		ComplaintMgmtHearingSchedule complaintMgmthearingData = service.getHearingData(id);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(complaintMgmthearingData)).toString());
	}

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException e) {
        logger.error("JSON Processing Exception: {}", e.getMessage());
        return buildErrorResponse("JSON processing error", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        logger.error("An unexpected error occurred: {}", e.getMessage());
        return buildErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("error", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        try {
            return ResponseEntity.status(status).body(objectMapper.writeValueAsString(errorResponse));
        } catch (JsonProcessingException e) {
            logger.error("Failed to build JSON error response", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"status\":500,\"error\":\"Failed to build error response\"}");
        }
    }

    private <T> String buildJsonResponse(T response) throws JsonProcessingException {
        Object result = response instanceof JSONObject ? response.toString() : response;
        return objectMapper.writeValueAsString(ResponseDTO.builder().status(200).result(result).build());
    }
}
