package app.ewarehouse.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_pledging_discharge_main")
public class PledgingDischargeMain {

	@Id
    @GeneratedValue(generator = "pledging_discharge_main_id_gen")
    @GenericGenerator(name = "pledging_discharge_main_id_gen", type = app.ewarehouse.util.CustomIdGenerator.class, parameters = {
            @org.hibernate.annotations.Parameter(name = "tableName", value = "t_pledging_discharge_main"),
            @org.hibernate.annotations.Parameter(name = "idName", value = "vchPledgingDischargeId") })
    @Column(name = "vchPledgingDischargeId", length = 20, nullable = false)
    private String pledgingDischargeId;

    @OneToOne
    @JoinColumn(name = "vchPdReceiptId", referencedColumnName = "pledging_discharge_receipt_id")
    private PledgingDischargeDepositorWarehouse depositorWarehouse;

    @OneToOne
    @JoinColumn(name = "vchPdLoanAppId", referencedColumnName = "vchPdLoanAppId")
    private PledgingDischargeDepositorLoanApp loanApp;

    @OneToOne
    @JoinColumn(name = "vchPdResidentialId", referencedColumnName = "vchPdResidentialId")
    private PledgingDischargeResidential residential;

    @OneToOne
    @JoinColumn(name = "vchPdBankDetailsId", referencedColumnName = "vchPdBankDetailsId")
    private PledgingDischargeBankDetails bankDetails;

    @OneToOne
    @JoinColumn(name = "vchPdUploadDocsId", referencedColumnName = "vchPdUploadDocsId")
    private PledgingDischargeUploadDocs uploadDocs;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private Timestamp createdOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn")
    private Timestamp updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;

	
	
}
