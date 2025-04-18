package app.ewarehouse.master.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PledgeRequest {
	private String depositorId;
    private String financialInstitution;
    private String dateOfPledging;
    private Double proposedLoanAmount;
    private String proposedDateOfLoanClearance;
    private Integer durationOfLoan;
    private Integer issueId;
    private Integer userId;
    private String  userName;
    private Integer roleId;
    private String roleName;
}