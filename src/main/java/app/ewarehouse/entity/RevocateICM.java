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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_revocate_icm")
@Getter
@Setter
public class RevocateICM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "icmId")
    private Long icmId;

    @Column(name = "icmRoleId")
    private Integer icmRoleId;

    @Column(name = "icmUserRoleId")
    private Integer icmUserRoleId;

    @Column(name = "icmUserId")
    private Integer icmUserId;

    @Column(name = "icmUserName")
    private String icmUserName;

    @Enumerated(EnumType.STRING)
    @Column(name = "icmStatus")
    private ComplaintStatus icmStatus;

    @CreationTimestamp
    @Column(name = "dtmCreatedOn")
    private LocalDateTime dtmCreatedOn;

    @Column(name = "intCreatedBy")
    private Integer intCreatedBy;

    @UpdateTimestamp
    @Column(name = "dtmUpdatedOn")
    private LocalDateTime dtmUpdatedOn;

    @Column(name = "intUpdatedBy")
    private Integer intUpdatedBy;

    @Column(name = "bitDeletedFlag")
    private Boolean bitDeletedFlag = false;

    @Column(name = "icmComplaintAppId")
    private Integer icmComplaintAppId;

    @Column(name = "icmActionTaken")
    private Boolean icmActionTaken = false;
    @Column(name = "bitMailFlag")
    private Boolean mailFlag;
    @Column(name = "bitSMSFlag")
    private Boolean sMSFlag;
    @Column(name = "bitNotificationFlag")
    private Boolean notificationFlag;
    @Column(name = "intSentMailId")
    private Integer sentMailId;
    @Column(name = "intSentSMSId")
    private Integer sentSMSId;
    @Column(name = "intSentNotificationId")
    private Integer sentNotificationId;
    
    @Column(name = "intLableId")
    private Integer lableId;
    
    @Column(name = "intProcessId")
    private Integer processId;
    
     
    // Getters and Setters
    public Long getIcmId() {
        return icmId;
    }

    public void setIcmId(Long icmId) {
        this.icmId = icmId;
    }

    public Integer getIcmRoleId() {
        return icmRoleId;
    }

    public void setIcmRoleId(Integer icmRoleId) {
        this.icmRoleId = icmRoleId;
    }

    public Integer getIcmUserRoleId() {
        return icmUserRoleId;
    }

    public void setIcmUserRoleId(Integer icmUserRoleId) {
        this.icmUserRoleId = icmUserRoleId;
    }

    public Integer getIcmUserId() {
        return icmUserId;
    }

    public void setIcmUserId(Integer icmUserId) {
        this.icmUserId = icmUserId;
    }

    public String getIcmUserName() {
        return icmUserName;
    }

    public void setIcmUserName(String icmUserName) {
        this.icmUserName = icmUserName;
    }

    public ComplaintStatus getIcmStatus() {
        return icmStatus;
    }

    public void setIcmStatus(ComplaintStatus icmStatus) {
        this.icmStatus = icmStatus;
    }

    public LocalDateTime getDtmCreatedOn() {
        return dtmCreatedOn;
    }

    public void setDtmCreatedOn(LocalDateTime dtmCreatedOn) {
        this.dtmCreatedOn = dtmCreatedOn;
    }

    public Integer getIntCreatedBy() {
        return intCreatedBy;
    }

    public void setIntCreatedBy(Integer intCreatedBy) {
        this.intCreatedBy = intCreatedBy;
    }

    public LocalDateTime getDtmUpdatedOn() {
        return dtmUpdatedOn;
    }

    public void setDtmUpdatedOn(LocalDateTime dtmUpdatedOn) {
        this.dtmUpdatedOn = dtmUpdatedOn;
    }

    public Integer getIntUpdatedBy() {
        return intUpdatedBy;
    }

    public void setIntUpdatedBy(Integer intUpdatedBy) {
        this.intUpdatedBy = intUpdatedBy;
    }

    public Boolean getBitDeletedFlag() {
        return bitDeletedFlag;
    }

    public void setBitDeletedFlag(Boolean bitDeletedFlag) {
        this.bitDeletedFlag = bitDeletedFlag;
    }

    public Integer getIcmComplaintAppId() {
        return icmComplaintAppId;
    }

    public void setIcmComplaintAppId(Integer icmComplaintAppId) {
        this.icmComplaintAppId = icmComplaintAppId;
    }

    public Boolean getIcmActionTaken() {
        return icmActionTaken;
    }

    public void setIcmActionTaken(Boolean icmActionTaken) {
        this.icmActionTaken = icmActionTaken;
    }

    public enum ComplaintStatus {
        APPLICATION_ASSIGNED,
        APPLICATION_REVIEWED
    }
}


