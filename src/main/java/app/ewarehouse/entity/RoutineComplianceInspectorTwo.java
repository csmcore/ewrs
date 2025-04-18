package app.ewarehouse.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "t_routine_compliance_inspector_two")
public class RoutineComplianceInspectorTwo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer intId;

    @Column(name = "bitOperationalCondition")
    private Boolean bitOperationalCondition;
    
    @Column(name = "vchCondition")
    private String vchCondition;
    
    @Column(name = "bitSuccessfullyCompleted")
    private Boolean bitSuccessfullyCompleted;
    
    @Column(name = "inspesucess")
    private String inspesucess;

    @Column(name = "actualStartDate")
    private String actualStartDate;
    
    @Column(name = "actualEndDate")
    private String actualEndDate;

    @Column(name = "timeOfInspection")
    private String timeOfInspection;


    @Column(name = "vchTimeChangeReason")
    private String vchTimeChangeReason;

    @Column(name = "bitUrgentIssues")
    private Boolean bitUrgentIssues;

    @Column(name = "vchOtherUrgentIssues")
    private String vchOtherUrgentIssues;
    
    @Column(name = "vchReportFilePath")
    private String vchReportFilePath;

    @Column(name = "vchPhotographicEvidenceFilePath")
    private String vchPhotographicEvidenceFilePath;

    @Column(name = "vchRemarks")
    private String vchRemarks;
    
    @ManyToOne
    @JoinColumn(name = "int_location_id", nullable = false)
    private RoutineComplianceInspectorLocation intLocationId;
    
    @ManyToOne
    @JoinColumn(name = "int_warehouse_personal_equipment_id", nullable = false)
    private WarehousePersonalEquipment intWareHousePersonalEquipmentId;

    @ManyToOne
    @JoinColumn(name = "t_rout_com_physical_fire_protection", nullable = false)
    private RoutineComplianceInspectorPhysicalFireProtection intphysicalFireProtectionId;

    @ManyToOne
    @JoinColumn(name = "int_condition_of_goods_storage_id", nullable = false)
    private RoutineComplianceInspectorConditionOfGoodsStorage intConditionOfGoodsStorageId;
    
    @ManyToOne
    @JoinColumn(name = "int_environment_issues_id", nullable = false)
    private RoutineComplianceInspectorEnvironmentIssues intEnviromentId;

    @ManyToOne
    @JoinColumn(name = "int_insurance_id", nullable = false)
    private RoutineComplianceInspectorInsurance intInsuranceId;

    @ManyToOne
    @JoinColumn(name = "int_shrink_id", nullable = false)
    private RoutineComplianceInspectorShrink intShrinkId;

    @ManyToOne
    @JoinColumn(name = "int_it_system_id", nullable = false)
    private RoutineComplianceInspectorITSystem intItSystemId;
    
    @Column(name = "int_recommendation_location_id")
    private Long recommendationIdLocation;

    @Column(name = "int_conclusion_location_id")
    private Long conclusionLocationId;
    
    @Column(name = "routine_com_id")
    private Long routineComplianceId;

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

