package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Data
@Table(name = "t_online_service_application_h")
public class TOnlineServiceApplicationH implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intOnlineServiceHistoryId; 
	private Integer intNotingId;
	private Integer intOnlineServiceId;
	private Integer intProfileId;
	private Integer intProcessId;
	private Integer intApplyStatus;
	private Integer intCreatedBy; 
	@CreationTimestamp
	private Date dtmCreatedOn; 
	private Integer intUpdatedBy;
	@UpdateTimestamp
	private Date stmUpdatedOn;
	private Boolean bitDeletedFlag=false;
	private Integer intApplicationStatus;
	private String vchApplicationNo;
	private Short tinResubmitStatus; 
	private Short tinQueryStatus;
	private String vchRemark;
	private Short tinApprovalStatus;
	private Date dtmAppliedOn;
	private String vchFinalSubmitUniqueId;
}
