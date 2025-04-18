package app.ewarehouse.dto;

import app.ewarehouse.entity.LegalStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CompanyResponse {
    private Integer intCompanyId;
    private String vchName;
    private String vchRegistrationNo;
    private Date dtmEstablishmentDate;
    private LegalStatus legalStatus;
    private String vchKraPin;
    private String vchEmail;
    private CompanyLocationResponse companyLocation;
}
