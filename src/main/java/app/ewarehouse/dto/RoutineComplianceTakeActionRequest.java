package app.ewarehouse.dto;

import app.ewarehouse.entity.RoutineCompliance;
import app.ewarehouse.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineComplianceTakeActionRequest {
    private Status action;
    private Integer officerId;
    private RoutineCompliance routineCompliance;
}
