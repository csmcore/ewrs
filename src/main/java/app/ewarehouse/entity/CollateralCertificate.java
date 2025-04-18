package app.ewarehouse.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_collateral_certificate")
public class CollateralCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming auto-increment
    @Column(name = "intCertificateId", nullable = false)
    private Integer certificateId;

    @Column(name = "certificateNo", length = 50)
    private String certificateNo;
    
    @Column(name = "applicationId", length = 20)
    private String applicationId;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "roleId")
    private Integer roleId;

    @Column(name = "validFrom")
    private LocalDate validFrom;

    @Column(name = "validTo")
    private LocalDate validTo;

    @Column(name = "approverRoleId")
    private Integer approverRoleId;

    @Column(name = "approverUserId")
    private Integer approverUserId;

    @Column(name = "approverUserName", length = 100)
    private String approverUserName;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmCreatedOn", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Timestamp createdOn;

    
    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmPaymentStatus")
    private PaymentStatus paymentStatus;

}

