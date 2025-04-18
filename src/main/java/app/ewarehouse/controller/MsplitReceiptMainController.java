package app.ewarehouse.controller;

import app.ewarehouse.dto.MsplitReceiptResponse;
import app.ewarehouse.dto.TsplitReceiptResponse;
import app.ewarehouse.entity.MsplitReceiptMain;
import app.ewarehouse.service.MsplitReceiptMainService;
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

import java.util.List;

@RestController
@RequestMapping("admin/splitReceipt")
public class MsplitReceiptMainController {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    MsplitReceiptMainService service;

    private static final Logger logger = LoggerFactory.getLogger(MsplitReceiptMainController.class);

    @GetMapping
    ResponseEntity<?> getAll() throws JsonProcessingException {
        List<MsplitReceiptResponse> response = service.findAll();
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }

    @PostMapping
    ResponseEntity<?> splitReceipt(@RequestBody String data) throws JsonProcessingException {
        String response = service.save(data);
        logger.info("inside post mapping ,M-split Controller");
        return  ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }

    @GetMapping("/paginated")
    ResponseEntity<?> getAllPaginated(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(required = false) String sortDirection) throws JsonProcessingException {
        logger.info("Inside getAllPaginated method of M Split Controller");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
                sortColumn != null ? sortColumn : "dtmCreatedOn");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<TsplitReceiptResponse> response = service.getFilteredReceipts(search, sortColumn, sortDirection, sortedPageable);
        logger.info("Content: " + response.getContent());

        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }

    @GetMapping("{id}")
    ResponseEntity<?> getCommodityByReceiveId(@PathVariable String id ) throws Exception {
        TsplitReceiptResponse receipt = service.getReceiptBySplitReceiptId(id);
        logger.info("commodity is :"+receipt);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(receipt,objectMapper));
    }

    @GetMapping("/checkSplitReceiptNo/{id}")
    ResponseEntity <?> checkIfSplitReceiptNoExists(@PathVariable("id")String Id) throws JsonProcessingException {
        boolean exists = service.checkIfSplitReceiptNoExists(Id);
        logger.info("checking if split receipt no exists in db:"+exists);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(exists,objectMapper));
    }

}
