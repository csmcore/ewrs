package app.ewarehouse.master.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * @author chinmaya.jena
 * @since 03-07-2024
 */

@Data
@Table(name = "m_master_complaint_status")
@Entity
public class ComplaintStatusMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intId")
	private Integer intId;
	@Column(name = "vchComplaintStatusName")
	private String vchComplaintStatusName;
	@Column(name = "intCreatedBy")
	private Integer intCreatedBy;
	@Column(name = "intUpdatedBy")
	private Integer intUpdatedBy;
	@Column(name = "dtmCreatedOn")
	@CreationTimestamp
	private Date dtmCreatedOn;
	@Column(name = "stmUpdatedOn")
	@UpdateTimestamp
	private Date stmUpdatedOn;
	@Column(name = "bitDeletedFlag")
	private Boolean bitDeletedFlag = false;

}
