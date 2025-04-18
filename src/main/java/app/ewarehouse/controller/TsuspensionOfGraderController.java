package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TsuspensionOfGrader;
import app.ewarehouse.service.TsuspensionOfGraderService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/suspensionGrader")
@CrossOrigin("*")
@Slf4j
public class TsuspensionOfGraderController {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TsuspensionOfGraderService service;

    @GetMapping("/paginated")
    ResponseEntity<?> getAllPaginated(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) throws JsonProcessingException {
        Page<TsuspensionOfGrader> suspensionsPage = service.getAllSuspensions(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", suspensionsPage.getContent());
        response.put("totalPages", suspensionsPage.getTotalPages());
        response.put("totalElements", suspensionsPage.getTotalElements());

        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }

    @GetMapping
    ResponseEntity<?> getAllList() throws JsonProcessingException {
        List<TsuspensionOfGrader> susGrader = service.getAll();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(susGrader).toString()));
    }

    @PostMapping("/createSuspension")
    ResponseEntity<?> createSuspension(@RequestBody String complaintFormDto) throws JsonProcessingException {
        TsuspensionOfGrader suspensionCompliance = service.createSuspension(complaintFormDto);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(suspensionCompliance)).toString());
    }

    @GetMapping("getSuspensionById/{complaintNumber}")
    ResponseEntity<?> getSuspensionByComplaintNumber(@PathVariable String complaintNumber) throws JsonProcessingException {
        TsuspensionOfGrader suspensionCompliance = service.getSuspensionByGraderId(complaintNumber);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(suspensionCompliance)).toString());
    }

    @GetMapping("/check-unique")
    public ResponseEntity<?> checkUniqueContactNumber(@RequestParam String contactNumber) throws JsonProcessingException {
        boolean isUnique = service.isContactNumberUnique(contactNumber);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(isUnique)).toString());
    }

    private <T> String buildJsonResponse(T response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(ResponseDTO.<T>builder().status(200).result(response).build());
    }


    @GetMapping("{complaintNumber}")
    ResponseEntity<String> getComplaintNumber(@PathVariable("complaintNumber") String encodedComplaintNumber) throws JsonProcessingException {
        String complaintNumber = new String(Base64.getDecoder().decode(encodedComplaintNumber), StandardCharsets.UTF_8);
        TsuspensionOfGrader tsuspension = service.getSuspensionByGraderId(complaintNumber);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(tsuspension)).toString());
    }

    @GetMapping("/pending")
    public ResponseEntity<String> getPendingComplaints(@RequestParam("params") String encodedParams) throws JsonProcessingException {
        log.info("Inside getPendingComplaints method of  TsuspensionOfGraderController");
        String decodedParams = new String(Base64.getDecoder().decode(encodedParams), StandardCharsets.UTF_8);
        var data = objectMapper.readValue(decodedParams, Map.class);
        int pageNumber = (int) data.get("pageNumber");
        int pageSize = (int) data.get("pageSize");
        int roleId = (int) data.get("roleId");

        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(service.getPendingComplaintsForUser(pageNumber, pageSize, roleId));
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }

    @PostMapping("/forward")
    public ResponseEntity<String> forwardComplaint(@RequestBody String data, HttpServletRequest request) throws JsonProcessingException {
        log.info("Inside forwardComplaint method of TsuspensionOfGraderController");
        service.forwardComplaint(data);
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setMessage("FORWARDED");
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }

    @GetMapping("/forwarded")
    public ResponseEntity<String> getForwardedComplaints(@RequestParam("params") String encodedParams) throws JsonProcessingException {
        log.info("Inside getForwardedComplaints method of  TsuspensionOfGraderController");
        String decodedParams = new String(Base64.getDecoder().decode(encodedParams), StandardCharsets.UTF_8);
        var data = objectMapper.readValue(decodedParams, Map.class);
        int pageNumber = (int) data.get("pageNumber");
        int pageSize = (int) data.get("pageSize");
        int roleId = (int) data.get("roleId");

        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(service.getForwardedComplaints(pageNumber, pageSize, roleId));
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }


    @PostMapping("/verdict")
    public ResponseEntity<String> verdict(@RequestBody String data) throws JsonProcessingException {
        log.info("Inside approveDispute method of DisputeDeclarationController");
         service.verdict(data);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(ResponseDTO.builder().status(HttpStatus.OK.value()).build())).toString());
    }


    @GetMapping("/approved")
    public ResponseEntity<String> getApprovedComplaints(@RequestParam("params") String encodedParams) throws JsonProcessingException {
        log.info("Inside getApprovedComplaints method of  TsuspensionOfGraderController");
        String decodedParams = new String(Base64.getDecoder().decode(encodedParams), StandardCharsets.UTF_8);
        var data = objectMapper.readValue(decodedParams, Map.class);
        int pageNumber = (int) data.get("pageNumber");
        int pageSize = (int) data.get("pageSize");
        int roleId = (int) data.get("roleId");

        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(service.getResolvedComplaints(pageNumber, pageSize, roleId, Status.Approved));
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }


    @GetMapping("/rejected")
    public ResponseEntity<String> getRejectedComplaints(@RequestParam("params") String encodedParams) throws JsonProcessingException {
        log.info("Inside getRejectedComplaints method of  TsuspensionOfGraderController");
        String decodedParams = new String(Base64.getDecoder().decode(encodedParams), StandardCharsets.UTF_8);
        var data = objectMapper.readValue(decodedParams, Map.class);
        int pageNumber = (int) data.get("pageNumber");
        int pageSize = (int) data.get("pageSize");
        int roleId = (int) data.get("roleId");

        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setResult(service.getResolvedComplaints(pageNumber, pageSize, roleId, Status.Rejected));
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }
}
