package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "t_financial_loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pledge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String depositorId;
    private String financialInstitution; 
    private LocalDate dateOfPledging;
    private Double proposedLoanAmount;
    private LocalDate proposedDateOfLoanClearance;
    private String loanStatus;
    private Integer durationOfLoan;
    private Integer intCreatedBy;
    private Boolean bitDeletedFlag;
    private Integer issueId;
    
    @ManyToOne
    @JoinColumn(name = "issueId", referencedColumnName = "intIssueanceWhId", insertable = false, updatable = false)
    @JsonBackReference
    private IssuanceWareHouseRecipt issuanceWareHouseRecipt;
}
