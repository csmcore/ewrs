package app.ewarehouse.dto;

import lombok.Data;

@Data
public class AocComplianceCollarterSignSealDTO {
    private String signSealId;
    private String applicationId;
    private String signName;
    private String signTitle;
    private String signPath;
    private String sealPath;
    private String applicationPath;
    private String taxCertificatePath;
    private String personalDetailsPath;
    private String incorporationPath;
    private String proofWhPath;
    private Boolean signPathfetchFromDb;
    private Boolean sealPathfetchFromDb;
    private Boolean applicationPathfetchFromDb;
    private Boolean taxCertificatePathfetchFromDb;
    private Boolean personalDetailsPathfetchFromDb;
    private Boolean incorporationPathfetchFromDb;
    private Boolean proofWhPathfetchFromDb;
    
   
}
