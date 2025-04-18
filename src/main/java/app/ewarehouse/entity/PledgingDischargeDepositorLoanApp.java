package app.ewarehouse.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_pledging_discharge_depositor_loanapp")
public class PledgingDischargeDepositorLoanApp implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "pledging_discharge_loanapp_id_gen")
	@GenericGenerator(name = "pledging_discharge_loanapp_id_gen", type = app.ewarehouse.util.CustomIdGenerator.class, parameters = {
			@org.hibernate.annotations.Parameter(name = "tableName", value = "t_pledging_discharge_depositor_loanapp"),
			@org.hibernate.annotations.Parameter(name = "idName", value = "vchPdLoanAppId") })
    @Column(name = "vchPdLoanAppId", nullable = false, length = 20)
    private String pdLoanAppId;

    @ManyToOne
    @JoinColumn(name = "loan_purpose_id", referencedColumnName = "loan_purpose_id", nullable = true)
    private MLoanPurpose loanPurpose;

    @ManyToOne
    @JoinColumn(name = "scheme_id", referencedColumnName = "scheme_id", nullable = true)
    private MScheme scheme;

    @ManyToOne
    @JoinColumn(name = "bank_id", referencedColumnName = "intId", nullable = true)
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "pledging_discharge_receipt_id", referencedColumnName = "pledging_discharge_receipt_id", nullable = true)
    private PledgingDischargeDepositorWarehouse pledgingDischargeDepositorWarehouse;

    @Column(name = "proposed_credit_amount", precision = 20, scale = 2)
    private BigDecimal proposedCreditAmount;

    @Column(name = "tenure_of_loan_repayment", length = 50)
    private String tenureOfLoanRepayment;

    @Column(name = "intCreatedBy", nullable = true)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn", nullable = true, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdOn;

    @Column(name = "intUpdatedBy", nullable = true)
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn", nullable = true, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedOn;

    @Column(name = "bitDeletedFlag", nullable = true)
    private Boolean deletedFlag = false;

    @Column(name = "bitDraftStatus", nullable = true)
    private Boolean draftStatus = false;

}
