package app.ewarehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "t_audit_trail")
public class AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intAuditTrailId")
    private Integer auditTrailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intUserId", referencedColumnName = "intId", nullable = false)
    private Tuser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intRoleId", referencedColumnName = "intId", nullable = false)
    private Mrole role;

    @Column(name = "vchAction", length = 100, nullable = false)
    private String action;

    @Column(name = "dtmCreatedOn", nullable = false)
    private LocalDateTime dtmCreatedOn;

    @Column(name = "vchRemarks", length = 100)
    private String remarks;

    @Column(name = "vchIpAddress", length = 45)
    private String ipAddress;

    @Column(name = "vchDeviceName", length = 100)
    private String deviceName;
    
    @Column(name = "vchLatitude", length = 45)
    private String latitude;

    @Column(name = "vchLongitude", length = 45)
    private String longitude;

    @Column(name = "vchOs", length = 45)
    private String os;

    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean deletedFlag = false;

}

