package app.ewarehouse.controller;

import app.ewarehouse.entity.TwarehouseCharges;
import app.ewarehouse.service.TwarehouseChargesService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/warehouseCharges")
public class TwarehouseChargesController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TwarehouseChargesService service;

    @GetMapping("{id}")
    public ResponseEntity<?> getCharges(@PathVariable String id) throws JsonProcessingException {
        TwarehouseCharges charges = service.findByTxtConformityId(id);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(charges,objectMapper));
    }
}
