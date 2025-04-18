package app.ewarehouse.service;

import app.ewarehouse.dto.receiveCommodityResponse;
import app.ewarehouse.entity.TreceiveCommodity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TreceiveCommodityService {


    String save(String complaintFormDto) throws JsonProcessingException;

    receiveCommodityResponse getCommodityByReceiveId(String complaintNumber);

    List<TreceiveCommodity> findAll() ;

    Page<receiveCommodityResponse> getFilteredCommodities(String search,
    		String sortColumn, String sortDirection, Pageable sortedPageable, String warehouseId);
}
