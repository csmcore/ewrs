package app.ewarehouse.dto;

import java.util.List;

import lombok.Data;

@Data
public class GraderWeigherMainFormDTO {
	
	private Integer userId;
	private GraderWeigherRegFormDTO graderweigher;
	private List<GraderWeigherExperienceDTO> experienceDetails;
	
	

}
