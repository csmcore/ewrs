package app.ewarehouse.dto;

import app.ewarehouse.entity.ComplaintApplicationStatus;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ComplaintmanagementResponse {

    private Integer intId;
    private String txtrAddress;
    private Boolean bitDeletedFlag;
    private Boolean chkdeclartion;
    private String selNameofCollateralManager;
    private Integer rdoComplaintAgainst;
    private String selTypeofComplain;
    private String txtContactNumber;
    private Integer selCounty;
    private Integer selCountyofWarehouse;
    private Integer selWardWarehouse;
    private String txtrDescriptionofIncident;
    private Timestamp dtmCreatedOn;
    private String txtEmailAddress;
    private Date txtDateofIncident;
    private String selNameofInspector;
    private Integer intCreatedBy;
    private Integer intInsertStatus;
    private Integer intUpdatedBy;
    private String selNameofGrader;
    private String selWarehouseShopName;
    private Timestamp stmUpdatedOn;
    private Integer selSubCounty;
    private Integer selSubCountyofWarehouse;
    private String txtFullName;
    private String txtWarehouseOperator;
    private String vchActionOnApplication;
    private String ActionCondition;
    private String complaintNo;
    private String applicationStatus;
    private Integer notingCount;
    private Integer tinStatus;
    private Integer intProcessId;
    private Integer resubmitStatus;
    private Integer resubmitCount;
    private String pendingATUser;
    private String selCountyVal;
    private String selSubCountyVal;
    private String typeOfCommodities;
    private String storageCapacity;
    private String allotment;
    private List<String> otherCommodities;
    private String selWarehouseShopNameVal;

}
