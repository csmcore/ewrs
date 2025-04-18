package app.ewarehouse.controller;


import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TrevocationOfCertificateOfCompliance;
import app.ewarehouse.service.TrevocationOfCertificateOfComplianceService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/revocation")
public class TrevocationOfCertificateOfComplianceController {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TrevocationOfCertificateOfComplianceService service;

    @GetMapping("/paginated")
    ResponseEntity<?> getAllPaginated(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) throws JsonProcessingException {
        Page<TrevocationOfCertificateOfCompliance> suspensionsPage = service.getAllSuspensions(page,size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", suspensionsPage.getContent());
        response.put("totalPages", suspensionsPage.getTotalPages());
        response.put("totalElements", suspensionsPage.getTotalElements());

        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }


    @PostMapping("/createSuspension")
    ResponseEntity<?> createSuspension(@RequestBody String complaintFormDto ) throws JsonProcessingException{
        TrevocationOfCertificateOfCompliance suspensionCompliance=service.createSuspension(complaintFormDto);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(suspensionCompliance)).toString());
    }
    @GetMapping("getSuspensionById/{complaintNumber}")
    ResponseEntity<?> getSuspensionByComplaintNumber(@PathVariable String complaintNumber ) throws JsonProcessingException{
        TrevocationOfCertificateOfCompliance suspensionCompliance = service.getSuspensionByComplaintNumber(complaintNumber);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(suspensionCompliance)).toString());
    }
    @GetMapping
    ResponseEntity<?> getAll() throws JsonProcessingException {
        List<TrevocationOfCertificateOfCompliance> revocation = service.findAll();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(revocation)).toString());
    }

    private <T> String buildJsonResponse(T response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(ResponseDTO.<T>builder().status(200).result(response).build());
    }
}

