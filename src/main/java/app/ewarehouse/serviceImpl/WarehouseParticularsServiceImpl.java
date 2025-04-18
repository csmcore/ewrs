package app.ewarehouse.serviceImpl;

import app.ewarehouse.dto.WarehouseParticularsResponse;
import app.ewarehouse.entity.WarehouseParticulars;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.WarehouseParticularsRepository;
import app.ewarehouse.service.WarehouseParticularsService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WarehouseParticularsServiceImpl implements WarehouseParticularsService {

    @Autowired
    private WarehouseParticularsRepository warehouseParticularsRepository;
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper om;
    private static final Logger logger = LoggerFactory.getLogger(WarehouseParticularsServiceImpl.class);
    @Override
    public String save(String data) {
        logger.info("Inside save method of WarehouseParticularsServiceImpl");

        String decodedData = CommonUtil.inputStreamDecoder(data);
        WarehouseParticulars warehouseParticulars;

        try {
            warehouseParticulars = om.readValue(decodedData, WarehouseParticulars.class);
        } catch (Exception e) {
            throw new CustomGeneralException("Invalid data format");
        }

        Set<ConstraintViolation<WarehouseParticulars>> violations = validator.validate(warehouseParticulars);
        if (!violations.isEmpty()) {
            throw new CustomGeneralException(violations);
        }

        return warehouseParticularsRepository.save(warehouseParticulars).getIntWareHouseParticularsId();
    }

    @Override
    public WarehouseParticularsResponse getById(String id) {
        logger.info("Inside getById method of WarehouseParticularsServiceImpl");
        WarehouseParticulars warehouseParticulars = warehouseParticularsRepository.findByintWareHouseParticularsIdAndBitDeletedFlag(id, false);

        return Mapper.mapToWarehouseParticularsResponse(warehouseParticulars);
    }

    @Override
    public List<WarehouseParticularsResponse> getAll() {
        logger.info("Inside getAll method of WarehouseParticularsServiceImpl");
        List<WarehouseParticulars> warehouseParticularsList = warehouseParticularsRepository.findAllByBitDeletedFlag(false);
        return warehouseParticularsList.stream()
                .map(Mapper::mapToWarehouseParticularsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<WarehouseParticularsResponse> getAll(Pageable pageable) {
        logger.info("Inside getAll paged method of WarehouseParticularsServiceImpl");
        Page<WarehouseParticulars> warehouseParticularsPage = warehouseParticularsRepository.findAllByBitDeletedFlag(false, pageable);

        List<WarehouseParticularsResponse> warehouseParticularsResponses = warehouseParticularsPage.getContent().stream()
                .map(Mapper::mapToWarehouseParticularsResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(warehouseParticularsResponses, pageable, warehouseParticularsPage.getTotalElements());
    }
}
