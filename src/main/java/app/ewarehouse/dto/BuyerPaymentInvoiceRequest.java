package app.ewarehouse.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BuyerPaymentInvoiceRequest {

	    private String buyerId;
	    private String depositorId;
	    private String wareHouseReceiptNo;
	    private BigDecimal priceOfReceipt;
	    private String contractAgreement;

//	    private String documentId;
}
