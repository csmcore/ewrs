package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyerPaymentInvoiceResponse {
	
	private String invoiceNumber;
    private BuyerDepositorResponse buyerResponse;
    private WarehouseReceiptResponse waReceiptResponse;
    private String price;
    private String contractAgreement;

}
