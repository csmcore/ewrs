package app.ewarehouse.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationOfConformityParticularOfApplicantsDTO {

	private Integer intParticularOfApplicantsId;
	private String applicantFullName;
    private String postalAddress;
    private String postalCode;
    private String town;
    private String telephoneNumber;
    private String mobilePhoneNumber;
    private String email;
    private String website;
    private Long entityTypeId;
    private String companyRegistrationNumber;
    private String kraPin;
    private String lrNumber;
    private Integer countyId;
    private Integer subCountyId;
    private String ward;
    private String streetName;
    private String building;
    //private List<AocTypeOfCommodityDto> typeOfCommodities;
    private String typeOfCommodities;
    private Integer storageCapacity;
    private String authorizedSignName;
    private String authorizedSignTitle;
    private String uploadSignPath;
    private Boolean signFetchFromDb;
    private Date date;
    private String uploadCompanySealPath;
    private Boolean sealFetchFromDb;
    private List<ApplicationOfConformityDirectorDetailsDTO> directors;
    private Integer userId;
    private String shop;
    private Integer unit;
    private Boolean draftStatus;
	
}
