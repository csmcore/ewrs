package app.ewarehouse.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.BuyerDepositorAndWareHouseOperator;
import app.ewarehouse.entity.IssuanceWareHouseRecipt;
import app.ewarehouse.entity.IssuanceWarehouseReceiptActionHistory;
import app.ewarehouse.entity.IssuanceWarehouseReceiptRetire;
import app.ewarehouse.repository.IssuanceWarehouseReceiptActionHistoryRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.service.CommodityService;
import app.ewarehouse.service.DepositorService;
import app.ewarehouse.service.IssuanceWareHouseReciptService;
import app.ewarehouse.service.IssuanceWarehouseReceiptRetireService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.FolderAndDirectoryConstant;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/issuance")

public class IssuanceWareHouseReciptController {
	
	@Value("${finalUpload.path}")
	private String finalUploadPath;
	String path = "src/resources/" + FolderAndDirectoryConstant.ISSUANCE_WAREHOUSE_RECIPT + "/";

	
	@Autowired
	private IssuanceWareHouseReciptService issuanceWareHouseReciptService;
	
	@Autowired
	private CommodityService commodityService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
    private DepositorService depositorService;
	
	@Autowired
	TuserRepository tuserRepository ;
	@Autowired
	IssuanceWarehouseReceiptActionHistoryRepository issuanceWarehouseReceiptActionHistoryRepository;
	
	@Autowired
    private IssuanceWarehouseReceiptRetireService retireService;
	
//	 @GetMapping("/getAllDepositorId/{vchWarehouseId}")
//	 public ResponseEntity<?> getAllDepositorId(@PathVariable String vchWarehouseId) throws JsonProcessingException {
//	        List<Map<String,Object>> depositorId = issuanceWareHouseReciptService.getAllDepositorId(vchWarehouseId);
//	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(depositorId)).toString()); // Returns 200 OK with the list
//	    }
	 
	 @GetMapping("/getAllDepositorId")
	 public ResponseEntity<?> getAllDepositorId() throws JsonProcessingException {
	        List<Map<String,Object>> depositorId = issuanceWareHouseReciptService.getAllDepositorId();
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(depositorId)).toString()); // Returns 200 OK with the list
	    }
  
	 
	 private <T> String buildJsonResponse(T response) throws JsonProcessingException {
	        return objectMapper.writeValueAsString(
	                ResponseDTO.<T>builder()
	                        .status(200)
	                        .result(response)
	                        .build()
	        );
	    }
	 
	 @GetMapping("/getDepositorIdByWareHouse/{id}")
	    public ResponseEntity<?> getDepositorIdByWareHouse(@PathVariable String id) throws JsonProcessingException {
		 Map<String, Object> depoDetails = issuanceWareHouseReciptService.getDepositorIdByWareHouse(id);
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(depoDetails)).toString());
	    }
	 
	 @GetMapping("/getAllCommodityName")
	    public ResponseEntity<?> getAllCommodityName() throws JsonProcessingException {
		 List<Map<String, Object>> commodityName = issuanceWareHouseReciptService.getAllCommodityName();
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(commodityName)).toString());
	    }
	 
	 @GetMapping("/getCommodityNameByOriginAndCode/{id}")
	    public ResponseEntity<?> getCommodityNameByOriginAndCode(@PathVariable Integer id) throws JsonProcessingException {
		 List<Map<String, Object>> commodityDetails = issuanceWareHouseReciptService.getCommodityNameByOriginAndCode(id);
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(commodityDetails)).toString());
	    }
	 @GetMapping("/getAllSeason")
	    public ResponseEntity<?> getAllSeason() throws JsonProcessingException {
		 List<Map<String, Object>> seasonName = issuanceWareHouseReciptService.getAllSeason();
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(seasonName)).toString());
	    }
	 
	 @GetMapping("/getAllChargesHeader")
	    public ResponseEntity<?> getAllChargesHeader() throws JsonProcessingException {
		 List<Map<String, Object>> chargesName = issuanceWareHouseReciptService.getAllChargesHeader();
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(chargesName)).toString());
	    }
	 
	 
	@GetMapping("/getChargesHeaderByUnitType/{chargesId}")
	    public ResponseEntity<?> getChargesHeaderByUnitType(@PathVariable Integer chargesId) throws JsonProcessingException {
		List<Map<String, String>> unitTypeName = issuanceWareHouseReciptService.getChargesHeaderByUnitType(chargesId);
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(unitTypeName)).toString());
	    }

    @PostMapping("/save")
    public ResponseEntity<String> saveWarehouseReceipt(@RequestBody String data) throws JsonProcessingException {
        IssuanceWareHouseRecipt savedReceipt = issuanceWareHouseReciptService.saveIssuanceWareHouseRecipt(data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(savedReceipt)).toString());
    }
    
    @GetMapping("/viewIssuance/{vchWarehouseId}")
    public ResponseEntity<?> viewIssuance(
    		 @PathVariable String vchWarehouseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<IssuanceWareHouseRecipt> issuances = issuanceWareHouseReciptService.viewIssuance( vchWarehouseId,pageable);
        
        
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(issuances)).toString());
    }
    
    @GetMapping("/viewIssuance")
    public ResponseEntity<?> viewIssuanceOic(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<IssuanceWareHouseRecipt> issuances = issuanceWareHouseReciptService.viewIssuanceOic(pageable);
        
        
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(issuances)).toString());
    }
    
    @GetMapping("/viewIssuanceCEO")
    public ResponseEntity<?> viewIssuanceCEO(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<IssuanceWareHouseRecipt> issuances = issuanceWareHouseReciptService.viewIssuanceCEO(pageable);
        
        
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(issuances)).toString());
    }
    
    @PostMapping("/confirmationByDepositor/{id}/{id1}")
    public ResponseEntity<?> confirmationByDepositor(@PathVariable String id,@PathVariable Integer id1, @RequestBody String data) throws JsonProcessingException {
    	Map<String, Object> updateResponse = issuanceWareHouseReciptService.updateConfirmationByDepositor(id,id1,data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(updateResponse)).toString());
    }
 
    @PostMapping("/generateWarehouseReceiptNo/{id}/{id1}")
    public ResponseEntity<?> generateWarehouseReceiptNo(@PathVariable String id,@PathVariable Integer id1,@RequestBody String data) throws JsonProcessingException {
    	Map<String, Object> updateResponse = issuanceWareHouseReciptService.generateWarehouseReceiptNo(id,id1,data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(updateResponse)).toString());
    }
    
  
    @PostMapping("/takeActionForCentralRegistration/{id}/{id1}")
    public ResponseEntity<String> takeActionForCentralRegistration(@PathVariable String id,@PathVariable Integer id1,@RequestBody String data) throws JsonProcessingException {
    	Map<String, Object> registration = issuanceWareHouseReciptService.takeActionForCentralRegistration(id,id1,data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(registration)).toString());
    }
    
    @PostMapping("/takeActionForIssueRegistration/{id}/{id1}")
    public ResponseEntity<String> takeActionForIssueRegistration(@PathVariable String id,@PathVariable Integer id1,@RequestBody String data) throws JsonProcessingException {
    	Map<String, Object> issueRegistration = issuanceWareHouseReciptService.takeActionForIssueRegistration(id,id1,data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(issueRegistration)).toString());
    }
    
    @PostMapping("/takeActionForBlockRegistration/{id}/{id1}")
    public ResponseEntity<String> takeActionForBlockRegistration(@PathVariable String id,@PathVariable Integer id1,@RequestBody String data) throws JsonProcessingException {
    	Map<String, Object> blockRegistration = issuanceWareHouseReciptService.takeActionForBlockRegistration(id,id1,data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(blockRegistration)).toString());
    }
    
    
    @GetMapping("/download/{folderName}/{name}")
    public ResponseEntity<Resource> download(@PathVariable("folderName") String folderName,
                                             @PathVariable("name") String name) throws IOException {
        // Resolve and normalize the file path
        Path filePath = Paths.get(finalUploadPath, folderName, name).normalize();

        // Optional: Prevent path traversal attacks
        Path basePath = Paths.get(finalUploadPath).toAbsolutePath().normalize();
        if (!filePath.toAbsolutePath().startsWith(basePath)) {
            throw new SecurityException("Unauthorized file access.");
        }

        File file = filePath.toFile();
        if (!file.exists() || file.isDirectory()) {
            throw new IOException("File not found: " + file.getAbsolutePath());
        }

        // Use InputStreamResource for efficient streaming
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));

        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .headers(headers(name))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(mimeType))
                .body(inputStreamResource);
    }

    @GetMapping("/getLicenceNo/{warehouseId}")
    public ResponseEntity<?> getLicenceNo(
            @PathVariable String warehouseId) throws JsonProcessingException {
//        String licenceNo = issuanceWareHouseReciptService.getOperatorLicenceNo(warehouseId);
//        Map<String, String> response = new HashMap<>();
//        response.put("licenceNo", licenceNo);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(issuanceWareHouseReciptService.getOperatorLicenceNo(warehouseId))).toString());
    }
    
    @PostMapping("/transferReceipt")
    public ResponseEntity<String> addEdit(@RequestBody String data) throws JsonProcessingException {
        BuyerDepositorAndWareHouseOperator savedDepositor = issuanceWareHouseReciptService.save(data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(savedDepositor)).toString());
    }
    
    @GetMapping("/viewTransferIssuance")
    public ResponseEntity<?> viewTransferIssuance(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<IssuanceWareHouseRecipt> issuances = issuanceWareHouseReciptService.viewTransferIssuance(pageable);
        
        
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(issuances)).toString());
    }
    
    @GetMapping("/viewTransferIssuanceOIC")
    public ResponseEntity<?> viewTransferIssuanceOIC(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<IssuanceWareHouseRecipt> issuances = issuanceWareHouseReciptService.viewTransferIssuanceOIC(pageable);
        
        
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(issuances)).toString());
    }
    
    @GetMapping("/viewTransferIssuanceCEO")
    public ResponseEntity<?> viewTransferIssuanceCEO(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<IssuanceWareHouseRecipt> issuances = issuanceWareHouseReciptService.viewTransferIssuanceCEO(pageable);
        
        
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(issuances)).toString());
    }
    
    @GetMapping("/getIssuanceWhReceiptActionHistory/{issuanceWhId}/{depositorId}")
    public  ResponseEntity<?> getIssuanceWhReceiptActionHistory(@PathVariable Integer issuanceWhId, @PathVariable String depositorId) throws JsonProcessingException {
    	List<IssuanceWarehouseReceiptActionHistory> IssuanceWhReceiptActionHistoryDetails = issuanceWareHouseReciptService.getIssuanceWhReceiptActionHistory(issuanceWhId,depositorId);
    	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(IssuanceWhReceiptActionHistoryDetails)).toString());
    	    }
    
    @GetMapping("/getAllRetiredReceipts")
    public ResponseEntity<?> getAllRetiredReceipts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
    	
    	 Pageable pageable = PageRequest.of(page, size);
         Page<IssuanceWarehouseReceiptRetire> retireIssuance = retireService.getAllRetiredReceipts(pageable);
         return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(retireIssuance)).toString());
    	
    }
    
    @PostMapping("/saveRetirementData")
    public ResponseEntity<String> saveRetirementData(@RequestBody String data) throws JsonProcessingException {
        IssuanceWarehouseReceiptRetire savedRetirementData = retireService.saveRetirementData(data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(savedRetirementData)).toString());
    }
    
    @GetMapping("/getAllRetiredReceiptsDepositor")
    public ResponseEntity<?> getAllRetiredReceiptsDepositor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String depositorId) throws JsonProcessingException {
    	
    	 Pageable pageable = PageRequest.of(page, size);
         Page<IssuanceWarehouseReceiptRetire> retireIssuance = retireService.getAllRetiredReceiptsForDepositor(pageable,depositorId);
         return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(retireIssuance)).toString());
    	
    }
    
    private HttpHeaders headers(String name) {
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + name);
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
		return header;
	}
	 
}
