package app.ewarehouse.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.PledgingDischargeBankDetails;
import app.ewarehouse.entity.PledgingDischargeDepositorLoanApp;
import app.ewarehouse.entity.PledgingDischargeDepositorWarehouse;
import app.ewarehouse.entity.PledgingDischargeResidential;
import app.ewarehouse.entity.PledgingDischargeUploadDocs;
import app.ewarehouse.service.PledgingDischargeWarehouseReceiptService;
import app.ewarehouse.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/pledging-and-discharge")
@RequiredArgsConstructor
@Slf4j
public class PledgingDischargeWarehouseController {
    private final PledgingDischargeWarehouseReceiptService service;
    private final ObjectMapper objectMapper;

    @PostMapping("draft")
    public ResponseEntity<?> saveAsDraft(@RequestBody String data) throws JsonProcessingException {
        log.info("Inside saveAsDraft method of PledgingDischargeWarehouseController");
        ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                .status(HttpStatus.CREATED.value())
                .message(service.saveAsDraft(data))
                .build();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }


    @PostMapping("publish")
    public ResponseEntity<?> publishDraft(@RequestBody String data) throws JsonProcessingException {
        log.info("Inside publishDraft method of PledgingDischargeWarehouseController");
        ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                .status(HttpStatus.CREATED.value())
                .message(service.publishDraft(data))
                .build();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value = "page", required = false) Integer pageNumber, @RequestParam(value = "size", required = false) Integer pageSize,  @RequestParam(value = "sortDirection", required = false) String sortDir,  @RequestParam(value = "sortColumn", required = false) String sortCol, @RequestParam(required = false) String search) throws JsonProcessingException {
        log.info("Inside getAll method of PledgingDischargeWarehouseController");
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(service.getAll(pageNumber, pageSize, sortCol, sortDir, search));
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }


    @GetMapping("get-draft")
    public ResponseEntity<?> getDraft() throws JsonProcessingException {
        log.info("Inside getDraft method of PledgingDischargeWarehouseController");
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(service.getDraft());
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }
    
    
    //Methods created by chinmaya jena
    
    @PostMapping("stepone")
	public ResponseEntity<String> saveStepOne(@RequestBody String data) throws JsonProcessingException {
    	JSONObject response = new JSONObject();
		log.info("Request to save saveStepOne details: {}", data);
		response = service.saveStepOne(data);
		log.info("depositor warehouse receipt data saved successfully");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
    
    @GetMapping("stepone/count/{intId}")
	public ResponseEntity<String> getDraftStatusOfStepOne(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		String count = service.getCountStepOneByCreatedByAndDraftStatus(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(count)).toString());
	}
    
    @GetMapping("stepone/{id}")
	public ResponseEntity<String> getStepOneDataById(@PathVariable("id") String id)
			throws JsonProcessingException {
		PledgingDischargeDepositorWarehouse data = service.getStepOneDataById(id);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(data)).toString());
	}
    
    @PostMapping("steptwo")
	public ResponseEntity<String> saveStepTwo(@RequestBody String data) throws JsonProcessingException {
    	JSONObject response = new JSONObject();
		log.info("Request to save saveStepTwo details: {}", data);
		response = service.saveStepTwo(data);
		log.info("depositor warehouse loan app saved successfully");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
    
    @GetMapping("steptwo/count/{intId}")
	public ResponseEntity<String> getDraftStatusOfStepTwo(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		String count = service.getDraftStatusOfStepTwo(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(count)).toString());
	}
    
    @GetMapping("steptwo/{id}")
	public ResponseEntity<String> getStepTwoDataById(@PathVariable("id") String id)
			throws JsonProcessingException {
		PledgingDischargeDepositorLoanApp data = service.getStepTwoDataById(id);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(data)).toString());
	}
    
    @PostMapping("stepthree")
	public ResponseEntity<String> saveStepThree(@RequestBody String data) throws JsonProcessingException {
    	JSONObject response = new JSONObject();
		log.info("Request to save saveStepThree details: {}", data);
		response = service.saveStepThree(data);
		log.info("depositor warehouse residential saved successfully");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
    
    @GetMapping("stepthree/count/{intId}")
	public ResponseEntity<String> getDraftStatusOfStepThree(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		String count = service.getDraftStatusOfStepThree(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(count)).toString());
	}
    
    @GetMapping("stepthree/{id}")
	public ResponseEntity<String> getStepThreeDataById(@PathVariable("id") String id)
			throws JsonProcessingException {
		PledgingDischargeResidential data = service.getStepThreeDataById(id);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(data)).toString());
	}
    
    @PostMapping("stepfour")
	public ResponseEntity<String> saveStepFour(@RequestBody String data) throws JsonProcessingException {
    	JSONObject response = new JSONObject();
		log.info("Request to save saveStepThree details: {}", data);
		response = service.saveStepFour(data);
		log.info("depositor warehouse residential saved successfully");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
    
    @GetMapping("stepfour/count/{intId}")
	public ResponseEntity<String> getDraftStatusOfStepFour(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		String count = service.getDraftStatusOfStepFour(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(count)).toString());
	}
    
    @GetMapping("stepfour/{id}")
	public ResponseEntity<String> getStepFourDataById(@PathVariable("id") String id)
			throws JsonProcessingException {
		PledgingDischargeBankDetails data = service.getStepFourDataById(id);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(data)).toString());
	}
    
    @PostMapping("stepfive")
	public ResponseEntity<String> saveStepFive(@RequestBody String data) throws JsonProcessingException {
    	JSONObject response = new JSONObject();
		log.info("Request to save saveStepThree details: {}", data);
		response = service.saveStepFive(data);
		log.info("depositor warehouse residential saved successfully");
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
    
    @GetMapping("stepfive/count/{intId}")
	public ResponseEntity<String> getDraftStatusOfStepFive(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		String count = service.getDraftStatusOfStepFive(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(count)).toString());
	}
    
    @GetMapping("stepfive/{id}")
	public ResponseEntity<String> getStepFiveDataById(@PathVariable("id") String id)
			throws JsonProcessingException {
    	PledgingDischargeUploadDocs data = service.getStepFiveDataById(id);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(data)).toString());
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
    
    @GetMapping("loanApplicants")
    public ResponseEntity<?> getLoanApplicants(@RequestParam(value = "page", required = false) Integer pageNumber, @RequestParam(value = "size", required = false) Integer pageSize,  @RequestParam(value = "sortDirection", required = false) String sortDir,  @RequestParam(value = "sortColumn", required = false) String sortCol, @RequestParam(required = false) String search) throws JsonProcessingException {
        log.info("Inside getAll method of PledgingDischargeWarehouseController");
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(service.getLoanApplicants(pageNumber, pageSize, sortCol, sortDir, search));
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }
    
    @GetMapping("details")
    public ResponseEntity<?> getDischargeDetailsByDepositor(
    		@RequestParam String depositorId,
    		@RequestParam(value = "page", required = false) Integer pageNumber,
    		@RequestParam(value = "size", required = false) Integer pageSize,
    		@RequestParam(value = "sortDirection", required = false) String sortDir,
    		@RequestParam(value = "sortColumn", required = false) String sortCol,
    		@RequestParam(required = false) String search) throws JsonProcessingException {
        log.info("Inside getDischargeDetailsByDepositor method of PledgingDischargeWarehouseController");
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(service.getByDepositorId(pageNumber, pageSize, sortCol, sortDir, search,depositorId));
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }

}
