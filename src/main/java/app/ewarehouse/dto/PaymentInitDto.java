package app.ewarehouse.dto;

import lombok.Data;

@Data
public class PaymentInitDto {
	private String vchWareHouseId;
    private String applicantName;
    private Double paymentAmount;
    private Integer userId;
    private Integer roleId;
    private String email;
    private String phoneNumber;
    private String description;
    private String currency;
}
