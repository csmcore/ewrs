package app.ewarehouse.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "t_pledging_discharge_bank_details")
@Data
public class PledgingDischargeBankDetails {

	@Id
	@GeneratedValue(generator = "pledging_discharge_bankdet_id_gen")
	@GenericGenerator(name = "pledging_discharge_bankdet_id_gen", type = app.ewarehouse.util.CustomIdGenerator.class, parameters = {
			@org.hibernate.annotations.Parameter(name = "tableName", value = "t_pledging_discharge_bank_details"),
			@org.hibernate.annotations.Parameter(name = "idName", value = "vchPdBankDetailsId") })
    @Column(name = "vchPdBankDetailsId", nullable = false, length = 20)
    private String pdBankDetailsId;

	@ManyToOne
    @JoinColumn(name = "pledging_discharge_receipt_id", referencedColumnName = "pledging_discharge_receipt_id", nullable = true)
    private PledgingDischargeDepositorWarehouse pledgingDischargeDepositorWarehouse;

    @Column(name = "name_of_account_holder", length = 50)
    private String nameOfAccountHolder;

    @Column(name = "account_no", length = 50)
    private String accountNo;

    @Column(name = "bank_branch_name", length = 50)
    private String bankBranchName;

    @Column(name = "swift_codes", length = 50)
    private String swiftCodes;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "dtmCreatedOn", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmUpdatedOn", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedOn;

    @Column(name = "bitDeletedFlag", columnDefinition = "BIT", length = 1)
    private Boolean deletedFlag = false;

    @Column(name = "bitDraftStatus", columnDefinition = "BIT", length = 1)
    private Boolean draftStatus = false;
}
