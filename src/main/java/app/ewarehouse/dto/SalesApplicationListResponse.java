package app.ewarehouse.dto;




import java.sql.Timestamp;

import app.ewarehouse.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesApplicationListResponse {
    private String salesApplicationMainId;
    private String depositorId;
    private String buyerId;
    private String receiptNo;
    private Double priceOfReceipt;
    private Timestamp dtmCreatedOn;
    private Timestamp stmUpdatedAt;
    private ApplicationStatus applicationStatus;
   
}
