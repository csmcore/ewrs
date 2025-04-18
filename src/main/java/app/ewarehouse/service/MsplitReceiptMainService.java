package app.ewarehouse.service;

import app.ewarehouse.dto.MsplitReceiptResponse;
import app.ewarehouse.dto.TsplitReceiptResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MsplitReceiptMainService {
    List<MsplitReceiptResponse> findAll();

    String save(String data);

    TsplitReceiptResponse getReceiptBySplitReceiptId(String id);

    Page<TsplitReceiptResponse> getFilteredReceipts(String search, String sortColumn, String sortDirection, Pageable sortedPageable);

    boolean checkIfSplitReceiptNoExists(String id);
}
