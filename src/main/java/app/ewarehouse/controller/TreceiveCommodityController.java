package app.ewarehouse.controller;

import app.ewarehouse.dto.WarehouseReceiptResponse;
import app.ewarehouse.dto.receiveCommodityResponse;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TreceiveCommodity;
import app.ewarehouse.service.TreceiveCommodityService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/receiveCommodity")
@CrossOrigin("*")
public class TreceiveCommodityController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    TreceiveCommodityService service;

    private static final Logger logger = LoggerFactory.getLogger(TreceiveCommodityController.class);

    @GetMapping
    ResponseEntity<?> getAll() throws JsonProcessingException {
       List<TreceiveCommodity> response = service.findAll();
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }

    @PostMapping
    ResponseEntity<?> receiveCommodity(@RequestBody String data) throws JsonProcessingException {
       String response = service.save(data);
        return  ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }

    @GetMapping("/paginated")
    ResponseEntity<?> getAllPaginated(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(required = false) String sortDirection,
            @RequestParam String warehouseId) throws JsonProcessingException {
        logger.info("Inside getAllPaginated method of TreceiveCommodityController");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
                sortColumn != null ? sortColumn : "dtmCreatedAt");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<receiveCommodityResponse> response = service.getFilteredCommodities(search,
        		sortColumn, sortDirection, sortedPageable, warehouseId);
        logger.info("Content: " + response.getContent());

        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }

    @GetMapping("{id}")
    ResponseEntity<?> getCommodityByReceiveId(@PathVariable String id ) throws Exception {
        receiveCommodityResponse commodity = service.getCommodityByReceiveId(id);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(commodity,objectMapper));
    }

}
