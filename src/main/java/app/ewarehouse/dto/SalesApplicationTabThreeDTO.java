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
public class SalesApplicationTabThreeDTO {
    private Integer id;
    private Boolean isDeclared;
    private Date dateOfIncident;
    private String uploadContractAgreement;
    private  String applicationId;
    private Integer intCreatedBy;
    private Integer intUpdatedBy;
    private Date dtmCreatedAt;
    private Date stmUpdatedAt;
    private Boolean bitDeleteFlag;
}
