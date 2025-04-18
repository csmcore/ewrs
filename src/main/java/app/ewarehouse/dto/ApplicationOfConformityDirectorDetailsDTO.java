package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationOfConformityDirectorDetailsDTO {

	private Integer directorDetailId;
	private Integer particularOfApplicantsId;
    private String directorName;
    private Long nationalityId;
    private String passportNumber;
    private String uploadWorkPermitPath;
    private Boolean fetchFromDb;
    
}
