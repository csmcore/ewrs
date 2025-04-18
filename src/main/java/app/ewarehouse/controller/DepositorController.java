/**
 * 
 */
package app.ewarehouse.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.DepositorResponse;
import app.ewarehouse.dto.DepositoryDetailsDto;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.BuyerDepositorAndWareHouseOperator;
import app.ewarehouse.entity.Depositor;
import app.ewarehouse.entity.Status;
import app.ewarehouse.service.DepositorService;
import app.ewarehouse.service.OperatorLicenceService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.FolderAndDirectoryConstant;

/**
 * Priyanka Singh
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/admin/depositor")
public class DepositorController {
	
	private static final Logger logger = LoggerFactory.getLogger(DepositorController.class);
    String path = "src/resources/"+ FolderAndDirectoryConstant.DEPOSITOR_REG_FOLDER +"/";

    @Autowired
    private DepositorService depositorService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private  OperatorLicenceService operatorLicenceService;

    @PostMapping("/addEdit")
    public ResponseEntity<String> addEdit(@RequestBody String data) throws JsonProcessingException {
        logger.info("Inside createOrUpdatedepositor method of DepositorController");
        BuyerDepositorAndWareHouseOperator savedDepositor = depositorService.save(data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(savedDepositor)).toString());
    }
    

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") String id) throws JsonProcessingException {
    	System.out.println(id);
    	logger.info("Inside getById method of DepositorController");
        DepositorResponse depositor = depositorService.getById(id);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(depositor)).toString());
    }

    @GetMapping("/getAllDepositor")
    public ResponseEntity<List<Depositor>> getAll() {
        List<Depositor> depositorList = depositorService.getAll();
        return new ResponseEntity<>(depositorList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id) throws JsonProcessingException {
        logger.info("Inside delete method of DepoController");
        String response = depositorService.deleteById(id);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }

    @GetMapping("/paginated")
    public ResponseEntity<String> getAllPaginated(
            Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) Status status) throws JsonProcessingException {

        Page<DepositorResponse> depositorPage = depositorService.getFilteredDepositors(fromDate, toDate, status, pageable);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(depositorPage)).toString());
    }
    private <T> String buildJsonResponse(T response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ResponseDTO.<T>builder()
                        .status(200)
                        .result(response)
                        .build()
        );
    }
    
    @GetMapping("/download/{folderName}/{name}")
    public ResponseEntity<Resource> download(@PathVariable String folderName,
                                             @PathVariable String name) throws IOException {
        logger.info("Inside download method of DepositorController");

        Path filePath = Paths.get("src/resources", folderName, name).normalize();

        // Secure base path
        Path basePath = Paths.get("src/resources").toAbsolutePath().normalize();
        if (!filePath.toAbsolutePath().startsWith(basePath)) {
            throw new SecurityException("Unauthorized file access.");
        }

        File file = filePath.toFile();
        if (!file.exists() || file.isDirectory()) {
            throw new IOException("File not found: " + file.getAbsolutePath());
        }

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

    
    private HttpHeaders headers(String name) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + URLEncoder.encode(name, StandardCharsets.UTF_8) + "\"");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }

    
    @GetMapping("/getTempUserData/{email}")
	public ResponseEntity<?> getUserDataByDepositor(@PathVariable String email) throws JsonProcessingException {
    	DepositoryDetailsDto details = depositorService.getUserDataByEmailIdByDepositor(email);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(details)).toString());
	}
    
   @GetMapping("/view/{id}")
   public ResponseEntity<?> getDepositorByUserId(
           @PathVariable Integer id,
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
       
       Pageable pageable = PageRequest.of(page, size);
       Page<BuyerDepositorAndWareHouseOperator> depositors = depositorService.findByIntCreatedBy(id, pageable);
       
       
       return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(depositors)).toString());
   }
    
   @GetMapping("/getById/{id}")
   public ResponseEntity<?> getDepositorById(@PathVariable("id") String id) throws JsonProcessingException {
   	BuyerDepositorAndWareHouseOperator details =depositorService.getDepositorById(id);
		if (details != null) {
			return ResponseEntity.ok(CommonUtil.encodedJsonResponse(details,objectMapper));
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
   	
   }
   
   @GetMapping("/getWareHouseOperatorData/{vchWareHouseId}")
   public ResponseEntity<?> getWareHouseOperatorDataByUserId(@PathVariable String vchWareHouseId) throws JsonProcessingException {
	   DepositoryDetailsDto warehouseDeetails = depositorService.getWareHouseOperatorDataByUserId(vchWareHouseId);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(warehouseDeetails)).toString());
   }
   
   @GetMapping("/countDepositor/{createdBy}")
   public ResponseEntity<?> getDepositorCount(@PathVariable Integer createdBy) throws JsonProcessingException {
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(depositorService.getDepositorCount(createdBy))).toString());
   }
    
   @GetMapping("/getAllWareHouseList")
   public ResponseEntity<?> getAllWareHouseList( @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "5") int size) throws JsonProcessingException {
	   Pageable pageable = PageRequest.of(page, size);
	   Page<BuyerDepositorAndWareHouseOperator> depositorList = depositorService.getAllWareHouseList(pageable);
       return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(depositorList)).toString());
   }
   
   
   @GetMapping("/getAllWareHouseListById/{vchWareHouseId}")
   public ResponseEntity<?> getAllWareHouseList(@PathVariable String vchWareHouseId, @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
	   Pageable pageable = PageRequest.of(page, size);
	   Page<BuyerDepositorAndWareHouseOperator> depositorList = depositorService.getAllWareHouseListById(vchWareHouseId,pageable);
       return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(depositorList)).toString());
   }
   
//   @GetMapping("/getWareHouseName/{compId}")
//	public ResponseEntity<String> getOperatorLicence(@PathVariable String compId) throws JsonProcessingException {
//		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(operatorLicenceService.getOperatorWarehouseNameByWareHouse(compId)).toString());
//
//	}

   


}
