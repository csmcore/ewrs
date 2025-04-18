package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "t_routine_compliance")
@DynamicUpdate
public class RoutineCompliance {

    @Id
    @GeneratedValue(generator = "routine-compliance-custom-id")
    @GenericGenerator(
            name = "routine-compliance-custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_routine_compliance"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "vchRoutineComplianceId")
            })
    @Column(name = "vchRoutineComplianceId")
    private String vchRoutineComplianceId;

    @Column(name = "vchInspectionPlan")
    private String vchInspectionPlan;

    @ManyToOne
    @JoinColumn(name = "vchWarehouseParticularsId")
    private MFinalOperatorLicense warehouseParticulars;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intInspectionObjectivesId")
    private InspectionObjective inspectionObjective;

    @ManyToOne
    @JoinColumn(name = "intLeadInspector")
    private Tuser leadInspector;

    @Column(name = "dtmInspectionDate")
    private Date dtmInspectionDate;

    @Column(name = "intInspectionDuration")
    private String intInspectionDuration;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "t_routine_compliance_team_members",
            joinColumns = @JoinColumn(name = "vchRoutineComplianceId"),
            inverseJoinColumns = @JoinColumn(name = "intTeamMemberId")
    )
    private List<TeamMember> teamMembers;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "intCeoApprovalId")
//    private RoutineComplianceCeoApproval ceoApproval;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intInspectorTwoId")
    private RoutineComplianceInspectorTwo inspectorTwo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intComplianceTwoId")
    private RoutineComplianceComplianceTwo complianceTwo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "intCeoSecondId")
    private RoutineComplianceCeoSecond ceoSecond;

    @Column(name = "vchComplianceRemark")
    private String vchComplianceRemark;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmStatus")
    private Status enmStatus = Status.Pending;

    @Column(name = "intCurrentRole")
    private Integer intCurrentRole;

    @Column(name = "intCurrentStage")
    private Integer intCurrentStage;

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
