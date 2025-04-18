package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.CommitteeDesignationService;
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
@RequestMapping("/committeeDesignation")
public class CommitteeDesignationController {

    private static final Logger logger = LoggerFactory.getLogger(CommitteeDesignationController.class);

    @Autowired
    private CommitteeDesignationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/addEdit")
    public ResponseEntity<String> addEdit(@RequestBody String data) throws JsonProcessingException {
        logger.info("Inside createOrUpdateBuyer method of CommitteeDesignationController");
        Integer response = service.save(data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id) throws JsonProcessingException {
        logger.info("Inside getById method of CommitteeDesignationController");
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(service.getById(id))).toString());
    }

    @GetMapping
    public ResponseEntity<String> getAll() throws JsonProcessingException {
        logger.info("Inside getAll method of CommitteeDesignationController");
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(service.getAll())).toString());
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
