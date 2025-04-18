package app.ewarehouse.master.dto;

import java.util.Date;
import java.util.List;

import app.ewarehouse.dto.CountyResponse;
import app.ewarehouse.dto.SubCountyResponse;
import app.ewarehouse.dto.WardResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WardMasterDto {

    private Integer intWardMasterId;
    private Integer intCountyId;
    private Integer intSubCountyId;
    private String wardName;
    private Boolean bitDeletedFlag;
    private String vchSubCountyName;
    private String county_name;

   
}
