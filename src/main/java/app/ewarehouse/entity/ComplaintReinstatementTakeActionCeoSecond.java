package app.ewarehouse.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
@Table(name = "t_complaint_reinstatement_take_action_ceos")
@Data
public class ComplaintReinstatementTakeActionCeoSecond {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intCeoTakeActionId")
    private Integer id;

	 @ManyToOne
	 @JoinColumn(name = "reinstatementId" , nullable = false)
	private ComplaintReinstatement reinstatement;


    @Column(name = "suspendStatus", length = 20)
    private String suspendStatus;
    
    @Column(name = "ceoRemark", length = 20)
    private String ceoRemark;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "dtmCreatedOn")
    private LocalDateTime createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmUpdatedOn")
    private LocalDateTime updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag;

}
