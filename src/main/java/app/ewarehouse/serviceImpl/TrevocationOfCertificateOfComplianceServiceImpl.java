package app.ewarehouse.serviceImpl;

import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TrevocationOfCertificateOfCompliance;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.TrevocationOfCertificateOfComplianceRepository;
import app.ewarehouse.service.TrevocationOfCertificateOfComplianceService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.FolderAndDirectoryConstant;
import app.ewarehouse.util.JsonFileExtractorUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TrevocationOfCertificateOfComplianceServiceImpl implements TrevocationOfCertificateOfComplianceService {

    @Autowired
    TrevocationOfCertificateOfComplianceRepository repo;
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper om;
    private static final Logger logger = LoggerFactory.getLogger(TrevocationOfCertificateOfComplianceServiceImpl.class);

    @Autowired
    ErrorMessages errorMessages;
    @Override
    public Page<TrevocationOfCertificateOfCompliance> getAllSuspensions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findDataByIsDeleted(pageable);
    }

    @Override
    public TrevocationOfCertificateOfCompliance createSuspension(String complaintFormDto) throws JsonProcessingException {
        try {
            String decodedData = CommonUtil.inputStreamDecoder(complaintFormDto);
            TrevocationOfCertificateOfCompliance revocation  = om.readValue(decodedData, TrevocationOfCertificateOfCompliance.class);
            revocation.setStatus(Status.Pending);
            revocation.setSupportingDocument(JsonFileExtractorUtil.uploadFile(revocation.getSupportingDocument(), FolderAndDirectoryConstant.REVOCATION_COMPLIANCE));

            Set<ConstraintViolation<TrevocationOfCertificateOfCompliance>> violations = validator.validate(revocation);
            if (!violations.isEmpty()) {
                logger.error("Inside save method of BuyerServiceImpl Validation errors: " + violations);
                throw new CustomGeneralException(violations);
            }
            repo.save(revocation);
            return revocation;
        }catch (CustomGeneralException exception) {
            logger.error("Inside save method of TrevocationOfCertificateOfComplianceServiceImpl some error occur:" + exception.getMessage());
            throw exception;
        }
        catch (JsonProcessingException e) {
            logger.error("Inside save method of TrevocationOfCertificateOfComplianceServiceImpl some error occur:" + e.getMessage());
            throw new CustomGeneralException(errorMessages.getInternalServerError());
        }catch (Exception e){
            logger.error("Inside save method of TrevocationOfCertificateOfComplianceServiceImpl some error occur:" + e.getMessage());
            throw new CustomGeneralException(errorMessages.getUnknownError());
        }
    }

    @Override
    public TrevocationOfCertificateOfCompliance getSuspensionByComplaintNumber(String complaintNumber) {
        return repo.getSuspensionByComplaintNumber(complaintNumber);
    }

    @Override
    public List<TrevocationOfCertificateOfCompliance> findAll() {
        return repo.findAllByIsDeleted();
    }
}
