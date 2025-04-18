package app.ewarehouse.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.CountyResponse;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.County;
import app.ewarehouse.service.CountyService;
import app.ewarehouse.util.CommonUtil;
import jakarta.persistence.EntityNotFoundException;
@RestController
@RequestMapping("/counties")
@CrossOrigin("*")
public class CountyController {

	
	 Logger logger=LoggerFactory.getLogger(CountyController.class);
	
    @Autowired
    private CountyService countyService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<?> getAllCounties() throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(countyService.getAllCounties(), objectMapper));
    }
    
    @GetMapping("/paginated")
    public ResponseEntity<?> getAllCounties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) throws JsonProcessingException {

        Page<CountyResponse> pageResult = countyService.getAllCounties(page, size, search);

        Map<String, Object> response = new HashMap<>();
        response.put("data", pageResult.getContent());
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());

        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response, objectMapper));
    }
    
    
    
    
    @GetMapping("/getCountyList")
    public ResponseEntity<String> getAllPaginated(
    		@RequestParam(value = "page", required = false) Integer pageNumber, 
        	@RequestParam(value = "size", required = false) Integer pageSize,  
        	@RequestParam(value = "sortDirection", required = false) String sortDir,  
        	@RequestParam(value = "sortColumn", required = false) String sortCol, 
        	@RequestParam(required = false) String search
           ) throws JsonProcessingException {
    	
        logger.info("Inside getAllPaginated method of BuyerController");

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "DESC"),sortCol != null ? sortCol : "id");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<CountyResponse> pageResult = countyService.getAllCounties(pageable,search);
        
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(pageResult, objectMapper));
    }
    
    
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCountyById(@PathVariable Integer id) {
    	var county = countyService.getCountyById(id);
        if (county != null) {
            return ResponseEntity.ok(county);
        } else {
            throw new EntityNotFoundException("County with id " + id + " not found");
        }
    }
    
    @PostMapping
    public ResponseEntity<?> saveCounty(@RequestBody String county) throws JsonProcessingException {
    	System.out.println(county);
    	County response = countyService.saveCounty(county);
    	return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response, objectMapper));
    } 

    @PostMapping("/{id}")
    public ResponseEntity<?> updateCounty(@PathVariable Integer id, @RequestBody String county) throws JsonProcessingException{ 
    	System.out.println("Inside this metho!!");
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(countyService.updateCounty(id,county))).toString());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCounty(@PathVariable Integer id) throws JsonProcessingException {
    	System.out.println("Inside delete county method!");
        countyService.deleteCounty(id);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(id)).toString());
    }
    
    @GetMapping("/approved")
    public ResponseEntity<?> getApprovedCounties() throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(countyService.getApprovedCounties(), objectMapper));
    }
    
    @GetMapping("/graderCounty")
    public ResponseEntity<?> getGraderCounties() throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(countyService.getGraderCounties(), objectMapper));
    }
    
    
    @GetMapping("/countyByRole/{roleid}")
    public ResponseEntity<?> getInspectorCounties(@PathVariable Integer roleid) throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(countyService.getInspectorCounties(roleid), objectMapper));
    }
    private <T> String buildJsonResponse(T response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ResponseDTO.<T>builder()
                        .status(200)
                        .result(response)
                        .build()
        );
    }
    
    
    @GetMapping("/warehouseCounty")
    public ResponseEntity<?> getWareHouseCounties() throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(countyService.getWareHouseCounties(), objectMapper));
    }
    
}
