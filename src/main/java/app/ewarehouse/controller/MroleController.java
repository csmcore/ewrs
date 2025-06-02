package app.ewarehouse.controller;

import app.ewarehouse.dto.UserRoleIdResponse;
import app.ewarehouse.service.MroleService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.MroleValidationCheck;
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
import java.util.List;
@RestController
@CrossOrigin("*")
@RequestMapping("/adminconsole")
public class MroleController {
	@Autowired
	private MroleService mroleService;
	String path = "src/storage/manage-role/";
	String data = "";
	JSONObject resp = new JSONObject();
	private static final Logger logger = LoggerFactory.getLogger(MroleController.class);
	@PostMapping("/manage-role/addEdit")
	public ResponseEntity<?> create(@RequestBody String trole) throws JsonMappingException, JsonProcessingException {
		logger.warn("Inside create method of MroleController Validation Error");
		data = CommonUtil.inputStreamDecoder(trole);
	
		if (MroleValidationCheck.BackendValidation(new JSONObject(data)) != null) {
			resp.put("status", 502);
			resp.put("errMsg", MroleValidationCheck.BackendValidation(new JSONObject(data)));
			logger.warn("Inside create method of MroleController Validation Error");
		} else {
			resp = mroleService.save(data);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-role/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		data = CommonUtil.inputStreamDecoder(formParams);
		logger.info("Inside getById method of MroleController");
		JSONObject json = new JSONObject(data);
		JSONObject entity = mroleService.getById(json.getInt("intId"));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-role/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of MroleController");
		JSONArray entity = mroleService.getAll(CommonUtil.inputStreamDecoder(formParams));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-role/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		logger.info("Inside delete method of MroleController");
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = mroleService.deleteById(json.getInt("intId"));
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(entity.toString()).toString());
	}

	@GetMapping("/manage-role/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		logger.info("Inside file download method of MroleController");
		System.out.println(name);
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
	
	
	@GetMapping("/roleuser/{roleid}")
	public List<UserRoleIdResponse> getUserByRoleId(@PathVariable String roleid) throws IOException {
		logger.info("Inside file download method of MroleController");
		List<UserRoleIdResponse> userlist=mroleService.getUserByRoleId(Integer.parseInt(roleid));
		//return ResponseEntity.ok(CommonUtil.inputStreamEncoder(userlist.toString()).toString());
		
		return userlist;
	}
}
