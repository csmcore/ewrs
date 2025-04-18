package app.ewarehouse.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import app.ewarehouse.service.MgroupsService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.MgroupsValidationCheck;
@RestController
@CrossOrigin("*")
@RequestMapping("/adminconsole")
public class MgroupsController {
	
	private MgroupsService mgroupsService;
	public MgroupsController(MgroupsService mgroupsService) {
		super();
		this.mgroupsService = mgroupsService;
	}

	private static final Logger logger = LoggerFactory.getLogger(MgroupsController.class);
	@PostMapping("/manage-groups/addEdit")
	public ResponseEntity<?> create(@RequestBody String tgroups) throws JsonMappingException, JsonProcessingException {
		logger.info("Inside create method of MgroupsController");
		JSONObject resp = new JSONObject();
		String data = "";
		data = CommonUtil.inputStreamDecoder(tgroups);
		if (MgroupsValidationCheck.BackendValidation(new JSONObject(data)) != null) {
			resp.put("status", 502);
			resp.put("errMsg", MgroupsValidationCheck.BackendValidation(new JSONObject(data)));
			logger.warn("Inside create method of MgroupsController Validation Error");
		} else {
			resp = mgroupsService.save(data);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-groups/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		JSONObject resp = new JSONObject();
		String data = "";
		logger.info("Inside getById method of MgroupsController");
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = mgroupsService.getById(json.getInt("intId"));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-groups/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		JSONObject resp = new JSONObject();
		logger.info("Inside getAll method of MgroupsController");
		JSONArray entity = mgroupsService.getAll(CommonUtil.inputStreamDecoder(formParams));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-groups/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		String data = "";
		logger.info("Inside delete method of MgroupsController");
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = mgroupsService.deleteById(json.getInt("intId"));
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(entity.toString()).toString());
	}

	@GetMapping("/manage-groups/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		String path = "src/storage/manage-groups/";
		logger.info("Inside file download method of MgroupsController");
		//System.out.println(name);
		File file = new File(path + name);
		//System.out.println("the file is:" + file);
		Path path1 = Paths.get(file.getAbsolutePath());
		ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path1));
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
