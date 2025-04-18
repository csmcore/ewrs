package app.ewarehouse.dto;

import lombok.Data;

@Data
public class SuspensionOfGraderResolutionDto {
    private String complaintNumber;
    private Integer roleId;
    private String remark;
    private String verdict;
}
