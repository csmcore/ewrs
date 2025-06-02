package app.ewarehouse.dto;

import app.ewarehouse.entity.CreatedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MBuyerDepositorDTO {
    private String vchEntityId;
    private BuyerDepositorResponse applicationDetails;
    private CreatedStatus enmStatus;
    private Date dtmCreatedOn;
    private Date stmUpdatedOn;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Boolean bitDeletedFlag;
}
