package app.ewarehouse.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ApplicationOfConformityDTO;
import app.ewarehouse.dto.MpesaCallbackResponse;
import app.ewarehouse.dto.PendingConformityDTO;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.ApplicationOfConformity;
import app.ewarehouse.entity.ApplicationOfConformityCommodityStorage;
import app.ewarehouse.entity.ApplicationOfConformityParticularOfApplicants;
import app.ewarehouse.entity.ApplicationOfConformitySupportingDocuments;
import app.ewarehouse.entity.ApplicationOfConformityWarehouseOperatorViability;
import app.ewarehouse.entity.ConformityMain;
import app.ewarehouse.entity.Status;
import app.ewarehouse.service.ConformityParticularService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/conformity")
@CrossOrigin("*")
public class ApplicationForConformityController {

	@Value("${finalUpload.path}")
	private String finalUploadPath;
	String path = "src/resources/" + FolderAndDirectoryConstant.AOC_PARTICULAR_FOLDER + "/";

	@Autowired
	private ConformityParticularService conformityParticularService;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("/count/applicantdetails/{intId}")
	public ResponseEntity<String> getDraftStatusOfApplicantDetails(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		Long count = conformityParticularService.getCountByCreatedByAndDraftStatus(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(count)).toString());
	}

	@GetMapping("/applicantdetails/{intId}")
	public ResponseEntity<String> getAocParticularDataById(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		ApplicationOfConformityParticularOfApplicants aocParticular = conformityParticularService
				.getAocParticularDataById(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(aocParticular)).toString());
	}

	@DeleteMapping("/deletedirector/{intId}")
	public ResponseEntity<String> deleteDirectorById(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		conformityParticularService.deleteDirectorById(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse("Data Deleted")).toString());
	}

	@PostMapping("/applicantdetails")
	public ResponseEntity<?> saveApplicantDetails(@RequestBody String data)
			throws JsonMappingException, JsonProcessingException {

		JSONObject response = new JSONObject();

		response = conformityParticularService.saveApplicantData(data);

		return ResponseEntity.ok(response.toString());
	}

	@GetMapping("/count/supportingdocs/{intId}")
	public ResponseEntity<String> getDraftStatusOfSupportingDocs(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		Long count = conformityParticularService.getDraftStatusOfSupportingDocs(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(count)).toString());
	}

	@GetMapping("/supportingdocs/{intId}")
	public ResponseEntity<String> getAocSupportindDocDataById(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		ApplicationOfConformitySupportingDocuments aocSupportingDocs = conformityParticularService
				.getAocSupportindDocDataById(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(aocSupportingDocs)).toString());
	}

	@PostMapping("/supportingdocs")
	public ResponseEntity<?> saveSupportingDocs(@RequestBody String data)
			throws JsonMappingException, JsonProcessingException {

		JSONObject response = new JSONObject();

		response = conformityParticularService.saveSupportingDocsData(data);
		return ResponseEntity.ok(response.toString());
	}

	@GetMapping("/count/operatorviability/{intId}")
	public ResponseEntity<String> getDraftStatusOfViability(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		Long count = conformityParticularService.getDraftStatusOfViability(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(count)).toString());
	}

	@GetMapping("/operatorviability/{intId}")
	public ResponseEntity<String> getViabilityDataById(@PathVariable("intId") Integer intId)
			throws JsonProcessingException {
		ApplicationOfConformityWarehouseOperatorViability aocViabilityDocs = conformityParticularService
				.getViabilityDataById(intId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(aocViabilityDocs)).toString());
	}

	@PostMapping("/operatorviability")
	public ResponseEntity<?> saveOperatorViability(@RequestBody String data) throws JsonProcessingException {

		JSONObject response = new JSONObject();

		response = conformityParticularService.saveOperatorViabilityData(data);
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping("/payment")
	public ResponseEntity<?> savePayment(@RequestBody String data)
			throws JsonMappingException, JsonProcessingException {

		JSONObject response = new JSONObject();

		response = conformityParticularService.savePaymentData(data);
		return ResponseEntity.ok(response.toString());
	}
	
	
	@GetMapping("/paymentResponse")
	public ResponseEntity<?> getPaymentResponse(@RequestBody String data)throws JsonMappingException, JsonProcessingException {

		System.out.println(data);
		
		return ResponseEntity.ok(data.toString());
	}
	
	//Callback URL Defind 
	@PostMapping("/paymentCallback")
	    public ResponseEntity<String> handleMpesaCallback(@RequestBody MpesaCallbackResponse callbackResponse) {
	        // Extract relevant details from the callback
	        MpesaCallbackResponse.StkCallback stkCallback = callbackResponse.getBody().getStkCallback();
	        String merchantRequestID = stkCallback.getMerchantRequestID();
	        String checkoutRequestID = stkCallback.getCheckoutRequestID();
	        int resultCode = stkCallback.getResultCode();
	        String resultDesc = stkCallback.getResultDesc();
	        // Optionally, print or log the callback response for debugging
	        System.out.println("MerchantRequestID: " + merchantRequestID);
	        System.out.println("CheckoutRequestID: " + checkoutRequestID);
	        System.out.println("ResultCode: " + resultCode);
	        System.out.println("ResultDesc: " + resultDesc);
	        // Handle the result based on the resultCode (success or failure)
	        if (resultCode == 0) {
	            // Payment was successful
	            System.out.println("Payment Success. Transaction complete!");
	            // Process further, update order status, etc.
	        } else {
	            // Payment failed
	            System.out.println("Payment Failed. Transaction not complete!");
	            // Handle failure (log error, update failure status, etc.)
	        }
	        // Return a success response to M-Pesa (important to respond with HTTP 200)
	        return ResponseEntity.ok("Callback received successfully");
	    }
	
	@GetMapping("/return")
    public ResponseEntity<String> handleReturnFromECitizen(
            @RequestParam Map<String, String> allParams) {
 
        // Log all the received parameters
        System.out.println("eCitizen Return Response Params: " + allParams);
 
        // Example: get specific params (adjust as per actual response fields)
        String transactionId = allParams.get("transactionId");
        String status = allParams.get("status");
        String amount = allParams.get("amount");
 
        // You can now process/save these details in your system
        if ("SUCCESS".equalsIgnoreCase(status)) {
            // process success
        } else {
            // process failure
        }
 
        return ResponseEntity.ok("Payment response received successfully.");
    }
	
	
	@RequestMapping(value = "/paymentReturn", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> handleReturnFromECitizen(HttpServletRequest request) throws IOException {
        // Log Query Params
        System.out.println("===== Query Parameters =====");
        request.getParameterMap().forEach((key, values) -> {
            System.out.println(key + " = " + Arrays.toString(values));
        });
 
        // Log Headers
        System.out.println("===== Headers =====");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            System.out.println(header + ": " + request.getHeader(header));
        }
 
        // Log Raw Body (for POST requests)
        System.out.println("===== Body =====");
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(body);
 
        return ResponseEntity.ok("Received");
    }
	
	
	
	// Role based view and take action APIs
	@GetMapping("/id/{intApplicantId}")
	public ResponseEntity<String> getDataById(@PathVariable("intApplicantId") String applicationId)
			throws JsonProcessingException {
		ApplicationOfConformity conformityData = conformityParticularService.findById(applicationId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(conformityData)).toString());
	}

	@PostMapping("/remarks")
	public ResponseEntity<?> giveRemarks(@RequestBody String data)
			throws JsonMappingException, JsonProcessingException {
		conformityParticularService.giveRemarks(data);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(data)).toString());
	}
	// End of Role based view and take action APIs

	@PostMapping("/updateStatus")
	public ResponseEntity<?> updateStatus(@RequestBody String data)
			throws JsonMappingException, JsonProcessingException {
		conformityParticularService.updateApplicationStatus(data);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(data)).toString());
	}

	@PostMapping("/getAll")
	public ResponseEntity<String> allcertificate() throws JsonProcessingException {
		List<ApplicationOfConformityDTO> conformityList = conformityParticularService.findAll();
		// System.out.println("in get all controller"+conformityList);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(conformityList)).toString());
	}

	@GetMapping("/getById/{intApplicantId}")
	public ResponseEntity<String> getById(@PathVariable("intApplicantId") String applicationId)
			throws JsonProcessingException {
		ApplicationOfConformity conformity = conformityParticularService
				.findByApplicationIdWithDirectors(applicationId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(conformity)).toString());
	}

	@GetMapping("/getByIdAndStatus")
	public ResponseEntity<String> getByIdAndStatus(@RequestParam("userId") Integer userId)
			throws JsonProcessingException {
		List<ApplicationOfConformityDTO> conformity = conformityParticularService.findByUserIdAndStatus(userId);
		return ResponseEntity.ok(CommonUtil.encodedJsonResponse(conformity, objectMapper));
	}
	
	
	@GetMapping("/getApplicationDetailsById")
	public ResponseEntity<String> getApplicationDetailsById(@RequestParam("userId") Integer userId,
			@RequestParam("applicationId") String applicationId)
			throws JsonProcessingException {
			ApplicationOfConformityDTO conformity = conformityParticularService.findByUserIdAndApplicationId(userId,
					applicationId);
			return ResponseEntity.ok(CommonUtil.encodedJsonResponse(conformity, objectMapper));
	
	}

	// user based view of all aoc
	@GetMapping("/paginated/{userId}")
	public ResponseEntity<String> getAllPaginatedByUserId(Pageable pageable, @PathVariable("userId") Integer userId,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate)
			throws JsonProcessingException {
		Page<ApplicationOfConformity> applicationsPage = conformityParticularService.getApplicationByUserId(fromDate,
				toDate, userId, pageable);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(applicationsPage)).toString());
	}

	@GetMapping("/paginated")
	public ResponseEntity<String> getAllPaginated(Pageable pageable,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@RequestParam(required = false) Status status,
			@RequestParam(required = false) Integer pendingAt
			) throws JsonProcessingException {
		Page<ApplicationOfConformity> applicationsPage = conformityParticularService
				.getApplicationByStatusAndRole(fromDate, toDate, status , pendingAt , pageable);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(applicationsPage)).toString());
	}

	@GetMapping("/commodityTypes/{id}")
	public ResponseEntity<?> getAllCommodityTypes(@PathVariable("id") String Id) throws JsonProcessingException {
		String CommodityList = conformityParticularService.getCommodityTypes(Id);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(CommodityList)).toString());
	}

	@GetMapping("/approved/users/{countyId}/{subCountyId}/{roleId}")
	public ResponseEntity<?> getApprovedApplicationIdAndShop(@PathVariable("countyId") Integer countyId,@PathVariable("subCountyId") Integer subCountyId,@PathVariable("roleId") Integer roleId )
			throws JsonProcessingException {
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(conformityParticularService.getApprovedApplicationIdAndShop(countyId , subCountyId,roleId)))
				.toString());
	}

	@GetMapping("/approved/name/{applicantId}")
	public ResponseEntity<?> getOperatorFullName(@PathVariable("applicantId") String applicantId)
			throws JsonProcessingException {
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(buildJsonResponse(conformityParticularService.getOperatorFullName(applicantId)))
				.toString());
	}
	
	@GetMapping("/certificate/{applicantId}")
	public ResponseEntity<?> getCertificate(@PathVariable("applicantId") String applicantId)
			throws JsonProcessingException {
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(buildJsonResponse(conformityParticularService.getCertificate(applicantId)))
				.toString());
	}
	
	@GetMapping("/remarks/{applicantId}")
	public ResponseEntity<?> getRemarks(@PathVariable("applicantId") String applicantId)
			throws JsonProcessingException {
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(buildJsonResponse(conformityParticularService.getRemarks(applicantId)))
				.toString());
	}
	
	//CEO Generate Certificate API
	@PostMapping("/generate-certificate")
	public ResponseEntity<?> generateCertificate(@RequestBody String data)
			throws JsonProcessingException {
		JSONObject response =  conformityParticularService.generateCertificate(data);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	
	@GetMapping("/all-approved/users/{warehouseId}")
	public ResponseEntity<?> getAllApprovedApplicationIdAndShop(@PathVariable("warehouseId") String warehouseId)
			throws JsonProcessingException {
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(conformityParticularService.getAllApprovedApplicationIdAndShop(warehouseId)))
				.toString());
	}
	
	
	@GetMapping("/verify-certificate/{code}")
	public ResponseEntity<?> verifyCertificate(@PathVariable String code) throws JsonProcessingException{
		JSONObject response =  conformityParticularService.verifyCertificate(code);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	

	/* ------------------------------------------ New Process Flow API Start---------------------------------------------- */
	
	@PostMapping("/location-details")
	public ResponseEntity<String> saveLocationDetails(@RequestBody String code) throws JsonProcessingException{
		JSONObject response =  conformityParticularService.saveLocationDetails(code);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	@GetMapping("/location-details/{companyId}")
	public ResponseEntity<String> getDraftedWarehouseLocationList(@PathVariable String companyId) throws JsonProcessingException{
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(conformityParticularService.getDraftedWarehouseLocationList(companyId))).toString());
	}
	
	@GetMapping("/location-details/delete/{locationId}")
	public ResponseEntity<String> deleteLocationDetailsByLocationId(@PathVariable Long locationId) throws JsonProcessingException {
	    try {
	        JSONObject jsonResponse = conformityParticularService.deleteLocationDetailsByLocationId(locationId);
	        String status = jsonResponse.getString("status");

	        if ("Not Found".equals(status)) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(CommonUtil.inputStreamEncoder(buildJsonResponse(jsonResponse)).toString());
	        } else if ("Error".equals(status)) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST) // Handle referenced locations
	                .body(CommonUtil.inputStreamEncoder(buildJsonResponse(jsonResponse)).toString());
	        }

	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(jsonResponse)).toString());

	    } catch (Exception e) {
	        JSONObject errorResponse = new JSONObject();
	        errorResponse.put("status", "Error");
	        errorResponse.put("message", "Internal Server Error: " + e.getMessage());

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(CommonUtil.inputStreamEncoder(buildJsonResponse(errorResponse)).toString());
	    }
	}


	
	
	
	@GetMapping("/get-drafted-location-details/{companyId}")
	public ResponseEntity<?> getDraftedLocationDetails(@PathVariable String companyId){
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(conformityParticularService.getDraftedLocationDetails(companyId).toString()).toString());
	}
	
	@PostMapping("/commodity-details")
	public ResponseEntity<String> saveCommodityDetails(@RequestBody String code) throws JsonProcessingException{
		JSONObject response =  conformityParticularService.saveCommodityDetails(code);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	@GetMapping("/commodity-details/{companyId}")
	public ResponseEntity<?> getDraftedCommodityList(@PathVariable String companyId) throws JsonProcessingException{
		List<ApplicationOfConformityCommodityStorage> response = conformityParticularService.getDraftedCommodityList(companyId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
		
	@PostMapping("/form-one-a")
	public ResponseEntity<String> saveFormOneADetails(@RequestBody String code) throws JsonProcessingException{
		JSONObject response =  conformityParticularService.saveFormOneADetails(code);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	@GetMapping("/form-one-a/{companyId}")
	public ResponseEntity<?> getDraftedFormOneADetails(@PathVariable String companyId) throws JsonProcessingException{
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(conformityParticularService.getDraftedFormOneADetails(companyId))).toString());
	}
	
	
	
	@PostMapping("/form-one-b")
	public ResponseEntity<String> saveFormOneBDetails(@RequestBody String code) throws JsonProcessingException{
		JSONObject response =  conformityParticularService.saveFormOneBDetails(code);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	@GetMapping("/form-one-b/{companyId}")
	public ResponseEntity<?> getDraftedFormOneBDetails(@PathVariable String companyId) throws JsonProcessingException{
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(conformityParticularService.getDraftedFormOneBDetails(companyId))).toString());
	}
	
	@GetMapping("/drafted-warehouses-for-payment/{companyId}")
	public ResponseEntity<?> getDraftedWarehousesForPayment(@PathVariable String companyId) throws JsonProcessingException{
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(conformityParticularService.getDraftedWarehousesForPayment(companyId))).toString());
	}
	
	
	@PostMapping("/save-main")
	public ResponseEntity<String> saveInMainTable(@RequestBody String code) throws JsonProcessingException{
		JSONObject response =  conformityParticularService.saveInMainTable(code);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	}
	
	
	
	
	@GetMapping("/download/{folderName}/{name}")
	public ResponseEntity<Resource> download(@PathVariable("folderName") String folderName,@PathVariable("name") String name) throws IOException {
		File file = new File(finalUploadPath + folderName +"/" + name);
		Path filePath = Paths.get(file.getAbsolutePath());
		ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(filePath));
		String mimeType = Files.probeContentType(filePath);
	    if (mimeType == null) {
	        mimeType = "application/octet-stream";
	    }
	    return ResponseEntity.ok()
	            .headers(headers(name))
	            .contentLength(file.length())
	            .contentType(MediaType.parseMediaType(mimeType))
	            .body(byteArrayResource);
	}

	private HttpHeaders headers(String name) {
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + name);
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
		return header;
	}
	
	

	
	 @PostMapping("/getStatus")
	    public ResponseEntity<?> getApplicationStatus(@RequestBody String data) throws JsonProcessingException {
		 String decodedData = CommonUtil.inputStreamDecoder(data);
		  List<ConformityMain> applications = conformityParticularService.getApplicationsByStatus(decodedData);
		  Map<String, Object> response = new HashMap<>();
		  response.put("applications", applications);
		  return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());

	    }
	 
	 /* ========================================== APPROVAL PROCESS NEW API Start ============================== */
	 
	 @PostMapping("/get-pending-applications")
	 public ResponseEntity<String> getPendingApplications(@RequestBody String data) throws JsonProcessingException{
		 Page<PendingConformityDTO> dto = conformityParticularService.getPendingApplications(data);
		 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(dto)).toString());
	 }
	 
	 	@GetMapping("/get-application-by-id/{id}")
		public ResponseEntity<?> getConfirmityDataById(@PathVariable Integer id) throws JsonProcessingException{
			return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(conformityParticularService.getConfirmityDataById(id))).toString());
		}
	 	
	 	@PostMapping("/forward-to-cc-member")
		 public ResponseEntity<String> forwardToCC(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.forwardToCC(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	@PostMapping("/submit-cc-data")
	 	public ResponseEntity<String> submitCCData(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.submitCCData(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	@PostMapping("/forward-to-inspector")
		 public ResponseEntity<String> forwardToInsp(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.forwardToInsp(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	
	 	@PostMapping("/check-inspection-status")
	 	public ResponseEntity<String> checkInspection(@RequestBody String data) throws JsonProcessingException{
	 		JSONObject response = conformityParticularService.checkInspection(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	 	}
	 	
	 	@PostMapping("/submit-inspection-schedule")
	 	public ResponseEntity<String> submitInspectionSchedule(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.submitInspectionSchedule(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	@GetMapping("/get-inspection-schedule/{applicantId}")
		public ResponseEntity<?> getInspectionSchedule(@PathVariable("applicantId") Integer applicantId)
				throws JsonProcessingException {
			return ResponseEntity.ok(CommonUtil
					.inputStreamEncoder(buildJsonResponse(conformityParticularService.getInspectionSchedule(applicantId)))
					.toString());
		}
	 	
	 	@GetMapping("/get-allocated-inspector-data/{applicantId}")
		public ResponseEntity<?> getAllocatedInspectorData(@PathVariable("applicantId") Integer applicantId)
				throws JsonProcessingException {
			return ResponseEntity.ok(CommonUtil
					.inputStreamEncoder(buildJsonResponse(conformityParticularService.getAllocatedInspectorData(applicantId)))
					.toString());
		}
	 	
	 	
	 	
	 	
	 	
	 	
	 	@PostMapping("/submit-complete-inspection")
	 	public ResponseEntity<String> submitCompleteInspection(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.submitCompleteInspection(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	@PostMapping("/submit-inspector-data")
	 	public ResponseEntity<String> submitInspectorData(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.submitInspectorData(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	@PostMapping("/get-inspector-cc-data")
	 	public ResponseEntity<String> getInspectorCCData(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.getInspectorCCData(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	@PostMapping("/submit-oic-data")
	 	public ResponseEntity<String> submitOicData(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.submitOicData(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	@PostMapping("/get-inspector-cc-oic-data")
	 	public ResponseEntity<String> getInspectorCCOicData(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.getInspectorCCOicData(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	@PostMapping("/generate-conformity-certificate")
	 	public ResponseEntity<String> generateConformityCertificate(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.generateConformityCertificate(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	
	 	@PostMapping("/reject-application")
	 	public ResponseEntity<String> rejectApplication(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.rejectApplication(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	@PostMapping("/defer-ceo-application")
	 	public ResponseEntity<String> deferCeoApplication(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.deferCeoApplication(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	@PostMapping("/defer-oic-application")
	 	public ResponseEntity<String> deferOicApplication(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.deferOicApplication(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	@PostMapping("/get-insp-requestedby")
	 	public ResponseEntity<String> getInspRequestedBy(@RequestBody String data) throws JsonProcessingException{
			 JSONObject response = conformityParticularService.getInspRequestedBy(data);
			 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		 }
	 	
	 	
	 	
	 	
	 	
	 	@GetMapping("/get-certificate/{applicantId}")
		public ResponseEntity<?> getConformityCertificate(@PathVariable("applicantId") Integer applicantId)
				throws JsonProcessingException {
			return ResponseEntity.ok(CommonUtil
					.inputStreamEncoder(buildJsonResponse(conformityParticularService.getConformityCertificate(applicantId)))
					.toString());
		}
	 	
	 	@PostMapping("/get-inspector-pdf")
	     public ResponseEntity<?> getInspectorPdf(@RequestBody String requestData) throws JsonProcessingException {
	     	 String decodedData = CommonUtil.inputStreamDecoder(requestData);
	     	 JSONObject jsonObject = new JSONObject(decodedData);
	     	Integer confId = jsonObject.getInt("confId");
	         Map<String, Object> response = conformityParticularService.getInspectorPdf(confId);
	         return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
	     }
	 	
	 	
	 	@GetMapping("/all-approved-data")
	 	public ResponseEntity<?> getAllApprovedData()
				throws JsonProcessingException {
			return ResponseEntity.ok(CommonUtil
					.inputStreamEncoder(buildJsonResponse(conformityParticularService.getAllApprovedData()))
					.toString());
		}
	 	
//	 	@GetMapping("/all-approved-data-for-take-action")
//	 	public ResponseEntity<?> getAllApprovedDataForTakeAction()
//				throws JsonProcessingException {
//			return ResponseEntity.ok(CommonUtil
//					.inputStreamEncoder(buildJsonResponse(conformityParticularService.getAllApprovedDataForTakeAction()))
//					.toString());
//		}
	 	
	 	@GetMapping("/all-approved-data-for-take-action")
	 	public ResponseEntity<?> getAllApprovedDataForTakeAction(
	 			@RequestParam(defaultValue = "0") int page,
	 	        @RequestParam(defaultValue = "10") int size
	 			)
				throws JsonProcessingException {
			return ResponseEntity.ok(CommonUtil
					.inputStreamEncoder(buildJsonResponse(conformityParticularService.getAllApprovedDataForTakeAction(page , size)))
					.toString());
		}
	 	
	 	
	 	
	 	@GetMapping("/all-rejected-data")
	 	public ResponseEntity<?> getAllRejectedData()
				throws JsonProcessingException {
			return ResponseEntity.ok(CommonUtil
					.inputStreamEncoder(buildJsonResponse(conformityParticularService.getAllRejectedData()))
					.toString());
		}
	 	
	 	@GetMapping("/all-rejected-data-for-take-action")
	 	public ResponseEntity<?> getAllRejectedDataForTakeAction(@RequestParam(defaultValue = "0") int page,
	 	        @RequestParam(defaultValue = "10") int size)
				throws JsonProcessingException {
			return ResponseEntity.ok(CommonUtil
					.inputStreamEncoder(buildJsonResponse(conformityParticularService.getAllRejectedDataForTakeAction(page , size)))
					.toString());
		}
	 	
//	 	@GetMapping("/all-deferred-data-for-take-action")
//	 	public ResponseEntity<?> getAllDeferedDataForTakeAction()
//				throws JsonProcessingException {
//			return ResponseEntity.ok(CommonUtil
//					.inputStreamEncoder(buildJsonResponse(conformityParticularService.getAllDeferedDataForTakeAction()))
//					.toString());
//		}
	 	
	 	@GetMapping("/all-deferred-data-for-take-action")
	 	public ResponseEntity<?> getAllDeferedDataForTakeAction(@RequestParam(defaultValue = "0") int page,
	 	        @RequestParam(defaultValue = "10") int size)
				throws JsonProcessingException {
			return ResponseEntity.ok(CommonUtil
					.inputStreamEncoder(buildJsonResponse(conformityParticularService.getAllDeferedDataForTakeAction(page , size)))
					.toString());
		}
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 
	 
	 
	 
	 /* ========================================== APPROVAL PROCESS NEW API End ============================== */
	 	
	 	
//		Edit Functionality
		
		@PostMapping("/updateWarehouseLocationDetails")
		public ResponseEntity<String> updateWarehouseLocationDetails(@RequestBody String updateData) throws JsonProcessingException{
			JSONObject response =  conformityParticularService.updateLocationDetails(updateData);
			return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		}
		
		@PostMapping("/update-form-one-a")
		public ResponseEntity<String> updateFormOneADetails(@RequestBody String code) throws JsonProcessingException{
			JSONObject response =  conformityParticularService.updateFormOneADetails(code);
			return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		}
	 
		@PostMapping("/update-form-one-b")
		public ResponseEntity<String> updateFormOneBDetails(@RequestBody String code) throws JsonProcessingException{
			JSONObject response =  conformityParticularService.updateFormOneBDetails(code);
			return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		}	
		
		@PostMapping("/update-commodity-details")
		public ResponseEntity<String> updateCommodityDetails(@RequestBody String code) throws JsonProcessingException{
			JSONObject response =  conformityParticularService.updateCommodityDetails(code);
			return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
		}
	
	/* ------------------------------------------ New Process Flow API End---------------------------------------------- */
//	private <T> String buildJsonResponse(T response) throws JsonProcessingException {
//		return objectMapper.writeValueAsString(ResponseDTO.<T>builder().status(200).result(response).build());
//	}
	private <T> String buildJsonResponse(T response) throws JsonProcessingException {
		Object result;
		if (response instanceof JSONObject) {
			result = response.toString();
		} else {
			result = response;
		}

		return objectMapper.writeValueAsString(ResponseDTO.builder().status(200).result(result).build());
	}
	
	@GetMapping("wareHouseName/{countyId}/{subCountyId}")
	public ResponseEntity<?> getWareHouseIdAndName(@PathVariable("countyId") Integer countyId,@PathVariable("subCountyId") Integer subCountyId)
			throws JsonProcessingException {
		return ResponseEntity.ok(CommonUtil
				.inputStreamEncoder(
						buildJsonResponse(conformityParticularService.getWareHouseIdAndName(countyId , subCountyId)))
				.toString());
	}
	
	//for action history
    @GetMapping("/conformityActionHistory/{wareHouseId}")
	public ResponseEntity<?> getConformityActionHistoryByWareHouseId(@PathVariable("wareHouseId") String wareHouseId)
	        throws JsonProcessingException {
	    return ResponseEntity.ok(CommonUtil
	            .inputStreamEncoder(
	                    buildJsonResponse(conformityParticularService.getConformityActionHistoryByWareHouseId(wareHouseId)))
	            .toString());
	}
	
	

}
