package app.ewarehouse.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import app.ewarehouse.service.TmenulinksService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.TmenulinksValidationCheck;
@RestController
@CrossOrigin("*")
@RequestMapping("/adminconsole")
public class TmenulinksController {
	@Autowired
	private TmenulinksService tmenulinksService;
	String path = "src/storage/manage-links/";
	String data = "";
	JSONObject resp = new JSONObject();

	 private static final Logger log = LoggerFactory.getLogger(TmenulinksController.class);

	@PostMapping("/manage-links/addEdit")
	public ResponseEntity<?> create(@RequestBody String tmenulinks)
			throws JsonMappingException, JsonProcessingException {
		data = CommonUtil.inputStreamDecoder(tmenulinks);
		if (TmenulinksValidationCheck.BackendValidation(new JSONObject(data)) != null) {
			resp.put("status", 502);
			resp.put("errMsg", TmenulinksValidationCheck.BackendValidation(new JSONObject(data)));
		} else {
			resp = tmenulinksService.save(data);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-links/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = tmenulinksService.getById(json.getInt("intId"));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-links/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		JSONArray resultArray = new JSONArray();
		JSONObject LinkjsonObj=new JSONObject();
		JSONArray entity = tmenulinksService.getAll(CommonUtil.inputStreamDecoder(formParams));
		LinkjsonObj.put("entityObject", entity);
		
		JSONObject  resultObj=  tmenulinksService.LinkType();
		
		resultArray.put(LinkjsonObj);
		resultArray.put(resultObj);
		
		resp.put("status", 200);
		resp.put("result", resultArray);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}
	
	@PostMapping("/manage-links/all/ordered")
	public ResponseEntity<?> getAllForOrderScreen(@RequestBody String formParams) {
		JSONArray entity = tmenulinksService.getAllForOrderScreen(CommonUtil.inputStreamDecoder(formParams));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-links/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = tmenulinksService.deleteById(json.getInt("intId"));
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(entity.toString()).toString());
	}

	@GetMapping("/manage-links/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
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
	
	@PostMapping(value = "/getLeftMenuLinks")
	public ResponseEntity<?> getLeftMenuLinks(@RequestBody String data) {
		JSONArray resultjsArray = new JSONArray();
		try {
			
			
			JSONObject PrimayMenuLinkjsonObj=new JSONObject();
			
			JSONArray jsArray = tmenulinksService.getByDataUsing(data);
			
			PrimayMenuLinkjsonObj.put("PrimaryLinkList", jsArray);
			
			JSONObject GlobalMenuLinkjsonObj=tmenulinksService.GlobalLinkwithIcon(jsArray);
			
			resultjsArray.put(PrimayMenuLinkjsonObj);
			resultjsArray.put(GlobalMenuLinkjsonObj);
			
			resp.put("status", 200);
			resp.put("result", resultjsArray);
		} catch (Exception e) {
			log.info("error TmenulinksController :: getLeftMenuLinks() ",e);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());

	}
	@PostMapping("/getAllFormList")
	public ResponseEntity<?> getAllFormList(@RequestBody String formParams) {
		JSONObject jsonObject = tmenulinksService.getAllFormList();
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(jsonObject.toString()).toString());
	}
	
	 @CacheEvict(value = "getMenuLinks", allEntries = true)
	  @PostMapping("/clearCache")
	  public ResponseEntity<?>  clearMenuCache() {
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder("Menu cache cleared successfully").toString());
	  }
}
