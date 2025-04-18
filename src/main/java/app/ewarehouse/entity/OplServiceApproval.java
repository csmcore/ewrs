package app.ewarehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "t_opl_service_approval")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OplServiceApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intOnlineServiceApprovalId")
    private Integer onlineServiceApprovalId;

    @Column(name = "intOnlineServiceId")
    private Integer onlineServiceId;

    @Column(name = "intProfileId")
    private Integer profileId;

    @Column(name = "intATAProcessId")
    private Integer ataProcessId;

    @Column(name = "intStageNo")
    private Integer stageNo;

    @Column(name = "intPendingAt")
    private Integer pendingAt;

    @Column(name = "intForwardTo")
    private Integer forwardTo;

    @Column(name = "intSentFrom")
    private Integer sentFrom;

    @Column(name = "dtmStatusDate")
    private LocalDateTime statusDate;

    @Column(name = "tinStatus")
    private Integer status;

    @Column(name = "intAssistantId")
    private Integer assistantId;

    @Column(name = "tinQueryTo")
    private Byte queryTo;

    @Column(name = "tinResubmitStatus")
    private Byte resubmitStatus;

    @Column(name = "stmCreatedOn", updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "intCreatedBy")
    private Integer createdBy;

    @Column(name = "dtmUpdatedOn")
    private LocalDateTime updatedOn;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag;

    @Column(name = "intProcessId")
    private Integer processId;

    @Column(name = "dtmApprovalDate")
    private LocalDateTime approvalDate;

    @Column(name = "vchApprovalDoc", length = 255)
    private String approvalDoc;

    @Column(name = "intApproveDocIndexId")
    private Integer approveDocIndexId;

    @Column(name = "tinDemandNoteGenStatus")
    private Byte demandNoteGenStatus;

    @Column(name = "tinVerifiedBy")
    private Integer verifiedBy;

    @Column(name = "vchATAAuths", length = 255)
    private String ataAuths;

    @Column(name = "vchRemainingATA", length = 255)
    private String remainingATA;

    @Column(name = "tinDemandNoteApplicableStatus")
    private Byte demandNoteApplicableStatus;

    @Column(name = "dtmDemandNoteDate")
    private LocalDateTime demandNoteDate;

    @Column(name = "tinVerifyLetterGenStatus")
    private Byte verifyLetterGenStatus;

    @Column(name = "intForwardedUserId")
    private Integer forwardedUserId;

    @Column(name = "vchForwardAllAction", length = 255)
    private String forwardAllAction;

    @Column(name = "intLabelId")
    private Integer labelId;

    @Column(name = "bitMailFlag")
    private Boolean mailFlag;

    @Column(name = "bitSMSFlag")
    private Boolean smsFlag;

    @Column(name = "bitNotificationFlag")
    private Boolean notificationFlag;

    @Column(name = "intSentMailId")
    private Integer sentMailId;

    @Column(name = "intSentSMSId")
    private Integer sentSMSId;

    @Column(name = "intSentNotificationId")
    private Integer sentNotificationId;

    @Column(name = "bitInspAllocated")
    private Boolean inspAllocated;
    @Column(name = "bitCLCAllocated")
    private Boolean clcAllocated;
    
    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = LocalDateTime.now();
    }
}