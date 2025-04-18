package app.ewarehouse.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "t_application_of_conformity_commodity_details")
public class ApplicationOfConformityCommodityDetails {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intCommodityDetailsId")
    private Long commodityDetailsId;

	@ManyToOne
    @JoinColumn(name = "intCommodityStorageId", nullable = false)
	@ToString.Exclude
	@JsonIgnoreProperties("commodityDetails")
    private ApplicationOfConformityCommodityStorage commodityStorage;
	
    
    @Column(name = "intStorageCapacity", nullable = false)
    private Integer storageCapacity;
    
    @ManyToOne
    @JoinColumn(name = "intCommodityTypeId", nullable = false)
    private Commodity commodityType;
    
//    @Column(name = "intUnit", nullable = false)
//    private Integer unit;
    
    @Column(name = "intUnit", nullable = false)
    private String unit;
    
    @ManyToOne
    @JoinColumn(name = "vchCompanyId", nullable = false)
    private AocCompProfileDetails company;
    
    @Column(name = "intCreatedBy", nullable = false)
    private Integer createdBy;
    
    @CreationTimestamp
    @Column(name = "dtmCreatedOn", nullable = false, updatable = false)
    private LocalDateTime createdOn;
    
    @Column(name = "intUpdatedBy")
    private Integer updatedBy;
    
    @Column(name = "dtmUpdatedOn", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedOn;
    
    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean deletedFlag = false;
    
    @Column(name = "bitDraftStatus", nullable = false)
    private Boolean draftStatus = true;
    
    
    @ManyToOne
    @JoinColumn(name = "intWhLocationId", nullable = false)
    private ApplicationOfConformityLocationDetails warehouseLocation;

}

