package app.ewarehouse.dto;

import app.ewarehouse.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineComplianceDTO {
    private String vchRoutineComplianceId;
    private WarehouseParticularsResponse warehouseParticulars;
    private InspectionObjectiveDTO inspectionObjective;
    private String intInspectionDuration;
    private Date dtmInspectionDate;
    private List<TeamMemberDTO> teamMembers;
    private UserRoleIdResponse leadInspector;
    private String vchInspectionPlan;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Date dtmCreatedAt;
    private Date stmUpdatedAt;
    private Boolean bitDeleteFlag;
    private RoutineComplianceCeoApproval ceoApproval;
    private RoutineComplianceInspectorTwo inspectorTwo;
    private RoutineComplianceComplianceTwo complianceTwo;
    private RoutineComplianceCeoSecond ceoSecond;
    private String vchComplianceRemark;
    private Status enmStatus;
    private Integer intCurrentRole;
    private Integer intCurrentStage;
}
