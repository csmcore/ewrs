package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "t_routine_compliance_compliance_two")
public class RoutineComplianceComplianceTwo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer intId;

    @Column(name = "bitConductedPerRequirement")
    private Boolean bitConductedPerRequirement;

    @Column(name = "bitNecessaryItemsChecked")
    private Boolean bitNecessaryItemsChecked;

    @Column(name = "bitTimelyPerformed")
    private Boolean bitTimelyPerformed;

    @Column(name = "bitSatisfied")
    private Boolean bitSatisfied;

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
    
    @Column(name = "intRoutineComplianceId", nullable = false)
    private Long routineComplianceId;
    
    @Column(name = "wasInspectionCond") // Added field
    private String wasInspectionCond;
    
    @Column(name = "wasNecessaryItem") // Added field
    private String wasNecessaryItem;
    
    @Column(name = "wasInsPerf") // Added field
    private String wasInsPerf;
    
    @Column(name = "wasSatis") // Added field
    private String wasSatis;
}

