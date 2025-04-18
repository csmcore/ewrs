package app.ewarehouse.service;

import app.ewarehouse.entity.TrevocationOfCertificateOfCompliance;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TrevocationOfCertificateOfComplianceService {
    Page<TrevocationOfCertificateOfCompliance> getAllSuspensions(int page, int size);

    TrevocationOfCertificateOfCompliance createSuspension(String complaintFormDto) throws JsonProcessingException;

    TrevocationOfCertificateOfCompliance getSuspensionByComplaintNumber(String complaintNumber);

    List<TrevocationOfCertificateOfCompliance> findAll();
}
