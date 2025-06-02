package app.ewarehouse.dto;

import lombok.Data;

@Data

public class OperatorLicenceApprovalResponseDto {

	private String vchOPlId;
    private String vchWarehouseName;
    private String vchWhOperatorName;
    private String vchEmail;
    private String vchMobileNumber;
    private String vchLrNumber;
    private String countyName;
    private String vchSubCountyName;
    private String wardName;
    private String userName;
    private String vchFormOneCId;
    private String paymentId;
    private String enmPaymentStatus;
    private String vchApplicationStatus;
    private String vchApprovalStatus;
    private Integer appId;
    private Integer stageNo;
    private Integer pendingAt;
    private Integer processId;
    private Integer labelId;
    private String vchWareHouseId;
    private String vchCompanyId;
    private String vchOplicenceId;
    private String icmStatus;
}
