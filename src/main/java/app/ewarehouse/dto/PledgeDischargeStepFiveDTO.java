package app.ewarehouse.dto;

import lombok.Data;

@Data
public class PledgeDischargeStepFiveDTO {

    private String pledgingDischargeReceiptId;

    private Integer userId;
    
    private String bankStatement;

    private String passportSizePhoto;

    private String latestIDOrPassport;

    private String addressProof;

    private String warehouseReceipt;

    private String pinCertificate;
}
