package app.ewarehouse.controller;

import app.ewarehouse.entity.CommodityType;
import app.ewarehouse.service.CommodityTypeService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commoditytype")
@CrossOrigin("*")
public class CommodityTypeController {
	
	@Autowired
	private CommodityTypeService commodityTypeService;
    @Autowired
    ObjectMapper objectMapper;
	
	@PostMapping
    public ResponseEntity<String> createCommodityType(@RequestBody CommodityType commodityType) throws JsonProcessingException {
        boolean isSaved = commodityTypeService.save(commodityType);
        if (isSaved) {
            return ResponseEntity.ok(CommonUtil.encodedJsonResponse(isSaved,objectMapper));
        } else {
            return new ResponseEntity<>("Failed to create commodity type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@GetMapping
    public ResponseEntity<?> getAllCommodityTypes() throws JsonProcessingException {
        List<CommodityType> commodityTypes = commodityTypeService.getAll();
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(commodityTypes,objectMapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommodityTypeById(@PathVariable Integer id) throws JsonProcessingException {
        CommodityType commodityType = commodityTypeService.getById(id);
        if (commodityType != null) {
            return ResponseEntity.ok(CommonUtil.encodedJsonResponse(commodityType,objectMapper));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
