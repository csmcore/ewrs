package app.ewarehouse.dto;

import app.ewarehouse.entity.TsplitReceipt;
import app.ewarehouse.entity.TwarehouseReceipt;
import app.ewarehouse.entity.enmReceiptStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MsplitReceiptResponse {
    private String txtSplitApplicationId;
    private WarehouseReceiptResponse warehouseReceipt;
    private Integer totalQuantity;
    private String totalLotNo;
    private Boolean surrenderReceiptCheck;
    private String oldWarehouseReceiptPath;
    private String receiptName;
    private List<TsplitReceiptResponse> splitIdReceipt;
    private String status;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Date dtmCreatedAt;
    private Date stmUpdatedAt;
    private Boolean bitDeleteFlag;
}
