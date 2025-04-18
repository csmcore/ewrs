package app.ewarehouse.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "t_complaint_reinstatement")
public class ComplaintReinstatement {

	@Id
	@Column(name = "reinstatementId", length = 20, nullable = false)
	private String reinstatementId;

	@Column(name = "complaint_id")
	private Integer complaintId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "justification")
	private String justification;

	@Column(name = "reinstatement_date")
	private String reinstatementDate;

	@Column(name = "supporting_doc")
	private String supportingDoc;

	@Column(name = "underwriter")
	private String underwriter;

	@Column(name = "status")
	private Integer status;

	@Column(name = "is_cer_lic_check")
	private String isCerLicCheck;

	@Transient
	private boolean fetchFileDb;

	@Column(name = "isCommitteeMemeber")
	private Boolean isCommitteeMemeber = false;

	@Column(name = "isCommitteMemberRemark")
	private Boolean isCommitteMemberRemark = false;
	
    @Column(name = "dtmCreatedOn",updatable = false)
    @CreationTimestamp
    private Timestamp createdOn;

}