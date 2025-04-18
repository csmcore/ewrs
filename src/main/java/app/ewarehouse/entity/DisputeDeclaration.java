package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "m_dispute_declaration")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class DisputeDeclaration {
    @Id
    @GeneratedValue(generator = "dispute-declaration-custom-id")
    @GenericGenerator(
            name = "dispute-declaration-custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "m_dispute_declaration"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "dispute_id")
            })
    @Column(name = "dispute_id")
    private String disputeId;

    @Column(name = "disputant_name")
    private String disputantName;

    @Column(name = "contact_number")
    private Integer contactNumber;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "intSubCountyId")
    private SubCounty subCounty;

    @Column(name = "address")
    private String address;

    @Column(name = "date_of_occurrence")
    private Date dateOfOccurrence;

    @Column(name = "location_of_occurrence")
    private String locationOfOccurrence;

    @ManyToOne
    @JoinColumn(name = "dispute_category_id")
    private DisputeCategory disputeCategory;

    @Column(name = "description_of_dispute")
    private String descriptionOfDispute;

    @Column(name = "other_party_name")
    private String otherPartyName;

    @Column(name = "other_party_number")
    private Integer otherPartyNo;

    @Column(name = "relationship")
    private String relationship;

    @OneToMany(mappedBy = "disputeDeclaration", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DisputeDeclarationSupportingDocument> supportingDocuments;

    @Column(name = "desiredResolution")
    private String desiredResolution;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intCeoApprovalId")
    private DisputeDeclarationCeo ceoApproval;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intCommitteeDetailsId")
    private DisputeDeclarationCommitteeDetails committeeDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intOicApprovalId")
    private DisputeDeclarationOic oicApproval;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intOicLegalTwoId")
    private DisputeDeclarationOicLegalTwo oicLegalTwo;

    @OneToMany(mappedBy = "disputeDeclaration", fetch = FetchType.EAGER)
    private List<DisputeDeclarationRemark> remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.Pending;

    @Column(name = "intCurrentRole")
    private Integer intCurrentRole;

    @Column(name = "intCurrentStage")
    private Integer intCurrentStage;

    @CreationTimestamp
    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date updatedAt;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;
}