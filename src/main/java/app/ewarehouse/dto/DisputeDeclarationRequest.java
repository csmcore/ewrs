package app.ewarehouse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class DisputeDeclarationRequest {
    @NotNull
    private String applicantId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
    @NotNull
    private Date dateOfOccurrence;

    @NotBlank(message = "Location of Occurrence cannot be blank")
    private String locationOfOccurrence;

    @NotNull
    private Integer disputeCategoryId;

    @NotBlank(message = "Description of Dispute cannot be blank")
    private String descriptionOfDispute;

    @NotNull
    private Integer disputeSupportingDocumentType;

    @NotBlank(message = "Supporting Document cannot be blank")
    private String base64EncodedSupportingDocument;
}
