package app.ewarehouse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WardResponse {
    private Integer intId;
    private String vchWardName;
    private SubCountyResponse subCounty;
}
