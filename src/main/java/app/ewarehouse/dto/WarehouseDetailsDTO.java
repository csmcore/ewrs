package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseDetailsDTO {
    private Long id;
    private String warehouseName;
    private String warehouseOperatorName;
    private String emailId;
    private String mobileNumber;
    private String lrNumber;
    private String county;
    private String subCounty;
    private String ward;
    private String longitude;
    private String latitude;
    private String streetName;
    private String building;
    private String policyNumber;
    private String depositorId;
}
