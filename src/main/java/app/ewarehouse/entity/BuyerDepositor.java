package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "t_buyer_depositor_application")
@DynamicUpdate
@NamedStoredProcedureQuery(
		name="DepositorValidReceiptNoGet",
		procedureName="check_depositor_getreceiptNo",
		parameters = {	
		   @StoredProcedureParameter(name="depositor_id",mode=ParameterMode.IN,type = String.class),		
		}
		)
public class BuyerDepositor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "buyer-custom-id")
    @GenericGenerator(
            name = "buyer-custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "t_buyer_depositor_application"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "intId")
            })
    @Column(name = "intId")
    private String intId;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "vchName")
    private String txtName;

    @NotBlank(message = "Passport number is mandatory")
    @Column(name = "vchPassportNo", unique = true)
    private String txtPassportNo;

    @Column(name = "vchPassportPath")
    private String txtPassportPath;

    @Column(name = "intCentralRegistryIdentifier")
    private String intCentralRegistryIdentifier;

    @NotBlank(message = "Postal address is mandatory")
    @Column(name = "vchPostalAddress")
    private String txtPostalAddress;

    @Pattern(regexp = "^0\\d{9}$", message = "Telephone number is invalid")
    @Column(name = "vchTelephoneNumber")
    private String txtTelephoneNumber;

    @Email(message = "Email address is invalid")
    @Column(name = "vchEmailAddress")
    private String txtEmailAddress;

    @NotBlank(message = "Ward is mandatory")
    @Column(name = "vchWard")
    private String txtWard;

    @ManyToOne
    @JoinColumn(name = "intSubCountyId")
    private SubCounty subCounty;

    @ManyToOne
    @JoinColumn(name = "intBankId")
    private Bank bank;

    @NotNull(message = "Bank account number is mandatory")
    @Column(name = "intBankAccNo")
    private Long intBankAccNo;

    @Column(name = "vchBankProofPath")
    private String txtBankProofPath;

    @NotNull(message = "Business registration number is mandatory")
    @Column(name = "intBusinessRegNo")
    private String intBusinessRegNo;

    @Column(name = "vchBusinessRegCertPath")
    private String txtBusinessRegCertPath;

    @NotNull(message = "Terms and conditions flag is mandatory")
    @Column(name = "bitTermsConditionsFlag")
    private Boolean bitTermsConditionsFlag;

    @Column(name = "dtmCreatedOn")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date dtmCreatedOn;

    @Column(name = "stmUpdatedOn")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date stmUpdatedOn;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmStatus")
    private Status enmStatus = Status.Pending;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmRegistrationType")
    private RegistrationType enmRegistrationType;

    @Column(name = "intApprovedBy")
    private Integer intApprovedBy;

    @ManyToOne
    @JoinColumn(name = "intBuyerDepositorType")
    private BuyerDepositorType buyerDepositorType;

    @Column(name = "vchAccountHolderName")
    private String vchAccountHolderName;

    @Column(name = "vchBranchName")
    private String vchBranchName;
}