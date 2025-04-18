package app.ewarehouse.dto;

import lombok.Data;

@Data
public class GraderWeigherExperienceDTO {
	
	private String experienceId;
	private String graderWeigherId;
	private String experience;
	private String uploadedCertificatePath;
	private Boolean fetchFromDb;
   

}
