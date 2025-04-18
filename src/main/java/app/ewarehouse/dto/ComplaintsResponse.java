package app.ewarehouse.dto;

import app.ewarehouse.entity.Status;
import lombok.Data;

import java.util.Date;

@Data
public class ComplaintsResponse {
	private String complaintid;
    private String complaintFor;
    private String warehouseOperatorName;
    private Integer complainertelephone;
    private String complaineremail;
    private String complaintCategory;
    private Date dateOfIncident;
    private String remark;
    private Status status;
    private String supportingDocument;
    private String complaintDescription;
    private String specificAllegations;
}
