package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_suspension_of_grader")
public class TsuspensionOfGrader {
    @Id
    @GeneratedValue(generator = "grader-complaint-custom-id")
    @GenericGenerator(
            name = "grader-complaint-custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_suspension_of_grader"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "grader_complaint_id")
            })
    @Column(name = "grader_complaint_id")
    private String graderComplaintId;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "complainant_name")
    private String complainantName;

    @NotBlank(message = "Contact Number is mandatory")
    @Column(name = "complainant_contact_number" ,unique = true)
    private String complainantContactNumber;

    @Email(message = "Email address is invalid")
    @Column(name = "complainant_email")
    private String complainantEmail;

    @NotBlank(message = "Address is mandatory")
    @Column(name = "complainant_address")
    private String complainantAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
    @Column(name = "date_of_incident")
    private Date dateOfIncident;

    @NotBlank(message = "Location is mandatory")
    @Column(name = "location_of_incident")
    private String locationOfIncident;

    @NotBlank(message = "Complaint Type is mandatory")
    @Column(name = "complaint_type")
    private String complaintType;

    @Column(name = "other_complaint_name")
    private String otherComplaintName;

    @NotBlank(message = "Description of Incident is mandatory")
    @Column(name = "description_of_incident")
    private String descriptionOfIncident;

    @NotBlank(message = "Document is mandatory")
    @Column(name = "supporting_document")
    private String supportingDocument;

    @Column(name ="documentName")
    private String documentName;

    @Column(name = "is_declared")
    private Boolean isDeclared;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "remark")
    private String remark;

    @Column(name = "ceo_initial_remark")
    private String ceoInitialRemark;

    @Column(name = "oic_legal_remark")
    private String oicLegalRemark;

    @Column(name = "approver_remark")
    private String approverRemark;

    @Column(name = "forwarded_by")
    private Integer forwardedBy;

    @Column(name = "pending_at_user")
    private Integer pendingAtUser;

    @Column(name = "ceo_final_remark")
    private String ceoFinalRemark;
}
