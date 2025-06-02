package app.ewarehouse.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.FormOneCRequestDto;
import app.ewarehouse.dto.OPLApplicationStatusDTO;
import app.ewarehouse.dto.OPLFormDetailsDTO;
import app.ewarehouse.dto.OperatorLicenceApprovalResponseDto;
import app.ewarehouse.dto.OperatorLicenceDTO;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.Action;
import app.ewarehouse.entity.Stakeholder;
import app.ewarehouse.entity.Status;
import app.ewarehouse.repository.ConfirmityFormOneCRepo;
import app.ewarehouse.service.OperatorLicenceService;
import app.ewarehouse.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/operator-licences")
@CrossOrigin("*")
@Slf4j
public class OperatorLicenceController {

    private final OperatorLicenceService operatorLicenceService;
    
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${finalUpload.path}")
	private String finalUploadPath;
    @Autowired
	ConfirmityFormOneCRepo documentRepository;

    public OperatorLicenceController(OperatorLicenceService operatorLicenceService) {
        this.operatorLicenceService = operatorLicenceService;
    }
    
    @GetMapping("/technicalGetPending")
    public ResponseEntity<String> getPendingApplicationsForTechnical(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search)
    				throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OperatorLicenceDTO> pendingApplications = operatorLicenceService.getApplications(pageable, Status.Pending, Stakeholder.TECHNICAL, Action.Pending, search);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(pendingApplications)).toString());
    }
    
    @GetMapping("/revenueGetPending")
    public ResponseEntity<String> getPendingApplicationsForRevenue(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search)
    				throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OperatorLicenceDTO> pendingApplications = operatorLicenceService.getApplications(pageable, Status.InProgress, Stakeholder.REVENUE, Action.Forwarded, search);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(pendingApplications)).toString());
    }

    @GetMapping("/inspectorGetPending")
    public ResponseEntity<String> getPendingApplicationsForInspector(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search)
    				throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OperatorLicenceDTO> pendingApplications = operatorLicenceService.getApplications(pageable, Status.InProgress, Stakeholder.INSPECTOR, Action.Forwarded, search);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(pendingApplications)).toString());
    }

    @GetMapping("/clcGetPending")
    public ResponseEntity<String> getPendingApplicationsForClc(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search)
    				throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OperatorLicenceDTO> pendingApplications = operatorLicenceService.getApplications(pageable, Status.InProgress, Stakeholder.CLC, Action.Forwarded, search);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(pendingApplications)).toString());
    }

    @GetMapping("/cecmGetPending")
    public ResponseEntity<String> getPendingApplicationsForCecm(
            Pageable pageable,
    		@RequestParam(defaultValue = "0") Integer page,
    		@RequestParam(defaultValue = "10") Integer size,
    		@RequestParam(required = false) String search,
    		@RequestParam(required = false) String sortColumn,
    		@RequestParam(required = false) String sortDirection) throws JsonProcessingException {
    	Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"), sortColumn != null ? sortColumn : "stmUpdatedAt");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("stmUpdatedAt")));
        Page<OperatorLicenceDTO> pendingApplications = operatorLicenceService.getApplications(sortedPageable, Status.InProgress, Stakeholder.CECM, Action.Forwarded,search);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(pendingApplications)).toString());
    }
    
    @GetMapping("/getApprovedApplications")
    public ResponseEntity<String> getApprovedApplications(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search)
    				throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OperatorLicenceDTO> approvedApplications = operatorLicenceService.getApplications(pageable, Status.Approved, Stakeholder.APPLICANT, Action.Forwarded, search);
        System.out.println(approvedApplications.getContent());
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(approvedApplications)).toString());
    }

    @GetMapping("/getRejectedApplications")
    public ResponseEntity<String> getRejectedApplications(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search)
    				throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OperatorLicenceDTO> rejectedApplications = operatorLicenceService.getApplications(pageable, Status.Rejected, Stakeholder.APPLICANT, Action.Rejected, search);
        System.out.println(rejectedApplications.getContent());
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(rejectedApplications)).toString());
    }
    
    @GetMapping("/getApprovedAndRejectedApplications")
    public ResponseEntity<String> getApprovedAndRejectedApplications(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search)
    				throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OperatorLicenceDTO> allApplications = operatorLicenceService.getAllApplications(pageable, Stakeholder.APPLICANT, search);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(allApplications)).toString());
    }
    
    @GetMapping("/clcGetForwarded")
    public ResponseEntity<String> getForwardedApplicationsToCLC(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search)
    				throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OperatorLicenceDTO> forwardedApplications = operatorLicenceService.getApplications(pageable, Status.Forwarded, Stakeholder.CLC, Action.Forwarded, search);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(forwardedApplications)).toString());
    }

    @GetMapping("/cecmGetForwarded")
    public ResponseEntity<String> getForwardedApplicationsToCECM(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search)
    				throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OperatorLicenceDTO> forwardedApplications = operatorLicenceService.getApplications(pageable, Status.InProgress, Stakeholder.CECM, Action.Forwarded, search);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(forwardedApplications)).toString());
    }
    
    @GetMapping("/revenueGetForwarded")
    public ResponseEntity<String> getForwardedApplicationsToRevenue(@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search)
    				throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<OperatorLicenceDTO> forwardedApplications = operatorLicenceService.getApplications(pageable, Status.InProgress, Stakeholder.REVENUE, Action.Forwarded, search);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(forwardedApplications)).toString());
    }
    
    @PostMapping
    public ResponseEntity<String> createOperatorLicence(@RequestBody String operatorLicence) throws JsonProcessingException {
    	
    	System.out.println(operatorLicence);
        String savedOperatorLicence = operatorLicenceService.saveOperatorLicence(operatorLicence);
        return ResponseEntity.ok(savedOperatorLicence);    
    }
    @PostMapping("/technicalTakeAction")
    public ResponseEntity<String> handleTechnicalAction(@RequestBody String technicalActionRequestData)
            throws JsonProcessingException {
    	System.out.println("Working!!");
        return ResponseEntity.ok(operatorLicenceService.handleAction(technicalActionRequestData, Stakeholder.REVENUE, Stakeholder.TECHNICAL , Action.Forwarded, Status.InProgress));
    }
    @PostMapping("/revenueTakeAction")
    public ResponseEntity<String> handleRevenueAction(@RequestBody String revenueActionRequestData)
            throws JsonProcessingException {
        return ResponseEntity.ok(operatorLicenceService.handleAction(revenueActionRequestData, Stakeholder.INSPECTOR, Stakeholder.REVENUE, Action.Forwarded, Status.InProgress));
    }

    @PostMapping("/inspectorTakeAction")
    public ResponseEntity<String> handleInspectorAction(@RequestBody String inspectorActionRequestData)
            throws JsonProcessingException {
        return ResponseEntity.ok(operatorLicenceService.handleAction(inspectorActionRequestData, Stakeholder.CLC, Stakeholder.INSPECTOR, Action.Forwarded, Status.InProgress));
    }

    @PostMapping("/clcTakeAction")
    public ResponseEntity<String> handleClcAction(@RequestBody String clcActionRequestData)
            throws JsonProcessingException {
        return ResponseEntity.ok(operatorLicenceService.handleAction(clcActionRequestData, Stakeholder.CECM, Stakeholder.CLC, Action.Forwarded, Status.InProgress));
    }

    @PostMapping("/cecmTakeAction")
    public ResponseEntity<String> handleCecmAction(@RequestBody String cecmActionRequestData)
            throws JsonProcessingException {
        return ResponseEntity.ok(operatorLicenceService.handleAction(cecmActionRequestData, Stakeholder.APPLICANT, Stakeholder.CECM, Action.Forwarded, Status.Approved));
    }

    @GetMapping
    public ResponseEntity<String> getOperatorLicences(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) Integer userId, @RequestParam(required = false) Status status) throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(operatorLicenceService.getOperatorLicences(status, userId, PageRequest.of(page, size)), objectMapper));
    }

    @GetMapping("/list")
    public ResponseEntity<String> getOperatorLicencesList(@RequestParam(required = false) Status status, @RequestParam(required = false) Integer userId) throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(operatorLicenceService.getOperatorLicences(status, userId), objectMapper));
    }
	
	private <T> String buildJsonResponse(T response) throws JsonProcessingException {
		return objectMapper.writeValueAsString(ResponseDTO.<T>builder().status(200).result(response).build());
	}
	
	
	
	@GetMapping("getLicenceDetails")
	public ResponseEntity<String> getOperatorLicence(@RequestParam Integer id) throws JsonProcessingException {
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(operatorLicenceService.getOperatorLicence(id))).toString());
//		return operatorLicenceService.getOperatorLicence(id);
	}
	
	@GetMapping("/getOperatorWhName/{compId}")
	public ResponseEntity<String> getOperatorLicence(@PathVariable String compId) throws JsonProcessingException {
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(operatorLicenceService.getOperatorWarehouseName(compId)).toString());

	}
	
	 @PostMapping("/form-one-c")
	    public ResponseEntity<String> uploadDocuments(@RequestBody String onecData) throws JsonMappingException, JsonProcessingException {
		 ObjectMapper objectMapper = new ObjectMapper();
		 
		 String onecDataValue = CommonUtil.inputStreamDecoder(onecData);
		 List<FormOneCRequestDto> documentRequestDtos = objectMapper.readValue(onecDataValue, new TypeReference<List<FormOneCRequestDto>>() {});
		 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(operatorLicenceService.saveDocuments(documentRequestDtos).toString()).toString());
	    }
	
	 
	 @GetMapping("/getFormOneC/{warehouseId}")
		public ResponseEntity<String> getFiles(@PathVariable String warehouseId) throws JsonProcessingException {
			return ResponseEntity.ok(CommonUtil.encodedJsonResponse(operatorLicenceService.getFilesByWarehouseId(warehouseId),objectMapper).toString());

		}
	 
	 @PostMapping("/addUpdatelicenceApplication")
	    public ResponseEntity<String> savelicenceApplication(@RequestBody String applicationData) throws JsonMappingException, JsonProcessingException { 
		 String applicationDataValue = CommonUtil.inputStreamDecoder(applicationData);
		 JSONObject dataobj=new JSONObject(applicationDataValue);
		 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(operatorLicenceService.saveLicenceApplication(dataobj).toString()).toString());
	    }
	 @GetMapping("/checkPayment/{warehouseId}")
		public ResponseEntity<String> checkPaymentStatus(@PathVariable String warehouseId) throws JsonProcessingException {
			return ResponseEntity.ok(CommonUtil.inputStreamEncoder(operatorLicenceService.checkPaymentStatus(warehouseId).toString()).toString());

		}
	 
	    @GetMapping("/getApprovalData")
	    public ResponseEntity<String> getAllPaginated(
	            Pageable pageable,
	            @RequestParam(required = false) String search,
	            @RequestParam(required = false) Integer roleId,
	            @RequestParam(required = false) Integer userId,
	            @RequestParam(required = false) Integer applicationRoleId,
	            @RequestParam(required = false) String status) throws JsonProcessingException {

	        log.info("Inside getAllPaginated method of BuyerController");

//	        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
//	                sortColumn != null ? sortColumn : "dtmCreatedOn");
	        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

	       log.info(sortedPageable + " " + status);
	       
	        Page<OperatorLicenceApprovalResponseDto> applicationData = operatorLicenceService.getAllApprovalData(status, search, roleId, applicationRoleId,userId,sortedPageable);
	        
	       // log.info("Content: " + buyerPage.getContent());
	        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(applicationData, objectMapper));
	    }
	 
	    @GetMapping("/getpaymentDet/{paymentId}")
		public ResponseEntity<String> getPaymentDetailsById(@PathVariable Long paymentId) throws JsonProcessingException {
			return ResponseEntity.ok(CommonUtil.encodedJsonResponse(operatorLicenceService.getPaymentDetailsById(paymentId),objectMapper).toString());

		}
	    
		@GetMapping("/oneCDoc/download/{name}")
		public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
			log.info("Inside download method of operator_licence Controller");
			File file=null;
			ByteArrayResource byteArrayResource=null;
			
			try {
				
				if(finalUploadPath!=null&&finalUploadPath.startsWith("src/")) {
					
					 file = new File(finalUploadPath + "operator_licence/" + name);
					Path path = Paths.get(file.getAbsolutePath());
					 byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
					
				}
				else {
					
					 file = new File(finalUploadPath + "operator_licence/" + name);
					Path path = Paths.get(file.getAbsolutePath());
					 byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
					return ResponseEntity.ok().headers(headers(name)).contentLength(file.length())
							.contentType(MediaType.parseMediaType("application/octet-stream")).body(byteArrayResource);
					
				}
				
			}
			catch(Exception e) {
			
				e.printStackTrace();
			}
			
			return ResponseEntity.ok().headers(headers(name)).contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(byteArrayResource);
			
		
		}

		private HttpHeaders headers(String name) {
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");
			return header;
		}
		
		// Approval process started
		
		
		 @PostMapping("/technicalTakeaction")
		    public ResponseEntity<String> handleTechnicalTakeAction(@RequestBody String technicalActionRequestData)throws JsonProcessingException {
		    	
			 String applicationDataValue = CommonUtil.inputStreamDecoder(technicalActionRequestData);
		        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(operatorLicenceService.AllTakeAction(applicationDataValue).toString()).toString());
		    }
		
		 @PostMapping("/allActionRemarks")
		    public ResponseEntity<String> allActionRemarks(@RequestBody String RequestData)throws JsonProcessingException {
			 String applicationDataValue = CommonUtil.inputStreamDecoder(RequestData);
		        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(operatorLicenceService.getAllActionRemarks(applicationDataValue).toString()).toString());
		    }
		
			@GetMapping("/inspReport/download/{name}")
			public ResponseEntity<Resource> downloadInpReport(@PathVariable("name") String name) throws IOException {
				log.info("Inside download method of Complaint_managmentController");
				File file = null;
				ByteArrayResource byteArrayResource = null;

				try {

					if (finalUploadPath != null && finalUploadPath.startsWith("src/")) {

						file = new File(finalUploadPath + "OPL_Report_Docs/" + name);
						Path path = Paths.get(file.getAbsolutePath());
						byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));

					} else {

						file = new File(finalUploadPath + "OPL_Report_Docs/" + name);
						Path path = Paths.get(file.getAbsolutePath());
						byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
						return ResponseEntity.ok().headers(headers(name)).contentLength(file.length())
								.contentType(MediaType.parseMediaType("application/octet-stream"))
								.body(byteArrayResource);

					}

				} catch (Exception e) {

					e.printStackTrace();
				}

				return ResponseEntity.ok().headers(headers(name)).contentLength(file.length())
						.contentType(MediaType.parseMediaType("application/octet-stream")).body(byteArrayResource);

			}

			@GetMapping("/getAllApplicationStatusData")
			public ResponseEntity<String> getAllApplicationStatusData(Pageable pageable,
					@RequestParam(required = false) String search, @RequestParam(required = false) Integer roleId,
					@RequestParam(required = false) Integer userId,
					@RequestParam(required = false) Integer applicationRoleId,
					@RequestParam(required = false) String status) throws JsonProcessingException {

				log.info("Inside getAllPaginated method of BuyerController");

				Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

				log.info(sortedPageable + " " + status);

				Page<OPLApplicationStatusDTO> applicationData = operatorLicenceService.getAllApprovalStatusData(applicationRoleId,userId,status,search, sortedPageable);

				// log.info("Content: " + buyerPage.getContent());
				return ResponseEntity.ok(CommonUtil.encodedJsonResponse(applicationData, objectMapper));
			}
			
			 @PostMapping("/generateCertificate")
			    public ResponseEntity<String> generateCertificate(@RequestBody String RequestData)throws JsonProcessingException {
				 String applicationDataValue = CommonUtil.inputStreamDecoder(RequestData);
			        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(operatorLicenceService.generateCertificate(applicationDataValue).toString()).toString());
			    }
			 
			 @GetMapping("/validateCertificate/{cerId}")
			    public ResponseEntity<String> validateCertificate(@PathVariable String cerId)throws JsonProcessingException {
				
			        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(operatorLicenceService.getCertificateDate(cerId).toString()).toString());
			    }			 
			 
			 @PostMapping("/getOPlFormDetailsData")
				public ResponseEntity<String> getAllApplicationStatusData(@RequestBody String RequestData) throws JsonProcessingException {

					log.info("Inside getAllPaginated method of BuyerController");
					String applicationDataValue = CommonUtil.inputStreamDecoder(RequestData);
	
					Page<OPLFormDetailsDTO> applicationData = operatorLicenceService.getOPLFormDetailsData(applicationDataValue);

					// log.info("Content: " + buyerPage.getContent());
					return ResponseEntity.ok(CommonUtil.encodedJsonResponse(applicationData, objectMapper));
				}
			 
			 
			 @PostMapping("/actionTakenOnApplication")
				public ResponseEntity<String> actionTakenOnApplication(@RequestBody String RequestData) throws JsonProcessingException {

					log.info("Inside getAllPaginated method of BuyerController");
					String applicationDataValue = CommonUtil.inputStreamDecoder(RequestData);
					
					Page<OPLApplicationStatusDTO> applicationData = operatorLicenceService.getActionTakenOnApplication(applicationDataValue);

					// log.info("Content: " + buyerPage.getContent());
					return ResponseEntity.ok(CommonUtil.encodedJsonResponse(applicationData, objectMapper));
				}
			
			 @PostMapping("/deleteUploadedFile")
			    public ResponseEntity<String> deleteTempFile(@RequestBody String fileDetails)throws JsonProcessingException {
				 JSONObject object=new JSONObject();
				 
				 String fileDetailsValue = CommonUtil.inputStreamDecoder(fileDetails);
				 
				 JSONObject reqobject=new JSONObject(fileDetailsValue);
				 String fileName=reqobject.optString("uploadedFileName",null);
				 Long docId=	 (long) reqobject.optInt("doCId",0);	 
			
					log.info("Inside download method of operator_licence Controller");
					
					try {
						
						if(finalUploadPath!=null&&finalUploadPath.startsWith("src/")) {
							
							documentRepository.deleteByDocId(docId);
							File filePath = new File(finalUploadPath + "operator_licence/" + fileName);
							Files.delete(filePath.toPath());
							 object.put("status",200);
							 object.put("msg","fileDeleted");
							 							
						}
						else {
							
							File filePath = new File(finalUploadPath + "operator_licence/" + fileName);
							Files.delete(filePath.toPath());
							 object.put("status",200);
							 object.put("msg","fileDeleted");
							 object.put("status",200);
							 object.put("msg","fileDeleted");
							 documentRepository.deleteByDocId(docId);
						}
						
					}
					catch(Exception e) {
						e.printStackTrace();
					
						 object.put("status",500);
						 object.put("msg","file not Deleted");
					}
					
					 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(object.toString()).toString());
			    }
			 

				//for action history
			    @GetMapping("/operatorLicenceActionHistory/{intLicenceSno}")
				public ResponseEntity<?> getOperatorLicenceActionHistory(@PathVariable("intLicenceSno") Integer intLicenceSno)
				        throws JsonProcessingException {
			    	List<Map<String, Object>> historyDetails=operatorLicenceService.getOperatorLicenceActionHistory(intLicenceSno);
				    return ResponseEntity.ok(CommonUtil
				            .inputStreamEncoder(
				                    buildJsonResponse(historyDetails)).toString());
				}
}
