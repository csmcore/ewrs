package app.ewarehouse.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "t_application_of_conformity_certificate")
public class ApplicationOfConformityCertificate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "certificateId")
    private Integer certificateId;
	
    @Column(name = "certificateNo", length = 20, nullable = false)
    private String certificateNo;

    @ManyToOne
    @JoinColumn(name = "applicationId", referencedColumnName = "vchApplicationId", nullable = false)
    private ApplicationOfConformity application;

    @ManyToOne
    @JoinColumn(name = "companyId", referencedColumnName = "vchProfDetId", nullable = false)
    private AocCompProfileDetails company;

    @Column(name = "userId", nullable = false)
    private Integer userId;

    @Column(name = "roleId", nullable = false)
    private Integer roleId;

    @Column(name = "validFrom")
    private LocalDate validFrom;

    @Column(name = "validTo")
    private LocalDate validTo;
    
    @Column(name = "approverRoleId")
    private Integer approverRoleId;
    
    @Column(name = "approverUserId")
    private Integer approverUserId;
    
    @Column(name = "approverUserName")
    private String approverUserName;

    @Column(name = "intCreatedBy", nullable = false)
    private Integer intCreatedBy;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @Column(name = "dtmCreatedOn")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date dtmCreatedOn;

    @Column(name = "stmUpdatedOn")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date stmUpdatedOn;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmPaymentStatus", nullable = false)
    private PaymentStatus enmPaymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "enmWarehouseStatus", nullable = false)
    private WarehouseStatus enmWarehouseStatus;
    
}
