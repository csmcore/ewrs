package app.ewarehouse.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "m_suspension_reason")
public class SuspensionReason {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="intId")
	private Integer intId;

	@Column(name="reason")
	private String txtReason;
	@Column(name="intCreatedBy")
	private Integer intCreatedBy;

	@Column(name="intUpdatedBy")
	private Integer intUpdatedBy;

	@Column(name="dtmCreatedOn")
	@CreationTimestamp
	private Timestamp dtmCreatedOn;

	@Column(name="dtmUpdatedOn")
	@UpdateTimestamp
	private Timestamp dtmUpdatedOn;

	@Column(name="bitDeletedFlag", columnDefinition="BIT")
	private Boolean bitDeletedFlag=false;

	@Column(name="intInsertStatus")
	private Integer intInsertStatus=1;

	public Integer getIntId() {
		return intId;
	}

	public void setIntId(Integer intId) {
		this.intId = intId;
	}

	public String getTxtReason() {
		return txtReason;
	}

	public void setTxtReason(String txtReason) {
		this.txtReason = txtReason;
	}

	public Integer getIntCreatedBy() {
		return intCreatedBy;
	}

	public void setIntCreatedBy(Integer intCreatedBy) {
		this.intCreatedBy = intCreatedBy;
	}

	public Integer getIntUpdatedBy() {
		return intUpdatedBy;
	}

	public void setIntUpdatedBy(Integer intUpdatedBy) {
		this.intUpdatedBy = intUpdatedBy;
	}

	public Timestamp getDtmCreatedOn() {
		return dtmCreatedOn;
	}

	public void setDtmCreatedOn(Timestamp dtmCreatedOn) {
		this.dtmCreatedOn = dtmCreatedOn;
	}

	public Timestamp getStmUpdatedOn() {
		return dtmUpdatedOn;
	}

	public void setStmUpdatedOn(Timestamp dtmUpdatedOn) {
		this.dtmUpdatedOn = dtmUpdatedOn;
	}

	public Boolean getBitDeletedFlag() {
		return bitDeletedFlag;
	}

	public void setBitDeletedFlag(Boolean bitDeletedFlag) {
		this.bitDeletedFlag = bitDeletedFlag;
	}

	public Integer getIntInsertStatus() {
		return intInsertStatus;
	}

	public void setIntInsertStatus(Integer intInsertStatus) {
		this.intInsertStatus = intInsertStatus;
	}

}
