package app.ewarehouse.entity;




import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
@Data
@Table(name="complaint_reporting")
@Entity
public class Complaint_reporting {
@Transient
private List<Supporting_doc> addMoreSupportingDocuments;
@Column(name="`add`")
private String txtrAddress;
@Column(name="bitDeletedFlag", columnDefinition="BIT")
private Boolean bitDeletedFlag=false;

@Column(name="`coll_manager`")
private String selNameofCollateralManager;
@Transient
private String selNameofCollateralManagerVal;
@Column(name="`complain_type`")
private String selTypeofComplain;
@Transient
private String selTypeofComplainVal;
@Column(name="`complaint_against`")
private Integer rdoComplaintAgainst;
@Transient
private String rdoComplaintAgainstVal;
@Column(name="`contact_no`")
private String txtContactNumber;
@Column(name="`county_warehouse`")
private String selCountyofWarehouse;
@Transient
private String selCountyofWarehouseVal;
@Column(name="`countyId`")
private Integer selCounty;
@Transient
private String selCountyVal;
@Column(name="`date_incident`")
private Date txtDateofIncident;
@Column(name="dtmCreatedOn")
@CreationTimestamp
private Timestamp dtmCreatedOn;

@Column(name="`email`")
private String txtEmailAddress;
@Column(name="`grader_name`")
private String selNameofGrader;
@Transient
private String selNameofGraderVal;
@Column(name="`incident_desc`")
private String txtrDescriptionofIncident;
@Column(name="`insp_name`")
private String selNameofInspector;
@Transient
private String selNameofInspectorVal;
@Column(name="intCreatedBy")
private Integer intCreatedBy;

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="intId")
private Integer intId;

@Column(name="intInsertStatus")
private Integer intInsertStatus=1;

@Column(name="intUpdatedBy")
private Integer intUpdatedBy;

@Column(name="`name`")
private String txtFullName;
@Column(name="stmUpdatedOn")
@UpdateTimestamp
private Timestamp stmUpdatedOn;

@Column(name="`sub_county_warehouse`")
private String selSubCountyofWarehouse;
@Transient
private String selSubCountyofWarehouseVal;
@Column(name="`sub_countyId`")
private Integer selSubCounty;
@Transient
private String selSubCountyVal;
@Column(name="`wh_shop_name`")
private String selWarehouseShopName;
@Transient
private String selWarehouseShopNameVal;
@Column(name="`whop_name`")
private String txtWarehouseOperator;
@Column(name="declaration_status")
private boolean ChkIherebydeclarethattheinformationprovidedistrueandaccuratetothebestofmyknowledgeandbelief;
}