package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.Status;
import app.ewarehouse.service.WarehouseParticularsService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/admin/warehouseParticulars")
public class WarehouseParticularsController {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseParticularsController.class);

    @Autowired
    private WarehouseParticularsService warehouseParticularsService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/addEdit")
    public ResponseEntity<String> addEdit(@RequestBody String data) throws JsonProcessingException {
        logger.info("Inside createOrUpdateBuyer method of WarehouseParticularsController");
        String response = warehouseParticularsService.save(data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") String id) throws JsonProcessingException {
        logger.info("Inside getById method of WarehouseParticularsController");
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(warehouseParticularsService.getById(id))).toString());
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAll() throws JsonProcessingException {
        logger.info("Inside getAll method of WarehouseParticularsController");
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(warehouseParticularsService.getAll())).toString());
    }

    @GetMapping("/paginated")
    public ResponseEntity<String> getAllPaginated(Pageable pageable) throws JsonProcessingException {
        logger.info("Inside getAllPaginated method of WarehouseParticularsController");
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(warehouseParticularsService.getAll(pageable))).toString());
    }

    private <T> String buildJsonResponse(T response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ResponseDTO.<T>builder()
                        .status(200)
                        .result(response)
                        .build()
        );
    }
}

