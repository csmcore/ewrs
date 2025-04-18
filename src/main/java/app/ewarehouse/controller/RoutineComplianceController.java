package app.ewarehouse.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.dto.RoutineComplianceInspDTO;
import app.ewarehouse.entity.RoutineComplianceCeoApproval;
import app.ewarehouse.entity.RoutineComplianceComplianceTwo;
import app.ewarehouse.service.RoutineComplianceService;
import app.ewarehouse.util.CommonUtil;

@RestController
@RequestMapping("/admin/routine-compliance")
public class RoutineComplianceController {

    private static final Logger logger = LoggerFactory.getLogger(RoutineComplianceController.class);

    @Autowired
    private RoutineComplianceService routineComplianceService;
    @Autowired
    private ObjectMapper objectMapper;

    
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody String routineComplianceInspection) throws JsonProcessingException {
        logger.info("Inside save method of RoutineComplianceController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(routineComplianceService.saveCompliance(routineComplianceInspection), objectMapper));
    }
    
    @PostMapping("/paginateAllData")
	public ResponseEntity<?> getAllCompanyDetailsData(
			@RequestBody String data) throws JsonProcessingException {
    	String decodedData = CommonUtil.inputStreamDecoder(data);
		JsonNode jsonNode = objectMapper.readTree(decodedData);
		int page = jsonNode.get("page").asInt();
        int size = jsonNode.get("size").asInt();
	    Page<RoutineComplianceInspDTO> paginatedDetails = routineComplianceService.getCompanyRoutineCompData(page, size);

	    Map<String, Object> response = new HashMap<>();
	    response.put("data", paginatedDetails.getContent());
	    response.put("currentPage", paginatedDetails.getNumber());
	    response.put("totalItems", paginatedDetails.getTotalElements());
	    response.put("totalPages", paginatedDetails.getTotalPages());

	    return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
    
    @GetMapping("/roleView")
    public ResponseEntity<String> getByRoleView(Pageable pageable,
                                                @RequestParam(required = false) Integer roleId,
                                                @RequestParam(required = false) Integer userId,
                                                @RequestParam(required = false) Integer action,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
                                                @RequestParam(required = false) String search,
                                                @RequestParam(required = false) String sortColumn,
                                                @RequestParam(required = false, defaultValue = "DESC") String sortDirection) throws JsonProcessingException {
        logger.info("Inside getByRoleView method of RoutineComplianceController");
        logger.info("roleId: " + roleId);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(routineComplianceService.findByFiltersData(roleId, search, sortColumn, sortDirection, userId, pageable), objectMapper));
        //return ResponseEntity.ok(CommonUtil.encodedJsonResponse(routineComplianceService.findByFilters(roleId, fromDate, toDate, search, sortColumn, sortDirection, userId, action, pageable), objectMapper));
    }
    
    @PostMapping("/getWareHouseData")
    public ResponseEntity<?> getCompanyData(@RequestBody String requestData) throws JsonProcessingException {
    	 String decodedData = CommonUtil.inputStreamDecoder(requestData);
    	 JSONObject jsonObject = new JSONObject(decodedData);
    	String warehouseId = jsonObject.getString("warehouseId");
    	String status = "gg";
        Map<String, Object> response = routineComplianceService.getWareHosedata(warehouseId,status);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
    
    @PostMapping("/getEditRoutineStageWiseData")
    public ResponseEntity<?> getEditRoutineStageWiseData(@RequestBody String requestData) throws JsonProcessingException {
    	 String decodedData = CommonUtil.inputStreamDecoder(requestData);
    	 JSONObject jsonObject = new JSONObject(decodedData);
    	Long routinComplianceId = jsonObject.getLong("routinComplianceId");
        Map<String, Object> response = routineComplianceService.getEditRoutineStageWiseData(routinComplianceId);
        return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(response))
				.toString());
    }

    @PostMapping("/takeAction")
    public ResponseEntity<String> takeAction(@RequestBody String data) throws JsonProcessingException {
        logger.info("Inside takeAction method of RoutineComplianceController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(routineComplianceService.takeAction(data), objectMapper));
    }
    
    @PostMapping("/getWareHouseIdandRcId")
    public ResponseEntity<?> getWareHouseIdandRcId(@RequestBody String requestData) throws JsonProcessingException  {
    	 String decodedData = CommonUtil.inputStreamDecoder(requestData);
    	 Map<String, Object> response = routineComplianceService.getWareHouseIdandRcId(Long.parseLong(decodedData));
        return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(response))
				.toString());
    }

    
    @PostMapping("/getActionEditData")
    public ResponseEntity<?> getActionEditData(@RequestBody String requestData) throws JsonProcessingException {
    	 String decodedData = CommonUtil.inputStreamDecoder(requestData);
    	 JsonNode rootNode = objectMapper.readTree(decodedData);
         Long editId = rootNode.path("editId").asLong(); // Get editId
         String stage = rootNode.path("stage").asText(); // Get stage
         Object response = null;
         if(stage.equals("isCeoOne")) {
        	  response = routineComplianceService.getCeoOneData(editId);
         }else if(stage.equals("isComplianceTwo")) {
        	  response=routineComplianceService.getComplianceTwoData(editId);
         }else if(stage.equals("isCeoSecond")) {
        	 response=routineComplianceService.getCeoSecondData(editId);
         }else if(stage.equals("isInspector")) {
        	 response=routineComplianceService.getInspectorData(editId);
         }
       
        return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(response))
				.toString());
    }

    
	@PostMapping("/checkDuplicateWarehouse")
	public ResponseEntity<?> checkDuplicateWarehouse(@RequestBody String requestData) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(requestData);
		JSONObject jsonObject = new JSONObject(decodedData);
		String warehouseId = jsonObject.getString("warehouseId");
		boolean exists = routineComplianceService.checkDuplicateWarehouse(warehouseId);
		Map<String, Object> response = new HashMap<>();
		response.put("isDuplicate", exists);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	@PostMapping("/deleteAddMore")
	public ResponseEntity<String> removeExperience(@RequestBody String data) throws JsonProcessingException {
		String decodedData = CommonUtil.inputStreamDecoder(data);
		JSONObject obj = new JSONObject(decodedData);
		Long id = ((Number) obj.get("id")).longValue();
		String type = (String) obj.get("type");
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(routineComplianceService.removeAddmore(id,type)))
				.toString());
		
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

