package app.ewarehouse.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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

import app.ewarehouse.dto.InspectorDTO;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.Action;
import app.ewarehouse.entity.Inspector;
import app.ewarehouse.entity.Stakeholder;
import app.ewarehouse.entity.Status;
import app.ewarehouse.service.InspectorService;
import app.ewarehouse.util.CommonUtil;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/inspectors")
public class InspectorController {

	@Autowired
	private InspectorService inspectorService;

	@Autowired
	private ObjectMapper objectMapper;
	
	@GetMapping("/getApplicationsByRole")
	public ResponseEntity<String> getApplicationsByRole(
	        @RequestParam(required = false) String roleId,
	        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
	        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
	        @RequestParam(required = false) Status status,
	        @RequestParam(required = false) Action action,
	        @RequestParam(required = false) String search,
	        @RequestParam(required = false) Stakeholder stakeholder,
	        @RequestParam(required = false) Stakeholder forwardedTo,
	        @RequestParam(required = false) String tabAction,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size) throws JsonProcessingException {

	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));

	    Page<InspectorDTO> applications = Page.empty();
	    switch (roleId.toLowerCase()) {
	        case "ceo":
	        	System.out.println("Inside ceo");
	        	switch (tabAction.toLowerCase()) {
                case "pending":
                    applications = inspectorService.getPendingApplicationsForCeo(fromDate, toDate, status, action, stakeholder, search, forwardedTo, pageable);
                    break;
                case "approved":
                    applications = inspectorService.findApplicationsByStatusAndForwardedTo(fromDate, toDate, search, Status.Approved, Stakeholder.APPLICANT, pageable);
                    break;
                case "rejected":
                    applications = inspectorService.findApplicationsByStatusAndForwardedTo(fromDate, toDate, search, Status.Rejected, Stakeholder.APPLICANT, pageable);
                    break;
            }
            break;
	        case "oic legal":
	        	System.out.println("Inside ceo");
	        	 switch (tabAction.toLowerCase()) {
	                case "pending":
	                    applications = inspectorService.getPendingApplicationsForOicLegal(fromDate, toDate, status, action, stakeholder, search, forwardedTo, pageable);
	                    break;
	                case "forwarded":
	                    applications = inspectorService.findApplicationsByActionAndForwardedTo(fromDate, toDate, search, Action.Forwarded, Stakeholder.APPROVER, pageable);
	                    break;
	                default:
	                    return ResponseEntity.badRequest().body("Invalid tab value for OIC Legal");
	            }
	            break;
	        case "approver":
	        	System.out.println("Inside ceo");
	        	switch (tabAction.toLowerCase()) {
                case "pending":
                    applications = inspectorService.getPendingApplicationsForApprover(fromDate, toDate, status, action, stakeholder, search, forwardedTo, pageable);
                    break;
                case "forwarded":
                    applications = inspectorService.findApplicationsByActionAndForwardedTo(fromDate, toDate, search, Action.Forwarded, Stakeholder.CEO_SECOND, pageable);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid tab value for Approver");
            }
            break;
	        default:
	            return ResponseEntity.badRequest().body("Invalid user role");
	    }

	    return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(applications)).toString());
	}	
	
	@GetMapping("/applications")
	public ResponseEntity<String> getApplications(
	        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
	        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
	        @RequestParam(required = false) Status status,
	        @RequestParam(required = false) Action action,
	        @RequestParam(required = false) String search,
	        @RequestParam(required = false) Stakeholder stakeholder,
	        @RequestParam(required = false) Stakeholder forwardedTo,
//	        Add role id
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size) throws JsonProcessingException {

	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("stmUpdatedOn")));
	    Page<InspectorDTO> applications = inspectorService.getFilteredApplications(fromDate, toDate, status, action, stakeholder, search, forwardedTo, pageable);

	    return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(applications)).toString());
	}


	@GetMapping("/{id}")
	public ResponseEntity<?> getInspectorById(@PathVariable("id") String id) throws JsonProcessingException {
		Inspector inspector = inspectorService.getInspectorById(Integer.parseInt(id));
		if (inspector != null) {
			return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(inspector)).toString());
		} else {
			throw new EntityNotFoundException("Inspector with id " + id + " not found");
		}
	}

	@GetMapping
	public ResponseEntity<String> getAllInspectors() throws JsonProcessingException {
		List<InspectorDTO> inspectors = inspectorService.getAllInspectors();
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(inspectors)).toString());
	}

	@GetMapping("/all")
	public ResponseEntity<String> getAllInspectorsPaged(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) throws JsonProcessingException {
		Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Order.desc("id")));
		Page<InspectorDTO> inspectors = inspectorService.getAllInspectors(pageable);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(inspectors)).toString());
	}

	@PostMapping
	public ResponseEntity<String> createInspector(@RequestBody String inspector) throws JsonProcessingException {
		return ResponseEntity.ok(inspectorService.createInspector(inspector));
	}

	@PostMapping("/ceoAction")
	public ResponseEntity<String> handleCeoAction(@RequestBody String ceoActionRequestData)
			throws JsonProcessingException {
		return ResponseEntity.ok(inspectorService.handleCeoAction(ceoActionRequestData));
	}

	@PostMapping("/oicLegalAction")
	public ResponseEntity<String> handleOicLegalAction(@RequestBody String oicLegalActionRequestData)
			throws JsonProcessingException {
		return ResponseEntity.ok(inspectorService.handleOicLegalAction(oicLegalActionRequestData));
	}
	
	@PostMapping("/approverAction")
	public ResponseEntity<String> handleApproverAction(@RequestBody String approverActionRequestData) throws JsonProcessingException {
	    return ResponseEntity.ok(inspectorService.handleApproverAction(approverActionRequestData));
	}
	
	@PostMapping("/ceoSecondAction")
	public ResponseEntity<String> handleCeoSecondAction(@RequestBody String ceoSecondActionRequestData) throws JsonProcessingException {
	    return ResponseEntity.ok(inspectorService.handleCeoSecondAction(ceoSecondActionRequestData));
	}
	
	private <T> String buildJsonResponse(T response) throws JsonProcessingException {
		return objectMapper.writeValueAsString(ResponseDTO.<T>builder().status(200).result(response).build());
	}
}
