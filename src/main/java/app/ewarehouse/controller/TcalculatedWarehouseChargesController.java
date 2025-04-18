package app.ewarehouse.controller;

import app.ewarehouse.entity.TcalculatedWarehouseCharges;
import app.ewarehouse.entity.TwarehouseReceipt;
import app.ewarehouse.service.TcalculatedWarehouseChargesService;
import app.ewarehouse.service.TwarehouseReceiptService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/calculatedWarehouseCharges")
public class TcalculatedWarehouseChargesController {

    @Autowired
    TcalculatedWarehouseChargesService service;
    @Autowired
    ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(TcalculatedWarehouseChargesController.class);

    @PostMapping
    ResponseEntity<String> calculatedCharges(@RequestBody String data) throws JsonProcessingException {
        TcalculatedWarehouseCharges response = service.save(data);
        return  ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }
    @GetMapping("/isReceiptPaid/{id}")
    ResponseEntity<String> getChargesForWarehouseReceipt(@PathVariable String id) throws JsonProcessingException {
       String response = service.getChargesForWarehouseReceipt(id);
        logger.info("inside getChargesForWarehouseReceipt details are: {}", response);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }


}
