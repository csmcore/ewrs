package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PendingConformityDTO {

	private Integer intcnfId;
    private String vchWareHouseId;
    private String vchWarehouseName;
    private String vchWhOperatorName;
    private String vchEmail;
    private String vchMobileNumber;
    private String vchLrNumber;
    private String countyName;
    private String vchSubCountyName;
    private String wardName;
    private String enmApplicationStatus;
    private Boolean bitOicOneCC;
    private Boolean bitOicOneInsp;
    private Boolean bitOicTwoCC;
    private Boolean bitOicTwoInsp;
    private Integer actionStatus;
    private Boolean forwardToOicStatus; 
    //private LocalDateTime assignedDate;
}
