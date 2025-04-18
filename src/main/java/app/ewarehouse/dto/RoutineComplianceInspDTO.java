package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineComplianceInspDTO {
	 private Long routineComId;
	    private String companyId;
	    private String warehouseId;
	    private String facilityType;
	    private String inspectionType;
	    private String inspectionTime;
	    private String startDate;
	    private String endDate;
	    private String requestedBy;
	    private String remarks;
	    private String inspectionPlan;
	    private Integer status;
	    private String companyName;
	    private String warehouseName;
	    private String statuspresent;
	    private Integer complianceOfficer;
	    private String officerOffice;
	    private String otherFacilityType;
	    private Integer intComplianceEdit;

}
