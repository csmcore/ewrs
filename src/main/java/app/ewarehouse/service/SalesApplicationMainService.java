package app.ewarehouse.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;

import app.ewarehouse.dto.ReceiptDealFinalDto;
import app.ewarehouse.dto.SalesApplicationListResponse;
import app.ewarehouse.dto.SalesApplicationMainResponse;
import app.ewarehouse.entity.SalesApplicationMain;
import app.ewarehouse.entity.SalesApplicationTabTwo;

public interface SalesApplicationMainService {

     SalesApplicationTabTwo saveTwo(String salesApplication);

     String saveOne(String salesApplicationTabOne) throws JsonProcessingException;

     String saveThree(String salesApplicationTabThree)throws JsonProcessingException;

    SalesApplicationMain getApplicationInProgress();

    SalesApplicationMainResponse getSalesReceiptDetails(String applicationId);

     SalesApplicationMain getSalesApplicationsByStatus();
    
    ReceiptDealFinalDto getDealDetails(String receiptNO);

    ReceiptDealFinalDto checkReceiptPayment(String receiptNO);
    
    Page<SalesApplicationListResponse> getAllSalesApplicationList(Pageable pageable,String search);

}
