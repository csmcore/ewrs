package app.ewarehouse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentStatusViewResponseDTO {
	private Integer intId;
    private String txtPaymentMethod;
    private String txtDescription;
    private Boolean bitDeletedFlag;
}
