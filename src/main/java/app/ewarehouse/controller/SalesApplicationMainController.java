package app.ewarehouse.controller;


import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


import app.ewarehouse.dto.ReceiptDealFinalDto;
import app.ewarehouse.dto.SalesApplicationListResponse;
import app.ewarehouse.dto.SalesApplicationMainResponse;
import app.ewarehouse.entity.SalesApplicationMain;
import app.ewarehouse.entity.SalesApplicationTabTwo;
import app.ewarehouse.entity.Status;
import app.ewarehouse.service.SalesApplicationMainService;
import app.ewarehouse.util.CommonUtil;


@RestController
@RequestMapping("/api/sales-applications")
public class SalesApplicationMainController {

    @Autowired
    private SalesApplicationMainService salesApplicationService;
    @Autowired
    ObjectMapper objectMapper;
    
    Logger logger=LoggerFactory.getLogger(SalesApplicationMainController.class);

    @PostMapping("tab-one")
    public ResponseEntity<?> saveTabOne(@RequestBody String salesApplication) throws JsonProcessingException {
        String salesApplication2 = salesApplicationService.saveOne(salesApplication);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(salesApplication2,objectMapper));

    }
    @PostMapping("tab-two")
    public ResponseEntity<?> saveTabTwo(@RequestBody String salesApplication) throws JsonProcessingException {
        SalesApplicationTabTwo salesApplication1 = salesApplicationService.saveTwo(salesApplication);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(salesApplication1,objectMapper));

    }

    @PostMapping("tab-three")
    public ResponseEntity<?> saveTabThree(@RequestBody String salesApplication) throws JsonProcessingException {
        
    	
    	String decodedData = CommonUtil.inputStreamDecoder(salesApplication);
    	
    	JSONObject dataObject=new JSONObject(decodedData);
    	JSONObject tabOneData=(JSONObject) dataObject.get("tabonedata");
    	String salesApplication2 = salesApplicationService.saveOne(tabOneData.toString());
    	JSONObject tabTwoData=(JSONObject)dataObject.get("tabtwodata");
    
    	tabTwoData.put("applicationId", salesApplication2);
    	String modifiedTabTwoData=tabTwoData.toString();
    	salesApplicationService.saveTwo(modifiedTabTwoData);
    	
    	
    	JSONObject tabThreeData=(JSONObject)dataObject.get("tabthreedata");
    	tabThreeData.put("applicationId", salesApplication2);
    	String modifiedTabThreeData=tabThreeData.toString();
    	
    	
    	String salesApplicationTabThree = salesApplicationService.saveThree(modifiedTabThreeData);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(salesApplicationTabThree,objectMapper));

    }
    @GetMapping("inProgress")
    public ResponseEntity<String> getApplicationInProgress() throws JsonProcessingException {
        SalesApplicationMain applicationInProgress = salesApplicationService.getApplicationInProgress();
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(applicationInProgress,objectMapper));
    }
    @GetMapping("receipt/{applicationId}")
    public ResponseEntity<String> getSalesReceiptDetails(@PathVariable String applicationId) throws JsonProcessingException {
        SalesApplicationMainResponse receiptDetails = salesApplicationService.getSalesReceiptDetails(applicationId);
        
        System.out.println(receiptDetails);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(receiptDetails,objectMapper));
    }

    @GetMapping("/deal-final-or-sold")
    public ResponseEntity<String> getSalesApplicationsByStatus() throws JsonProcessingException {
        SalesApplicationMain applicationByStatus = salesApplicationService.getSalesApplicationsByStatus();
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(applicationByStatus, objectMapper));
    }

    @PostMapping("checkSalesFinalDeal")
    public ResponseEntity<String> getDealDetails(@RequestBody String salesApplication) throws JsonProcessingException {
    	
    	String decodedData = CommonUtil.inputStreamDecoder(salesApplication);
    	
    	JSONObject jsonObject=new JSONObject(decodedData);
    	
    	String receiptNo=jsonObject.getString("receiptNo");
    	
    	ReceiptDealFinalDto receiptDetails = salesApplicationService.getDealDetails(receiptNo);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(receiptDetails,objectMapper));
    }
    
    @PostMapping("checkSoldReceiptPaymentStatus")
    public ResponseEntity<String> checkReceiptPayment(@RequestBody String salesApplication) throws JsonProcessingException {
    	
    	String decodedData = CommonUtil.inputStreamDecoder(salesApplication);
    	
    	JSONObject jsonObject=new JSONObject(decodedData);
    	
    	String receiptNo=jsonObject.getString("receiptNo");
    	
    	ReceiptDealFinalDto receiptDetails = salesApplicationService.checkReceiptPayment(receiptNo);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(receiptDetails,objectMapper));
    }
    
    
    @GetMapping("/getAllSalesApplicationList")
    public ResponseEntity<String> getAllPaginated(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(required = false) String sortDirection) throws JsonProcessingException {

        logger.info("Inside getAllPaginated method of BuyerController");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
                sortColumn != null ? sortColumn : "dtmCreatedOn");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<SalesApplicationListResponse> applicationResponse= salesApplicationService.getAllSalesApplicationList(sortedPageable,search);
       
        logger.info("Content: " + applicationResponse);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(applicationResponse, objectMapper));
    }
    
}
