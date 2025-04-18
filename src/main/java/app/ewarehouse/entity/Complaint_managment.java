package app.ewarehouse.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import java.util.List;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Table(name = "complaint_managment")
@Entity
public class Complaint_managment {
	@Transient
	private List<Supporting_document> addMoreSupportingDocuments;
	@Column(name = "`add`")
	private String txtrAddress;
	@Column(name = "bitDeletedFlag", columnDefinition = "BIT")
	private Boolean bitDeletedFlag = false;

	@Column(name = "`chkdeclartion`")
	private Boolean chkdeclartion;
	@Column(name = "`coll_manager_name`")
	private String selNameofCollateralManager;
	@Transient
	private String selNameofCollateralManagerVal;
	@Column(name = "`complaint_against`")
	private Integer rdoComplaintAgainst;
	@Transient
	private String rdoComplaintAgainstVal;
	@Column(name = "`complaint_type`")
	private String selTypeofComplain;
	@Transient
	private String selTypeofComplainVal;
	@Column(name = "`contact_no`")
	private String txtContactNumber;
	@Column(name = "`county`")
	private Integer selCounty;
	@Transient
	private String selCountyVal;
	@Column(name = "`county_warehouse`")
	private Integer selCountyofWarehouse;
	@Transient
	private String selCountyofWarehouseVal;
	@Column(name = "`desc_incident`")
	private String txtrDescriptionofIncident;
	@Column(name = "dtmCreatedOn")
	@CreationTimestamp
	private Timestamp dtmCreatedOn;

	@Column(name = "`email`")
	private String txtEmailAddress;
	@Column(name = "`incident_date`")
	private Date txtDateofIncident;
	@Column(name = "`inspector_name`")
	private String selNameofInspector;
	@Transient
	private String selNameofInspectorVal;
	@Column(name = "intCreatedBy")
	private Integer intCreatedBy;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intId")
	private Integer intId;

	@Column(name = "intInsertStatus")
	private Integer intInsertStatus = 1;

	@Column(name = "intUpdatedBy")
	private Integer intUpdatedBy;

	@Column(name = "`name_grader`")
	private String selNameofGrader;
	@Transient
	private String selNameofGraderVal;
	@Column(name = "`shopname_warehouse`")
	private String selWarehouseShopName;
	@Transient
	private String selWarehouseShopNameVal;
	@Column(name = "stmUpdatedOn")
	@UpdateTimestamp
	private Timestamp stmUpdatedOn;

	@Column(name = "`subcounty`")
	private Integer selSubCounty;
	@Transient
	private String selSubCountyVal;
	@Column(name = "`subcounty_warehouse`")
	private Integer selSubCountyofWarehouse;
	@Transient
	private String selSubCountyofWarehouseVal;
	@Column(name = "`user_name`")
	private String txtFullName;
	@Column(name = "`wop_name`")
	private String txtWarehouseOperator;
	@Transient
	private Integer tinStatus;
	@Transient
	private Integer intProcessId;
	@Transient
	private Integer resubmitStatus;
	@Transient
	private Integer resubmitCount;
	@Transient
	private Integer notingCount;
	@Column(name = "`vchActionOnApplication`")
	private String vchActionOnApplication;
	@Column(name = "`ActionCondition`")
	private String ActionCondition;
	@Column(name = "`complaint_no`")
	private String complaintNo;
	
	@Transient
	private String pendingATUser;
	@Transient
	private String forwardedToUser;
	@Transient
	private String sentFromUser;
	
    @Column(name = "applicationStatus")
    private String applicationStatus;
    
	@Transient
	private String remarks;
	
	@Transient
	private String susReason;
	@Transient
	private Integer selWard;
	@Transient
	private String  selWardofWarehouseVal;
	@Transient
    private Integer selWardofWarehouse;
	
	@Transient
    private Integer intCreatedByRole;
	
	
    
	
}