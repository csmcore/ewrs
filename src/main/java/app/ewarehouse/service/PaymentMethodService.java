package app.ewarehouse.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import app.ewarehouse.dto.PaymentStatusViewResponseDTO;

public interface PaymentMethodService {
    JSONObject save(String paymentMethod);
    JSONObject getById(Integer Id);
    JSONArray getAll(String formParams);

    JSONArray getAllActive(String formParams);

    JSONObject deleteById(Integer id);
    
    Page<PaymentStatusViewResponseDTO> getAllPaymentTypes(Integer pageNumber, Integer pageSize, String sortCol);
}
