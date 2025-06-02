package app.ewarehouse.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.PaymentService;
import app.ewarehouse.util.CommonUtil;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin("*")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    JSONObject resp = new JSONObject();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    PaymentService   paymentservice;
    @PostMapping("/paymentInit")
    public ResponseEntity<String> paymentAdd(@RequestBody String data) throws JsonProcessingException {
        logger.info("Inside addEdit method of PaymentController");
           String requestData = CommonUtil.inputStreamDecoder(data);
           JSONObject jsonData = new JSONObject(requestData);
          String resultResponse= paymentservice.secureHashData(jsonData);
          resp.put("status", 200);
  		  resp.put("result", resultResponse);
        return ResponseEntity.ok(resp.toString());
        //return ResponseEntity.ok(resultResponse.toString());
    }
    
    @PostMapping("/paymentForDepositor")
    public ResponseEntity<String> paymentForDepositor(@RequestBody String data) throws JsonProcessingException{
    	return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(paymentservice.paymentForDepositor(data))).toString());
    }
    
    @PostMapping("/updatePaymentStatus")
    public ResponseEntity<String> updatePaymentStatus(@RequestBody String data) throws JsonProcessingException{
    	return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(paymentservice.updatePaymentStatus(data))).toString());
    }
    
    @PostMapping("/updateDoPaymentLater")
    public ResponseEntity<String> updateDoPaymentLater(@RequestBody String data) throws JsonProcessingException{
    	return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(paymentservice.updateDoPaymentLater(data))).toString());
    }

//    @GetMapping("/get/{id}")
//    public ResponseEntity<String> getPaymentById(@PathVariable("id") Integer id) throws JsonProcessingException {
//        logger.info("Inside getById method of PaymentController");
//        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(bankService.getById(id))).toString());
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<String> getAllPayment() throws JsonProcessingException {
//        logger.info("Inside getAll method of PaymentController");
//        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(bankService.getAll())).toString());
//    }

    private <T> String buildJsonResponse(T response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ResponseDTO.<T>builder()
                        .status(200)
                        .result(response)
                        .build()
        );
    }
}

