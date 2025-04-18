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
public class SalesApplicationTabTwoDTO {
    private Integer salesApplicationId;
    private Double priceOfReceipt;
    private String reasonForSale;
    private String uploadWarehouseReceipt;
    private String uploadProofOfOwnership;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Date dtmCreatedAt;
    private Date stmUpdatedAt;
    private Boolean bitDeleteFlag;
    private  String applicationId;
    private  String otherSaleReason;
}
