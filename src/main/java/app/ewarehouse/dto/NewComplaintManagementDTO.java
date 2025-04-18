package app.ewarehouse.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import app.ewarehouse.entity.Supporting_document;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class NewComplaintManagementDTO {

	    private String txtrAddress;
	    private Boolean bitDeletedFlag;
	    private Boolean chkdeclartion;
	    private Integer selNameofCollateralManager;
	    private String selNameofCollateralManagerVal;
	    private Integer rdoComplaintAgainst;
	    private String rdoComplaintAgainstVal;
	    private Integer selTypeofComplain;
	    private String selTypeofComplainVal;
	    private String txtContactNumber;
	    private Integer selCounty;
	    private String selCountyVal;
	    private Integer selCountyofWarehouse;
	    private String selCountyofWarehouseVal;
	    private Integer selWard;
	    private String selWardVal;
	    private String txtrDescriptionofIncident;
	    private Timestamp dtmCreatedOn;
	    private String txtEmailAddress;
	    private Date txtDateofIncident;
	    private Integer selNameofInspector;
	    private String selNameofInspectorVal;
	    private Integer intCreatedBy;
	    private Integer intId;
	    private Integer intInsertStatus;
	    private Integer intUpdatedBy;
	    private Integer selNameofGrader;
	    private String selNameofGraderVal;
	    private Integer selWarehouseShopName;
	    private String selWarehouseShopNameVal;
	    private Timestamp stmUpdatedOn;
	    private Integer selSubCounty;
	    private String selSubCountyVal;
	    private Integer selSubCountyofWarehouse;
	    private String  selWardofWarehouseVal;
	    private Integer selWardofWarehouse;
	    private String selSubCountyofWarehouseVal;
	    private String txtFullName;
	    private String txtWarehouseOperator;
	    private Integer tinStatus;
	    private Integer intProcessId;
	    private Integer resubmitStatus;
	    private Integer resubmitCount;
	    private Integer notingCount;
	    private String vchActionOnApplication;
	    private String ActionCondition;
	    private String complaintNo;
	    private String pendingATUser;
	    private String forwardedToUser;
	    private String sentFromUser;
	    private String applicationStatus;
	    private String remarks;
	    private String susReason;
	    private Integer stageNo;
	    private Integer complaintAgainstUserId;
	    private String complaintAgainstUserName;
	    private String companyProfId;
	    private String wareHouseId;
	    private Integer paymentId;
		private List<Supporting_document> addMoreSupportingDocuments;
}
