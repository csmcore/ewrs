package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "t_routine_compliance_ceo_second")
public class RoutineComplianceCeoSecond {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer intId;

    @Column(name = "bitReportPerRequirement")
    private Boolean bitReportPerRequirement;

    @Column(name = "bitIssuesRaisedCorrect")
    private Boolean bitIssuesRaisedCorrect;

    @Column(name = "bitCommentsValid")
    private Boolean bitCommentsValid;

    @Column(name = "vchLevelOfCompliance")
    private String vchLevelOfCompliance;

    @Column(name = "vchRemedialAction")
    private String vchRemedialAction;

    @Column(name = "dtmLastRemedialActionDate")
    private Date dtmLastRemedialActionDate;

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
    
    @Column(name = "isTheReport") // Added field
    private String isTheReport;
    
    @Column(name = "areTheIssue") // Added field
    private String areTheIssue;
    
    @Column(name = "areInspector") // Added field
    private String areInspector;
    
   
}

