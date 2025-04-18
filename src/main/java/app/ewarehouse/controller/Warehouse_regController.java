package app.ewarehouse.controller;

import app.ewarehouse.service.Warehouse_regService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.Warehouse_regValidationCheck;
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
@RequestMapping("/registration")
 public class Warehouse_regController {
@Autowired
private Warehouse_regService warehouse_regService; 
String data ="";
private static final Logger logger = LoggerFactory.getLogger(Warehouse_regController.class);
JSONObject resp = new JSONObject();
@Value("${finalUpload.path}")
	private String finalUploadPath;
@PostMapping("/warehouse-registration/addEdit")
public ResponseEntity<?> create(@RequestBody  String   warehouse_reg)  throws Exception {
 logger.info("Inside create method of Warehouse_regController");
try{
JSONObject requestObj = new JSONObject(warehouse_reg);
if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"), requestObj.getString("REQUEST_TOKEN"))) {
data = CommonUtil.inputStreamDecoder(warehouse_reg);String validationMsg = Warehouse_regValidationCheck.BackendValidation(new JSONObject(data));
if ( validationMsg!= null) {
 resp.put("status", 502);
 resp.put("errMsg",validationMsg );
logger.warn("Inside create method of Warehouse_regController Validation Error");
} else {JSONObject jsob=new JSONObject(data);
data=jsob.toString();
resp=warehouse_regService.save(data);
}
} else {
resp.put("msg", "error");
resp.put("status", 417);
}
}catch(Exception e){
logger.error("Inside create method of Warehouse_regController error occur:"+e);
resp.put("status", 400);
}
 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
} 
@PostMapping("/warehouse-registration/preview")
public ResponseEntity<?>getById(@RequestBody String formParams) {
logger.info("Inside getById method of Warehouse_regController");
try {
JSONObject requestObj = new JSONObject(formParams);
if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"), requestObj.getString("REQUEST_TOKEN"))) {
 data = CommonUtil.inputStreamDecoder(formParams); 
JSONObject json=new JSONObject(data);
JSONObject entity=null;
 entity=warehouse_regService.getById(json.getInt("intId"));
resp.put("status", 200);
  resp.put("result", entity);
}
 else {
resp.put("msg", "error");
resp.put("status", 417);
}
} catch (Exception e) {
logger.error("Inside getById method of Warehouse_regController error occur:"+e);
resp.put("status", 400);
}
return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
}@PostMapping("/warehouse-registration/all")
public ResponseEntity<?>getAll(@RequestBody String formParams){
logger.info("Inside getAll method of Warehouse_regController");
try {
JSONObject requestObj = new JSONObject(formParams);
if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"), requestObj.getString("REQUEST_TOKEN"))) {
resp=warehouse_regService.getAll(CommonUtil.inputStreamDecoder(formParams));
}
else {
	resp.put("msg", "error");
resp.put("status", 417);
}
} catch (Exception e) {
logger.error("Inside getAll method of Warehouse_regController error occur:"+e);resp.put("status", 400);
}
 return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
} 
@PostMapping("/warehouse-registration/delete")
public ResponseEntity<?>delete(@RequestBody String formParams) { 
logger.info("Inside delete method of Warehouse_regController");
try {
JSONObject requestObj = new JSONObject(formParams);
if (CommonUtil.hashRequestMatch(requestObj.getString("REQUEST_DATA"), requestObj.getString("REQUEST_TOKEN"))) { data = CommonUtil.inputStreamDecoder(formParams); 
JSONObject json=new JSONObject(data);
resp=warehouse_regService.deleteById(json.getInt("intId"));
} else {
resp.put("msg", "error");
resp.put("status", 417);
}
} catch (Exception e) {
logger.error("Inside delete method of Warehouse_regController error occur:"+e);resp.put("status", 400);
}
return ResponseEntity.ok(CommonUtil.inputStreamEncoder(resp.toString()).toString());
}@GetMapping("/warehouse-registration/download/{name}")
 public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException{
logger.info("Inside download method of Warehouse_regController");
 File file=new File(finalUploadPath + "warehouse-registration/"+name);
 Path path=Paths.get(file.getAbsolutePath());
 ByteArrayResource byteArrayResource=new ByteArrayResource(Files.readAllBytes(path));
 return ResponseEntity.ok().headers(headers(name))
               .contentLength(file.length())
                 .contentType(MediaType
                  .parseMediaType("application/octet-stream"))
              .body(byteArrayResource);
 }

 private HttpHeaders headers(String name) {
 HttpHeaders header = new HttpHeaders();
         header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
         header.add("Cache-Control", 
                      "no-cache, no-store, must-revalidate");
         header.add("Pragma", "no-cache");
         header.add("Expires", "0");
         return header;
 }
}