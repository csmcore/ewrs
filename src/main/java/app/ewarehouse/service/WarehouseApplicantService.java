package app.ewarehouse.service;

import app.ewarehouse.dto.WarehouseApplicantResponse;
import app.ewarehouse.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface WarehouseApplicantService {
    String save(String warehouseApplicant);
    WarehouseApplicantResponse getById(String id);
    List<WarehouseApplicantResponse> getAll();
    Page<WarehouseApplicantResponse> getAll(Pageable pageable);
    String deleteById(String id);
    Page<WarehouseApplicantResponse> getFilteredApplicants(Date fromDate, Date toDate, Status status, Pageable pageable);
	void giveWareHouseRemarks(String data);
}
