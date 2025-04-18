package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "t_dispute_declaration_oic")
public class DisputeDeclarationOic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer intId;

    @Column(name = "bitEvidenceSufficient")
    private Boolean bitEvidenceSufficient;

    @Column(name = "bitValid")
    private Boolean bitValid;

    @Column(name = "bitRequireInvestigation")
    private Boolean bitRequireInvestigation;

    @Column(name = "bitWithinCommitteeScope")
    private Boolean bitWithinCommitteeScope;

    @Column(name = "bitResolutionReasonable")
    private Boolean bitResolutionReasonable;

    @Column(name = "reportPath")
    private String reportPath;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedAt")
    private Date dtmCreatedAt;

    @UpdateTimestamp
    @Column(name = "stmUpdatedAt")
    private Date stmUpdatedAt;

    @Column(name = "bitDeleteFlag")
    private Boolean bitDeleteFlag = false;
}



