package app.ewarehouse.service;

import org.springframework.data.domain.Page;

import app.ewarehouse.dto.AuditTrailDto;

public interface AuditTrailService {

	Page<AuditTrailDto> getAllAuditTrialList(Integer pageNumber, Integer pageSize, String sortCol, String sortDir, String search);

	String saveAuditTrail(String data);

}
