package app.ewarehouse.dto;

import lombok.Data;

@Data
public class WareHouseRemarksDto {
	private String intWareHouseId;
	private String txtRemark;
	private String enmStatus;
    private Integer roleId;
    private Integer userId;
    private String userName;
    private Integer inspectorId;
    private String txtInspectorFilePath;
    private String dtmInspection;
}
