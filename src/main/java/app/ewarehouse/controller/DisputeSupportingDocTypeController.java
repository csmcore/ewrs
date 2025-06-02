package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.DisputeSupportingDocTypeService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dispute-supporting-doctype")
@CrossOrigin("*")
public class DisputeSupportingDocTypeController {

    @Autowired
    DisputeSupportingDocTypeService disputeSupportingDocTypeService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<String> getAllDisputeCategories() throws JsonProcessingException {
        ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .result(disputeSupportingDocTypeService.getAllDisputeSupportingDocumentType())
                .build();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }

}
