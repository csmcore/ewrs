package app.ewarehouse.controller;

import app.ewarehouse.service.SuspensionReasonService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.CommonValidator;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class SuspensionReasonController {
	
	@Autowired
	private SuspensionReasonService m_suspension_reasonService;
	
	String data = "";
	private static final Logger logger = LoggerFactory.getLogger(SuspensionReasonController.class);
	
	JSONObject resp = new JSONObject();
	
	@Value("${finalUpload.path}")
	private String finalUploadPath;

	@PostMapping("/suspension-master/addEdit")
	public ResponseEntity<?> create(@RequestBody String m_suspension_reason) throws Exception {
		logger.info("Inside create method of M_suspension_reasonController");
		try {
			JSONObject requestObj = new JSONObject(m_suspension_reason);
			if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
					requestObj.getString("REQUEST_TOKEN"))) {
				data = CommonUtil.inputStreamDecoder(m_suspension_reason);
				JSONObject jsob = new JSONObject(data);
				if (CommonValidator.isCharecterKey(jsob.getString("txtReason"), "0-9")) {
					resp.put("status", 502);
					resp.put("errMsg", "Special characters are not allowed");
					logger.warn("Inside create method of M_suspension_reasonController Validation Error");
				} else {
					data = jsob.toString();
					if(m_suspension_reasonService.checkDuplicateData(jsob.getString("txtReason"))) {
						resp.put("status", 409);
						resp.put("errMsg", "Record already exists");
					}
					else {
						resp = m_suspension_reasonService.save(data);
					}
				}
			} else {
				resp.put("msg", "error");
				resp.put("status", 417);
			}
		} catch (Exception e) {
			logger.error("Inside create method of M_suspension_reasonController error occur:" + e);
			resp.put("status", 400);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/suspension-master/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		logger.info("Inside getById method of M_suspension_reasonController");
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
					requestObj.getString("REQUEST_TOKEN"))) {
				data = CommonUtil.inputStreamDecoder(formParams);
				JSONObject json = new JSONObject(data);
				JSONObject entity = null;
				entity = m_suspension_reasonService.getById(json.getInt("intId"));
				resp.put("status", 200);
				resp.put("result", entity);
			} else {
				resp.put("msg", "error");
				resp.put("status", 417);
			}
		} catch (Exception e) {
			logger.error("Inside getById method of M_suspension_reasonController error occur:" + e);
			resp.put("status", 400);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/suspension-master/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		logger.info("Inside getAll method of M_suspension_reasonController");
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
					requestObj.getString("REQUEST_TOKEN"))) {
				resp = m_suspension_reasonService.getAll(CommonUtil.inputStreamDecoder(formParams));
			} else {
				resp.put("msg", "error");
				resp.put("status", 417);
			}
		} catch (Exception e) {
			logger.error("Inside getAll method of M_suspension_reasonController error occur:" + e);
			resp.put("status", 400);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/suspension-master/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		logger.info("Inside delete method of M_suspension_reasonController");
		try {
			JSONObject requestObj = new JSONObject(formParams);
			if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"),
					requestObj.getString("REQUEST_TOKEN"))) {
				data = CommonUtil.inputStreamDecoder(formParams);
				JSONObject json = new JSONObject(data);
				resp = m_suspension_reasonService.deleteById(json.getInt("intId"));
			} else {
				resp.put("msg", "error");
				resp.put("status", 417);
			}
		} catch (Exception e) {
			logger.error("Inside delete method of M_suspension_reasonController error occur:" + e);
			resp.put("status", 400);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@GetMapping("/suspension-master/download/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException {
		logger.info("Inside download method of M_suspension_reasonController");
		File file = new File(finalUploadPath + "suspension-master/" + name);
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
