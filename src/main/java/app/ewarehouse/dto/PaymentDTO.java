package app.ewarehouse.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentDTO {
	private String applicantName;
    private String applicationFees;
    private String paymentTransactionNo;
    private LocalDateTime paymentDateTime;
    private String paymentMode;
	
}
