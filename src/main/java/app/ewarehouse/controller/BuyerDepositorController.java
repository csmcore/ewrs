package app.ewarehouse.controller;

import app.ewarehouse.dto.BuyerDepositorResponse;
import app.ewarehouse.entity.Status;
import app.ewarehouse.service.BuyerDepositorService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.FileDownloadUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/buyer")
public class BuyerDepositorController {

    private static final Logger logger = LoggerFactory.getLogger(BuyerDepositorController.class);

    @Autowired
    private BuyerDepositorService buyerService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/addEdit")
    public ResponseEntity<String> addEdit(@RequestBody String buyer) throws JsonProcessingException {
        logger.info("Inside createOrUpdateBuyer method of BuyerController");
        String response = buyerService.save(buyer);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response, objectMapper));
    }

    @PostMapping("/takeAction")
    public ResponseEntity<String> takeAction(@RequestBody String buyer) throws JsonProcessingException {
        logger.info("Inside takeAction method of BuyerController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(buyerService.takeAction(buyer), objectMapper));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") String id) throws JsonProcessingException {
        logger.info("Inside getById method of BuyerController");
        BuyerDepositorResponse buyer = buyerService.getById(id);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(buyer, objectMapper));
    }

    @GetMapping("/getByIdAndRegType/{id}")
    public ResponseEntity<String> getByIdAndRegType(@PathVariable() String id) throws JsonProcessingException {
        logger.info("Inside getByIdAndRegType method of BuyerController");
            String count= buyerService.checkDepostiroId(id);
            
            System.out.println(count);
            BuyerDepositorResponse buyer=null;
            
           // buyer = buyerService.findByIdAndRegType(id);
            if("1".equals(count)) {
            	  buyer = buyerService.findByIdAndRegType(id);
            }  
        logger.info("Buyer info is:"+buyer);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(buyer, objectMapper));
    }

    @GetMapping("/getAllDepositors")
    public ResponseEntity<String> getAllDepositors() throws JsonProcessingException {
        logger.info("Inside getAllDepositors method of BuyerController");
        List<BuyerDepositorResponse> depositors = buyerService.getAllDepositorsPd();
        logger.info("All depositors with approved receipts"+depositors);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(depositors, objectMapper));
    }

    @GetMapping("/getAllBuyers")
    public ResponseEntity<String> getAllBuyers() throws JsonProcessingException {
        logger.info("Inside getAllBuyers method of BuyerController");
        List<BuyerDepositorResponse> depositors = buyerService.getAllBuyers();
        logger.info("Buyers: {} ", depositors);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(depositors, objectMapper));
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAll() throws JsonProcessingException {
        logger.info("Inside getAll method of BuyerController");
        List<BuyerDepositorResponse> buyersList = buyerService.getAll();
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(buyersList, objectMapper));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id) throws JsonProcessingException {
        logger.info("Inside delete method of BuyerController");
        String response = buyerService.deleteById(id);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response, objectMapper));
    }

    @GetMapping("/paginated")
    public ResponseEntity<String> getAllPaginated(
            Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(required = false) String sortDirection) throws JsonProcessingException {

        logger.info("Inside getAllPaginated method of BuyerController");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
                sortColumn != null ? sortColumn : "dtmCreatedOn");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        logger.info(sortedPageable + " " + status);

        Page<BuyerDepositorResponse> buyerPage = buyerService.getFilteredBuyers(fromDate, toDate, status, search, sortColumn, sortDirection, sortedPageable);
        logger.info("Content: " + buyerPage.getContent());
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(buyerPage, objectMapper));
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(HttpServletResponse response, HttpServletRequest request, @RequestParam("filePath") String filePath) throws IOException {
        String[] parts = filePath.split("/");
        String fileName = parts[parts.length - 1];
        return FileDownloadUtil.fileDownloadUtil(fileName, filePath, response, request);
    }

    @GetMapping("/getDepositorIds")
    public ResponseEntity<String> getDepositorById() throws JsonProcessingException {
       List<String> depositorIdList = buyerService.getDepositorById();
        if (depositorIdList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Depositor not found");
        }
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(depositorIdList, objectMapper));
    }
    @GetMapping("/buyerIds")
    public ResponseEntity<String> getAllBuyersId() throws JsonProcessingException {
        List<String> buyerIdList = buyerService.getAllBuyersId();
        if (buyerIdList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Depositor not found");
        }
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(buyerIdList, objectMapper));
    }
    
    
    @GetMapping("/getValidReceiptNo/{id}")
    ResponseEntity<String> getValidReceiptsNoForDepositor(@PathVariable String id ) throws JsonProcessingException {
        List<String> response = buyerService.getValidReceiptDepositorById(id);
        logger.info("inside getReceiptsNoForDepositor details are: {}", response);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    } 
    
}
