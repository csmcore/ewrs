package app.ewarehouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import app.ewarehouse.dto.UnitOfMeasurementDTO;
import app.ewarehouse.service.UnitOfMeasurementService;
import app.ewarehouse.util.BuildJsonResponseUtil;
import app.ewarehouse.util.CommonUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/master/units")
public class UnitOfMeasurementController {

    @Autowired
    private UnitOfMeasurementService unitOfMeasurementService;
    
    @Autowired
    private BuildJsonResponseUtil jsonUtil;

    @PostMapping
    public ResponseEntity<UnitOfMeasurementDTO> createUnitOfMeasurement(@RequestBody UnitOfMeasurementDTO unitOfMeasurementDTO) {
        UnitOfMeasurementDTO createdUnit = unitOfMeasurementService.saveUnitOfMeasurement(unitOfMeasurementDTO);
        return ResponseEntity.ok(createdUnit);
    }

    @GetMapping
    public ResponseEntity<String> getAllUnitsOfMeasurement() throws JsonProcessingException {
        List<UnitOfMeasurementDTO> units = unitOfMeasurementService.getAllUnitsOfMeasurement();
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(jsonUtil.buildJsonResponse(units)).toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitOfMeasurementDTO> getUnitOfMeasurementById(@PathVariable("id") Integer unitId) {
        UnitOfMeasurementDTO unit = unitOfMeasurementService.getUnitOfMeasurementById(unitId);
        if (unit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(unit);
    }
}