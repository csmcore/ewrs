package app.ewarehouse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyLocationResponse {
    private Integer intId;
    private Integer intPostalCode;
    private String vchPostalAddress;
    private String vchStreetName;
    private String vchPlotNo;
    private WardResponse ward;
    private String vchLocation;
    private String vchSubLocation;
    private String vchTown;
    private String vchVillage;
    private String vchBuildingName;
}
