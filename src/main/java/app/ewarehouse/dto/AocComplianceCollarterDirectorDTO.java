package app.ewarehouse.dto;

import lombok.Data;

@Data
public class AocComplianceCollarterDirectorDTO {
    private String directorId;
    private String applicationId;
    private String directorName;
    private Integer nationalityId;
    private String passportNo;
    private String position;
    private String workPermitPath;
    private Boolean fetchFromDb;
}
