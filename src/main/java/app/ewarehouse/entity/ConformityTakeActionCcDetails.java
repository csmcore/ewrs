package app.ewarehouse.entity;

import java.time.LocalDateTime;

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
@Table(name = "t_cnf_take_action_cc_details")
public class ConformityTakeActionCcDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intCCDetailsId")
    private Integer ccDetailsId;

    @ManyToOne
	@JoinColumn(name = "intAllocateCCId" , nullable = false)
    private ConformityTakeActionCc allocateCCId;

    @Column(name = "vchWareHouseId", length = 50, nullable = false)
    private String wareHouseId;

    @ManyToOne
	@JoinColumn(name = "intcnfId" , nullable = false)
	private ConformityMain conformity;

    @Column(name = "vchSupportingDocumentName", length = 255)
    private String supportingDocumentName;

    @Column(name = "vchSupportingDocumentFile", length = 255)
    private String supportingDocumentFile;

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
}

