package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
@Data
@Entity
@Table(name="t_online_service_approval")
public class TOnlineServiceApproval implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Integer intOnlineServiceApprovalId;
	private Integer intOnlineServiceId;
	private Integer intProfileId = 0;
	private Integer intATAProcessId; 
	private Integer intStageNo;
	private String intPendingAt;
	private String intForwardTo; 
	private Integer intSentFrom; 
	@UpdateTimestamp
	private Date dtmStatusDate;
	private Integer tinStatus = 0;
	private Integer intAssistantId; 
	private Integer tinQueryTo;
	private Integer tinResubmitStatus; 
	@CreationTimestamp
	private Date stmCreatedOn;
	private Integer intCreatedBy; 
	@UpdateTimestamp
	private Date dtmUpdatedOn; 
	private Integer intUpdatedBy; 
	private Boolean bitDeletedFlag=false;
	private Integer intProcessId;
	private Date dtmApprovalDate;
	private String vchApprovalDoc; 
	private Integer intApproveDocIndexId;
	private Integer tinDemandNoteGenStatus;
	private Integer tinVerifiedBy=0; 
	private String vchATAAuths;
	private String vchRemainingATA; 
	private Integer tinDemandNoteApplicableStatus; 
	private Date dtmDemandNoteDate; 
	private Integer tinVerifyLetterGenStatus=0;
	private Integer intForwardedUserId=0;
	private String vchForwardAllAction;
	private Integer intLabelId;
	
}
