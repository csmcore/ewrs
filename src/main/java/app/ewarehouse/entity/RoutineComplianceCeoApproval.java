package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "t_routine_compliance_ceo_approval")
public class RoutineComplianceCeoApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer intId;

    @Column(name = "bitPlanComprehensive")
    private Boolean bitPlanComprehensive;

    @Column(name = "bitOjectivesAchievable")
    private Boolean bitOjectivesAchievable;

    @Column(name = "bitResourcesAdequate")
    private Boolean bitResourcesAdequate;

    @Column(name = "bitPotentialRisks")
    private Boolean bitPotentialRisks;

    @Column(name = "vchRecommendations")
    private String vchRecommendations;

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
    
    //AS screen changes
    @Column(name = "intRoutineComplianceId", nullable = false)
    private Long routineComplianceId;
    
    @Column(name = "plan", length = 255)
    private String plan;

    @Column(name = "objectives", length = 255)
    private String objectives;

    @Column(name = "resources", length = 255)
    private String resources;

    @Column(name = "risks", length = 255)
    private String risks;
}

