package app.ewarehouse.controller;

import app.ewarehouse.service.MdepartmentService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.MdepartmentValidationCheck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
public class MdepartmentController {
	@Autowired
	private MdepartmentService mdepartmentService;
	String path = "src/storage/manage-department/";
	String data = "";
	JSONObject resp = new JSONObject();
	private static final Logger logger = LoggerFactory.getLogger(MdepartmentController.class);

	@PostMapping("/manage-department/addEdit")
	public ResponseEntity<?> create(@RequestBody String tdepartment)
			throws JsonMappingException, JsonProcessingException {
		logger.info("Inside create method of MdepartmentController");
		data = CommonUtil.inputStreamDecoder(tdepartment);
		if (MdepartmentValidationCheck.BackendValidation(new JSONObject(data)) != null) {
			resp.put("status", 502);
			resp.put("errMsg", MdepartmentValidationCheck.BackendValidation(new JSONObject(data)));
			logger.warn("Inside create method of MdepartmentController Validation Error");
		} else {
			resp = mdepartmentService.save(data);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-department/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of MdepartmentController");
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = mdepartmentService.getById(json.getInt("intId"));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-department/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of MdepartmentController");
		JSONArray entity = mdepartmentService.getAll(CommonUtil.inputStreamDecoder(formParams));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-department/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		logger.info("Inside delete method of MdepartmentController");
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = mdepartmentService.deleteById(json.getInt("intId"));
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(entity.toString()).toString());
	}

	@GetMapping("/manage-department/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		logger.info("Inside file download method of MdepartmentController");
		File file = new File(path + name);
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
	
}
