package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponse {
    private Integer countryId;
    private String countryName;
    private String countryCode;
    private Boolean isActive;
    private List<CountyResponse> counties;
}
