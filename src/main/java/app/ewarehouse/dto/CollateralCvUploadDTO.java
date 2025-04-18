package app.ewarehouse.dto;

import lombok.Data;

@Data
public class CollateralCvUploadDTO {
	
	private Integer cvId;
    private String applicationId;
    private String technicalStaffName;
    private String uploadCvPath;
    private Boolean fetchFromDbOne;

}
