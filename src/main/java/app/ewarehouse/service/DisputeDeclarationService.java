package app.ewarehouse.service;

import app.ewarehouse.dto.BuyerDepositorResponse;
import app.ewarehouse.dto.DisputeDeclarationResponse;
import app.ewarehouse.dto.RoutineComplianceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface DisputeDeclarationService {
    String create (String data);
    DisputeDeclarationResponse getById(String Id);
    List<DisputeDeclarationResponse> getAll();
    Page<DisputeDeclarationResponse> getAll(Pageable pageable);
    Page<DisputeDeclarationResponse> findByFilters(Integer roleId, Date fromDate, Date toDate, String search, String sortColumn, String sortDirection, Integer userId, Integer action, Pageable pageable);
    String takeAction(String data);
}
