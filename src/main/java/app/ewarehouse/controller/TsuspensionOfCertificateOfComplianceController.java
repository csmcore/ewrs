package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TsuspensionOfCertificateOfCompliance;
import app.ewarehouse.service.TsuspensionOfCertificateOfComplianceService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("suspensionCompliance")
@CrossOrigin("*")
@Slf4j
public class TsuspensionOfCertificateOfComplianceController {
	
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	TsuspensionOfCertificateOfComplianceService suspensionService;

	@GetMapping("/getAll")
	ResponseEntity<?> getAllPaginated(
			@RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) throws JsonProcessingException{
		Page<TsuspensionOfCertificateOfCompliance> suspensionsPage = suspensionService.getAllSuspensions(page,size);

		  Map<String, Object> response = new HashMap<>();
		    response.put("content", suspensionsPage.getContent()); 
		    response.put("totalPages", suspensionsPage.getTotalPages());
		    response.put("totalElements", suspensionsPage.getTotalElements()); 
		    
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}

	
	@PostMapping("/createSuspension")
	ResponseEntity<?> createSuspension(@RequestBody String complaintFormDto ) throws JsonProcessingException{
		TsuspensionOfCertificateOfCompliance suspensionCompliance=suspensionService.createSuspension(complaintFormDto);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(suspensionCompliance)).toString());
	}
	@GetMapping("getSuspensionById/{complaintNumber}")
	ResponseEntity<?> getSuspensionByComplaintNumber(@PathVariable String complaintNumber ) throws JsonProcessingException{
		TsuspensionOfCertificateOfCompliance suspensionCompliance = suspensionService.getSuspensionByComplaintNumber(complaintNumber);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(suspensionCompliance)).toString());
	}

	@GetMapping("/check-unique")
	public ResponseEntity<?> checkUniqueContactNumber(@RequestParam String contactNumber) throws JsonProcessingException {
		boolean isUnique = suspensionService.isContactNumberUnique(contactNumber);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(isUnique)).toString());
	}

	@PostMapping("/takeAction")
	public ResponseEntity<String> takeAction(@RequestBody String data) throws JsonProcessingException {
		log.info("Inside takeAction method of SuspensionCollateralController");
		return ResponseEntity.ok(CommonUtil.encodedJsonResponse(suspensionService.takeAction(data), objectMapper));
	}

	@GetMapping("/roleView")
	public ResponseEntity<String> getByRoleView(Pageable pageable,
												@RequestParam(required = false) Integer roleId,
												@RequestParam(required = false) Status status,
												@RequestParam(required = false) String search,
												@RequestParam(required = false) String sortColumn,
												@RequestParam(required = false) String sortDirection) throws JsonProcessingException {
		log.info("Inside getByRoleView method of SuspensionCollateralController");
		return ResponseEntity.ok(CommonUtil.encodedJsonResponse(suspensionService.findByFilters(roleId, status, search, sortColumn,
				sortDirection, pageable), objectMapper));
	}
	
	private <T> String buildJsonResponse(T response) throws JsonProcessingException {
		return objectMapper.writeValueAsString(ResponseDTO.<T>builder().status(200).result(response).build());
	}
}
