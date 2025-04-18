package app.ewarehouse.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.entity.TcalculatedWarehouseCharges;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.TcalculatedWarehouseChargesRepository;
import app.ewarehouse.service.TcalculatedWarehouseChargesService;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.ErrorMessages;

@Service
public class TcalculatedWarehouseChargesServiceImpl implements TcalculatedWarehouseChargesService {

    private static final Logger logger = LoggerFactory.getLogger(TcalculatedWarehouseChargesServiceImpl.class);
    private final TcalculatedWarehouseChargesRepository tcalculatedWarehouseChargesRepository;
    private final ObjectMapper om;
    private final ErrorMessages errorMessages;

    public TcalculatedWarehouseChargesServiceImpl(TcalculatedWarehouseChargesRepository tcalculatedWarehouseChargesRepository,
                                                  ErrorMessages errorMessages,ObjectMapper om) {
        this.tcalculatedWarehouseChargesRepository = tcalculatedWarehouseChargesRepository;
        this.errorMessages = errorMessages;
        this.om = om;
    }

    @Override
    public TcalculatedWarehouseCharges save(String data) {
        try {
            String decodedData = CommonUtil.inputStreamDecoder(data);
            logger.info("decoded data is: {} ", decodedData);
            TcalculatedWarehouseCharges twarehouseCharges = om.readValue(decodedData, TcalculatedWarehouseCharges.class);

            String warehouseReceipt = twarehouseCharges.getWarehouseReceiptNo();
            TcalculatedWarehouseCharges calculatedCharges = tcalculatedWarehouseChargesRepository.findByWarehouseReceiptNoAndBitDeleteFlag(warehouseReceipt,false);
            if (calculatedCharges!=null){
                calculatedCharges.setTotalAmount(twarehouseCharges.getTotalAmount());
                calculatedCharges.setTotalDays(twarehouseCharges.getTotalDays());
                return  tcalculatedWarehouseChargesRepository.save(calculatedCharges);
            }else{
                return  tcalculatedWarehouseChargesRepository.save(twarehouseCharges);
            }
        }catch (JsonProcessingException e){
            throw new CustomGeneralException(errorMessages.getUnknownError());
        }
    }

    @Override
    public String getChargesForWarehouseReceipt(String id) {
        try{
            TcalculatedWarehouseCharges calculatedCharges = tcalculatedWarehouseChargesRepository.findByWarehouseReceiptNoAndBitDeleteFlag(id,false);
            if (calculatedCharges == null){
                throw new CustomGeneralException(errorMessages.getNoChargesCalculator());
            }
            return calculatedCharges.getPaymentStatus().name();
        }catch (DataIntegrityViolationException e){
            throw new CustomGeneralException(errorMessages.getNoChargesCalculator());
        }
    }
}
