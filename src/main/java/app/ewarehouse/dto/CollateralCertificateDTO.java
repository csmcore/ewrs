package app.ewarehouse.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CollateralCertificateDTO {

    private Integer certificateId;
    private String certificateNo;
    private String applicationId;
    private String companyName;
    private String postalCode;
    private String applicantName;
    private Integer userId;
    private Integer roleId;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Integer approverRoleId;
    private Integer approverUserId;
    private String approverUserName;
    private String sealPath;
    private LocalDate dateOfIssue;
    

   
}
