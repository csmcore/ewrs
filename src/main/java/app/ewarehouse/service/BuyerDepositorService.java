package app.ewarehouse.service;

import app.ewarehouse.dto.BuyerDepositorResponse;
import app.ewarehouse.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface BuyerDepositorService {
    String save(String buyer);

    BuyerDepositorResponse getById(String Id);

    BuyerDepositorResponse findByIdAndRegType(String id);

    List<BuyerDepositorResponse> getAll();

    Page<BuyerDepositorResponse> getAll(Pageable pageable);

    String deleteById(String id);

    String takeAction(String data);

    Page<BuyerDepositorResponse> getFilteredBuyers(Date fromDate, Date toDate, Status status, Pageable pageable);

    Page<BuyerDepositorResponse> getFilteredBuyers(Date fromDate, Date toDate, Status status, String search, String sortColumn,
                                                   String sortDirection, Pageable pageable);

    List<BuyerDepositorResponse> getAllDepositors();

    List<BuyerDepositorResponse> getAllBuyers();

    List<String> getDepositorById();

    List<String> getAllBuyersId();
    
    String checkDepostiroId(String id);
    
    List<String> getValidReceiptDepositorById(String id);

	List<BuyerDepositorResponse> getAllDepositorsPd();
    
    
}
