package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.BuyerDepositorTypeService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/buyerDepositorType")
@Slf4j
public class BuyerDepositorTypeController {
    @Autowired
    private BuyerDepositorTypeService buyerDepositorTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/addEdit")
    public ResponseEntity<String> addEdit(@RequestBody String data) throws JsonProcessingException {
        log.info("Inside addEdit method of BuyerDepositorTypeController");
        Integer response = buyerDepositorTypeService.save(data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id) throws JsonProcessingException {
        log.info("Inside getById method of BuyerDepositorTypeController");
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(buyerDepositorTypeService.getById(id))).toString());
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAll() throws JsonProcessingException {
        log.info("Inside getAll method of BuyerDepositorTypeController");
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(buyerDepositorTypeService.getAll())).toString());
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


