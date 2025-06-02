package app.ewarehouse.service;

import java.util.Date;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.ewarehouse.dto.PaymentDataStringDto;
import app.ewarehouse.entity.IssuanceWareHouseRecipt;
import app.ewarehouse.entity.IssuanceWarehouseReceiptActionHistory;
import app.ewarehouse.entity.PaymentData;
import app.ewarehouse.entity.PaymentStatus;
import app.ewarehouse.entity.Status;
import app.ewarehouse.entity.Tuser;
import app.ewarehouse.repository.IssuanceWareHouseReciptRepository;
import app.ewarehouse.repository.IssuanceWarehouseReceiptActionHistoryRepository;
import app.ewarehouse.repository.PaymentDataRepository;
import app.ewarehouse.repository.TuserRepository;
import app.ewarehouse.util.ApplicationPropertiesValue;
import app.ewarehouse.util.CommonUtil;
import app.ewarehouse.util.PaymentSecureHashGenerate;

@Service
public class PaymentService {
	@Autowired
	PaymentSecureHashGenerate  paymentSecureHashGenerate;
	@Autowired
	PaymentDataRepository paymentDataRepository;
	@Autowired
	IssuanceWareHouseReciptRepository issuanceWareHouseReciptRepository;
	
	@Autowired
	TuserRepository tuserRepository ;
	@Autowired
	IssuanceWarehouseReceiptActionHistoryRepository issuanceWarehouseReceiptActionHistoryRepository;
	
	private final ApplicationPropertiesValue applicationPropertiesValue;

    public PaymentService(ApplicationPropertiesValue applicationPropertiesValue) {
        this.applicationPropertiesValue = applicationPropertiesValue;
    }
	
	public String   secureHashData(JSONObject jsonData) {
		
		  String secretKey = applicationPropertiesValue.getSecretKey();
          String apiClientID = applicationPropertiesValue.getApiClientID();
          String serviceID = applicationPropertiesValue.getServiceID();
          if (jsonData == null) {
             throw new IllegalArgumentException("Input JSON data cannot be null");
          }
		String amountExpected=validateString(jsonData.get("amountExpected").toString());
		String clientIDNumber=validateString(jsonData.get("clientIDNumber").toString());
		String currency=validateString(jsonData.get("currency").toString());
		String billRefNumber=validateString(jsonData.get("billRefNumber").toString());
		String billDesc=validateString(jsonData.get("billDesc").toString());
		String clientName=validateString(jsonData.get("clientName").toString());
		
		
		PaymentDataStringDto   datadto=new PaymentDataStringDto(amountExpected,clientIDNumber,currency,billRefNumber,billDesc,clientName);
//		PaymentData paymentData=new PaymentData();
//		BeanUtils.copyProperties(datadto, paymentData);
//		paymentData.setDepositorId(jsonData.getString("depositorId"));
//		paymentData.setIntCreatedBy(jsonData.getInt("userId"));
//		paymentDataRepository.save(paymentData);
//		Integer issuanceId=jsonData.getInt("issuanceId");
//		IssuanceWareHouseRecipt wareHouseRecipt = issuanceWareHouseReciptRepository.findByIdAndIntDepositorWhOperatorAndBitDeletedFlag(jsonData.getString("depositorId"),issuanceId,false);
//        
//        if (wareHouseRecipt != null) {
//            wareHouseRecipt.setPaymentStatus(Status.Received);
//            issuanceWareHouseReciptRepository.save(wareHouseRecipt);
//        }
		
		String secureHas= paymentSecureHashGenerate.generateSignature(datadto);
		
		return secureHas;
	}
	
	 private String validateString(String value) {
	        if (value == null || value.isEmpty()) {
	            throw new IllegalArgumentException(value + " cannot be null or empty.");
	        }
	        return value;
	    }

	public String updatePaymentStatus(String data) {
		JSONObject response = new JSONObject();
		String decodedData = CommonUtil.inputStreamDecoder(data);
		try {
		JSONObject json = new JSONObject(decodedData);
		String clientIDNumber = json.getString("clientIDNumber");
		String billRefNumber = json.getString("billRefNumber");
		PaymentData entity = paymentDataRepository.findByBillRefNumberAndClientIDNumberAndBitDeletedFlagFalse(billRefNumber, clientIDNumber);
		entity.setEnmPaymentStatus(PaymentStatus.PAID);
		PaymentData savedData = paymentDataRepository.save(entity);
		response.put("status", 200);
		response.put("warehouseId", savedData.getDepositorId());
		response.put("paymentStatus", savedData.getEnmPaymentStatus());
		}catch(Exception e) {
			response.put("status", 500);
		}
		return response.toString();
	}
	
	
	public String paymentForDepositor(String data) {
		JSONObject response = new JSONObject();
		String decodedData = CommonUtil.inputStreamDecoder(data);
		try {
		JSONObject jsonData = new JSONObject(decodedData);
		
		String amountExpected=validateString(jsonData.get("amountExpected").toString());
		String clientIDNumber=validateString(jsonData.get("clientIDNumber").toString());
		String currency=validateString(jsonData.get("currency").toString());
		String billRefNumber=validateString(jsonData.get("billRefNumber").toString());
		String billDesc=validateString(jsonData.get("billDesc").toString());
		String clientName=validateString(jsonData.get("clientName").toString());
		boolean isRetired = jsonData.has("isretire") && "true".equalsIgnoreCase(jsonData.get("isretire").toString());
		PaymentDataStringDto   datadto=new PaymentDataStringDto(amountExpected,clientIDNumber,currency,billRefNumber,billDesc,clientName);
		
		PaymentData paymentData=new PaymentData();
		BeanUtils.copyProperties(datadto, paymentData);
		paymentData.setDepositorId(jsonData.getString("depositorId"));
		paymentData.setIntCreatedBy(jsonData.getInt("userId"));
		paymentDataRepository.save(paymentData);
		Integer issuanceId=jsonData.getInt("issuanceId");
		IssuanceWareHouseRecipt wareHouseRecipt = issuanceWareHouseReciptRepository.findByIdAndIntDepositorWhOperatorAndBitDeletedFlag(jsonData.getString("depositorId"),issuanceId,false);
        
        if (wareHouseRecipt != null) {
            wareHouseRecipt.setPaymentStatus(Status.Received);
            wareHouseRecipt.setIsRetired(isRetired);
            wareHouseRecipt.setStmUpdatedOn(new Date());
            issuanceWareHouseReciptRepository.save(wareHouseRecipt);
            
            // Step 2: Update existing ActionHistory record
            
            IssuanceWarehouseReceiptActionHistory actionHistory = new IssuanceWarehouseReceiptActionHistory();
            if(!isRetired) {
            actionHistory.setIntIssueanceWhId(wareHouseRecipt.getIssuanceWhId()); // or whatever field holds the PK
            actionHistory.setDepositorId(jsonData.getString("depositorId"));
            actionHistory.setVchActionTakenBy(jsonData.getString("userName"));
            actionHistory.setRoleId(jsonData.getInt("roleId"));
            actionHistory.setRoleName(jsonData.getString("roleName"));
            actionHistory.setUserId(jsonData.getInt("userId"));
            actionHistory.setVchStatus("Payment For Issue Certificate");
            }
            else {
            actionHistory.setIntIssueanceWhId(wareHouseRecipt.getIssuanceWhId()); // or whatever field holds the PK
            actionHistory.setDepositorId(jsonData.getString("depositorId"));
            actionHistory.setVchActionTakenBy(jsonData.getString("userName"));
            actionHistory.setRoleId(jsonData.getInt("roleId"));
            actionHistory.setRoleName(jsonData.getString("roleName"));
            actionHistory.setUserId(jsonData.getInt("userId"));
            actionHistory.setVchStatus("Payment Received & Retirement");
           }
            issuanceWarehouseReceiptActionHistoryRepository.save(actionHistory);
        }
        
       
		
//		String clientIDNumber = json.getString("clientIDNumber");
//		String billRefNumber = json.getString("billRefNumber");
//		PaymentData entity = paymentDataRepository.findByBillRefNumberAndClientIDNumberAndBitDeletedFlagFalse(billRefNumber, clientIDNumber);
//		entity.setEnmPaymentStatus(PaymentStatus.PAID);
//		PaymentData savedData = paymentDataRepository.save(entity);
		response.put("status", 200);
		}catch(Exception e) {
			response.put("status", 500);
		}
		return response.toString();
	}

	public String updateDoPaymentLater(String data) {
		JSONObject response = new JSONObject();
		String decodedData = CommonUtil.inputStreamDecoder(data);
		try {
			JSONObject jsonData = new JSONObject(decodedData);
	
			Optional<IssuanceWareHouseRecipt> findById = issuanceWareHouseReciptRepository
					.findById(jsonData.getInt("issueId"));
			if (findById.isPresent()) {
				IssuanceWareHouseRecipt issuanceWareHouseRecipt = findById.get();
				issuanceWareHouseRecipt.setDoPaymentLater(true);
				issuanceWareHouseReciptRepository.save(issuanceWareHouseRecipt);
				
				IssuanceWarehouseReceiptActionHistory actionHistory = new IssuanceWarehouseReceiptActionHistory();
		        actionHistory.setIntIssueanceWhId(jsonData.getInt("issueId")); // or whatever field holds the PK
		        actionHistory.setDepositorId(jsonData.getString("depositorId"));
		        actionHistory.setVchActionTakenBy(jsonData.getString("userName"));
		        actionHistory.setRoleId(jsonData.getInt("roleId"));
		        actionHistory.setRoleName(jsonData.getString("roleName"));
		        actionHistory.setUserId(jsonData.getInt("userId"));
		        actionHistory.setVchStatus("Do Payment Later");
		        
		        issuanceWarehouseReceiptActionHistoryRepository.save(actionHistory);
		            
				response.put("status", 200);
			}
		} catch (Exception e) {
			response.put("status", 500);
		}
		return response.toString();
	}
	

	
}
