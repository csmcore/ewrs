package app.ewarehouse.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name="t_routine_compliance_inspection")
public class RoutineComplianceInspection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_com_id")
    private Long routineComId;
    
    @Column(name = "company_id")
    private String companyId;
    
    @Column(name = "warehouse_id")
    private String warehouseId;
    
    @Column(name = "start_date")
    private String startDate;
    
    @Column(name = "end_date")
    private String endDate;
    
    @Column(name = "inspection_time")
    private String inspectionTime;
    
    @Column(name="compliance_officer")
    private Integer complianceOfficer;
    
    @Column(name="officer_office")
    private String officerOffice;
    
    @Column(name = "facility_type")
    private String facilityType;
    
    @Column(name = "inspection_type")
    private String inspectionType;
    
    @Column(name="other_facilityType")
    private String otherFacilityType;
    
    @Column(name = "requested_by")
    private String requestedBy;
    
    @Column(name = "remarks")
    private String remarks;
    
    @Column(name = "inspectionPlan")
    private String inspectionPlan;
    
    @CreationTimestamp
    @Column(name = "dtm_created_at")
    private Date dtmCreatedAt;

    @UpdateTimestamp
    @Column(name = "stm_updated_at")
    private Date stmUpdatedAt;
    
    @Column(name = "status")
    private Integer status;
    
    @Transient
    private boolean fetchFileDb;
    
    @Column(name = "int_compliance_edit")
    private Integer intComplianceEdit;

    
}

