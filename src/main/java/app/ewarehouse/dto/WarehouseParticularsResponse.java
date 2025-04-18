package app.ewarehouse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseParticularsResponse {
    private String intWareHouseParticularsId;
    private String vchWarehouseName;
    private String vchOperatorName;
    private String vchOperatorLicenseNo;
    private String vchNameAndBuildingLocation;
    private String vchStreetName;
    private WardResponse ward;
    private SubCountyResponse subCounty;
    private String vchOperatorsInsurance;
    private String vchPolicyNo;
    private String uploadSignPath;
    private String uploadlogoPath;
    private String position;
}