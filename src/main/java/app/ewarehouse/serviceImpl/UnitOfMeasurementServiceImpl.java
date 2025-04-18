package app.ewarehouse.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.UnitOfMeasurementDTO;
import app.ewarehouse.entity.UnitOfMeasurement;
import app.ewarehouse.repository.UnitOfMeasurementRepository;
import app.ewarehouse.service.UnitOfMeasurementService;

@Service
public class UnitOfMeasurementServiceImpl implements UnitOfMeasurementService {

    @Autowired
    private UnitOfMeasurementRepository unitOfMeasurementRepository;

    @Override
    public UnitOfMeasurementDTO saveUnitOfMeasurement(UnitOfMeasurementDTO unitOfMeasurementDTO) {
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
        BeanUtils.copyProperties(unitOfMeasurementDTO, unitOfMeasurement);
        unitOfMeasurement = unitOfMeasurementRepository.save(unitOfMeasurement);
        BeanUtils.copyProperties(unitOfMeasurement, unitOfMeasurementDTO);
        return unitOfMeasurementDTO;
    }

    @Override
    public List<UnitOfMeasurementDTO> getAllUnitsOfMeasurement() {
        return unitOfMeasurementRepository.findAll().stream()
                .map(unit -> {
                    UnitOfMeasurementDTO dto = new UnitOfMeasurementDTO();
                    BeanUtils.copyProperties(unit, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UnitOfMeasurementDTO getUnitOfMeasurementById(Integer unitId) {
        UnitOfMeasurement unit = unitOfMeasurementRepository.findById(unitId).orElse(null);
        if (unit == null) {
            return null;
        }
        UnitOfMeasurementDTO dto = new UnitOfMeasurementDTO();
        BeanUtils.copyProperties(unit, dto);
        return dto;
    }
}
