package app.ewarehouse.controller;

import app.ewarehouse.dto.MBuyerDepositorDTO;
import app.ewarehouse.service.MBuyerDepositorService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/mbuyerdepositor")
@Slf4j
public class MBuyerDepositorController {

    @Autowired
    private MBuyerDepositorService service;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/addEdit")
    public ResponseEntity<String> addEdit(@RequestBody String buyer) throws JsonProcessingException {
        log.info("Inside createOrUpdateBuyer method of MBuyerDepositorController");
        String response = service.save(buyer);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response, objectMapper));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") String id) throws JsonProcessingException {
        log.info("Inside getById method of MBuyerDepositorController");
        MBuyerDepositorDTO buyer = service.getById(id);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(buyer, objectMapper));
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAll() throws JsonProcessingException {
        log.info("Inside getAll method of MBuyerDepositorController");
        List<MBuyerDepositorDTO> buyersList = service.getAll();
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(buyersList, objectMapper));
    }

    @GetMapping("/paginated")
    public ResponseEntity<String> getAllPaginated(Pageable pageable) throws JsonProcessingException {

        log.info("Inside getAllPaginated method of BuyerController");
        Page<MBuyerDepositorDTO> buyerPage = service.getAll(pageable);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(buyerPage, objectMapper));
    }
}

