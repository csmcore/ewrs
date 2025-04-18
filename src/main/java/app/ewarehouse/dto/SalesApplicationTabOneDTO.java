package app.ewarehouse.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesApplicationTabOneDTO {
        private Integer id;
        private BuyerDepositorResponse depositorId;
        private BuyerDepositorResponse buyerId;
        private WarehouseReceiptResponse receiptNo;
        private Integer intCreatedBy;
        private Integer intUpdatedBy;
        private Date dtmCreatedOn;
        private Date stmUpdatedAt;
        private Boolean bitDeleteFlag;
        private  String applicationId;
}
