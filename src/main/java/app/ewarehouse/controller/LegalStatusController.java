package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.LegalStatusService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/legalStatus")
public class LegalStatusController {

    private static final Logger logger = LoggerFactory.getLogger(LegalStatusController.class);

    @Autowired
    private LegalStatusService legalStatusService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/addEdit")
    public ResponseEntity<String> addEdit(@RequestBody String data) throws JsonProcessingException {
        logger.info("Inside createOrUpdateBuyer method of LegalStatusController");
        Integer response = legalStatusService.save(data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id) throws JsonProcessingException {
        logger.info("Inside getById method of LegalStatusController");
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(legalStatusService.getById(id))).toString());
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAll() throws JsonProcessingException {
        logger.info("Inside getAll method of LegalStatusController");
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(legalStatusService.getAll())).toString());
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
