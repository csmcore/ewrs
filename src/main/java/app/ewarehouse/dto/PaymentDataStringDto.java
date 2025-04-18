package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentDataStringDto {
	private String amountExpected;	
	private String clientIDNumber;	
	private String currency;
	private String billRefNumber;	
	private String billDesc;
	private String clientName;	
		
}
