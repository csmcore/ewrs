package app.ewarehouse.dto;

import java.util.List;

import lombok.Data;

@Data
public class CollateralManagerMainFormDTO {
	
	private Integer userId;
	private ApplicationCertificateOfCollateralDto collateralProfile;
    private List<AocComplianceCollarterDirectorDTO> directors;
    private List<CollateralCvUploadDTO> cvUploadFiles;
    private AocComplianceCollarterSignSealDTO signSeal;
    
    private Boolean isDraft;

}
