package app.ewarehouse.dto;

import lombok.Data;

@Data
public class AocCompProfileDetDTO {
    private String profDetId;
    private String applicantName;
    private String mobileNumber;
    private String email;
    private String postalAddress;
    private String postalCode;
    private String town;
    private String telephone;
    private String website;
    private String typeOfEntity;
    private String company;
    private String companyRegNo;
    private String kraPin;
    private Boolean isEditOptionEnabled;
}

