package app.ewarehouse.serviceImpl;

import app.ewarehouse.entity.LegalStatus;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.LegalStatusRepository;
import app.ewarehouse.service.LegalStatusService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LegalStatusServiceImpl implements LegalStatusService {
    @Autowired
    private LegalStatusRepository legalStatusRepository;
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper om;
    private static final Logger logger = LoggerFactory.getLogger(LegalStatusServiceImpl.class);

    @Override
    public Integer save(String data) {
        logger.info("Inside save method of LegalStatusServiceImpl");

        String decodedData = CommonUtil.inputStreamDecoder(data);
        LegalStatus legalStatus;

        try {
            legalStatus = om.readValue(decodedData, LegalStatus.class);
        } catch (Exception e) {
            throw new CustomGeneralException("Invalid data format: " + e);
        }

        Set<ConstraintViolation<LegalStatus>> violations = validator.validate(legalStatus);
        if (!violations.isEmpty()) {
            throw new CustomGeneralException(violations);
        }

        return legalStatusRepository.save(legalStatus).getIntId();
    }

    @Override
    public LegalStatus getById(Integer id) {
        logger.info("Inside getById method of LegalStatusServiceImpl");

        return legalStatusRepository.findByIntIdAndBitDeletedFlag(id, false);
    }

    @Override
    public List<LegalStatus> getAll() {
        logger.info("Inside getAll method of LegalStatusServiceImpl");
        return legalStatusRepository.findAllByBitDeletedFlag(false);
    }
}
