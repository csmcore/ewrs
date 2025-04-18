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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.AocCompProfDetailsMainDTO;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.AocCompProfDetailsMainService;
import app.ewarehouse.util.CommonUtil;

@RestController
@RequestMapping("/aoccompany")
@CrossOrigin("*")
public class AocCompanyProfileDetailsController {

	private static final Logger logger = LoggerFactory.getLogger(AocCompanyProfileDetailsController.class);

	@Autowired
	private AocCompProfDetailsMainService aocService;

	@Autowired
	private ObjectMapper objectMapper;

	// Endpoint to save company details
	@PostMapping("/save")
	public ResponseEntity<String> saveCompanyDetails(@RequestBody String data) throws JsonProcessingException {
		JSONObject response = new JSONObject();
		logger.info("Request to save company details: {}", data);
		response = aocService.saveCompanyDetails(data);
		logger.info("Company details saved successfully");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	@GetMapping("/getData/{userId}")
	public ResponseEntity<?> getAllCompanyDetailsData(@PathVariable Integer userId) throws JsonProcessingException{
		List<AocCompProfDetailsMainDTO> companyDetailsDTO = aocService.getCompanyProfileDataByUserId(userId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(companyDetailsDTO)).toString());
	}
	
	//for view page with pagination
	@GetMapping("/paginated/{userId}")
	public ResponseEntity<?> getAllCompanyDetailsData(
	    @PathVariable Integer userId,
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {

	    Page<AocCompProfDetailsMainDTO> paginatedCompanyDetails = aocService.getCompanyProfileDataByUserId(userId, page, size);

	    Map<String, Object> response = new HashMap<>();
	    response.put("data", paginatedCompanyDetails.getContent());
	    response.put("currentPage", paginatedCompanyDetails.getNumber());
	    response.put("totalItems", paginatedCompanyDetails.getTotalElements());
	    response.put("totalPages", paginatedCompanyDetails.getTotalPages());

	    return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}


	// Endpoint to get company details by profDetId
	@GetMapping("/{profDetId}")
	public ResponseEntity<AocCompProfDetailsMainDTO> getCompanyDetails(@PathVariable String profDetId) {
		logger.info("Request to get company details for profDetId: {}", profDetId);
		try {
			AocCompProfDetailsMainDTO companyDetailsDTO = aocService.getCompanyDetails(profDetId);
			logger.info("Retrieved company details successfully for profDetId: {}", profDetId);
			return ResponseEntity.ok(companyDetailsDTO);
		} catch (Exception e) {
			logger.error("Error retrieving company details for profDetId {}: {}", profDetId, e.getMessage());
			return ResponseEntity.status(500).build();
		}
	}
	
	@GetMapping("/count/{userId}")
	public ResponseEntity<?> getCountProfileDetails(@PathVariable("userId") Integer userId) throws JsonProcessingException{
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(aocService.getCountProfileDetails(userId)))
				.toString());
	}
	
	//get company data by id for edit purpose
	@PostMapping("/getCompanyData")
	public ResponseEntity<String> getCompanyDataById(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject obj = new JSONObject(decodedData);
		String profDetId = (String) obj.get("profDetId");
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(aocService.getCompanyDetails(profDetId)))
				.toString());
		
	}
	
	@PostMapping("/removeDirector")
	public ResponseEntity<String> removeDirector(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject obj = new JSONObject(decodedData);
		String directorId = (String) obj.get("directorId");
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(aocService.removeDirector(directorId)))
				.toString());
		
	} 
	
	@GetMapping("get-sign-seal-director-details/{profDetId}")
	public ResponseEntity<String> getSignSealDirectorDetails(@PathVariable String profDetId) {
		logger.info("Request to get company details for profDetId: {}", profDetId);
		try {
			Map<String , Object> response = new HashMap<>();
			AocCompProfDetailsMainDTO companyDetailsDTO = aocService.getCompanyDetails(profDetId);
			response.put("signSeal" , companyDetailsDTO.getSignSeal());
			response.put("directors", companyDetailsDTO.getDirectors());
			logger.info("Retrieved getSignSealDirectorDetails successfully for profDetId: {}", profDetId);
			return ResponseEntity.ok(CommonUtil
					.inputStreamEncoder(
							buildJsonResponse(response))
					.toString());
		} catch (Exception e) {
			logger.error("Error retrieving getSignSealDirectorDetails for profDetId {}: {}", profDetId, e.getMessage());
			return ResponseEntity.status(500).build();
		}
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
