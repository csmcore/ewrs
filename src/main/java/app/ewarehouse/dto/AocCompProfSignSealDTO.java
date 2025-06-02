package app.ewarehouse.dto;

import lombok.Data;

@Data
public class AocCompProfSignSealDTO {
    private String signSealId;
    private String profDetId;
    private String signName;
    private String signTitle;
    private String signPath;
    private String sealPath;
    private Boolean signPathfetchFromDb;
    private Boolean sealPathfetchFromDb;
}

