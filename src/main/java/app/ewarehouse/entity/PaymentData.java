package app.ewarehouse.entity;

import java.time.LocalDateTime;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "t_payment_ewarehouse")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="vchPaymentInitId")
    private String depositorId;
    private String amountExpected;
    private String clientIDNumber;
    private String currency;
    private String billRefNumber;
    private String billDesc;
    private String clientName;
    private Integer intCreatedBy;
    
    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private LocalDateTime dtmCreatedOn;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn")
    private LocalDateTime dtmUpdatedOn;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmPaymentAgainst")
    private PaymentAgainst enmPaymentAgainst;

    @Column(name = "intRoleId")
    private Integer intRoleId;

    @Column(name = "vchEmail", length = 100)
    private String vchEmail;

    @Column(name = "vchPhoneNumber", length = 15)
    private String vchPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmPaymentStatus")
    private PaymentStatus enmPaymentStatus;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    

    

}

