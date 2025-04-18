package app.ewarehouse.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import app.ewarehouse.service.PledgeService;

@RestController
@CrossOrigin("*")
public class DepositorPaymentController {

	@Autowired
    private PledgeService service;
    
    @PostMapping("/paymentOfDepositor")
	public ResponseEntity<?> savePayment(@RequestBody String data)
			throws JsonMappingException, JsonProcessingException {

		JSONObject response = new JSONObject();

		response = service.savePaymentData(data);
		return ResponseEntity.ok(response.toString());
	}
    
}
