package app.ewarehouse.controller;

import app.ewarehouse.dto.ApplicationCollateralDTO;
import app.ewarehouse.dto.ComplaintmanagementResponse;
import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.dto.SubCountyResponse;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TapplicationOfCertificateOfCompliance;
import app.ewarehouse.service.TapplicationOfCertificateOfComplianceService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("certComplaince")
@Slf4j
public class TapplicationOfCertificateOfComplianceController {


    @Autowired
    TapplicationOfCertificateOfComplianceService service;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("getAll")
    public ResponseEntity<?> getAllCompliance() throws JsonProcessingException {
        List<TapplicationOfCertificateOfCompliance> certificateOfComplianceList = service.findAll();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(certificateOfComplianceList)).toString());
    }
    @PostMapping("createApplication")
    public ResponseEntity<?> createApplication(@RequestBody String certApplication) throws JsonProcessingException {
        ApplicationCollateralDTO certCompliance = service.save(certApplication);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(certCompliance)).toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") String id) throws JsonProcessingException {
        ApplicationCollateralDTO application = service.getApplicationById(id);
        return  ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(application)).toString());
    }

    @GetMapping()
    public ResponseEntity<?> getApplicationByRoleIdAndStatus(@RequestParam Integer intCreatedBy,@RequestParam String status) throws JsonProcessingException {
        ApplicationCollateralDTO application = service.getApplication(intCreatedBy,status);
        return  ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(application)).toString());
    }

    @PostMapping("/takeAction")
    public ResponseEntity<String> takeAction(@RequestBody String data) throws JsonProcessingException {
        log.info("Inside takeAction method of TapplicationOfCertificateOfComplianceController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(service.takeAction(data), objectMapper));
    }

    @GetMapping("/roleView")
    public ResponseEntity<String> getByRoleView(
            Pageable pageable,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(required = false) String sortDirection) throws JsonProcessingException {
        log.info("Inside getByRoleView method of TapplicationOfCertificateOfComplianceController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(service.findByFilters(roleId, userId, status, search, sortColumn,
                sortDirection, pageable), objectMapper));
    }

    private <T> String buildJsonResponse(T response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(ResponseDTO.<T>builder().status(200).result(response).build());
    }

    @GetMapping("getAllSubCounties")
    public ResponseEntity<?> getAllSubcounties() throws JsonProcessingException {
        List<SubCountyResponse> subCountyList = service.findAllsubCounty();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(subCountyList)).toString());
    }

    @GetMapping("subcounty/{subCountyId}")
    public ResponseEntity<?> getBysubCountyId(@PathVariable("subCountyId") Integer subCountyId) throws JsonProcessingException {
        List<ApplicationCollateralDTO> application = service.getApplicationBySubCountyId(subCountyId);
        return  ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(application)).toString());
    }

}
