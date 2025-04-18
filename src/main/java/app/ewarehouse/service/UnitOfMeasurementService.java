package app.ewarehouse.service;

import java.util.List;

import app.ewarehouse.dto.UnitOfMeasurementDTO;

public interface UnitOfMeasurementService {
    UnitOfMeasurementDTO saveUnitOfMeasurement(UnitOfMeasurementDTO unitOfMeasurementDTO);
    List<UnitOfMeasurementDTO> getAllUnitsOfMeasurement();
    UnitOfMeasurementDTO getUnitOfMeasurementById(Integer unitId);
}
