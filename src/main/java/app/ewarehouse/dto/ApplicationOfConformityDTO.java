package app.ewarehouse.dto;

import app.ewarehouse.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationOfConformityDTO {
	private String applicationId;
    private AocCompProfDetailsMainDTO companyDetails;
    private Status enmStatus;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Date dtmCreatedOn;
    private Date stmUpdatedOn;
    private Boolean bitDeletedFlag;
    private ApplicationOfConformityParticularOfApplicants particularOfApplicantsId;
    private ApplicationOfConformitySupportingDocuments supportingDocumentId;
    private ApplicationOfConformityWarehouseOperatorViability viabilityId;
    private ApplicationOfConformityPaymentDetails paymentId;
    private Integer role;

}
