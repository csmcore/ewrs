package app.ewarehouse.controller;

import app.ewarehouse.service.TrolepermissionService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.TrolepermissionValidationCheck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
@CrossOrigin("*")
@RequestMapping("/adminconsole")
public class TrolepermissionController {
	@Autowired
	private TrolepermissionService trolepermissionService;
	String path = "src/storage/set-role-permission/";
	String data = "";
	JSONObject resp = new JSONObject();
	private static final Logger logger = LoggerFactory.getLogger(TrolepermissionController.class);

	@PostMapping("/set-role-permission/addEdit")
	public ResponseEntity<?> create(@RequestBody String trolepermission)
			throws JsonMappingException, JsonProcessingException {
		logger.info("Inside create method of TrolepermissionController");
		data = CommonUtil.inputStreamDecoder(trolepermission);
		if (TrolepermissionValidationCheck.BackendValidation(new JSONObject(data)) != null) {
			resp.put("status", 502);
			resp.put("errMsg", TrolepermissionValidationCheck.BackendValidation(new JSONObject(data)));
			logger.warn("Inside create method of TrolepermissionController Validation Error");
		} else {
			resp = trolepermissionService.save(data);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/set-role-permission/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of TrolepermissionController");
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONArray entity = trolepermissionService.getById(json.getInt("roleId"),json.getInt("userId"));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/set-role-permission/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of TrolepermissionController");
		JSONArray entity = trolepermissionService.getAll(CommonUtil.inputStreamDecoder(formParams));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/set-role-permission/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		logger.info("Inside delete method of TrolepermissionController");
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = trolepermissionService.deleteById(json.getInt("intId"));
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(entity.toString()).toString());
	}

	@GetMapping("/set-role-permission/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		logger.info("Inside file download method of TrolepermissionController");
		File file = new File(path + name);
		System.out.println("the file is:" + file);
		Path path = Paths.get(file.getAbsolutePath());
		ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
		return ResponseEntity.ok().headers(headers(name)).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(byteArrayResource);
	}

	private HttpHeaders headers(String name) {
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");
		return header;
	}
	
	@PostMapping("/set-role-permission/bindAllMenuLinks")
	public ResponseEntity<?> getLinksData(@RequestBody String formParams){
		logger.info("Inside getLinksData method of TrolepermissionController");
		data= CommonUtil.inputStreamDecoder(formParams);
		JSONArray linkData=trolepermissionService.bindAllMenuLinks(data);
		resp.put("status", 200);
		resp.put("result", linkData);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	@PostMapping(value = "/getRoleWisePermissionDetails")
	public ResponseEntity<?> getRoleWisePermissionDetails(@RequestBody String data) {
		logger.info("Inside getRoleWisePermissionDetails method of TrolepermissionController");
		try {
			JSONObject jsObject = trolepermissionService.givePermissionDetails(data);
			resp.put("status", 200);
			resp.put("result", jsObject);
		} catch (Exception e) {
			logger.error("Inside getRoleWisePermissionDetails method of TrolepermissionController some error occur:" + e);
			resp.put("status", 404);
			resp.put("msg", "No Record Found!!");
		}
		return ResponseEntity.ok(resp.toString());
	}
}
