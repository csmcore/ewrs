package app.ewarehouse.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.PaymentStatusViewResponseDTO;
import app.ewarehouse.entity.PaymentMethod;
import app.ewarehouse.exception.CustomGeneralException;
import app.ewarehouse.repository.PaymentMethodRepository;
import app.ewarehouse.service.PaymentMethodService;
import app.ewarehouse.util.Mapper;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private ObjectMapper om;
    Integer parentId = 0;
    private static final Logger logger = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);

    @Override
    public JSONObject save(String data) {
        logger.info("Inside save method of PaymentMethodServiceImpl");
        JSONObject json = new JSONObject();
        try {
            PaymentMethod paymentMethod = om.readValue(data, PaymentMethod.class);
            if (!Objects.isNull(paymentMethod.getIntId()) && paymentMethod.getIntId() > 0) {
            	PaymentMethod pmData = paymentMethodRepository.findByIntId(paymentMethod.getIntId());
            	paymentMethod.setTxtPaymentMethod(paymentMethod.getTxtPaymentMethod().trim());
                paymentMethod.setTxtDescription(paymentMethod.getTxtDescription().trim());
                paymentMethod.setStmUpdatedOn(new Date());
                paymentMethod.setBitDeletedFlag(pmData.getBitDeletedFlag());
                json.put("status", 202);
            } else {
            	paymentMethod.setTxtPaymentMethod(paymentMethod.getTxtPaymentMethod().trim());
                paymentMethod.setTxtDescription(paymentMethod.getTxtDescription().trim());
                json.put("status", 200);
            }

            PaymentMethod saveData = paymentMethodRepository.save(paymentMethod);
            parentId = saveData.getIntId();


            json.put("id", parentId);
        }
        catch (DataIntegrityViolationException e){
            logger.error("Inside save method of PaymentMethodServiceImpl some error occur:" + e.getMessage());
            json.put("status", 401);
        }
        catch (Exception e) {
            logger.error("Inside save method of PaymentMethodServiceImpl some error occur:" + e.getMessage());
            json.put("status", 400);
        }
        return json;
    }

    @Override
    public JSONObject getById(Integer id) {
        logger.info("Inside getById method of PaymentMethodServiceImpl");
        //PaymentMethod entity = paymentMethodRepository.findByIntIdAndBitDeletedFlag(id, false);
        PaymentMethod entity = paymentMethodRepository.findByIntId(id);
        return new JSONObject(entity);
    }

    @Override
    public JSONArray getAll(String formParams) {
        logger.info("Inside getAll method of PaymentMethodServiceImpl");
        List<PaymentMethod> tPaymentMethodResp = paymentMethodRepository.findAll();
        return new JSONArray(tPaymentMethodResp);
    }

    @Override
    public JSONArray getAllActive(String formParams) {
        logger.info("Inside getAll method of PaymentMethodServiceImpl");
        List<PaymentMethod> tPaymentMethodResp = paymentMethodRepository.findAllByBitDeletedFlag(false);
        return new JSONArray(tPaymentMethodResp);
    }

    @Override
    public JSONObject deleteById(Integer id) {
        logger.info("Inside deleteById method of PaymentMethodServiceImpl");
        JSONObject json = new JSONObject();
        try {
            PaymentMethod entity = paymentMethodRepository.findById(id).orElseThrow(() -> new CustomGeneralException("Entity not found"));
            entity.setBitDeletedFlag(!entity.getBitDeletedFlag());
            paymentMethodRepository.save(entity);
            json.put("status", 200);
        } catch (Exception e) {
            logger.error("Inside deleteById method of PaymentMethodServiceImpl some error occur:" + e);
            json.put("status", 400);
        }
        return json;
    }

	@Override
	@Transactional(readOnly = true)
	public Page<PaymentStatusViewResponseDTO> getAllPaymentTypes(Integer pageNumber, Integer pageSize, String search) {
		logger.info("Inside getAllPaymentTypes paginated method of PaymentMethodServiceImpl");
        try {

            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            Page<PaymentMethod> page;
            if (StringUtils.hasText(search)) {
                page = paymentMethodRepository.findByFilters(search, pageable);
            } else {
                page = paymentMethodRepository.getAllPaymentTypes(pageable);
            }

            List<PaymentStatusViewResponseDTO> responses = page.getContent()
                    .stream()
                    .map(Mapper::mapToPaymentTypeViewResponse)
                    .toList();
            return new PageImpl<>(responses, pageable, page.getTotalElements());
        } catch (Exception e) {
            logger.info("Error occurred in getAllPaymentTypes paginated method of PaymentMethodServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
	}

}
