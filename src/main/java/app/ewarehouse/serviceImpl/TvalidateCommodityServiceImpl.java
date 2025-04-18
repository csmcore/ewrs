package app.ewarehouse.serviceImpl;

import app.ewarehouse.entity.TreceiveCommodity;
import app.ewarehouse.entity.TvalidateCommodity;
import app.ewarehouse.entity.TwarehouseReceipt;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.TreceiveCommodityRepository;
import app.ewarehouse.repository.TvalidateCommodityRepository;
import app.ewarehouse.repository.TwarehouseReceiptRepository;
import app.ewarehouse.service.TvalidateCommodityService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TvalidateCommodityServiceImpl implements TvalidateCommodityService {

    @Autowired
    TvalidateCommodityRepository repo;

    @Autowired
    TreceiveCommodityRepository receiveRepo;

    @Autowired
    TwarehouseReceiptRepository warehouseRepo;

    @Autowired
    ErrorMessages errorMessages;
    
    @Autowired
    private ObjectMapper om;


    private static final Logger logger = LoggerFactory.getLogger(TvalidateCommodity.class);

    @Override
    public List<TvalidateCommodity> findAll() {
        List<TvalidateCommodity> activeCommodities = repo.findAllByBitDeleteFlag( false);
        return activeCommodities != null ? activeCommodities : Collections.emptyList();
    }

    @Transactional
    @Override
    public String save(String data) {
        try {
            String decodedData = CommonUtil.inputStreamDecoder(data);
            logger.info("decoded data is "+decodedData);
            TvalidateCommodity tvalidateCommodity = om.readValue(decodedData, TvalidateCommodity.class);

            TreceiveCommodity treceiveCommodity = receiveRepo.findByTxtReceiveCIdAndBitDeleteFlag(tvalidateCommodity.getReceiveCommodity().getTxtReceiveCId(),false);
            if(treceiveCommodity!=null){
                treceiveCommodity.setStatus(tvalidateCommodity.getStatus());
                receiveRepo.save(treceiveCommodity);
            }else {
                throw new CustomGeneralException(errorMessages.getFailedToFetch() + tvalidateCommodity.getReceiveCommodity().getTxtReceiveCId());
            }

            repo.save(tvalidateCommodity);
            return tvalidateCommodity.getTxtValidateId();
        }catch (DataIntegrityViolationException e){
            throw new CustomGeneralException(errorMessages.getValidCommodityId());
        }
        catch (JsonProcessingException e){
            throw new CustomGeneralException(errorMessages.getUnknownError());
        }
    }

    @Override
    public Page<TvalidateCommodity> getAllCommodities(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Optional.ofNullable(repo.findAllByBitDeleteFlagOrderByDtmCreatedAtDesc(false, pageable)).orElse(Page.empty(pageable));

    }

    @Override
    public TvalidateCommodity getCommodityByValidateId(String txtValidateId) {
        TvalidateCommodity commodity = repo.findAllByTxtValidateIdAndBitDeleteFlag(txtValidateId,false);
        if (commodity == null) {
            throw new EntityNotFoundException(errorMessages.getFailedToFetch() + txtValidateId );
        }
        return commodity;
    }
}
