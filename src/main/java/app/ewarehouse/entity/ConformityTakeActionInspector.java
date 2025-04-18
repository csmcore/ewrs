package app.ewarehouse.entity;

import java.time.LocalDateTime;
import java.util.List;

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

@Data
@Entity
@Table(name = "t_cnf_take_action_inspector")
public class ConformityTakeActionInspector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intAllocateInspectorId")
    private Integer allocateInspectorId;

    @ManyToOne
	@JoinColumn(name = "intcnfId" , nullable = false)
	private ConformityMain conformity;

    @Column(name = "vchWareHouseId", length = 50)
    private String wareHouseId;

    @Column(name = "intInspectorTeamLeadId")
    private Integer inspectorTeamLeadId;

    @Column(name = "txtInspectorId", columnDefinition = "TEXT")
    private String inspectorId;

    @Column(name = "vchInspectionFilePath", length = 255)
    private String inspectionFilePath;

    @Column(name = "vchRemarkByInspector")
    private String remarkByInspector;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private LocalDateTime createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;
    
    @Column(name = "bitInspScheduled")
    private Boolean inspScheduled = false;
    
    @Column(name = "bitInspCompleted")
    private Boolean inspCompleted = false;
    
    @ManyToOne
    @JoinColumn(name = "int_location_id")
    private RoutineComplianceInspectorLocation intLocationId;
    
    @ManyToOne
    @JoinColumn(name = "int_warehouse_personal_equipment_id")
    private WarehousePersonalEquipment intWareHousePersonalEquipmentId;

    @ManyToOne
    @JoinColumn(name = "t_rout_com_physical_fire_protection")
    private RoutineComplianceInspectorPhysicalFireProtection intphysicalFireProtectionId;

    @ManyToOne
    @JoinColumn(name = "int_condition_of_goods_storage_id")
    private RoutineComplianceInspectorConditionOfGoodsStorage intConditionOfGoodsStorageId;
    
    @ManyToOne
    @JoinColumn(name = "int_environment_issues_id")
    private RoutineComplianceInspectorEnvironmentIssues intEnviromentId;

    @ManyToOne
    @JoinColumn(name = "int_insurance_id")
    private RoutineComplianceInspectorInsurance intInsuranceId;

    @ManyToOne
    @JoinColumn(name = "int_shrink_id")
    private RoutineComplianceInspectorShrink intShrinkId;

    @ManyToOne
    @JoinColumn(name = "int_it_system_id")
    private RoutineComplianceInspectorITSystem intItSystemId;
    
    @Column(name = "int_recommendation_location_id")
    private Long recommendationIdLocation;

    @Column(name = "int_conclusion_location_id")
    private Long conclusionLocationId;
    
    @Column(name = "bitForwardToOicStatus")
    private Boolean forwardToOicStatus = false;
    
    @Transient
    List<Recommendation> recomendationData; 
    
    @Transient
    List<RoutineComplianceConclusion> conclusionData;
    
}

