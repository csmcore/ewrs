package app.ewarehouse.master.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.master.dto.TypeMasterBean;
import app.ewarehouse.master.service.ITypeMasterService;
import app.ewarehouse.util.ClassHelperUtils;
import app.ewarehouse.util.EncryptionUtils;

/**
 * @Project : EWAR Backend
 * @author pravat.behera
 * @Date: 24-march-2024 : 01:25 PM
 */
@RestController
@RequestMapping(value = "/typeMaster")
public class TypeMasterController {
	private final Logger logger;
	private ITypeMasterService service;

	public TypeMasterController(Logger logger, ITypeMasterService service) {
		this.logger = logger;
		this.service = service;
	}
	
	/**
	 * @author pravat.behera
	 * @Date 24-03-2025
	 * @Description this method used for save type master data
	 * 
	 * 
	 */
	
	@PostMapping("/saveType")
	public ResponseEntity<Map<String, Object>> saveTypeMaster(@RequestBody  Map<String,Object> requestTypeData) {
		try {
			logger.info("Inside TypeMasterController for saveTypeMaster() Method ");
			
			ObjectMapper objectMapper = new ObjectMapper();
			String data=EncryptionUtils.decryptCode((String) requestTypeData.get("encrData"));
			JSONObject jsonObject = new JSONObject(data);
			TypeMasterBean typeMasterBean=  objectMapper.readValue(jsonObject.toString(), TypeMasterBean.class);
			typeMasterBean=service.saveTypeMaster(typeMasterBean);
			if (typeMasterBean != null) {
			    logger.info("TypeMasterController for saveTypeMaster() Method after execute");
				return ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(typeMasterBean,"Type master save successfully."));
			}
			else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("Something Went Wrong."));
			
		} catch (Exception e) {
			logger.error("Exception Occurred in saveTypeMaster() Method of TypeMasterController : {}", e.getMessage());
			return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap(e.getMessage()));
		}
	}

	/**
	 * @author pravat.behera
	 * @Purpose This method used to get Type master Data
	 * @MethodType POST Mapping
	 *
	 */

	@PostMapping(value = "/getTypeMaster")
	public ResponseEntity<Map<String, Object>> viewmasterTypeDetails() {
		try {
			List<TypeMasterBean> map = service.allTypeMaster();
			if (map != null && map.size() > 0)
				return ResponseEntity
						.ok(ClassHelperUtils.createSuccessEncryptedMap(map, "Details fetched successfully"));
			else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("No Data Found"));
		} catch (Exception e) {
			logger.error("Exception Occurred in viewmasterTypeDetails() Method of TypeMasterController : {}",
					e.getMessage());
			return ResponseEntity.ok(ClassHelperUtils.createErrorResponse(e.getMessage()));
		}
	}
	
	/**
	 * @author pravat.behera
	 * @Purpose This method used to get Type master Data
	 * @MethodType POST Mapping
	 *
	 */

	@PostMapping(value = "/typeMasterDropDown")
	public ResponseEntity<Map<String, Object>> getAllTypeMasterForDropDown() {
		try {
			List<Map<Integer,String>> map = service.getDropDownTypeMaster();
			if (map != null && map.size() > 0)
				return ResponseEntity
						.ok(ClassHelperUtils.createSuccessEncryptedMap(map, "Details fetched successfully"));
			else
				return ResponseEntity.ok(ClassHelperUtils.createNoContentEncryptedMap("No Data Found"));
		} catch (Exception e) {
			logger.error("Exception Occurred in getAllTypeMasterForDropDown() Method of TypeMasterController : {}",
					e.getMessage());
			return ResponseEntity.ok(ClassHelperUtils.createErrorResponse(e.getMessage()));
		}
	}

}
