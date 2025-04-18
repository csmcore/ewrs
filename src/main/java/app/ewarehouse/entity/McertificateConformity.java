package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "m_certificate_conformity")
public class McertificateConformity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intApplicantId")
	private Integer intApplicantId;

	@Column(name = "vchApplicantName")
	private String txtApplicantName;

	@Column(name = "vchPostalAddress")
	private String txtPostalAddress;

	@Column(name = "vchPostalCode")
	private String txtPostalCode;

	@Column(name = "vchTown")
	private String txtTown;

	@Column(name = "vchTelephoneNumber")
	private String txtTelephoneNo;

	@Column(name = "vchMobileNumber")
	private String txtMobilNo;

	@Column(name = "vchEmailAddress")
	private String txtEmail;

	@Column(name = "vchWebsiteAddress")
	private String txtWebsite;

	@Column(name = "intEntityTypeId")
	private Integer txtEntityTypeId;

	@Column(name = "vchCompanyRegNumber")
	private String txtCompanyRegNo;

	@Column(name = "vchKraPin")
	private String txtKraPin;

	@Column(name = "bitDeletedFlag", columnDefinition = "BIT")
	private Boolean bitDeletedFlag = false;

	@Column(name = "dtmCreatedOn")
	@CreationTimestamp
	private Timestamp dtmCreatedOn;

	@Column(name = "intCreatedBy")
	private Integer intCreatedBy;

	@Column(name = "intUpdatedBy")
	private Integer intUpdatedBy;

	@Column(name = "stmUpdatedOn")
	@UpdateTimestamp
	private Timestamp stmUpdatedOn;
}
