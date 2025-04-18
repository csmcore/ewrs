package app.ewarehouse.dto;

import app.ewarehouse.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class DisputeDeclarationResponse {
    private String disputeId;
    private String disputantName;
    private Integer contactNumber;
    private String email;
    private SubCountyResponse subCounty;
    private String address;
    private Date dateOfOccurrence;
    private String locationOfOccurrence;
    private DisputeCategory disputeCategory;
    private String descriptionOfDispute;
    private String otherPartyName;
    private Integer otherPartyNo;
    private String relationship;
    private List<DisputeDeclarationSupportingDocument> supportingDocuments;
    private String desiredResolution;
    private DisputeDeclarationCeo ceoApproval;
    private DisputeDeclarationCommitteeDetails committeeDetails;
    private DisputeDeclarationOic oicApproval;
    private DisputeDeclarationOicLegalTwo oicLegalTwo;
    private List<DisputeDeclarationRemark> remarks;
    private Status status;
    private Integer intCurrentRole;
    private Integer intCurrentStage;
    private Date createdAt;
    private Date updatedAt;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
}
