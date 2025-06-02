package app.ewarehouse.dto;

import app.ewarehouse.entity.OperatorLicenceFiles;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class StakeHolderActionRequestData {
    private Integer id;
    private String remark;
    private String action;
    private String createdBy;
    private Integer approvedBy;
    private OperatorLicenceFiles inspectionReport;
}
