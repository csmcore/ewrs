package app.ewarehouse.dto;

import java.util.Date;
import java.util.List;

import app.ewarehouse.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperatorLicenceDTO {
    private Integer id;
    private String businessName;
    private String businessRegNumber;
    private String businessEntityType;
    private String businessAddress;
    private String emailAddress;
    private String phoneNumber;
    private String kraPin;
    private String physicalAddressWarehouse;
    private Integer warehouseSize;
    private String goodsStored;
    private Integer storageCapacity;
    private String securityMeasures;
    private String wasteDisposalMethods;
    private Boolean declaration;
    private String paymentMethod;
    private Status status;
    private Action action;
    private Stakeholder forwardedTo;
    private List<OperatorLicenceRemarks> remarks;
    private List<OperatorLicenceFiles> files;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Date dtmCreatedAt;
    private Date stmUpdatedAt;
    private Boolean bitDeleteFlag = false;
    private SaveStatus enmSaveStatus;
    private String vchApplicationNo;
}

