package app.ewarehouse.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AocRemarksDto {
	private String txtRemark;
    private Integer roleId;
    private Integer userId;
    private String username;
    private String intApplicantId;
    private String enmStatus;
    private String txtInspectorFilePath;
    private LocalTime timeOfInspect;
    private LocalDate dateOfInspect;
    private String txtDescriptionOfInspection;
}
