package app.ewarehouse.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class GraderWeigherRegFormDTO {

	private String graderWeigherId;
	private String designationType;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String mobileNo;
    private String emailId;
    private String alternateContactNo;
    private String address;
    private String employeeId;
    private String startDate;
    private String endDate;
    private String govtIssuedId;
    //private String idType;
    private String idNo;
    private String expiryDate;
    //private String uploadedCertificatePath;
    //private Boolean fetchFromDb;

}
