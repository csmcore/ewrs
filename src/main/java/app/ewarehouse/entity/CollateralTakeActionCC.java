package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;


@Entity
@Table(name = "t_oic_collateral_take_action_cc")
@Data
public class CollateralTakeActionCC {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intAllocateCCId", nullable = false)
    private Integer allocateCCId;

//    @Column(name = "vchapplicationId", length = 20, nullable = false)
//    private String applicationId;
    
    @ManyToOne
	@JoinColumn(name = "vchapplicationId" , nullable = false)
	private ApplicationCertificateOfCollateral colletral;

    @Column(name = "intCertificateCommitteeId", nullable = false)
    private Integer certificateCommitteeId;

    @Column(name = "vchRemarkByCC", length = 255)
    private String remarkByCC;

    @Column(name = "intCreatedBy", nullable = false)
    private Integer createdBy;

    @Column(name = "dtmCreatedOn", updatable = false, insertable = false)
    private Timestamp createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmUpdatedOn", insertable = false)
    private Timestamp updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag;
    
    @Column(name = "bitForwardToOicStatus")
    private Boolean forwardToOicStatus = false;  // New column added

}
