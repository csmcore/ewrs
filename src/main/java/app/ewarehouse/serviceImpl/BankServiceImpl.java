package app.ewarehouse.serviceImpl;

import app.ewarehouse.entity.Bank;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.BankRepository;
import app.ewarehouse.service.BankService;
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
public class BankServiceImpl implements BankService {
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private Validator validator;
    private static final Logger logger = LoggerFactory.getLogger(BankServiceImpl.class);

    @Override
    public Integer save(String data) {
        logger.info("Inside save method of BankServiceImpl");

        String decodedData = CommonUtil.inputStreamDecoder(data);
        Bank bank;

        try {
            bank = new ObjectMapper().readValue(decodedData, Bank.class);
        } catch (Exception e) {
            logger.error("Invalid data format: " + e);
            throw new CustomGeneralException("Invalid data format");
        }

        Set<ConstraintViolation<Bank>> violations = validator.validate(bank);
        if (!violations.isEmpty()) {
            throw new CustomGeneralException(violations);
        }

        return bankRepository.save(bank).getIntId();
    }

    @Override
    public Bank getById(Integer id) {
        logger.info("Inside getById method of BankServiceImpl");

        return bankRepository.findByIntIdAndBitDeletedFlag(id, false);
    }

    @Override
    public List<Bank> getAll() {
        logger.info("Inside getAll method of BankServiceImpl");
        return bankRepository.findAllByBitDeletedFlag(false);
    }
}
