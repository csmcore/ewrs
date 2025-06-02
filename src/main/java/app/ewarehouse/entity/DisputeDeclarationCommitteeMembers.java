package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "t_dispute_declaration_committee_members")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisputeDeclarationCommitteeMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intId")
    private Integer intId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "committee_id")
    private DisputeDeclarationCommitteeDetails committeeDetails;

    @ManyToOne
    @JoinColumn(name = "designation_id")
    private CommitteeDesignation designation;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private TeamMember member;

    @Column(name = "dtmCreatedOn")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date dtmCreatedOn;

    @Column(name = "stmUpdatedOn")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date stmUpdatedOn;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    @Column(name = "intApprovedBy")
    private Integer intApprovedBy;
}

