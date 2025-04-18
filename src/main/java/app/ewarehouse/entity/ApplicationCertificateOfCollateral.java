package app.ewarehouse.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "t_application_of_certificate_of_compliance_collarter")
@Data
public class ApplicationCertificateOfCollateral {

    @Id
    @Column(name = "vchapplicationId", length = 20, nullable = false)
    private String applicationId;

    @Column(name = "vchFullName", length = 120)
    private String fullName;

    @Column(name = "vchMobileNumber", length = 20)
    private String mobileNo;

    @Column(name = "vchEmail", length = 45)
    private String email;

    @Column(name = "vchPostalCode", length = 10)
    private String postalCode;

    @Column(name = "vchPostalAddress", length = 255)
    private String postalAddress;
    
    @Column(name = "vchTown", length = 55)
    private String town;

    @Column(name = "vchTelephoneNumber", length = 20)
    private String telephoneNo;

    @Column(name = "vchWebsite", length = 45)
    private String website;

    @Column(name = "vchEntityType", length = 45)
    private String entityType;

    @Column(name = "vchCompanyName", length = 45)
    private String companyName;

    @Column(name = "vchCompanyRegistrationNumber", length = 45)
    private String companyRegNo;

    @Column(name = "vchKraPin", length = 15)
    private String kraPin;

    @Column(name = "vchpaymentMode", length = 45)
    private String paymentMode;

    @Column(name = "bitPaymentSuccess")
    private Boolean paymentSuccess = false;

    @Column(name = "actionStatus")
    private Integer actionStatus;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmCreatedOn")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Timestamp createdOn;

    @Column(name = "dtmUpdatedOn")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Timestamp updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;

    @Column(name = "isInspector")
    private Boolean isInspector = false;

    @Column(name = "isCertificatecommittee")
    private Boolean isCertificatecommittee = false;

    @Column(name = "isDefer")
    private Boolean defer = false;
    
    @Column(name = "isInspectRemark")
    private Boolean isInspectRemark=false;

    @Column(name = "isCcRemark")
    private Boolean isCcRemark =false;
    
    @Column(name="isDraft")
    private Boolean isDraft;
    
    @Column(name = "isEdit")
    private Boolean isEdit=false;
}


