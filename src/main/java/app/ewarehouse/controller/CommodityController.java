package app.ewarehouse.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.Commodity;
import app.ewarehouse.entity.CommodityMarket;
import app.ewarehouse.service.CommodityService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;

@RestController
@RequestMapping("/commodities")
@CrossOrigin("*")
public class CommodityController {
	 private static final Logger log = LoggerFactory.getLogger(CommodityController.class);
	@Autowired
	private CommodityService commodityService;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	ErrorMessages errorMessages;
	
	
	@GetMapping("/market-data")
    public ResponseEntity<?> getMarketData(
            @RequestParam String depositorId, 
            @RequestParam String commodityName, 
            @RequestParam(required = false) String date) throws JsonProcessingException {
		 CommodityMarket isFetched = commodityService.getMarketData(depositorId, commodityName, date);
		return ResponseEntity.ok(CommonUtil.encodedJsonResponse(isFetched,objectMapper));
    }

    @PostMapping("/update-selling-price")
    public ResponseEntity<?> updateSellingPrice(@RequestBody String request) throws JsonProcessingException {
         CommodityMarket isUpdated = commodityService.updateSellingPrice(request);
         return ResponseEntity.ok(CommonUtil.encodedJsonResponse(isUpdated,objectMapper));
    }
	
  
	
	
	
	@PostMapping
	public ResponseEntity<?> createCommodity(@RequestBody String commodityRequest) throws JsonProcessingException {
        try {
			Integer isSaved = commodityService.save(commodityRequest);
			return ResponseEntity.ok(CommonUtil.encodedJsonResponse(isSaved,objectMapper));
        } catch (Exception e){
			return ResponseEntity.ok(1);
        }
    }
	    @GetMapping
	    public ResponseEntity<?> getAllCommodities() throws JsonProcessingException {
			List<Commodity> commodities = commodityService.getAll();
			return ResponseEntity.ok(CommonUtil.encodedJsonResponse(commodities,objectMapper));
	    }
	@GetMapping("/paginated")
	public ResponseEntity<?> getAllCommoditiesPageable(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search) throws JsonProcessingException {
		    Pageable pageable = PageRequest.of(page, size); 
			Page<Commodity> pageResult = commodityService.getAllCommoditiesList(pageable,search);
			return ResponseEntity.ok(CommonUtil.encodedJsonResponse(pageResult,objectMapper));
	}
	
	//Data Table implementation
	 @GetMapping("/list")
	    public ResponseEntity<String> getAll(@RequestParam(value = "page", required = false) Integer pageNumber, @RequestParam(value = "size", required = false) Integer pageSize,  @RequestParam(value = "sortDirection", required = false) String sortDir,  @RequestParam(value = "sortColumn", required = false) String sortCol, @RequestParam(required = false) String search) throws JsonProcessingException {
	        log.info("Inside getAll method of CommodityController");
	        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
	        responseDTO.setStatus(HttpStatus.OK.value());
	        responseDTO.setResult(commodityService.allCommoditiesList(pageNumber, pageSize, sortCol, sortDir, search));
	        return buildResponse(responseDTO);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<?> getCommodityById(@PathVariable Integer id) throws JsonProcessingException {
			Commodity commodity = commodityService.getById(id);
			if (commodity != null) {
				return ResponseEntity.ok(CommonUtil.encodedJsonResponse(commodity,objectMapper));
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
	    }

	    @PostMapping("/{id}")
	    public ResponseEntity<?> updateCommodity(@PathVariable Integer id, @RequestBody String updatedCommodity) throws JsonProcessingException {
			 Integer isUpdated = commodityService.update(id, updatedCommodity);
				return ResponseEntity.ok(CommonUtil.encodedJsonResponse(isUpdated,objectMapper));
	    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCommodity(@RequestBody String data) throws Exception {
		log.info("Inside deleteCommodity method of CommodityController");
		commodityService.delete(data);
        return buildResponse(ResponseDTO.builder().status(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).build());
    }
    
    private ResponseEntity<String> buildResponse(ResponseDTO<?> responseDTO) throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }
}
