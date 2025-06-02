package app.ewarehouse.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "t_opl_service_approval_notings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OplServiceApprovalNotings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intNotingsId")
    private Integer notingsId;

    @Column(name = "intOnlineServiceId")
    private Integer onlineServiceId;

    @Column(name = "intProfileId")
    private Integer profileId;

    @Column(name = "intFromAuthority")
    private Integer fromAuthority;

    @Column(name = "intToAuthority")
    private Integer toAuthority;

    @CreationTimestamp
    @Column(name = "dtActionTaken")
    private Date actionTakenDate;

    @Column(name = "intStatus")
    private Integer status;

    @Column(name = "txtNoting", columnDefinition = "TEXT")
    private String noting;

    @Column(name = "tinResubmitStatus")
    private Byte resubmitStatus;

    @Column(name = "txtRevertRemark", columnDefinition = "TEXT")
    private String revertRemark;

    @Column(name = "dtmResubmitDate")
    private LocalDateTime resubmitDate;

    @Column(name = "intStageNo")
    private Integer stageNo;

    @Column(name = "tinQueryTo")
    private Byte queryTo;

    @Column(name = "vchIPaddress", length = 50)
    private String ipAddress;

    @Column(name = "dtmCreatedOn", updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag;

    @Column(name = "intProcessId")
    private Integer processId;

    @Column(name = "jsnOtherDetails")
    private String otherDetails;

    @Column(name = "inspectionDocId")
    private Integer inspectionDocId;

    @Column(name = "inspObservation")
    private String inspObservation;

    @Column(name = "dateOfReportGen")
    private LocalDate reportGenDate;

    @Column(name = "intActionTakenId")
    private Integer actionTakenId;

    @Column(name = "vchActionTaken", length = 255)
    private String actionTaken;

    @Column(name = "conditionsToRemoveRevoc")
    private String conditionsToRemoveRevoc;

    @Column(name = "provideReasonRecomm")
    private String provideReasonRecomm;

    @Column(name = "wasPrevMitigate")
    private Boolean wasPrevMitigate;

    @Column(name = "issueWithOperator")
    private String issueWithOperator;

    @Column(name = "bitIsHearingScheduled")
    private Boolean isHearingScheduled;

    @Column(name = "vchSuspensionReason")
    private String suspensionReason;

    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
    }
}
