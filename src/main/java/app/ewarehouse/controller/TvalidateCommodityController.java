package app.ewarehouse.controller;

import app.ewarehouse.entity.TreceiveCommodity;
import app.ewarehouse.entity.TvalidateCommodity;
import app.ewarehouse.service.TvalidateCommodityService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/validateCommodity")
@CrossOrigin("*")
public class TvalidateCommodityController {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TvalidateCommodityService service;

    @GetMapping
    ResponseEntity<?> getAll() throws JsonProcessingException {
        List<TvalidateCommodity> response = service.findAll();
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }

    @PostMapping
    ResponseEntity<?> validateCommodity(@RequestBody String data) throws JsonProcessingException {
        String response = service.save(data);
        return  ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }

    @GetMapping("/paginated")
    ResponseEntity<?> getAllPaginated(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) throws JsonProcessingException {
        Page<TvalidateCommodity> suspensionsPage = service.getAllCommodities(page,size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", suspensionsPage.getContent());
        response.put("totalPages", suspensionsPage.getTotalPages());
        response.put("totalElements", suspensionsPage.getTotalElements());

        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response,objectMapper));
    }

    @GetMapping("{id}")
    ResponseEntity<?> getCommodityByReceiveId(@PathVariable String id ) throws Exception {
        TvalidateCommodity commodity = service.getCommodityByValidateId(id);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(commodity,objectMapper));
    }
}
