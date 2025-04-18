package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "t_application_compliance_approver")
public class TApplicationComplianceApprover {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer intId;

    @Column(name = "bitApplicationComplete")
    private Boolean bitApplicationComplete;

    @Column(name = "bitCompetent")
    private Boolean bitCompetent;

    @Column(name = "bitCandidatePreferred")
    private Boolean bitCandidatePreferred;

    @Column(name = "bitRecommend")
    private Boolean bitRecommend;

    @Column(name = "vchRemark")
    private String vchRemark;

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
