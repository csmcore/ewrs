package app.ewarehouse.dto;

import lombok.Data;

@Data
public class AocCompProfDirectorDetDTO {
    private String directorId;
    private String profDetId;
    private String directorName;
    private Integer nationalityId;
    private String position;
    private String passportNo;
    private String workPermitPath;
    private Boolean fetchFromDb;
}

