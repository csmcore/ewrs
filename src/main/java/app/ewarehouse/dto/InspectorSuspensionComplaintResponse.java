package app.ewarehouse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class InspectorSuspensionComplaintResponse {
    private String complaintId;
    private String complainantName;
    private String complainantContactNumber;
    private String complainantEmail;
    private String complainantAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
    private Date dateOfIncident;
    private String locationOfIncident;
    private String complaintType;
    private String descriptionOfIncident;
    private String supportingDocument;
    private String status;
    private String remark;
    private String ceoRemarks;
    private String oicRemarks;
    private String ceo2Remarks;
    private String approverRemarks;
    private Integer actionTakenBy;
}
