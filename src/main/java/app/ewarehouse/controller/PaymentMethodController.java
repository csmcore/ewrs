package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.PaymentMethodService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.PaymentMethodValidationCheck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adminconsole")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private ObjectMapper objectMapper;
    String data = "";
    JSONObject resp = new JSONObject();
    private static final Logger logger = LoggerFactory.getLogger(PaymentMethodController.class);
    @PostMapping("/payment_method/addEdit")
    public ResponseEntity<?> create(@RequestBody String paymentMethod) throws JsonProcessingException {
        logger.warn("Inside create method of PaymentMethodController");
        data = CommonUtil.inputStreamDecoder(paymentMethod);

        if (PaymentMethodValidationCheck.BackendValidation(new JSONObject(data)) != null) {
            resp.put("status", 502);
            resp.put("errMsg", PaymentMethodValidationCheck.BackendValidation(new JSONObject(data)));
            logger.warn("Inside create method of PaymentMethodController Validation Error");
        } else {
            resp = paymentMethodService.save(data);
        }
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
    }

    @PostMapping("/payment_method/preview")
    public ResponseEntity<?> getById(@RequestBody String formParams) {
        data = CommonUtil.inputStreamDecoder(formParams);
        logger.info("Inside getById method of PaymentMethodController");
        JSONObject json = new JSONObject(data);
        JSONObject entity = paymentMethodService.getById(json.getInt("intId"));
        resp.put("status", 200);
        resp.put("result", entity);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
    }

    @PostMapping("/payment_method/all")
    public ResponseEntity<?> getAll(@RequestBody String formParams) {
        logger.info("Inside getAll method of PaymentMethodController");
        JSONArray entity = paymentMethodService.getAll(CommonUtil.inputStreamDecoder(formParams));
        resp.put("status", 200);
        resp.put("result", entity);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
    }

    @PostMapping("/payment_method/allActive")
    public ResponseEntity<?> getAllActive(@RequestBody String formParams) {
        logger.info("Inside getAll method of PaymentMethodController");
        JSONArray entity = paymentMethodService.getAllActive(CommonUtil.inputStreamDecoder(formParams));
        resp.put("status", 200);
        resp.put("result", entity);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
    }

    @PostMapping("/payment_method/delete")
    public ResponseEntity<?> delete(@RequestBody String formParams) {
        logger.info("Inside delete method of PaymentMethodController");
        data = CommonUtil.inputStreamDecoder(formParams);
        JSONObject json = new JSONObject(data);
        JSONObject entity = paymentMethodService.deleteById(json.getInt("intId"));
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(entity.toString()).toString());
    }
    
    //Data table pagination API
    @GetMapping("/payment_method/paginated")
    public ResponseEntity<String> getAllPaymentTypes(@RequestParam(value = "page", required = false) Integer pageNumber, @RequestParam(value = "size", required = false) Integer pageSize, @RequestParam(required = false) String search) throws JsonProcessingException {
        logger.info("Inside getAllPaymentTypes method of PaymentMethodController");
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(paymentMethodService.getAllPaymentTypes(pageNumber, pageSize, search));
        return buildResponse(responseDTO);
    }
    
    private ResponseEntity<String> buildResponse(ResponseDTO<?> responseDTO) throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }
}
