package app.ewarehouse.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_application_of_conformity_commodity_storage")
public class ApplicationOfConformityCommodityStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intCommodityStorageId", nullable = false, updatable = false)
    private Long commodityStorageId;

    @Column(name = "intStorageCapacity")
    private Integer storageCapacity;

    @ManyToOne
    @JoinColumn(name = "vchCompanyId", nullable = false)
    private AocCompProfileDetails company;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn", columnDefinition = "DATETIME")
    private LocalDateTime createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(name = "intUpdatedOn", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;

    @Column(name = "bitDraftStatus")
    private Boolean draftStatus = true;
    
    @ManyToOne
    @JoinColumn(name = "intWhLocationId", nullable = false)
    private ApplicationOfConformityLocationDetails warehouseLocation;
    
    @OneToMany(mappedBy = "commodityStorage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("commodityStorage") // Prevent infinite recursion in JSON response
    private List<ApplicationOfConformityCommodityDetails> commodityDetails;
}

