package app.ewarehouse.controller;

import app.ewarehouse.dto.InspectorSuspensionComplaintResponse;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.Status;
import app.ewarehouse.service.InspectorSuspensionComplaintService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/inspector-suspension")
@Slf4j
public class InspectorSuspensionComplaintController {

    @Autowired
    private InspectorSuspensionComplaintService inspectorSuspensionComplaintService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String data) throws JsonProcessingException {
        log.info("Inside create method of ComplaintController");
        ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                .status(HttpStatus.CREATED.value())
                .message(inspectorSuspensionComplaintService.save(data))
                .build();
            return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value = "page", required = false) Integer pageNumber, @RequestParam(value = "size", required = false) Integer pageSize,  @RequestParam(value = "sortDirection", required = false) String sortDir,  @RequestParam(value = "sortColumn", required = false) String sortCol, @RequestParam(required = false) String search) throws JsonProcessingException {
        log.info("Inside getAll method of ComplaintController");
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(inspectorSuspensionComplaintService.getAll(pageNumber, pageSize, sortCol, sortDir, search));
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
    	InspectorSuspensionComplaintResponse response = inspectorSuspensionComplaintService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getComplaintList")
    public ResponseEntity<String> getByRoleView(@RequestParam String status, @RequestParam Integer actionTakenBy, Pageable pageable) throws JsonProcessingException {
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(inspectorSuspensionComplaintService.getComplaintList(status,actionTakenBy,pageable), objectMapper));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("fileName") String name) throws IOException {
        log.info("Inside download method of ComplaintController");

        final String path = "src/main/resources/uploadedDocuments/";

        Path filePath = Paths.get(path, name);
        File file = filePath.toFile();
        log.info("The file is: {}", file.getAbsolutePath());
        if (!file.exists() || file.isDirectory()) {
            throw new IOException("File not found: " + file.getAbsolutePath());
        }
        ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(filePath));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getName());
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(byteArrayResource);
    }
    
    @PostMapping("forwardComplaint")
    public void forwardComplaint(@RequestBody String data) {
    	JSONObject json = new JSONObject(data);
    	String status = "".equals(json.getString("status"))?"Pending":json.getString("status");
    	inspectorSuspensionComplaintService.forwardComplaint(json.getString("id"), json.getInt("actionTakenBy"), json.getString("remarks"), Status.valueOf(status), json.getInt("userId"));
    }
}
