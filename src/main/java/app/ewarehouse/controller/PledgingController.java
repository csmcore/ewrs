package app.ewarehouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import app.ewarehouse.dto.LoanSanctionDTO;
import app.ewarehouse.entity.IssuanceWareHouseRecipt;
import app.ewarehouse.entity.Pledge;
import app.ewarehouse.service.CommodityService;
import app.ewarehouse.service.PledgeService;
import app.ewarehouse.util.CommonUtil;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin("*")
public class PledgingController {

	@Autowired
	private CommodityService commodityService;
	@Autowired
    private PledgeService service;
    
    @Autowired
	ObjectMapper objectMapper;

    @PostMapping("/savePledging")
    public ResponseEntity<?> saveLoan(@RequestBody String request) throws JsonProcessingException {
    	 Pledge isSaved = service.saveLoan(request);
    	return ResponseEntity.ok(CommonUtil.encodedJsonResponse(isSaved,objectMapper));
    }
    
    
    @GetMapping("/getDepositorDetails/{emailId}")
    public ResponseEntity<?> getDepositorDetails(@PathVariable String emailId
           ) throws JsonProcessingException {
    	List<IssuanceWareHouseRecipt> depositorDetails=service.getDepositorDetails(emailId);
    	return ResponseEntity.ok(CommonUtil.encodedJsonResponse(depositorDetails,objectMapper));
    }
    
    @GetMapping("/getDepositorDetailsById/{id}/{id1}")
    public ResponseEntity<?> getDepositorDetailsById(@PathVariable String id,@PathVariable Integer id1
           ) throws JsonProcessingException {
    	List<IssuanceWareHouseRecipt> depositorDetails=service.getDepositorDetailsById(id,id1);
    	return ResponseEntity.ok(CommonUtil.encodedJsonResponse(depositorDetails,objectMapper));
    }
    
    
    @GetMapping("/sanction-details")
    public ResponseEntity<String> getLoanSanctionDetails(@RequestParam Integer issueId,
            @RequestParam String loanStatus) throws JsonProcessingException {
         LoanSanctionDTO loanSanctionDetails = service.getLoanSanctionDetails(issueId,loanStatus);
         return ResponseEntity.ok(CommonUtil.encodedJsonResponse(loanSanctionDetails,objectMapper));
    }
    
    
}