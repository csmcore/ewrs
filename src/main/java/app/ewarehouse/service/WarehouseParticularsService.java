package app.ewarehouse.service;

import app.ewarehouse.dto.WarehouseParticularsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WarehouseParticularsService {
    String save(String warehouseParticulars);
    WarehouseParticularsResponse getById(String Id);
    List<WarehouseParticularsResponse> getAll();
    Page<WarehouseParticularsResponse> getAll(Pageable pageable);
}
