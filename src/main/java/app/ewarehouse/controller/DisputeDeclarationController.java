package app.ewarehouse.controller;

import app.ewarehouse.service.DisputeDeclarationService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/dispute-declarations")
@Slf4j
public class DisputeDeclarationController {
    private final DisputeDeclarationService disputeDeclarationService;
    private final ObjectMapper objectMapper;

    public DisputeDeclarationController(DisputeDeclarationService disputeDeclarationService, ObjectMapper objectMapper) {
        this.disputeDeclarationService = disputeDeclarationService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String data) throws JsonProcessingException {
        log.info("Inside create method of DisputeDeclarationController");
        String response = disputeDeclarationService.create(data);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response, objectMapper));
    }

    @PostMapping("/takeAction")
    public ResponseEntity<String> takeAction(@RequestBody String data) throws JsonProcessingException {
        log.info("Inside takeAction method of DisputeDeclarationController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(disputeDeclarationService.takeAction(data), objectMapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") String id) throws JsonProcessingException {
        log.info("Inside getById method of BuyerDepositorTypeController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(disputeDeclarationService.getById(id), objectMapper));
    }

    @GetMapping("/all-paginated")
    public ResponseEntity<?> getAll(Pageable pageable) throws JsonProcessingException {
        log.info("Inside getAll method of DisputeDeclarationController");
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(disputeDeclarationService.getAll(pageable), objectMapper));
    }

    @GetMapping("/roleView")
    public ResponseEntity<String> getByRoleView(Pageable pageable,
                                                @RequestParam(required = false) Integer roleId,
                                                @RequestParam(required = false) Integer userId,
                                                @RequestParam(required = false) Integer action,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
                                                @RequestParam(required = false) String search,
                                                @RequestParam(required = false) String sortColumn,
                                                @RequestParam(required = false) String sortDirection) throws JsonProcessingException {
        log.info("Inside getByRoleView method of DisputeDeclarationController");
        log.info("roleId: " + roleId);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(disputeDeclarationService.findByFilters(roleId, fromDate, toDate, search, sortColumn, sortDirection, userId, action, pageable), objectMapper));
    }
}