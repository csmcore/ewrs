package app.ewarehouse.master.controller;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import app.ewarehouse.entity.Ward;
import app.ewarehouse.master.dto.WardMasterDto;
import app.ewarehouse.master.entity.WardMaster;
import app.ewarehouse.master.service.WardMasterService;
import app.ewarehouse.util.CommonUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/master/ward-master")
public class WardMasterController {
	
	private final WardMasterService wardMasterservice;
	private final ObjectMapper objectMapper;

    public WardMasterController(WardMasterService wardMasterservice , ObjectMapper objectMapper) {
        this.wardMasterservice = wardMasterservice;
        this.objectMapper = objectMapper;
    }
	
    private static final Logger logger = LoggerFactory.getLogger(WardMasterController.class);
    
    //Save Api
    @PostMapping
    public ResponseEntity<?> createWardMaster(@RequestBody String wardData) throws JsonProcessingException {
    	logger.info("Inside createWardMaster method of ComplaintStatusMasterController");
    	WardMaster createWardMaster = wardMasterservice.createWardMaster(wardData);
        //return ResponseEntity.ok(createWardMaster);
    	 return ResponseEntity.ok(CommonUtil.encodedJsonResponse(createWardMaster, objectMapper));
    }
    
    //Update Api
    @PostMapping("/{id}")
    public ResponseEntity<?> updateWardMaster(@PathVariable Integer id, @RequestBody String wardMasterData) throws JsonProcessingException {
    	logger.info("Inside updateWardMaster method of WardMasterController");
        WardMaster updateWardMaster = wardMasterservice.updateWardMaster(id, wardMasterData);
        if (updateWardMaster == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(updateWardMaster, objectMapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WardMaster> getWardMasterById(@PathVariable Integer id) {
    	logger.info("Inside getComplaintStatusById method of WardMasterController");
        WardMaster wardMasterById = wardMasterservice.getWardById(id);
        if (wardMasterById == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(wardMasterById);
    }
    
    //Normal List 
    @GetMapping("/")
    public ResponseEntity<List<WardMaster>> getAllWardList() {
    	logger.info("Inside getAllWardList method of WardMasterController");
        List<WardMaster> wardList = wardMasterservice.getAllWardLists();
        return ResponseEntity.ok(wardList);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> changeWardMasterStatus(@RequestBody String data) throws JsonProcessingException {
    	logger.info("Inside changeWardMasterStatus method of WardMasterController");
    	JSONObject response = wardMasterservice.changeWardMasterStatus(data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
    
    @GetMapping("/getWardList")
    public ResponseEntity<String> getAllPaginated(
    		@RequestParam(value = "page", required = false) Integer pageNumber, 
        	@RequestParam(value = "size", required = false) Integer pageSize,  
        	@RequestParam(required = false) String search
           ) throws JsonProcessingException {
    	
        logger.info("Inside getAllPaginated method of WardMasterController");

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<WardMasterDto> pageResult = wardMasterservice.getAllWard(pageable,search);
        
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(pageResult, objectMapper));
    }
    
    @GetMapping("/getWard/{countyId}/{subcountyId}")
    public ResponseEntity<String> getWards(@PathVariable Integer countyId,@PathVariable Integer subcountyId) throws JsonProcessingException {
    	JSONArray wards = wardMasterservice.getWardsByCountyAndSubCounty(countyId,subcountyId);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(wards.toString()).toString());
    }
    
    @GetMapping("/list")
    public ResponseEntity<String> getAllWards() throws JsonProcessingException {
    	logger.info("Inside getAllWards method of WardMasterController");
        List<WardMaster> wardList = wardMasterservice.getAllWardLists();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(wardList)).toString());
    }

    
    
    private <T> String buildJsonResponse(T response) throws JsonProcessingException {
		Object result;
		if (response instanceof JSONObject) {
			result = response.toString();
		} else {
			result = response;
		}

		return objectMapper.writeValueAsString(ResponseDTO.builder().status(200).result(result).build());
	}
  

}
