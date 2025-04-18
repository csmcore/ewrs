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
@Table(name = "t_cnf_take_action_oic")
public class ConformityTakeActionOic {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intOicTakeActionId")
    private Integer id;
    
    @ManyToOne
	@JoinColumn(name = "intcnfId" , nullable = false)
	private ConformityMain conformity;

    @Column(name = "vchWareHouseId", length = 50)
    private String wareHouseId;
    
    @Column(name = "remarkForDeferring", length = 255)
    private String remarkForDeferring;
    
    @Column(name = "remarkByOic", length = 255)
    private String remarkByOic;
    
    @Column(name = "intCreatedBy", nullable = false)
    private Integer createdBy;
    
    @CreationTimestamp
    @Column(name = "dtmCreatedOn", nullable = false, updatable = false)
    private LocalDateTime createdOn;
    
    @Column(name = "intUpdatedBy")
    private Integer updatedBy;
    
    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn", nullable = false)
    private LocalDateTime updatedOn;
    
    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean deletedFlag = false;
}

