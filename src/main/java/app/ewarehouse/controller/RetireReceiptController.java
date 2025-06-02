package app.ewarehouse.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import app.ewarehouse.service.RetireReceiptService;

@RestController
@RequestMapping("/api/warehouse")
public class RetireReceiptController {

    @Autowired
    private RetireReceiptService retireReceiptService;

    @PostMapping("/retireReceipt")
    public ResponseEntity<String> retireWarehouseReceipt(@RequestBody String  retireReceipt) throws JsonProcessingException{
        return ResponseEntity.ok(retireReceiptService.retireWarehouseReceipt(retireReceipt));
    }
    
    @PostMapping("/replaceLostReceipt")
    public ResponseEntity<String> replaceLostReceipt(@RequestBody String lostReceipt) throws JsonProcessingException{
        return ResponseEntity.ok(retireReceiptService.replaceLostReceipt(lostReceipt));
    }
    
    @GetMapping("/validateReceipt")
    public ResponseEntity<String> validateReceipt(@RequestParam String receipt) throws JsonProcessingException {
    	return ResponseEntity.ok(retireReceiptService.validateReceipt(receipt));
    }
}
