package app.ewarehouse.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "m_admin_security_setting")
@Entity
public class SecuritySetting {
     
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ss_id")
	private Integer ssId;
	
	@Column(name = "ss_type")
	private String ssType;
	
	@Column(name = "sub_ss_type")
	private String ssSubType;
	
	@Column(name = "action_status")
	private Boolean actionStatus = false;
	
	@Column(name = "dtmCreatedOn")
	@CreationTimestamp
	private Date dtmCreatedOn;

	@Column(name = "dtmUpdatedOn")
	@UpdateTimestamp
	private Date dtmUpdatedOn; 
	
	@Column(name = "actionTakenBy")
	private Integer actionTakenBy;
	
	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag = false;
	
}
