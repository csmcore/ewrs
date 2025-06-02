package app.ewarehouse.controller;

import app.ewarehouse.service.ManageLanguageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ManageLanguageController {
	@Autowired
	private ManageLanguageService manageLanguageService;
	
	@PostMapping("/viewLanguage")
	public ResponseEntity<String> viewLanguage(@RequestBody String params){
		
		JSONObject languageDetails=manageLanguageService.getActiveLanguageDetailsList();
		
		return ResponseEntity.ok(languageDetails.toString());
	}
	
	
}
