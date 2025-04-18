package app.ewarehouse.controller;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.LoanPurposeService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/loan-purposes")
@RequiredArgsConstructor
@Slf4j
public class LoanPurposeController {
    private final LoanPurposeService loanPurposeService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<String> getAll() throws JsonProcessingException {
        log.info("Inside getAll method of LoanPurposeController");
        ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .result(loanPurposeService.getAll())
                .build();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(objectMapper.writeValueAsString(responseDTO)).toString());
    }
}

