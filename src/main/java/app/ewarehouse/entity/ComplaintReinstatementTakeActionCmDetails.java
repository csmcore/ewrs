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
@Table(name = "t_complaint_reinstatement_take_action_cm_details")
@Data
public class ComplaintReinstatementTakeActionCmDetails {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intCMDetailsId", nullable = false)
    private Integer cmDetailsId;

    @ManyToOne
   	@JoinColumn(name = "reinstatementId" , nullable = false)
   	private ComplaintReinstatement reinstatement;

    @ManyToOne
    @JoinColumn(name = "intAllocateCCId", nullable = false)
    private ComplaintReinstatementTakeActionCm allocateCCId;

    @Column(name = "vchSupportingDocumentName", length = 255)
    private String supportingDocumentName;
    
    @Column(name = "vchRemarkByCM", length = 255)
    private String remarkByCM;

    @Column(name = "vchSupportingDocument", length = 255)
    private String supportingDocument;

    @Column(name = "intCreatedBy", nullable = false)
    private Integer createdBy;

    @Column(name = "dtmCreatedOn", updatable = false, insertable = false)
    private Timestamp createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmUpdatedOn", insertable = false)
    private Timestamp updatedOn;

    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean deletedFlag = false;
    
    @Column(name = "bitIsEdit")
    private Boolean isEdit = false;

}
