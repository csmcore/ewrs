package app.ewarehouse.dto;

import app.ewarehouse.entity.Commodity;
import app.ewarehouse.entity.Seasonality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class receiveCommodityResponse {
    private String txtReceiveCId;
    private BuyerDepositorResponse depositor;
    private Commodity commodity;
    private Double intQuantity;
    private Seasonality seasonality;
    private String txtGrade;
    private String txtLotNo;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Date dtmCreatedAt;
    private Date stmUpdatedAt;
    private Boolean bitDeleteFlag;
    private String status;
    private String warehouseId;
    private Integer totalDays;
}
