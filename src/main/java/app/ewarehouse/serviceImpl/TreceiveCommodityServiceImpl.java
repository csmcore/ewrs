package app.ewarehouse.serviceImpl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.receiveCommodityResponse;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.TreceiveCommodity;
import app.ewarehouse.entity.TwarehouseReceipt;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.TreceiveCommodityRepository;
import app.ewarehouse.repository.TwarehouseReceiptRepository;
import app.ewarehouse.service.TreceiveCommodityService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;
import app.ewarehouse.util.Mapper;
import jakarta.validation.Validator;

@Service
public class TreceiveCommodityServiceImpl implements TreceiveCommodityService {

    @Autowired
    TreceiveCommodityRepository repo;
    @Autowired
    TwarehouseReceiptRepository warehouseRepo;
    @Autowired
    private Validator validator;
    @Autowired
    ErrorMessages errorMessages;
    @Autowired
    private ObjectMapper om;



    private static final Logger logger = LoggerFactory.getLogger(TreceiveCommodityServiceImpl.class);


    @Override
    @Transactional
    public String save(String commodities) throws JsonProcessingException {
        try {
            logger.info("inside t receive commodity");
            String decodedData = CommonUtil.inputStreamDecoder(commodities);

            TreceiveCommodity treceiveCommodity = om.readValue(decodedData, TreceiveCommodity.class);
            TreceiveCommodity existingCommodity = repo.findByTxtReceiveCIdAndBitDeleteFlag(treceiveCommodity.getTxtReceiveCId(),false);
            if (existingCommodity != null) {
                existingCommodity.setSeasonality(treceiveCommodity.getSeasonality());
                existingCommodity.setCommodity(treceiveCommodity.getCommodity());
                existingCommodity.setTxtGrade(treceiveCommodity.getTxtGrade());
                existingCommodity.setIntQuantity(treceiveCommodity.getIntQuantity());
                existingCommodity.setTxtLotNo(treceiveCommodity.getTxtLotNo());

                TwarehouseReceipt existingWarehouseReceipt = warehouseRepo.findByReceiveCommodity_TxtReceiveCIdAndBitDeleteFlag(existingCommodity.getTxtReceiveCId(),false);
                if (existingWarehouseReceipt != null) {
                    existingWarehouseReceipt.setReceiveCommodity(existingCommodity);
                    existingWarehouseReceipt.setStmUpdatedAt(new Date());
                    warehouseRepo.save(existingWarehouseReceipt);
                }

                repo.save(existingCommodity);
                return existingCommodity.getTxtReceiveCId();
            }else{

                TreceiveCommodity savedCommodity = repo.save(treceiveCommodity);

                TwarehouseReceipt twarehouseReceipt = new TwarehouseReceipt();
                twarehouseReceipt.setReceiveCommodity(savedCommodity);
                twarehouseReceipt.setDtmCreatedOn(new Date());
                warehouseRepo.save(twarehouseReceipt);
                return savedCommodity.getTxtReceiveCId();
            }
        }catch (DataIntegrityViolationException e){
            logger.error("Data integrity violation: " + e.getMessage());
            throw new CustomGeneralException(errorMessages.getDepositorNonExistent());
        }
        catch (JsonProcessingException e) {
            logger.error("Json Processing violation: "+e);
            throw new CustomGeneralException(errorMessages.getUnknownError());
        }
        catch (Exception e){
            logger.error("General exception:"+e);
            throw new CustomGeneralException(errorMessages.getInternalServerError());
        }

    }

    @Override
    public receiveCommodityResponse getCommodityByReceiveId(String txtReceiveCId) {
        TreceiveCommodity commodity = repo.findByTxtReceiveCIdAndBitDeleteFlag(txtReceiveCId,false);
        if (commodity == null) {
            throw new CustomGeneralException(errorMessages.getFailedToFetch() +" "+txtReceiveCId);
        } else if (commodity.getStatus()!=Status.Pending) {
            throw new CustomGeneralException(errorMessages.getAlreadyValidated() +" "+ commodity.getStatus());
        }
        return Mapper.mapToReceiveCommodityResponse(commodity);
    }

    @Override
    public List<TreceiveCommodity> findAll() {
        List<TreceiveCommodity> activeCommodities = repo.findAllByBitDeleteFlag(false);
        return activeCommodities != null ? activeCommodities : Collections.emptyList();
    }

    @Override
    public Page<receiveCommodityResponse> getFilteredCommodities(String search,
    		String sortColumn, String sortDirection, Pageable pageable, String warehouseId) {
        logger.info("Inside getFilteredReceipts method of TwarehouseReceiptServiceImpl");

        Page<TreceiveCommodity> response = repo.findByFilters(search, pageable, warehouseId);
        List<receiveCommodityResponse> receiveResponse = response.getContent().stream()
                .map(Mapper::mapToReceiveCommodityResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(receiveResponse, pageable, response.getTotalElements());
    }
}
