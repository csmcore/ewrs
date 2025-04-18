package app.ewarehouse.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.ewarehouse.dto.ResponseDTO;
import app.ewarehouse.service.DashboardService;
import app.ewarehouse.util.CommonUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DashboardController {
	
	/**
	 * 
	 * @author chinmaya.jena
	 * @since 16-10-2024
	 * 
	 *  */

	private final DashboardService service;
	private final ObjectMapper objectMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	
	@GetMapping("/quantity-commodity")
    ResponseEntity<String> getQuantityAgainstCommodity() throws JsonProcessingException {
		JSONObject response = service.getQuantityAgainstCommodity();
        logger.info("inside getQuantityAgainstCommodity details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	@GetMapping("/quantity-seasonwise")
    ResponseEntity<String> getQuantityAgainstSeasonwise() throws JsonProcessingException {
		JSONObject response = service.getQuantityAgainstSeasonwise();
        logger.info("inside getQuantityAgainstSeasonwise details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	@GetMapping("/quantity-monthwise")
    ResponseEntity<String> getQuantityAgainstMonthwise() throws JsonProcessingException {
		JSONObject response = service.getQuantityAgainstMonthwise();
        logger.info("inside getQuantityAgainstMonthwise details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	@GetMapping("/quantity-countywise")
    ResponseEntity<String> getQuantityAgainstCountywise() throws JsonProcessingException {
		JSONObject response = service.getQuantityAgainstCountywise();
        logger.info("inside getQuantityAgainstCountywise details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	@GetMapping("/receipt-status")
    ResponseEntity<String> getReceiptStatus() throws JsonProcessingException {
		JSONObject response = service.getReceiptStatus();
        logger.info("inside getReceiptStatus details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	@GetMapping("/pledge-receipt-status")
    ResponseEntity<String> getPledgeReceiptStatus() throws JsonProcessingException {
		JSONObject response = service.getPledgeReceiptStatus();
        logger.info("inside getPledgeReceiptStatus details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	@GetMapping("/receipt-monthwise")
    ResponseEntity<String> getReceiptAgainstMonthwise() throws JsonProcessingException {
		JSONObject response = service.getReceiptAgainstMonthwise();
        logger.info("inside getReceiptAgainstMonthwise details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	
	@GetMapping("/receipt-commodity")
    ResponseEntity<String> getReceiptAgainstCommodity() throws JsonProcessingException {
		JSONObject response = service.getReceiptAgainstCommodity();
        logger.info("inside getReceiptAgainstCommodity details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	@GetMapping("/receipt-countywise")
    ResponseEntity<String> getReceiptAgainstCountywise() throws JsonProcessingException {
		JSONObject response = service.getReceiptAgainstCountywise();
        logger.info("inside getReceiptAgainstCountywise details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	@GetMapping("/sold-receipt-monthwise")
    ResponseEntity<String> getSoldReceiptAgainstMonthwise() throws JsonProcessingException {
		JSONObject response = service.getSoldReceiptAgainstMonthwise();
        logger.info("inside getSoldReceiptAgainstMonthwise details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	@GetMapping("/sold-receipt-countywise")
    ResponseEntity<String> getSoldReceiptAgainstCountywise() throws JsonProcessingException {
		JSONObject response = service.getSoldReceiptAgainstCountywise();
        logger.info("inside getSoldReceiptAgainstCountywise details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	@GetMapping("/total-price-countywise")
    ResponseEntity<String> getTotalPriceAgainstCountywise() throws JsonProcessingException {
		JSONObject response = service.getTotalPriceAgainstCountywise();
        logger.info("inside getTotalPriceAgainstCountywise details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	@GetMapping("/total-price-commoditywise")
    ResponseEntity<String> getTotalPriceAgainstCommoditywise() throws JsonProcessingException {
		JSONObject response = service.getTotalPriceAgainstCommoditywise();
        logger.info("inside getTotalPriceAgainstCommoditywise details are: {}", response);
        return ResponseEntity.ok(CommonUtil.inputStreamEncoder(buildJsonResponse(response)).toString());
    }
	
	
	
	
	
	
	private <T> String buildJsonResponse(T response) throws JsonProcessingException {
		Object result;
		if (response instanceof JSONObject) {
			result = response.toString();
		} else {
			result = response;
		}

		return objectMapper.writeValueAsString(ResponseDTO.builder().status(200).result(result).build());
	}
}
