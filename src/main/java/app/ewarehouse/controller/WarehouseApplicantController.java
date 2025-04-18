package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.dto.WarehouseApplicantResponse;
import app.ewarehouse.entity.Status;
import app.ewarehouse.service.WarehouseApplicantService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/warehouseApplicant")
public class WarehouseApplicantController {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseApplicantController.class);

    @Autowired
    private WarehouseApplicantService warehouseApplicantService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/addEdit")
    public ResponseEntity<String> addEdit(@RequestBody String warehouseApplicant) throws JsonProcessingException {
        logger.info("Inside addEdit method of WarehouseApplicantController");
        String response = warehouseApplicantService.save(warehouseApplicant);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") String id) throws JsonProcessingException {
        logger.info("Inside getById method of WarehouseApplicantController");
        WarehouseApplicantResponse warehouseApplicant = warehouseApplicantService.getById(id);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(warehouseApplicant)).toString());
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAll() throws JsonProcessingException {
        logger.info("Inside getAll method of WarehouseApplicantController");
        List<WarehouseApplicantResponse> warehouseApplicantList = warehouseApplicantService.getAll();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(warehouseApplicantList)).toString());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id) throws JsonProcessingException {
        logger.info("Inside delete method of WarehouseApplicantController");
        String response = warehouseApplicantService.deleteById(id);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }

    @GetMapping("/paginated")
    public ResponseEntity<String> getAllPaginated(
            Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) Status status) throws JsonProcessingException {

        Page<WarehouseApplicantResponse> warehouseApplicantPage = warehouseApplicantService.getFilteredApplicants(fromDate, toDate, status, pageable);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(warehouseApplicantPage)).toString());
    }
    
    //Role based Take Action
    @PostMapping("/warehouseremarks")
	public ResponseEntity<String> giveWSRemarks(@RequestBody String data) throws JsonProcessingException {
    	warehouseApplicantService.giveWareHouseRemarks(data);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(data)).toString());
	}
    
    //End of Role based Take Action

    private <T> String buildJsonResponse(T response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ResponseDTO.<T>builder()
                        .status(200)
                        .result(response)
                        .build()
        );
    }
}
