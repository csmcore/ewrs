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
@Table(name = "t_cnf_take_action_cc")
public class ConformityTakeActionCc {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intAllocateCCId")
    private Integer allocateCCId;
	
	@ManyToOne
	@JoinColumn(name = "intcnfId" , nullable = false)
	private ConformityMain conformity;
	
	
    @Column(name = "vchWareHouseId", length = 50)
    private String warehouseId;

    @Column(name = "intCertificateCommitteeId")
    private Integer certificateCommitteeId;

    @Column(name = "vchRemarkByCC", length = 255)
    private String remarkByCC;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private LocalDateTime createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn")
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;
    
    @Column(name = "bitForwardToOicStatus")
    private Boolean forwardToOicStatus = false;
	
}
