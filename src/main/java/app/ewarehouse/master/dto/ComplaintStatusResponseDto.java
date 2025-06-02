package app.ewarehouse.master.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComplaintStatusResponseDto {
	private Integer intId;
	private String vchComplaintStatusName;
	private Boolean isActive;
}
