package app.ewarehouse.serviceImpl;

import app.ewarehouse.dto.MBuyerDepositorDTO;
import app.ewarehouse.entity.MBuyerDepositor;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.mapper.MBuyerDepositorMapper;
import app.ewarehouse.repository.MBuyerDepositorRepository;
import app.ewarehouse.service.MBuyerDepositorService;
import app.ewarehouse.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MBuyerDepositorServiceImpl implements MBuyerDepositorService {
    @Autowired
    private MBuyerDepositorRepository repository;
    @Autowired
    ErrorMessages errorMessages;
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper om;

    @Override
    public String save(String data) {
        log.info("Inside save method of MBuyerDepositorServiceImpl");

        String decodedData = CommonUtil.inputStreamDecoder(data);
        MBuyerDepositor buyer;

        try {
            buyer = om.readValue(decodedData, MBuyerDepositor.class);

            Set<ConstraintViolation<MBuyerDepositor>> violations = validator.validate(buyer);
            if (!violations.isEmpty()) {
                log.error("Inside save method of MBuyerDepositorServiceImpl Validation errors: " + violations);
                throw new CustomGeneralException(violations);
            }

            buyer = repository.save(buyer);
        }
        catch (DataIntegrityViolationException exception) {
            log.error("Inside save method of MBuyerDepositorServiceImpl some error occur:" + exception.getMessage());
            String msg = exception.getMessage();
            if (msg.contains("'vchApplicationId'")) {
                throw new CustomGeneralException(errorMessages.getBuyerApplicationExists());
            }
            throw new CustomGeneralException(errorMessages.getGeneralDuplicateRecords());
        }
        catch (CustomGeneralException exception) {
            log.error("Inside save method of MBuyerDepositorServiceImpl some error occur:" + exception.getMessage());
            throw exception;
        }
        catch (Exception e) {
            log.error("Inside save method of MBuyerDepositorServiceImpl some error occur:" + e.getMessage());
            throw new CustomGeneralException(errorMessages.getUnknownError());
        }

        return buyer.getVchEntityId();
    }


    @Override
    public MBuyerDepositorDTO getById(String id) {
        log.info("Inside getById method of MBuyerDepositorServiceImpl");
        MBuyerDepositor buyer = repository.findByVchEntityIdAndBitDeletedFlag(id, false);

        return MBuyerDepositorMapper.mapToDTO(buyer);
    }

    @Override
    public List<MBuyerDepositorDTO> getAll() {
        log.info("Inside getAll method of MBuyerDepositorServiceImpl");
        List<MBuyerDepositor> buyersList = repository.findAllByBitDeletedFlagOrderByDtmCreatedOnDesc(false);
        return buyersList.stream()
                .map(MBuyerDepositorMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MBuyerDepositorDTO> getAll(Pageable pageable) {
        log.info("Inside getAll pageable method of MBuyerDepositorServiceImpl");
        Page<MBuyerDepositor> buyersPage = repository.findAllByBitDeletedFlagOrderByDtmCreatedOnDesc(false, pageable);

        List<MBuyerDepositorDTO> buyerResponses = buyersPage.getContent().stream()
                .map(MBuyerDepositorMapper::mapToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(buyerResponses, pageable, buyersPage.getTotalElements());
    }
}
