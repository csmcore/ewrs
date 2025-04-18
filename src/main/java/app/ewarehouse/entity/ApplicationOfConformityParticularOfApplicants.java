package app.ewarehouse.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "t_application_of_conformity_particular_of_applicants")
public class ApplicationOfConformityParticularOfApplicants implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intParticularOfApplicantsId")
	private Integer id;

	@Column(name = "vchApplicantFullName")
	private String applicantFullName;

	@Column(name = "vchPostalAddress")
	private String postalAddress;

	@Column(name = "vchPostalCode")
	private String postalCode;

	@Column(name = "vchTown")
	private String town;

	@Column(name = "vchTelephoneNumber")
	private String telephoneNumber;

	@Column(name = "vchMobilePhoneNumber")
	private String mobilePhoneNumber;

	@Column(name = "vchEmail")
	private String email;

	@Column(name = "vchWebsite")
	private String website;

	@ManyToOne
	@JoinColumn(name = "intEntityTypeId", referencedColumnName = "intEntityTypeId")
	private EntityType entityTypeId;

	@Column(name = "vchCompanyRegistrationNumber")
	private String companyRegistrationNumber;

	@Column(name = "vchKraPin")
	private String kraPin;

	@Column(name = "vchLrNumber")
	private String lrNumber;

	@ManyToOne
	@JoinColumn(name = "intCountyId", referencedColumnName = "county_id")
	private County countyId;

	@ManyToOne
	@JoinColumn(name = "intSubCountyId", referencedColumnName = "intId")
	private SubCounty subCountyId;

	@Column(name = "vchWard")
	private String ward;

	@Column(name = "vchStreetName")
	private String streetName;

	@Column(name = "vchBuilding")
	private String building;

	@Column(name = "intTypeOfCommodities")
	private String typeOfCommodities;

	@Column(name = "intStorageCapacity")
	private Integer storageCapacity;

	@Column(name = "vchAuthorizedSignName")
	private String authorizedSignName;

	@Column(name = "vchAuthorizedSignTitle")
	private String authorizedSignTitle;

	@Column(name = "vchUploadSignPath")
	private String uploadSignPath;

	@JsonManagedReference
	@OneToMany(mappedBy = "particularOfApplicants",fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private List<ApplicationOfConformityDirectorDetails> directors;

//	@JsonManagedReference
//	@OneToMany(mappedBy = "particularOfApplicants",fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
//	private List<ApplicationOfConformityCommodityType> typeOfCommodities;

	@Column(name = "dtmDate")
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "vchUploadCompanySealPath")
	private String uploadCompanySealPath;

	@Column(name = "vchShop")
	private String shop;

	@ManyToOne
	@JoinColumn(name = "intUnitId", referencedColumnName = "intUnitId")
	private UnitOfMeasurement unitId;

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

	@Column(name = "bitDraftStatusFlag")
	private Boolean bitDraftStatusFlag = false;


}

