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
public class CountyResponse {
    private Integer id;
    private String name;
    private CountryResponse country;
    private List<SubCountyResponse> subCounties;
    private Boolean bitDeletedFlag;
    
}
