package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCountyResponse {
    private Integer intId;
    private String txtSubCountyName;
    private CountyResponse county;
    private List<WardResponse> wards;
    private Date dtmCreatedOn;
    private Date stmUpdatedOn;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Boolean bitDeletedFlag;
    
}
