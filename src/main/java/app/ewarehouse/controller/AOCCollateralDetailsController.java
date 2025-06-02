package app.ewarehouse.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.AocCompProfDetailsMainDTO;
import app.ewarehouse.dto.ApplicationCertificateOfCollateralDto;
import app.ewarehouse.dto.CollateralManagerMainFormDTO;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.dto.TempUserDTO;
import app.ewarehouse.entity.CollateralTakeActionCC;
import app.ewarehouse.service.AOCCollateralDetailsService;
import app.ewarehouse.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/collateral")
@CrossOrigin("*")
public class AOCCollateralDetailsController {
	
	private static final Logger logger = LoggerFactory.getLogger(AOCCollateralDetailsController.class);
	
	@Autowired
	private AOCCollateralDetailsService aocCollateralDetailsService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@GetMapping("/getCollateralInfo/{emailId}")
	public ResponseEntity<?> getAllCompanyDetailsData(@PathVariable("emailId") String emailId) throws JsonProcessingException{
		TempUserDTO companyDetailsDTO = aocCollateralDetailsService.getCollateralInfoByUserId(emailId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(companyDetailsDTO)).toString());
	}
	
	@PostMapping("/saveAsDraft")
	public ResponseEntity<String> saveAsDraftCollateralDetails(@RequestBody String data) throws JsonProcessingException {
		JSONObject response = new JSONObject();
		logger.info("Request to save company details: {}", data);
		response = aocCollateralDetailsService.saveAsDraftCollateralDetails(data);
		logger.info("Company details saved successfully");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	//get company data by id for edit purpose
	@PostMapping("/getCollateralById")
	public ResponseEntity<String> getCollateralDataById(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject obj = new JSONObject(decodedData);
		
		String collateralId = (String) obj.get("collateralId");
	   
        return ResponseEntity.ok(CommonUtil
			.inputStreamEncoder(buildJsonResponse(aocCollateralDetailsService.getCollateralDetails(collateralId))).toString());
			
	}
	
	@GetMapping("/getCollateralDetails/{userId}")
	public ResponseEntity<?> getAllCompanyDetailsData(@PathVariable Integer userId) throws JsonProcessingException{
		List<CollateralManagerMainFormDTO> collateralDetailsDTO = aocCollateralDetailsService.getCollateralDataByUserId(userId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(collateralDetailsDTO)).toString());
	}
	
	@GetMapping("/paginated/{userId}")
	public ResponseEntity<?> getAllCollateralDetailsData(
	    @PathVariable Integer userId,
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {

	    Page<CollateralManagerMainFormDTO> paginatedCollateralDetails = aocCollateralDetailsService.getCollateralDataByUserId(userId, page, size);

	    Map<String, Object> response = new HashMap<>();
	    response.put("data", paginatedCollateralDetails.getContent());
	    response.put("currentPage", paginatedCollateralDetails.getNumber());
	    response.put("totalItems", paginatedCollateralDetails.getTotalElements());
	    response.put("totalPages", paginatedCollateralDetails.getTotalPages());

	    return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	@PostMapping("/updatePaymentStatus")
	public ResponseEntity<String> updateMainTable(@RequestBody String code) throws JsonProcessingException{
		JSONObject response =  aocCollateralDetailsService.updateMainTable(code);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	} 
	
	
	@PostMapping("/getStatusDataCollateral")
	public ResponseEntity<?> getCollateralStatus(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JsonNode jsonNode = objectMapper.readTree(decodedData);
		String status = jsonNode.get("status").asText();
		Integer page = jsonNode.get("page").asInt();
		Integer size = jsonNode.get("size").asInt();
		Integer roleId=jsonNode.get("roleId").asInt();
		Integer userId=jsonNode.get("userId").asInt();
		Page<ApplicationCertificateOfCollateralDto> applications =  aocCollateralDetailsService.getAllApplicationsData(page,size,status,roleId,userId);

		   	Map<String, Object> response = new HashMap<>();
		    response.put("applications", applications.getContent());
		    response.put("currentPage", applications.getNumber());
		    response.put("totalItems", applications.getTotalElements());
		    response.put("totalPages", applications.getTotalPages());
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());

	}
	
	@PostMapping("/forward-to-cc-member")
	 public ResponseEntity<String> forwardToCC(@RequestBody String data) throws JsonProcessingException{
		 JSONObject response = aocCollateralDetailsService.forwardToCC(data);
		 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	 }
	
	@PostMapping("/forward-to-inspector")
	 public ResponseEntity<String> forwardToInsp(@RequestBody String data) throws JsonProcessingException{
		 JSONObject response = aocCollateralDetailsService.forwardToInsp(data);
		 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	 }
	
	@PostMapping("/submit-cc-data")
 	public ResponseEntity<String> submitCCData(@RequestBody String data) throws JsonProcessingException{
		 JSONObject response = aocCollateralDetailsService.submitCCData(data);
		 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	 }
	
	@PostMapping("/submit-inspector-data")
 	public ResponseEntity<String> submitInspectorData(@RequestBody String data) throws JsonProcessingException{
		 JSONObject response = aocCollateralDetailsService.submitInspectorData(data);
		 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	 }
	
	@PostMapping("/submit-oicsecond-data")
 	public ResponseEntity<String> submitOicSecondData(@RequestBody String data) throws JsonProcessingException{
		 JSONObject response = aocCollateralDetailsService.submitOicSecondData(data);
		 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	 }
	
	@PostMapping("/getCCDataById")
	public ResponseEntity<String> getCCDataById(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject obj = new JSONObject(decodedData);
		
		String collateralId = (String) obj.get("collateralId");
		Integer userId = (Integer) obj.get("userId");
	   
        return ResponseEntity.ok(CommonUtil
			.inputStreamEncoder(buildJsonResponse(aocCollateralDetailsService.getInspectorDataById(collateralId,userId))).toString());
	}
	
	 @PostMapping("/getOicTwoData")
	    public ResponseEntity<?> getCompanyData(@RequestBody String requestData) throws JsonProcessingException {
	    	 String decodedData = CommonUtil.inputStreamDecoder(requestData);
	    	 JSONObject jsonObject = new JSONObject(decodedData);
	    	String collateralId = jsonObject.getString("collateralId");
	    	Integer userId = jsonObject.getInt("userId");
	        Map<String, Object> response = aocCollateralDetailsService.getOicTwoData(collateralId);
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	    }
	
	 @GetMapping("/paymentDetails/{applicationId}/{userId}")
	 public ResponseEntity<?> getPaymentByTransactionNo(@PathVariable String applicationId,@PathVariable Integer userId) throws JsonProcessingException {
			
		    return ResponseEntity.ok(CommonUtil
		     .inputStreamEncoder(buildJsonResponse(aocCollateralDetailsService.getPaymentDetailsByUserId(applicationId,userId))).toString());
	}
	 
	 @GetMapping("/paymentDetails/{applicationId}")
	 public ResponseEntity<?> getPaymentByTransactionNo(@PathVariable String applicationId) throws JsonProcessingException {
			
		    return ResponseEntity.ok(CommonUtil
		     .inputStreamEncoder(buildJsonResponse(aocCollateralDetailsService.getPaymentDetails(applicationId))).toString());
	}
		
    @PostMapping("/saveCeoTakeAction")
	public ResponseEntity<String> saveTakeAction(@RequestBody String data) throws JsonProcessingException {
		        
		JSONObject response = new JSONObject();
		logger.info("Request to save ceo remark: {}", data);
		response = aocCollateralDetailsService.saveCeoTakeAction(data);
		logger.info("Ceo remark saved successfully");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	 }
    
    @PostMapping("/removeCollateralDirector")
	public ResponseEntity<String> removeDirector(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject obj = new JSONObject(decodedData);
		String directorId = (String) obj.get("directorId");
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(aocCollateralDetailsService.removeCollateralDirector(directorId)))
				.toString());
		
	} 
    
    @PostMapping("/removeCvDetails")
	public ResponseEntity<String> removeCvDetails(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject obj = new JSONObject(decodedData);
		String cvId = (String) obj.get("cvId");
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(aocCollateralDetailsService.removeCvDetails(cvId)))
				.toString());
		
	} 
    
    @GetMapping("/countCollateralByUserId/{userId}")
    public ResponseEntity<String> getCollateralCountByUserId(@PathVariable Integer userId) throws JsonProcessingException {
 		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(aocCollateralDetailsService.getCollateralCountByUserId(userId))).toString());
    }
    
    @GetMapping("/countCollateral/{userId}")
    public ResponseEntity<String> getCollateralCount(@PathVariable Integer userId) throws JsonProcessingException {
 		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(aocCollateralDetailsService.getCollateralCount(userId))).toString());
    }
    
    @PostMapping("/getCCdDetailsDataById")
	public ResponseEntity<?> getCCdDetailsDataById(@RequestBody String requestData) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(requestData);
		JSONObject jsonObject = new JSONObject(decodedData);
		Integer roleId = jsonObject.getInt("roleId");
		Integer userid = jsonObject.getInt("userId");
		String applicationId = jsonObject.getString("applicationId");
		CollateralTakeActionCC data = aocCollateralDetailsService.getCCdDetailsDataById(roleId,userid,applicationId);
		Map<String, Object> response = new HashMap<>();
		response.put("isDuplicate", data);
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
	
	@PostMapping("/generate-collateral-certificate")
 	public ResponseEntity<String> generateCollateralCertificate(@RequestBody String data) throws JsonProcessingException{
		 JSONObject response = aocCollateralDetailsService.generateCollateralCertificate(data);
		 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	 }
	
	@GetMapping("/getCollateralCertificate/{applicantId}")
	public ResponseEntity<?> getCollateralcertificate(@PathVariable("applicantId") String applicantId)
			throws JsonProcessingException {
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(buildJsonResponse(aocCollateralDetailsService.getCollateralcertificate(applicantId)))
				.toString());
	}

}
