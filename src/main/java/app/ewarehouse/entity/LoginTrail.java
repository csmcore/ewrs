package app.ewarehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "t_login_trail")
public class LoginTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intLoginTrailId")
    private Integer loginTrailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intUserId", referencedColumnName = "intId", nullable = false)
    private Tuser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intRoleId", referencedColumnName = "intId", nullable = false)
    private Mrole role;

    @Column(name = "vchAction", length = 45, nullable = false)
    private String action;

    @Column(name = "dtmDateTime", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "vchIpAddress", length = 45)
    private String ipAddress;

    @Column(name = "vchLatitude", length = 45)
    private String latitude;

    @Column(name = "vchLongitude", length = 45)
    private String longitude;

    @Column(name = "vchOs", length = 100)
    private String os;

    @Column(name = "vchDeviceName")
    private String deviceName;
    
    @Column(name="bitLoginAttempted")
    private Boolean bitLoginAttempted;
    
    @Enumerated(EnumType.STRING)
    @Column(name="enmStatus")
    private LoginAttemptStatus enmStatus;
    
    @Column(name="vchEnteredUserName" ,length=50)
    private String vchEnteredUserName;

    @Column(name = "bitDeletedFlag", nullable = false)
    private Boolean deletedFlag = false;

}

