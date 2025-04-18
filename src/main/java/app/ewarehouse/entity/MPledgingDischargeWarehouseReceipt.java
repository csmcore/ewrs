package app.ewarehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "m_pledging_discharge_receipt")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MPledgingDischargeWarehouseReceipt {

    @Id
    @GeneratedValue(generator = "pledging_discharge_receipt_id_gen")
    @GenericGenerator(
            name = "pledging_discharge_receipt_id_gen",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "m_pledging_discharge_receipt"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "pledging_discharge_receipt_id")
            })
    @Column(name = "pledging_discharge_receipt_id")
    private String pledgingDischargeReceiptId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "depositor_id")
    private BuyerDepositor buyer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_receipt_no")
    private TwarehouseReceipt warehouseReceipt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loan_purpose_id")
    private MLoanPurpose loanPurpose;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "scheme_id")
    private MScheme scheme;

    @Column(name = "proposed_credit_amount")
    private BigDecimal proposedCreditAmount;

    @Column(name = "tenure_of_loan_repayment")
    private String tenureOfLoanRepayment;

    @Column(name = "area")
    private String area;

    @Column(name = "plot_no")
    private String plotNo;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "length_of_stay_at_present_address")
    private String lengthOfStayAtPresentAddress;

    @Column(name = "nearest_market")
    private String nearestMarket;

    @Column(name = "current_police_station")
    private String currentPoliceStation;

    @Column(name = "years_of_stay")
    private Integer yearsOfStay;

    @Column(name = "gender")
    private String gender;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "religion")
    private String religion;

    @Column(name = "is_employed")
    private String isEmployed;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_details_id", referencedColumnName = "id")
    private EmploymentDetails employmentDetails;

    @Column(name = "name_of_account_holder")
    private String nameOfAccountHolder;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "bank_branch_name")
    private String bankBranchName;

    @Column(name = "swift_codes")
    private String swiftCodes;

    @Column(name = "bank_statement_upload")
    private String bankStatementUpload;

    @Column(name = "passport_photo_upload")
    private String passportPhotoUpload;

    @Column(name = "latest_id_or_passport_upload")
    private String latestIdOrPassportUpload;

    @Column(name = "address_proof_upload")
    private String addressProofUpload;

    @Column(name = "warehouse_receipt_upload")
    private String warehouseReceiptUpload;

    @Column(name = "pin_certificate_upload")
    private String pinCertificateUpload;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "created_by")
    private Integer createdBy;
}
