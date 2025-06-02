package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintTypeResponse {
	private Integer complaintId;
	private String complaintType;
	private String complaintStatus;
	private String createdOn;
	private String updatedOn;
	private String createdBy;
	private String updatedBy;
	private Boolean isActive;
}
