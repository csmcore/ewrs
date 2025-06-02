package app.ewarehouse.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
import lombok.Data;

@Data
@Entity
@Table(name = "t_cnf_take_action_inspector_schedule")
public class ConformityTakeActionInspectorSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intScheduleId")
    private Integer scheduleId;
    
    
    @ManyToOne
	@JoinColumn(name = "intAllocateInspectorId" , nullable = false)
    private ConformityTakeActionInspector allocateInspector;

    @ManyToOne
	@JoinColumn(name = "intcnfId" , nullable = false)
	private ConformityMain conformity;

    @Column(name = "vchWareHouseId", length = 50)
    private String wareHouseId;

    @Column(name = "dtInspectionDate")
    private LocalDate inspectionDate;

    @Column(name = "inspectionTime")
    private LocalTime inspectionTime;

    @Column(name = "vchComplianceOfficerName", length = 50)
    private String complianceOfficerName;

    @Column(name = "vchComplianceOfficerOffice", length = 100)
    private String complianceOfficerOffice;

    @Column(name = "vchFacilityType", length = 45)
    private String facilityType;

    @Column(name = "vchOtherFacility", length = 45)
    private String otherFacility;

    @Column(name = "vchInspectionRequestedBy", length = 50)
    private String inspectionRequestedBy;

    @Column(name = "vchInspectionType", length = 45)
    private String inspectionType;

    @Column(name = "txtRemarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private LocalDateTime createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn", insertable = false, updatable = true)
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;
}

