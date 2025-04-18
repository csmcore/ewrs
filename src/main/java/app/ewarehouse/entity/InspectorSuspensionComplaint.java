package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_complaint")
public class InspectorSuspensionComplaint {
    @Id
    @GeneratedValue(generator = "inspector-suspension-complaint-custom-id")
    @GenericGenerator(
            name = "inspector-suspension-complaint-custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "m_complaint"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "complaint_id")
            })
    @Column(name = "complaint_id")
    private String complaintId;

    @Column(name = "complainant_name")
    private String complainantName;

    @Column(name = "complainant_contact_number")
    private String complainantContactNumber;

    @Column(name = "complainant_email")
    private String complainantEmail;

    @Column(name = "complainant_address")
    private String complainantAddress;

    @Column(name = "date_of_incident")
    private Date dateOfIncident;

    @Column(name = "location_of_incident")
    private String locationOfIncident;

    @Column(name = "complaint_type")
    private String complaintType;

    @Column(name = "description_of_incident")
    private String descriptionOfIncident;

    @Column(name = "supporting_document")
    private String supportingDocument;

    @Column(name = "is_declared")
    private Boolean isDeclared;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "final_remark")
    private String remark;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "ceo_remarks")
    private String ceoRemarks;
    
    @Column(name = "oic_remarks")
    private String oicRemarks;
    
    @Column(name = "ceo2_remarks")
    private String ceo2Remarks;
    
    @Column(name = "approver_remarks")
    private String approverRemarks;
    
    @Column(name = "action_taken_by")
    @Builder.Default
    private Integer actionTakenBy=1;
    
    @Column(name = "pending_at_user")
    private Integer pendingAtUser;
}
