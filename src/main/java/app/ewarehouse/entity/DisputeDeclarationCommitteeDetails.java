package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
        import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "t_dispute_declaration_committee_details")
public class DisputeDeclarationCommitteeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer intId;

    @Column(name = "vchName", length = 50)
    private String vchName;

    @Column(name = "vchDescription", length = 100)
    private String vchDescription;

    @Column(name = "vchObjective", length = 100)
    private String vchObjective;

    @OneToMany(mappedBy = "committeeDetails", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DisputeDeclarationCommitteeMembers> committeeMembers;

    @Column(name = "vchActionPlan", length = 100)
    private String vchActionPlan;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "timeLine", columnDefinition = "TIME")
    private Date timeLine;

    @Column(name = "vchMilestones", length = 100)
    private String vchMilestones;

    @Column(name = "vchRemark", length = 2000)
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


