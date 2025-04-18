package app.ewarehouse.util;

import app.ewarehouse.dto.*;
import app.ewarehouse.entity.*;

import java.util.Map;
import java.util.stream.Collectors;

public class RoutineComplianceMapper {

    public static RoutineComplianceDTO mapRoutineComplianceToDto(RoutineCompliance routineCompliance) {
        return RoutineComplianceDTO.builder()
                .vchRoutineComplianceId(routineCompliance.getVchRoutineComplianceId())
                .warehouseParticulars(mapToWarehouseParticularsResponse(routineCompliance.getWarehouseParticulars()))
                .inspectionObjective(mapInspectionObjectiveToDto(routineCompliance.getInspectionObjective()))
                .intInspectionDuration(routineCompliance.getIntInspectionDuration())
                .dtmInspectionDate(routineCompliance.getDtmInspectionDate())
                .teamMembers(routineCompliance.getTeamMembers().stream()
                        .map(RoutineComplianceMapper::mapTeamMemberToDtoWithoutRoutineCompliance)
                        .collect(Collectors.toList()))
                .leadInspector((routineCompliance.getLeadInspector() != null)?mapInspectorToDtoWithoutRoutineCompliance(routineCompliance.getLeadInspector()): null)
                .vchInspectionPlan(routineCompliance.getVchInspectionPlan())
                .intCreatedBy(routineCompliance.getIntCreatedBy())
                .intUpdatedBy(routineCompliance.getIntUpdatedBy())
                .dtmCreatedAt(routineCompliance.getDtmCreatedAt())
                .stmUpdatedAt(routineCompliance.getStmUpdatedAt())
                .bitDeleteFlag(routineCompliance.getBitDeleteFlag())
                //.ceoApproval(routineCompliance.getCeoApproval())
                .inspectorTwo(routineCompliance.getInspectorTwo())
                .complianceTwo(routineCompliance.getComplianceTwo())
                .ceoSecond(routineCompliance.getCeoSecond())
                .vchComplianceRemark(routineCompliance.getVchComplianceRemark())
                .enmStatus(routineCompliance.getEnmStatus())
                .intCurrentRole(routineCompliance.getIntCurrentRole())
                .intCurrentStage(routineCompliance.getIntCurrentStage())
                .build();
    }

    public static WarehouseParticularsResponse mapToWarehouseParticularsResponse(MFinalOperatorLicense warehouseParticulars) {
        if (warehouseParticulars == null) {
            return null;
        }
        ApplicationOfConformity aoc = warehouseParticulars.getApplicationOfConformity();
        return WarehouseParticularsResponse.builder()
                .intWareHouseParticularsId(aoc.getApplicationId())
                .vchWarehouseName(aoc.getParticularOfApplicantsId().getShop())
                .vchOperatorName(aoc.getParticularOfApplicantsId().getApplicantFullName())
                .vchOperatorLicenseNo(warehouseParticulars.getVchLicenseNo())
                .vchNameAndBuildingLocation(aoc.getParticularOfApplicantsId().getBuilding())
                .vchStreetName(aoc.getParticularOfApplicantsId().getStreetName())
                .ward(WardResponse.builder().vchWardName(aoc.getParticularOfApplicantsId().getWard())
                        .subCounty(Mapper.mapToSubCountyResponse(aoc.getParticularOfApplicantsId().getSubCountyId()))
                        .build())
                .uploadSignPath(aoc.getParticularOfApplicantsId().getUploadSignPath())
                .uploadlogoPath(aoc.getParticularOfApplicantsId().getUploadCompanySealPath())
                .vchOperatorsInsurance("---")
                .vchPolicyNo("---")
                .position("----")
                .build();
        
    }

    public static TeamMemberDTO mapTeamMemberToDtoWithoutRoutineCompliance(TeamMember teamMember) {
        return TeamMemberDTO.builder()
                .intTeamMemberId(teamMember.getIntTeamMemberId())
                .vchTeamMemberName(teamMember.getVchTeamMemberName())
                .intCreatedBy(teamMember.getIntCreatedBy())
                .intUpdatedBy(teamMember.getIntUpdatedBy())
                .dtmCreatedAt(teamMember.getDtmCreatedAt())
                .stmUpdatedAt(teamMember.getStmUpdatedAt())
                .bitDeleteFlag(teamMember.getBitDeleteFlag())
                .build();
    }

    public static InspectionObjectiveDTO mapInspectionObjectiveToDto(InspectionObjective inspectionObjective) {
        return InspectionObjectiveDTO.builder()
                .intInspectionObjectivesId(inspectionObjective.getIntInspectionObjectivesId())
                .vchPrimaryObjectives(inspectionObjective.getVchPrimaryObjectives())
                .vchExpectedOutcomes(inspectionObjective.getVchExpectedOutcomes())
                .bitSafety(inspectionObjective.getBitSafety())
                .bitOperations(inspectionObjective.getBitOperations())
                .bitDocumentation(inspectionObjective.getBitDocumentation())
                .bitCompliance(inspectionObjective.getBitCompliance())
                .bitSecurity(inspectionObjective.getBitSecurity())
                .focusAreaItems(inspectionObjective.getFocusAreaItems().stream()
                        .map(RoutineComplianceMapper::mapFocusAreaItemToDtoWithoutInspectionObjective)
                        .collect(Collectors.toList()))
                .intCreatedBy(inspectionObjective.getIntCreatedBy())
                .intUpdatedBy(inspectionObjective.getIntUpdatedBy())
                .dtmCreatedAt(inspectionObjective.getDtmCreatedAt())
                .stmUpdatedAt(inspectionObjective.getStmUpdatedAt())
                .bitDeleteFlag(inspectionObjective.getBitDeleteFlag())
                .build();
    }

    public static FocusAreaItemDTO mapFocusAreaItemToDtoWithoutInspectionObjective(FocusAreaItem focusAreaItem) {
        return FocusAreaItemDTO.builder()
                .id(focusAreaItem.getId())
                .focusArea(focusAreaItem.getFocusArea())
                .focusAreaItem(focusAreaItem.getFocusAreaItem())
                .focusAreaItemValue(focusAreaItem.getFocusAreaItemValue())
                .createdBy(focusAreaItem.getCreatedBy())
                .updatedBy(focusAreaItem.getUpdatedBy())
                .createdAt(focusAreaItem.getCreatedAt())
                .updatedAt(focusAreaItem.getUpdatedAt())
                .deleteFlag(focusAreaItem.getDeleteFlag())
                .build();
    }

    public static UserRoleIdResponse mapInspectorToDtoWithoutRoutineCompliance(Tuser inspector) {
        if (inspector != null) {
            UserRoleIdResponse userRoleIdResponse = new UserRoleIdResponse();
            userRoleIdResponse.setUid(inspector.getIntId().toString());
            userRoleIdResponse.setUname(inspector.getTxtFullName());
            userRoleIdResponse.setLoginid(inspector.getTxtUserId());
            return userRoleIdResponse;
        }
        return null;
    }
}