package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "t_inspection_objectives")
public class InspectionObjective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intInspectionObjectivesId")
    private Integer intInspectionObjectivesId;

    @Column(name = "vchPrimaryObjectives")
    private String vchPrimaryObjectives;

    @Column(name = "vchExpectedOutcomes")
    private String vchExpectedOutcomes;

    @Column(name = "bitSafety")
    private Boolean bitSafety;

    @Column(name = "bitOperations")
    private Boolean bitOperations;

    @Column(name = "bitDocumentation")
    private Boolean bitDocumentation;

    @Column(name = "bitCompliance")
    private Boolean bitCompliance;

    @Column(name = "bitSecurity")
    private Boolean bitSecurity;

    @OneToMany(mappedBy = "inspectionObjective", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FocusAreaItem> focusAreaItems;

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
