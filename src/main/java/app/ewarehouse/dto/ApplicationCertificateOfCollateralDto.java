package app.ewarehouse.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationCertificateOfCollateralDto {
	private String applicationId;
    private String fullName;
    private String mobileNo;
    private String email;
    private String postalCode;
    private String postalAddress;
    private String town;
    private String telephoneNo;
    private String website;
    private String entityType;
    private String companyName;
    private String companyRegNo;
    private String kraPin;
    private String paymentMode;
    private Boolean paymentSuccess;
    private Integer actionStatus;
    private Boolean deletedFlag;
    private Boolean isInspector;
    private Boolean isCertificatecommittee;
    private Boolean defer;
    private String statusDescription;
    private Boolean isInspectRemark;
    private Boolean isCcRemark;
    private Boolean bitForwardToOicStatus;
    
    
    
}



