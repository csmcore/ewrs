package app.ewarehouse.dto;

import app.ewarehouse.entity.FocusArea;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FocusAreaItemDTO {
    private Integer id;
    private FocusArea focusArea;
    private String focusAreaItem;
    private Boolean focusAreaItemValue;
    private InspectionObjectiveDTO inspectionObjective;
    private Integer createdBy;
    private Integer updatedBy;
    private Date createdAt;
    private Date updatedAt;
    private Boolean deleteFlag;
}
