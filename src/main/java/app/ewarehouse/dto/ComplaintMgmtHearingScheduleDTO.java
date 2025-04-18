package app.ewarehouse.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class ComplaintMgmtHearingScheduleDTO {
    private Integer complaintId;
    private String reasonForHearing;
    private LocalDate dateOfHearing;
    private LocalTime timeOfHearing;
    private String documentsToBeProduced;
    private String remedialAction;
    private Integer createdBy;
}

