package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionObjectiveDTO {

    private Integer intInspectionObjectivesId;
    private String vchPrimaryObjectives;
    private String vchExpectedOutcomes;
    private Boolean bitSafety;
    private Boolean bitOperations;
    private Boolean bitDocumentation;
    private Boolean bitCompliance;
    private Boolean bitSecurity;
    private List<FocusAreaItemDTO> focusAreaItems;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Date dtmCreatedAt;
    private Date stmUpdatedAt;
    private Boolean bitDeleteFlag;
}
