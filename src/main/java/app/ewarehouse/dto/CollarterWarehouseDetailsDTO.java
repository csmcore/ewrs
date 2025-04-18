package app.ewarehouse.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class CollarterWarehouseDetailsDTO {
    private Integer id;
    private String warehouseId;
    private String warehouseName;
    private String warehouseOperatorName;
    private String email;
    private String mobileNumber;
    private String lrNumber;
    private Integer countyId;
    private Integer subCountyId;
    private Integer wardId;
    private String longitude;
    private String latitude;
    private String streetName;
    private String building;
    private String policyNumber;
    private Integer createdBy;
    private Integer updatedBy;
    private Timestamp createdOn;
    private Timestamp updatedOn;
    private Boolean deletedFlag;
}

