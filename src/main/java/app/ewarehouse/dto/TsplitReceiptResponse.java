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
public class TsplitReceiptResponse {
        private String splitId;
        private String splitReceiptNumber;
        private MsplitReceiptResponse msplitReceiptResponse;
        private Integer lotNo;
        private Double qty;
        private Integer intCreatedBy;
        private Integer intUpdatedBy;
        private Date dtmCreatedOn;
        private Date stmUpdatedOn;
        private Boolean bitDeleteFlag;
}
