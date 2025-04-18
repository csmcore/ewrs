package app.ewarehouse.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "t_complaint_reinstatement_take_action_cm")
@Data
public class ComplaintReinstatementTakeActionCm {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intAllocateCCId", nullable = false)
    private Integer allocateCCId;

    @ManyToOne
	@JoinColumn(name = "reinstatementId" , nullable = false)
	private ComplaintReinstatement reinstatement;

    @Column(name = "intCommitteeMemberId", nullable = false)
    private Integer committeeMemberId;

    @Column(name = "vchRemarkByCM", length = 255)
    private String remarkByCM;

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
    
    @Column(name = "bitForwardStatus")
    private Boolean forwardStatus = false;  // New column added

}
