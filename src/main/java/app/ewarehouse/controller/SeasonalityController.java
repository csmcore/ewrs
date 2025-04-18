package app.ewarehouse.controller;

import app.ewarehouse.entity.Seasonality;
import app.ewarehouse.service.SeasonalityService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seasonalities")
@CrossOrigin("*")
public class SeasonalityController {
	
	@Autowired
	private SeasonalityService seasonalityservice;
    @Autowired
    ObjectMapper objectMapper;
	
	@PostMapping
	public ResponseEntity<String> createSeasonality(@RequestBody Seasonality seasonality) throws JsonProcessingException {
        boolean isSaved = seasonalityservice.save(seasonality);
        if (isSaved) {
//            return new ResponseEntity<>("Seasonality created successfully", HttpStatus.CREATED);
            return ResponseEntity.ok(CommonUtil.encodedJsonResponse(isSaved,objectMapper));
        } else {
            return new ResponseEntity<>("Failed to create seasonality", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@GetMapping
    public ResponseEntity<?> getAllSeasonalities() throws JsonProcessingException {
        List<Seasonality> seasonalities = seasonalityservice.getAll();
//        return new ResponseEntity<>(seasonalities, HttpStatus.OK);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(seasonalities,objectMapper));
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getSeasonalityById(@PathVariable Integer id) throws JsonProcessingException {
        Seasonality seasonality = seasonalityservice.getById(id);
        if (seasonality != null) {
//            return new ResponseEntity<>(seasonality, HttpStatus.OK);
            return ResponseEntity.ok(CommonUtil.encodedJsonResponse(seasonality,objectMapper));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
}
