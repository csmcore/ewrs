package app.ewarehouse.entity;


import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "complaint_managment_new")
public class ComplaintManagementNewEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intCMPId")
    private Integer id;

    @Column(name = "complaint_no", nullable = false)
    private String complaintNo;

    @Column(name = "userId", nullable = false)
    private Integer userId;
    
    @ManyToOne
    @JoinColumn(name = "complaint_against_userId", referencedColumnName = "intId", insertable = false, updatable = false)
    private Tuser user;  // This establishes the relationship

    @Column(name = "complaint_against", nullable = false)
    private Integer complaintAgainst;

    @Column(name = "complaint_against_userId")
    private Integer complaintAgainstUserId;

    @Column(name = "complaint_type", nullable = false)
    private Integer complaintType;

    @Column(name = "incident_date")
    private Date incidentDate;

    @Column(name = "desc_incident", columnDefinition = "TEXT")
    private String descriptionIncident;

    @Column(name = "vchActionOnApplication")
    private String actionOnApplication;

    @Column(name = "ActionCondition")
    private String actionCondition;

    @Column(name = "applicationStatus")
    private String applicationStatus;

    @Column(name = "intSentNotifyId")
    private Integer sentNotifyId=0;

    @Column(name = "intSentMailId")
    private Integer sentMailId=0;

    @Column(name = "intSentSMSId")
    private Integer sentSMSId=0;

    @Column(name = "intCreatedBy", nullable = false)
    private Integer createdBy;

    @Column(name = "intUpdatedBy")
    private Integer updatedBy;

    @Column(name = "dtmCreatedOn", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "stmUpdatedOn")
    private LocalDateTime updatedOn=LocalDateTime.now();

    @Column(name = "bitDeletedFlag")
    private Boolean deletedFlag=false;
}
