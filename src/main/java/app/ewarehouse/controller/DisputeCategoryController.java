package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.DisputeCategoryService;
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
@RequestMapping("/dispute-categories")
@CrossOrigin("*")
public class DisputeCategoryController {

    @Autowired
    private DisputeCategoryService disputeCategoryService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<String> getAllDisputeCategories() throws JsonProcessingException {
        ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .result(disputeCategoryService.getAllDisputeCategories())
                .build();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }
}
