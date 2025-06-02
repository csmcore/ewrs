package app.ewarehouse.service;

import app.ewarehouse.dto.RoutineComplianceDTO;
import app.ewarehouse.entity.Status;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import app.ewarehouse.entity.TsuspensionOfCertificateOfCompliance;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TsuspensionOfCertificateOfComplianceService {

	List<TsuspensionOfCertificateOfCompliance> getAllSuspensions();

	Page<TsuspensionOfCertificateOfCompliance> getAllSuspensions(int page, int size);

	TsuspensionOfCertificateOfCompliance createSuspension(String complaintsForm) throws JsonMappingException, JsonProcessingException;

	TsuspensionOfCertificateOfCompliance getSuspensionByComplaintNumber(String id);

    boolean isContactNumberUnique(String contactNumber);

	Page<TsuspensionOfCertificateOfCompliance> findByFilters(Integer intCurrentRole, Status status, String search, String sortColumn, String sortDirection, Pageable pageable);

	public String takeAction(String data);
}
