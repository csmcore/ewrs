package app.ewarehouse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class InspectorSuspensionComplaintRequest {
    @NotBlank(message = "Complainant Name cannot be blank")
    private String complainantName;

    @NotBlank(message = "Complainant Contact Number cannot be blank")
    private String complainantContactNumber;

    @NotBlank(message = "Complainant Email cannot be blank")
    private String complainantEmail;

    @NotBlank(message = "Complainant address cannot be blank")
    private String complainantAddress;

    @NotNull(message = "Date of Incident cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
    private Date dateOfIncident;

    @NotBlank(message = "Location of Incident cannot be blank")
    private String locationOfIncident;

    @NotBlank(message = "Complaint Type cannot be blank")
    private String complaintType;

    @NotBlank(message = "Description of Incident cannot be blank")
    private String descriptionOfIncident;

    @NotBlank(message = "Supporting Document cannot be blank")
    private String supportingDocument;

    @NotNull(message = "Declared cannot be null")
    private Boolean isDeclared;
}
