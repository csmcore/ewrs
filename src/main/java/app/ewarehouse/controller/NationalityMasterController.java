package app.ewarehouse.controller;

import app.ewarehouse.dto.NationalityMasterDto;
import app.ewarehouse.entity.Nationality;
import app.ewarehouse.service.NationalityMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/master/nationalities")
public class NationalityMasterController {

	 private static final Logger logger = LoggerFactory.getLogger(NationalityMasterController.class);

	    private final NationalityMasterService nationalityService;

	    public NationalityMasterController(NationalityMasterService nationalityService) {
	        this.nationalityService = nationalityService;
	    }

	    @GetMapping("/all")
	    public ResponseEntity<List<Nationality>> getAllNationalities() {
	        logger.info("Fetching all nationalities");
	        List<Nationality> nationalities = nationalityService.getAllNationalities();
	        return new ResponseEntity<>(nationalities, HttpStatus.OK);
	    }
	    
	    @GetMapping("/")
	    public ResponseEntity<List<Nationality>> getNationalities() {
	        logger.info("Fetching nationalities");
	        List<Nationality> nationalities = nationalityService.getNationalities();
	        return new ResponseEntity<>(nationalities, HttpStatus.OK);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Nationality> getNationalityById(@PathVariable Long id) {
	        logger.info("Fetching nationality with ID: {}", id);
	        Optional<Nationality> nationality = nationalityService.getNationalityById(id);
	        return nationality.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
	                .orElseGet(() -> {
	                    logger.warn("Nationality with ID: {} not found", id);
	                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	                });
	    }

	    @PostMapping("/")
	    public ResponseEntity<Nationality> createNationality(@RequestBody NationalityMasterDto nationalityDto) {
	        logger.info("Creating new nationality: {}", nationalityDto);
	        Nationality createdNationality = nationalityService.createNationality(nationalityDto);
	        return new ResponseEntity<>(createdNationality, HttpStatus.CREATED);
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<Nationality> updateNationality(@PathVariable Long id, @RequestBody NationalityMasterDto nationalityDto) {
	        logger.info("Updating nationality with ID: {}", id);
	        Nationality updatedNationality = nationalityService.updateNationality(id, nationalityDto);
	        if (updatedNationality != null) {
	            logger.info("Updated nationality: {}", updatedNationality);
	            return new ResponseEntity<>(updatedNationality, HttpStatus.OK);
	        } else {
	            logger.warn("Nationality with ID: {} not found", id);
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> changeNationalityStatus(@PathVariable Long id) {
	        logger.info("Changing status of nationality with ID: {}", id);
	        nationalityService.changeNationalityStatus(id);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	
}
