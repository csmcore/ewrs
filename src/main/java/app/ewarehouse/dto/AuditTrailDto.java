package app.ewarehouse.dto;

import java.time.LocalDateTime;

import app.ewarehouse.entity.Mrole;
import app.ewarehouse.entity.Users;
import lombok.Data;
@Data
public class AuditTrailDto {

    
    private Integer auditTrailId;
    private String firstname; 
    private String roleName;
    private String action;
    private LocalDateTime dateTime;
    private String remarks;
    private String ipAddress;
    private String deviceName;
    private Boolean deletedFlag = false;

}

