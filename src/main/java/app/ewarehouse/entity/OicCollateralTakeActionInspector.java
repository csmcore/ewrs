package app.ewarehouse.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_oic_collateral_take_action_inspector")
public class OicCollateralTakeActionInspector {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intAllocateInspectorId")
    private Integer allocateInspectorId;

//    @Column(name = "vchapplicationId", length = 20, nullable = false)
//    private String applicationId;
    
    @ManyToOne
   	@JoinColumn(name = "vchapplicationId" , nullable = false)
   	private ApplicationCertificateOfCollateral colletral;

    @Column(name = "intInspectorTeamLeadId")
    private Integer inspectorTeamLeadId;

    @Column(name = "txtInspectorId", columnDefinition = "TEXT")
    private String inspectorId;

    @Column(name = "vchInspectionFilePath", length = 255)
    private String inspectionFilePath;

    @Column(name = "vchRemarkByInspector", length = 255)
    private String remarkByInspector;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "dtmCreatedOn", updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmUpdatedOn")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag;
    
    @Column(name = "bitIsEdit")
    private Boolean isEdit = false;

    @PrePersist
    protected void onCreate() {
        this.createdOn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedOn = LocalDateTime.now();
    }

}
