package app.ewarehouse.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import app.ewarehouse.service.TuserService;
import app.ewarehouse.util.CommonUtil;

@RestController
@CrossOrigin("*")
public class PersonalInformationController {
	
	@Autowired
	private TuserService tuserService;
		
	@GetMapping("/getPersonalInfoByUser/{intId}")
    public ResponseEntity<String> getPersonalInfoByUser(@PathVariable("intId") Integer intId){
		
		JSONObject response = new JSONObject();
	     JSONObject jsonObj = tuserService.getById(intId) ;
	     response.put("status", 200);
	     response.put("result", jsonObj);
			
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
	}
	
	@GetMapping("/getLastLoginTimeOfUser/{userId}")
	public ResponseEntity<String> getLastLoginTimeOfUser(@PathVariable("userId") Integer userId){
		JSONObject resp = new JSONObject();
		String lastLoginTime = tuserService.getLastLoginTime(userId);
		resp.put("lastLoginTime", lastLoginTime);
	    return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
		
  }

