package app.ewarehouse.dto;

import app.ewarehouse.entity.ApplicationOfConformityDirectorDetails;
import app.ewarehouse.entity.Nationality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationOfConformityDirectorResponseDTO {
	    private Integer id;
	    private String directorName;
	  //  private Integer particularOfApplicantsId;
	    private Long nationalityId;
	    private String passportNumber;
	    private String uploadWorkPermitPath;
	    
	    public ApplicationOfConformityDirectorDetails toEntity() {
	        ApplicationOfConformityDirectorDetails entity = new ApplicationOfConformityDirectorDetails();
	        entity.setId(this.id);
	        entity.setDirectorName(this.directorName);
	        Nationality nationality = new Nationality();
	        nationality.setIntNationalityId(this.nationalityId);
	        entity.setNationality(nationality);
	    //    entity.setParticularOfApplicants(particularOfApplicants);
	        entity.setPassportNumber(this.passportNumber);
	        entity.setUploadWorkPermitPath(this.uploadWorkPermitPath);
	        return entity;
	    }
}
