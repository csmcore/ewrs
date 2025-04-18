package app.ewarehouse.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.TuserService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.DocumentUploadutil;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.TuserValidationCheck;
@RestController
@CrossOrigin("*")
@RequestMapping("/adminconsole")
public class TuserController {
	@Autowired
	private TuserService tuserService;
	
	@Autowired
	private  ObjectMapper objectMapper;
	
	String path = "src/storage/manage-users/";
	String data = "";
	JSONObject resp = new JSONObject();
	@PostMapping("/manage-users/addEdit")
	public ResponseEntity<?> create(@RequestBody String tuser) throws JsonMappingException, JsonProcessingException {
		data = CommonUtil.inputStreamDecoder(tuser);
		if (TuserValidationCheck.BackendValidation(new JSONObject(data)) != null) {
			resp.put("status", 502);
			resp.put("errMsg", TuserValidationCheck.BackendValidation(new JSONObject(data)));
		} else {
			resp = tuserService.save(data);
		}
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
		       
	}

	@PostMapping("/manage-users/preview")
	public ResponseEntity<?> getById(@RequestBody String formParams) {
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = tuserService.getById(json.getInt("intId"));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-users/all")
	public ResponseEntity<?> getAll(@RequestBody String formParams) {
		JSONArray entity = tuserService.getAll(CommonUtil.inputStreamDecoder(formParams));
		resp.put("status", 200);
		resp.put("result", entity);
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
	}

	@PostMapping("/manage-users/delete")
	public ResponseEntity<?> delete(@RequestBody String formParams) {
		data = CommonUtil.inputStreamDecoder(formParams);
		JSONObject json = new JSONObject(data);
		JSONObject entity = tuserService.deleteById(json.getInt("intId"));
		return ResponseEntity.ok(CommonUtil.inputStreamEncoder(entity.toString()).toString());
	}
	
	@PostMapping("/manage-users/userList")
	public ResponseEntity<?> userList(@RequestBody String formParams) {
		data = CommonUtil.inputStreamDecoder(formParams);
		
		JSONArray entity = tuserService.findUserList(data);
		resp.put("status", 200);
		resp.put("result", entity);
		
		return ResponseEntity.ok(resp.toString());
	}

	@GetMapping("/manage-users/download/{name}")
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
	

	   @GetMapping("/getfileurl")
	   public ResponseEntity<String> uploadFile() {
		   String msg="/9j/4AAQSkZJRgABAQEAYABgAAD//gATQ3JlYXRlZCB3aXRoIEdJTVD/4gKwSUNDX1BST0ZJTEUAAQEAAAKgbGNtcwQwAABtbnRyUkdCIFhZWiAH5AABAAEADwAZAABhY3NwQVBQTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9tYAAQAAAADTLWxjbXMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA1kZXNjAAABIAAAAEBjcHJ0AAABYAAAADZ3dHB0AAABmAAAABRjaGFkAAABrAAAACxyWFlaAAAB2AAAABRiWFlaAAAB7AAAABRnWFlaAAACAAAAABRyVFJDAAACFAAAACBnVFJDAAACFAAAACBiVFJDAAACFAAAACBjaHJtAAACNAAAACRkbW5kAAACWAAAACRkbWRkAAACfAAAACRtbHVjAAAAAAAAAAEAAAAMZW5VUwAAACQAAAAcAEcASQBNAFAAIABiAHUAaQBsAHQALQBpAG4AIABzAFIARwBCbWx1YwAAAAAAAAABAAAADGVuVVMAAAAaAAAAHABQAHUAYgBsAGkAYwAgAEQAbwBtAGEAaQBuAABYWVogAAAAAAAA9tYAAQAAAADTLXNmMzIAAAAAAAEMQgAABd7///MlAAAHkwAA/ZD///uh///9ogAAA9wAAMBuWFlaIAAAAAAAAG+gAAA49QAAA5BYWVogAAAAAAAAJJ8AAA+EAAC2xFhZWiAAAAAAAABilwAAt4cAABjZcGFyYQAAAAAAAwAAAAJmZgAA8qcAAA1ZAAAT0AAACltjaHJtAAAAAAADAAAAAKPXAABUfAAATM0AAJmaAAAmZwAAD1xtbHVjAAAAAAAAAAEAAAAMZW5VUwAAAAgAAAAcAEcASQBNAFBtbHVjAAAAAAAAAAEAAAAMZW5VUwAAAAgAAAAcAHMAUgBHAEL/2wBDAAMCAgMCAgMDAwMEAwMEBQgFBQQEBQoHBwYIDAoMDAsKCwsNDhIQDQ4RDgsLEBYQERMUFRUVDA8XGBYUGBIUFRT/2wBDAQMEBAUEBQkFBQkUDQsNFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBT/wgARCAAFAAUDAREAAhEBAxEB/8QAFAABAAAAAAAAAAAAAAAAAAAAB//EABUBAQEAAAAAAAAAAAAAAAAAAAYH/9oADAMBAAIQAxAAAAFWQQT/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/9oACAEBAAEFAn//xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oACAEDAQE/AX//xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oACAECAQE/AX//xAAUEAEAAAAAAAAAAAAAAAAAAAAA/9oACAEBAAY/An//xAAUEAEAAAAAAAAAAAAAAAAAAAAA/9oACAEBAAE/IX//2gAMAwEAAgADAAAAEL//xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oACAEDAQE/EH//xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oACAECAQE/EH//xAAUEAEAAAAAAAAAAAAAAAAAAAAA/9oACAEBAAE/EH//2Q==";
		   byte[] decode = Base64.getDecoder().decode(msg.getBytes());
		   
		   String file_url=new DocumentUploadutil().uploadFileByte("test", decode, FolderAndDirectoryConstant.INSP_SUSPENSION_FOLDER);
		   
	      return ResponseEntity.status(HttpStatus.CREATED).body(file_url);
	  } 
	   
	   @GetMapping("/changeUser/{id}")
		public ResponseEntity<String> updateStatusSubcategory(@PathVariable Integer id) throws JsonProcessingException {
			// logger.info("Updating nationality with ID: {}", id);
			JSONObject response = tuserService.updateUserStatus(id);

			return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());

		}
	   
	   private <T> String buildJsonResponse(T response) throws JsonProcessingException {
			Object result;
			if (response instanceof JSONObject) {
				result = response.toString();
			} else {
				result = response;
			}

			return objectMapper.writeValueAsString(ResponseDTO.builder().status(200).result(result).build());
		}
	   
	   @PostMapping("/saveActivityLog")
		  public ResponseEntity<String> saveActivityLogEnbaleDisable(@RequestBody String data) throws JsonProcessingException{
		   		
		   	    JSONObject response = new JSONObject();
		   	
		   		response=tuserService.saveActLogEnableDisable(data);
		   	 return ResponseEntity.ok(response.toString());
		   	}
		  
		  @PostMapping("/saveIsTwoFactorAuthEnabled")
		  public ResponseEntity<String> saveIsTwoFactorAuthEnabled(@RequestBody String data) throws JsonProcessingException{
		   		
		   	    JSONObject response = new JSONObject();
		   	
		   		response=tuserService.saveIsTwoFactorAuthEnabled(data);
		   	 return ResponseEntity.ok(response.toString());
		   	}
		   	
		  @GetMapping("/isActivityLogStatus")
		  public ResponseEntity<String> checkActivityLogStatus(){
			  JSONObject response = new JSONObject();
			  response=tuserService.getActivityLogStatus();
			  return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
			  
		  }
		  
		  @GetMapping("/getLastChangePassword")
		  public ResponseEntity<String> getLastChangePasswordDate() {
			    JSONObject response = new JSONObject();
		        String lastChangedDate = tuserService.getLastChangePswd();
		        response.put("lastChangedDate", lastChangedDate);
		        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(response.toString()).toString());
		    } 
		  
		  @GetMapping("/changeActivityLogStatusByUser/{id}")
			public ResponseEntity<String> updateActivityLogStatusByUser(@PathVariable Integer id) throws JsonProcessingException {
				JSONObject response = tuserService.updateActivityLogStatusByUser(id);

				return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());

			}
		  
		  @PostMapping("/uploadExcelForUser")
		    public ResponseEntity<?> uploadExcelData(@RequestParam("file") MultipartFile file) {
		        try {
		        	JSONObject response = tuserService.uploadUserDetailList(file);
		        	if (response.has("error")) {
		                return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // <-- Set proper 401 status
		                        .body(response.toMap()); // Convert JSONObject to Map for JSON response
		            }

		            return ResponseEntity.ok(response.toMap()); 
		        } catch (IOException e) {
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                    .body("Failed to upload Excel data: " + e.getMessage());
		        } catch (Exception e) {
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                    .body("An unexpected error occurred: " + e.getMessage());
		        }
		    }
		 
	   
}
