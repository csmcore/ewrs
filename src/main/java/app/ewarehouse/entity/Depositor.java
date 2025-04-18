/**
 * 
 */
package app.ewarehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * Priyanka Singh 
 */
@Entity
@Data
@Table(name = "m_depositor_register")
@DynamicUpdate
public class Depositor {

	@Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(
            name = "custom-id",
            type = app.ewarehouse.util.CustomIdGenerator.class,
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "tableName", value = "m_depositor_register"),
                    @org.hibernate.annotations.Parameter(name = "idName", value = "intId")
            })
    @Column(name = "intId")
    private String intId;

//    @NotBlank(message = "Name is mandatory")
    @Column(name = "vchName")
    private String txtName;

//    @NotBlank(message = "Passport number is mandatory")
    @Column(name = "vchPassportNo", unique = true)
    private String txtPassportNo;

    @Column(name = "vchPassportPath")
    private String txtPassportPath;

    @Column(name = "intCentralRegistryIdentifier")
    private String intCentralRegistryIdentifier;

    @NotBlank(message = "Postal address is mandatory")
    @Column(name = "vchPostalAddress")
    private String txtPostalAddress;

//    @Pattern(regexp = "^\\+\\d{11,14}$", message = "Telephone number is invalid")
    @Column(name = "vchTelephoneNumber")
    private String txtTelephoneNumber;

//    @Email(message = "Email address is invalid")
    @Column(name = "vchEmailAddress")
    private String txtEmailAddress;

//    @NotBlank(message = "Ward is mandatory")
    @Column(name = "vchWard")
    private String txtWard;

    @ManyToOne
    @JoinColumn(name = "intSubCountyId")
    private SubCounty subCounty;

//    @NotNull(message = "Bank account number is mandatory")
    @Column(name = "intBankAccNo")
    private Integer intBankAccNo;

//    @NotBlank(message = "Bank name is mandatory")
    @Column(name = "vchBankName")
    private String txtBankName;

    @Column(name = "vchBankProofPath")
    private String txtBankProofPath;

//    @NotNull(message = "Business registration number is mandatory")
    @Column(name = "intBusinessRegNo")
    private Integer intBusinessRegNo;

    @Column(name = "vchBusinessRegCertPath")
    private String txtBusinessRegCertPath;

//    @NotNull(message = "Terms and conditions flag is mandatory")
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

    @Column(name = "intApprovedBy")
    private Integer intApprovedBy;
}
