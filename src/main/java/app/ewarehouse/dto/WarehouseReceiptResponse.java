package app.ewarehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseReceiptResponse {
    private String txtWarehouseReceiptId;
    private receiveCommodityResponse receiveCommodity;
    private String txtRemark;
    private String status;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Date dtmCreatedOn;
    private Date stmUpdatedAt;
    private Boolean bitDeleteFlag;
    private  Integer intIndex;
}
