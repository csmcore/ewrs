package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import app.ewarehouse.master.entity.WardMaster;

@Entity
@Table(name = "t_application_of_conformity_location_det")
@Getter
@Setter
public class ApplicationOfConformityLocationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intWhLocationId")
    private Long whLocationId;

    @Column(name = "vchWarehouseId", length = 50, nullable = false)
    private String warehouseId;

    @Column(name = "vchWarehouseName", length = 50, nullable = false)
    private String warehouseName;

    @Column(name = "vchWhOperatorName", length = 50, nullable = false)
    private String whOperatorName;

    @Column(name = "vchEmail", length = 50, nullable = false)
    private String email;

    @Column(name = "vchMobileNumber", length = 15, nullable = false)
    private String mobileNumber;

    @Column(name = "vchLrNumber", length = 20)
    private String lrNumber;

    @ManyToOne
    @JoinColumn(name = "intCountyId", nullable = false)
    private County county;

    @ManyToOne
    @JoinColumn(name = "intSubCountyId", nullable = false)
    private SubCounty subCounty;

    @ManyToOne
    @JoinColumn(name = "intWardId", nullable = false)
    private WardMaster ward;

    @Column(name = "vchLongitude", length = 20)
    private String longitude;

    @Column(name = "vchLatitude", length = 20)
    private String latitude;

    @Column(name = "vchStreetName", length = 50)
    private String streetName;

    @Column(name = "vchBuilding", length = 50)
    private String building;

    @Column(name = "vchPolicyNumber", length = 30)
    private String policyNumber;

    @ManyToOne
    @JoinColumn(name = "vchCompanyId", nullable = false)
    private AocCompProfileDetails company;

    @Column(name = "intCreatedBy", nullable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn", nullable = false)
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean deletedFlag = false;
    
    @Column(name = "bitDraftStatus", nullable = false)
    private Boolean draftStatus = true;
}

