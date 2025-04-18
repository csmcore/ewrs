package app.ewarehouse.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ComplaintMgmtInspScheduleDto {

	private Integer inspectionId;
	private Integer complaintId;
	private Integer inspectorId;
	private String inspectorName;
	private LocalTime timeOfInspect;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
    private LocalDate dateOfInspect;
    private String txtDescriptionOfInspection;
}
