package app.ewarehouse.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import app.ewarehouse.util.CommonUtil;
import jakarta.persistence.EntityManager;

@RestController
@CrossOrigin("*")
public class CommonController {

	@Autowired
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
	JSONObject response = new JSONObject();
	@Value("${tempUpload.path}")
	private String tempUploadPath;

	@PostMapping(value = "/saveFileToTemp")
	public ResponseEntity<?> saveDocImgToTemp(@RequestParam("file") MultipartFile multipart) {
		logger.info("Inside saveDocImgToTemp method of CommonController");
		try {
			SecureRandom rand = new SecureRandom();
			int x = rand.nextInt(900 - 100) + 100;
			Timestamp tt = new Timestamp(System.currentTimeMillis());
			String fileNameForType = (multipart.getOriginalFilename());
			String[] fileArray = fileNameForType.split("[.]");
			String actualType = fileArray[fileArray.length - 1];
			String mimeType = multipart.getContentType();
			JSONObject jsonMimeType = new JSONObject("{'pdf':'application/pdf','jpeg':'image/jpeg',"
					+ "'jpe':'image/jpeg','png':'image/png','gif':'image/gif','jpg':'image/jpeg','csv':'text/csv',"
					+ "'dbf':'application/octet-stream','htm':'text/htm','html':'text/html','mht':'multipart/related',"
					+ "'mhtml':'application/pdf','ods':'application/vnd.oasis.opendocument.spreadsheet','prn':'application/octet-stream','txt':'text/plain','xla':'application/vnd.ms-excel',"
					+ "'xlam':'application/vnd.ms-excel.sheet.binary.macroEnabled.12','xls':'application/vnd.ms-excel','xlsb':'application/octet-stream','xlsx':'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',"
					+ "'xlt':'application/octet-stream','xltm':'application/vnd.ms-excel','xlsm':'application/octet-stream','xlw':'application/vnd.ms-excel',"
					+ "'xps':'application/octet-stream','doc':'application/msword','docm':'application/octet-stream','docx':'application/vnd.openxmlformats-officedocument.wordprocessingml.document',"
					+ "'dot':'application/msword','dotm':'application/vnd.ms-word.template.macroEnabled.12','dotx':'application/octet-stream','odt':'application/vnd.oasis.opendocument.text','rtf':'application/rtf',"
					+ "'wps':'application/octet-stream','xml':'text/xml','msword':'application/msword','mp4':'video/mp4','ogx':'application/ogg','oga':'audio/ogg','ogv':'video/ogg','ogg':'audio/ogg','webm':'video/webm','mp3':'audio/mpeg','mpeg':'video/mpeg',"
					+ "'document':'application/vnd.openxmlformats-officedocument.wordprocessingml.document'}");
			if (jsonMimeType.get(actualType.toLowerCase()).equals(mimeType)) {
				
				Path path = Paths.get(tempUploadPath);

				if (!Files.exists(path) && !Files.isDirectory(path)) {
					try {
						Files.createDirectories(path);
					} catch (IOException e) {
						logger.info("Inside getById method of Complaint_managmentServiceImpl" + e.getMessage());

					}
				}

				File file1 = new File(tempUploadPath + x + tt.getTime() + "." + actualType);
				try(BufferedOutputStream bf= new BufferedOutputStream(new FileOutputStream(file1))) {
					byte[] bytes = multipart.getBytes();
					bf.write(bytes);
					bf.close();
				}
				response.put("fileName", "" + x + tt.getTime() + "." + actualType);
				response.put("status", 200);
			} else {
				logger.error(
						"Inside saveDocImgToTemp method of CommonController some error occur:MIME Type not found or Invalid File Type");
				response.put("status", 400);
			}

		} catch (Exception e) {
			logger.error("Inside saveDocImgToTemp method of CommonController some error occur:",e);
			response.put("status", 400);
		}
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping(value = "/fillDropDown")
	public ResponseEntity<?> getAllDynamicDropDownValue(@RequestBody String data)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, JSONException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		logger.info("Inside getAllDynamicDropDownValue method of CommonController");
		JSONObject requestObj = new JSONObject(data);
		if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"), requestObj.getString("REQUEST_TOKEN"))) {
			String getData = CommonUtil.inputStreamDecoder(data);
			JSONObject json = new JSONObject(getData);
			String[] str = json.getString("method").split("/");
			Class<?> cls = Class.forName("app.ewarehouse.serviceImpl." + str[0]);
			Method method = cls.getMethod(str[1], EntityManager.class, String.class);
			response.put("status", 200);
			response.put("result", method.invoke(method, entityManager, json.toString()));
		} else {
			response.put("msg", "error");
			response.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());

	}

	@PostMapping(value = "/fillRadio")
	public ResponseEntity<?> getAllDynamicRadioValue(@RequestBody String data)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, JSONException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		logger.info("Inside getAllDynamicRadioValue method of CommonController");
		JSONObject requestObj = new JSONObject(data);
		if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"), requestObj.getString("REQUEST_TOKEN"))) {
			String getData = CommonUtil.inputStreamDecoder(data);
			JSONObject json = new JSONObject(getData);
			String[] str = json.getString("method").split("/");
			Class<?> cls = Class.forName("app.ewarehouse.serviceImpl." + str[0]);
			Method method = cls.getMethod(str[1], EntityManager.class, String.class);
			response.put("status", 200);
			response.put("result", method.invoke(method, entityManager, json.toString()));
		} else {
			response.put("msg", "error");
			response.put("status", 417);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());

	}
	
	
	 @GetMapping("/deleteFileFromTemp/{fileName}")
	    public ResponseEntity<String> deleteTempFile(@PathVariable String fileName)throws JsonProcessingException {
		 JSONObject object=new JSONObject();
		 try {
			 File filePath = new File(tempUploadPath + fileName);
			Files.delete(filePath.toPath());
			 object.put("status",200);
			 object.put("msg","fileDeleted");
		} catch (IOException e) {
			 object.put("status",500);
			 object.put("msg","fileDeleted");
			// TODO Auto-generated catch block
			logger.error("Inside deleteDocImgToTemp method of CommonController some error occur:",e);
		}
		  
		
	        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(object.toString()).toString());
	    }
//-------------------common method write----------------------------
}