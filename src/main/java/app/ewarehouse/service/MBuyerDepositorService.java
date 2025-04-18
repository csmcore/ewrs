package app.ewarehouse.service;

import app.ewarehouse.dto.MBuyerDepositorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MBuyerDepositorService {
    String save(String mBuyerDepositor);
    MBuyerDepositorDTO getById(String Id);
    List<MBuyerDepositorDTO> getAll();
    Page<MBuyerDepositorDTO> getAll(Pageable pageable);
}
