package app.ewarehouse.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.AuditTrailService;
import app.ewarehouse.util.CommonUtil;

@RestController
@RequestMapping("/api/audiTrial")
public class AuditTraillController {
	 private static final Logger logger = LoggerFactory.getLogger(AuditTraillController.class);
	 
	 @Autowired
	 private ObjectMapper objectMapper;

	 @Autowired
	 private AuditTrailService auditTrailService;
	 
	 @GetMapping("/list")
	    public ResponseEntity<String> getAllTrialList(@RequestParam(value = "page", required = false) Integer pageNumber, 
	    											  @RequestParam(value = "size", required = false) Integer pageSize,  
	    											  @RequestParam(value = "sortDirection", required = false) String sortDir,  
	    											  @RequestParam(value = "sortColumn", required = false) String sortCol, 
	    											  @RequestParam(required = false) String search) throws JsonProcessingException {
	    	logger.info("Inside getAllTrialList method of AuditTrialController");
	        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
	        responseDTO.setStatus(HttpStatus.OK.value());
	        responseDTO.setResult(auditTrailService.getAllAuditTrialList(pageNumber, pageSize, sortCol, sortDir, search));
	        return buildResponse(responseDTO);
	    }


	 @PostMapping("/save")
	 public ResponseEntity<String> saveAuditTrail(@RequestBody String data) throws JsonProcessingException{
//		Runnable r= ()->{auditTrailService.saveAuditTrail(data);};
//		new Thread(r).start();
		 		 ResponseDTO<Object> responseDTO = new ResponseDTO<>();
	        responseDTO.setStatus(HttpStatus.OK.value());
	        responseDTO.setResult(auditTrailService.saveAuditTrail(data));
	        return buildResponse(responseDTO);
	 }
	 
	 private ResponseEntity<String> buildResponse(ResponseDTO<?> responseDTO) throws JsonProcessingException {
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
	    }
	 

}
