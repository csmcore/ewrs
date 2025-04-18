package app.ewarehouse.dto;

import app.ewarehouse.entity.ApplicationOfConformity;
import app.ewarehouse.entity.CreatedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MFinalOperatorLicenseResponse {
        private String vchLicenseNo;
        private OperatorLicenceDTO operatorLicenceApplication;
        private ApplicationOfConformityDTO applicationOfConformity;
        private String enmStatus;
        private Date dtmCreatedOn;
        private Date stmUpdatedOn;
        private Integer intCreatedBy;
        private Integer intUpdatedBy;
        private Boolean bitDeletedFlag;
        private Integer intApprovedBy;


}
