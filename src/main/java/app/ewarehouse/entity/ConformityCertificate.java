package app.ewarehouse.entity;

import java.time.Instant;
import java.time.LocalDate;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_cnf_certificate")
public class ConformityCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificateId")
    private Integer certificateId;

    @Column(name = "certificateNo", length = 50)
    private String certificateNo;

    @ManyToOne
	@JoinColumn(name = "intcnfId" , nullable = false)
	private ConformityMain conformity;

    @Column(name = "vchWareHouseId", length = 50)
    private String wareHouseId;

    @ManyToOne
    @JoinColumn(name = "companyId", referencedColumnName = "vchProfDetId", nullable = false)
    private AocCompProfileDetails company;

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
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private LocalDateTime dtmCreatedOn;

    @UpdateTimestamp
    @Column(name = "stmUpdatedOn", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Instant stmUpdatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmPaymentStatus")
    private PaymentStatus enmPaymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmWarehouseStatus")
    private WarehouseStatus enmWarehouseStatus;

}

